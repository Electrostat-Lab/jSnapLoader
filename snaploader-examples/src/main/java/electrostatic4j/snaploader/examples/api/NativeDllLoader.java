/*
 * Copyright (c) 2023-2025, The Electrostatic-Sandbox Distributed Simulation Framework, jSnapLoader
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'AvrSandbox' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package electrostatic4j.snaploader.examples.api;

import com.github.stephengold.joltjni.Jolt;
import java.util.List;
import java.util.logging.Level;
import electrostatic4j.snaploader.ConcurrentNativeBinaryLoader;
import electrostatic4j.snaploader.LibraryInfo;
import electrostatic4j.snaploader.LoadingCriterion;
import electrostatic4j.snaploader.NativeBinaryLoader;
import electrostatic4j.snaploader.NativeBinaryLoadingListener;
import electrostatic4j.snaploader.filesystem.FileLocalizingListener;
import electrostatic4j.snaploader.filesystem.FileLocator;
import electrostatic4j.snaploader.platform.NativeDynamicLibrary;
import electrostatic4j.snaploader.throwable.FilesystemResourceInitializationException;
import electrostatic4j.snaploader.throwable.LoadingRetryExhaustionException;
import electrostatic4j.snaploader.throwable.UnSupportedSystemError;
import electrostatic4j.snaploader.util.CallingStackMetaData;
import electrostatic4j.snaploader.util.SnapLoaderLogger;

/**
 * A suggested cross-platform algorithm to use fallback mechanisms for Jolt-Jni.
 * <p>
 * <ul>
 * <li> This is an efficient implementation that follows best
 * practices for software engineering, and the computational theory. </li>
 * <li> This is a theoretical implementation technique. </li>
 * <li> Feel free to copy-paste to your projects and modify as required. </li>
 * <li> Open issues as required. </li>
 * </ul>
 * </p>
 *
 * @author pavl_g.
 */
public class NativeDllLoader implements NativeBinaryLoadingListener, FileLocalizingListener {

    /**
     * Instance reference for the associated binary loader.
     */
    protected final NativeBinaryLoader loader;
    /**
     * Instance reference for libraries with base features
     * for the {@link NativeDllLoader#loadBaseLibraries(LoadingCriterion)}
     * as a fallback mechanism.
     */
    protected NativeDynamicLibrary[] baseLibs;
    /**
     * Instance reference for libraries with CPU-based specific features
     * for the {@link NativeDllLoader#loadCpuEnhancedLibs(LoadingCriterion)} routine.
     */
    protected NativeDynamicLibrary[] cpuEnhancedLibs;

    /**
     * Instantiates a DLL Loader wrapper setting its logging, retry criteria,
     * and setting up its loading listeners.
     *
     * @param baseLibs             the base feature libraries group (not null).
     * @param cpuEnhancedLibs      the cpu enhanced libraries group (not null).
     * @param info                 the cross-platform library info metadata (not null).
     * @param enableLogging        true to enable snap-loader logger and failure logs (default: false).
     * @param enableRetryCriterion true to enable retrying when loading from a jar file (default: false).
     * @throws IllegalArgumentException if the caller stack has passed undefined library groups and/or
     *                                  undefined library information reference.
     */
    public NativeDllLoader(NativeDynamicLibrary[] baseLibs, NativeDynamicLibrary[] cpuEnhancedLibs,
                           LibraryInfo info, boolean enableLogging, boolean enableRetryCriterion) {
        this(info, enableLogging, enableRetryCriterion);

        if (baseLibs == null || cpuEnhancedLibs == null) {
            throw new IllegalArgumentException("Libraries groups cannot be null!");
        }

        this.baseLibs = baseLibs;
        this.cpuEnhancedLibs = cpuEnhancedLibs;
    }

    /**
     * Instantiates a DLL Loader wrapper setting its logging, retry criteria,
     * and setting up its loading listeners.
     *
     * @param enableLogging        enables the API level logger (default: false).
     * @param enableRetryCriterion enables retrying with clean extraction (default: false).
     *                             when loading failure for {@link LoadingCriterion#INCREMENTAL_LOADING}
     *                             routine.
     * @throws IllegalArgumentException if the caller stack has passed an undefined library information
     *                                  reference.
     */
    public NativeDllLoader(LibraryInfo info, boolean enableLogging, boolean enableRetryCriterion) {
        if (info == null) {
            throw new IllegalArgumentException("Cannot proceed with no library information!");
        }
        loader = new ConcurrentNativeBinaryLoader(List.of(), info);
        loader.setLoggingEnabled(enableLogging);
        loader.setRetryWithCleanExtraction(enableRetryCriterion);
        loader.setNativeBinaryLoadingListener(this);
        loader.setLibraryLocalizingListener(this);
    }

    /**
     * Loads the base defined libraries providing an anti-failure routine
     * for loading from archive commands. This routine is thread-safe with other object routines.
     *
     * <p>
     * Possible execution stack:
     * Legend:
     * <code>
     * ">>" represents the initial state of the automata
     * "*" represents the terminal state of the automata; after
     * which the stack is owned by another machine.
     * "**" represents an imminent failure signal.
     * "->" represents a machine transitional delta.
     * </code>
     * <ul>
     * <li> Case 1:
     *     <code> >> loadBaseLibraries(LoadingCriterion.INCREMENTAL_LOADING) ->
     *            if (failure-cause == UnSupportedSystemError) -> exit() -> **.
     *     </code>
     * </li>
     * <li> Case 2:
     *     <code> >> loadBaseLibraries(LoadingCriterion.INCREMENTAL_LOADING) ->
     *            if (failure-cause == LoadingRetryExhaustionException) -> onLoadingFailure()
     *            -> loadBaseLibraries(LoadingCriterion.SYSTEM_LOAD) ->
     *            if (failure-cause == UnsatisfiedLinkError) -> exit() -> **.
     *     </code>
     * </li>
     * </ul>
     * </p>
     *
     * @param criterion the loading criterion, it's recommended to start with
     *                  {@link LoadingCriterion#INCREMENTAL_LOADING}; moreover
     *                  start with the {@link NativeDllLoader#loadCpuEnhancedLibs(LoadingCriterion)} (not null).
     * @throws Exception if I/O event or thread signal interrupt occurs.
     */
    public synchronized void loadBaseLibraries(LoadingCriterion criterion) throws Exception {
        if (criterion == null) {
            throw new IllegalArgumentException("Cannot proceed with null loading criterion!");
        }
        try {
            loader.registerNativeLibraries(baseLibs)
                    .initPlatformLibrary()
                    .loadLibrary(criterion);
        } catch (UnSupportedSystemError e) {
            signalImminentFailure(
                    new CallingStackMetaData(Thread.currentThread().getStackTrace()[1], criterion, e));
        } catch (LoadingRetryExhaustionException e) {
            // re-route retry failure to the same anti-failure routine
            this.onLoadingFailure(loader,
                    new CallingStackMetaData(Thread.currentThread().getStackTrace()[1], criterion, e));
        }
    }

    /**
     * Loads the registered CPU-enhanced libraries. Providing an anti-failure routine
     * for loading from archive commands. This is thread-safe with other routines.
     *
     * <p>
     * Possible execution stack:
     * <ul>
     * <li> Case 1 (Notice UnSupportedSystemError is thrown from the initPlatformLibrary
     *            during which system selection is performed):
     *     <code> >> exec: loadCpuEnhancedLibs(LoadingCriterion.INCREMENTAL_LOADING) ->
     *            if (failure-cause == UnSupportedSystemError) -> onLoadingFailure ->
     *            exec: loadBaseLibraries(LoadingCriterion.INCREMENTAL_LOADING) -> *.
     *     </code>
     * </li>
     * <li> Case 2:
     *     <code> >> exec: loadCpuEnhancedLibs(LoadingCriterion.INCREMENTAL_LOADING) ->
     *            if (failure-cause == LoadingRetryExhaustionException) -> loadCpuEnhancedLibs(LoadingCriterion.SYSTEM_LOAD)
     *            if (failure-cause == UnsatisfiedLinkError) -> onLoadingFailure -> exec: loadBaseLibraries(LoadingCriterion.INCREMENTAL_LOADING)
     *            -> *.
     *     </code>
     * </li>
     * <li> Case 3:
     *     <code>
     *         >> ...Some states... -> Any other failure cause -> exit() -> *.
     *     </code>
     * </li>
     * </ul>
     * </p>
     *
     * @param criterion the type of loading; it's recommended to start with {@link LoadingCriterion#INCREMENTAL_LOADING}.
     * @throws Exception if I/O event or thread signal interrupt occurs.
     */
    public synchronized void loadCpuEnhancedLibs(LoadingCriterion criterion) throws Exception {
        if (criterion == null) {
            throw new IllegalArgumentException("Cannot proceed with null loading criterion!");
        }
        try {
            loader.registerNativeLibraries(cpuEnhancedLibs)
                    .initPlatformLibrary()
                    .loadLibrary(criterion);
        } catch (UnSupportedSystemError e) {
            // re-route system not found and retry failure to the same anti-failure routine
            this.onLoadingFailure(loader,
                    new CallingStackMetaData(Thread.currentThread().getStackTrace()[1], criterion, e));
        } catch (LoadingRetryExhaustionException e) {
            // retry with SYSTEM_LOAD
            // notice that LoadingRetryExhaustionException will never
            // happen with LoadingCriterion.SYSTEM_LOAD
            // the LoadingRetryExhaustionException is thrown as a result
            // of greater than 2 times throwing "UnSatisfiedLinkError" on the
            // INCREMENTAL Loading Stack (i.e., extracting stack).
            loadCpuEnhancedLibs(LoadingCriterion.SYSTEM_LOAD);
        }
    }

    @Override
    public void onLoadingSuccess(NativeBinaryLoader nativeBinaryLoader, CallingStackMetaData callingStackMetaData) {
        // initialize Jolt-Jni and physics update system
        // handling lifecycle to the internal Jolt-Physics Native

        // copied from Jolt-Jni Example
        String configuration = Jolt.getConfigurationString();
        /*
         * Depending which native library was loaded, the configuration string
         * should be one of the following:
         *
         * On LINUX_X86_64 platforms, either
         *  Single precision x86 64-bit with instructions: SSE2 SSE4.1 SSE4.2 AVX AVX2 F16C LZCNT TZCNT FMADD (Debug Renderer) (16-bit ObjectLayer) (Assertions) (ObjectStream) (Debug) (C++ RTTI) (C++ Exceptions)
         * or
         *  Single precision x86 64-bit with instructions: SSE2 (Debug Renderer) (16-bit ObjectLayer) (Assertions) (ObjectStream) (Debug) (C++ RTTI) (C++ Exceptions)
         *
         * On WIN_X86_64 platforms, either
         *  Single precision x86 64-bit with instructions: SSE2 SSE4.1 SSE4.2 AVX AVX2 F16C LZCNT TZCNT (FP Exceptions) (Debug Renderer) (16-bit ObjectLayer) (Assertions) (ObjectStream) (Debug) (C++ RTTI) (C++ Exceptions)
         * or
         *  Single precision x86 64-bit with instructions: SSE2 (FP Exceptions) (Debug Renderer) (16-bit ObjectLayer) (Assertions) (ObjectStream) (Debug) (C++ RTTI) (C++ Exceptions)
         */
        System.out.println(configuration);
    }

    @Override
    public synchronized void onLoadingFailure(NativeBinaryLoader nativeBinaryLoader, CallingStackMetaData callingStackMetaData) {
        // validate input!
        if (callingStackMetaData == null || callingStackMetaData.getCallingStack() == null ||
                callingStackMetaData.getErrorCause() == null || callingStackMetaData.getLoadingCriterion() == null) {
            throw new IllegalArgumentException("Failure stack metadata structure cannot be null!");
        }

        final String callingMethod = callingStackMetaData.getCallingStack().getMethodName();
        // log calling stack!
        SnapLoaderLogger.log(Level.INFO, callingStackMetaData.getCallingStack().getClassName(),
                callingStackMetaData.getCallingStack().getMethodName(),
                "Failure Stack", callingStackMetaData.getErrorCause());

        try {
            if (callingStackMetaData.getErrorCause() instanceof LoadingRetryExhaustionException) {
                if (callingMethod.contains("loadCpuEnhancedLibs")) {
                    // try cpu enhanced libs from system directory
                    loadCpuEnhancedLibs(LoadingCriterion.SYSTEM_LOAD);
                } else if (callingMethod.contains("loadBaseLibraries")) {
                    // try loading base libraries from system directories if loading is exhausted!
                    loadBaseLibraries(LoadingCriterion.SYSTEM_LOAD);
                }
            } else if (callingStackMetaData.getErrorCause() instanceof UnsatisfiedLinkError) {
                if (callingMethod.contains("loadCpuEnhancedLibs")) {
                    // no retry criteria?
                    // or loading from system directory?
                    // Exit the loadCpuEnhancedLibs stack frames!
                    loadBaseLibraries(LoadingCriterion.INCREMENTAL_LOADING);
                } else if (callingMethod.contains("loadBaseLibraries")) {
                    signalImminentFailure(callingStackMetaData);
                }
            } else if (callingStackMetaData.getErrorCause() instanceof FilesystemResourceInitializationException) {
                if (callingMethod.contains("onFileLocalizationFailure")) {
                    loadCpuEnhancedLibs(LoadingCriterion.SYSTEM_LOAD);
                }
            }
        } catch (Exception e) {
            signalImminentFailure(callingStackMetaData);
        }
    }

    @Override
    public void onRetryCriterionExecution(NativeBinaryLoader nativeBinaryLoader, CallingStackMetaData callingStackMetaData) {
    }

    @Override
    public void onFileLocalizationSuccess(FileLocator locator) {
    }

    /**
     * Sets base feature libraries group strong reference. This command
     * will only take effect before dispatching the loading
     * routines. Warning: Non-thread safe.
     *
     * @param baseLibs the new base libraries groups reference.
     */
    public void setBaseLibs(NativeDynamicLibrary[] baseLibs) {
        this.baseLibs = baseLibs;
    }

    /**
     * Sets the cpu-specific enhanced libraries group strong reference. This command
     * will only take effect before dispatching the loading routines. Warning: Non-thread safe.
     *
     * @param cpuEnhancedLibs the new enhanced libraries groups reference.
     */
    public void setCpuEnhancedLibs(NativeDynamicLibrary[] cpuEnhancedLibs) {
        this.cpuEnhancedLibs = cpuEnhancedLibs;
    }

    /**
     * Signals an imminent failure disposing the application process with an
     * error code formed from the hashcode of the causing throwable on the
     * calling stack.
     *
     * @param callingStackMetaData a calling stack metadata structure strong reference.
     */
    protected void signalImminentFailure(CallingStackMetaData callingStackMetaData) {
        SnapLoaderLogger.log(Level.SEVERE, callingStackMetaData.getCallingStack().getClassName(),
                callingStackMetaData.getCallingStack().getMethodName(),
                "Imminent Failure", callingStackMetaData.getErrorCause());
        // signal an imminent failure and crash the application
        Runtime.getRuntime().exit(-callingStackMetaData.getErrorCause().hashCode());
    }

    /**
     * Retrieves the associated native binary loader object.
     *
     * @return a native binary loader object that is associated wtih this object.
     */
    public NativeBinaryLoader getLoader() {
        return loader;
    }
}
