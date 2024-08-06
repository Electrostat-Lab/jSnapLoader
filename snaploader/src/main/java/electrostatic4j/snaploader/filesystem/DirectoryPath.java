/*
 * Copyright (c) 2023-2024, The Electrostatic-Sandbox Distributed Simulation Framework, jSnapLoader
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
 * * Neither the name of 'Electrostatic-Sandbox' nor the names of its contributors
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

package electrostatic4j.snaploader.filesystem;

import electrostatic4j.snaploader.platform.util.PropertiesProvider;

/**
 * A class denotes and provides a directory absolute path.
 *
 * @author pavl_g
 */
public final class DirectoryPath {

    /**
     * An alias object for the current working directory absolute path.
     */
    public static final DirectoryPath USER_DIR =
            new DirectoryPath(PropertiesProvider.USER_DIR.getSystemProperty());

    /**
     * An alias object for the root user home directory absolute path.
     */
    public static final DirectoryPath USER_HOME =
            new DirectoryPath(PropertiesProvider.USER_HOME.getSystemProperty());

    /**
     * When combined with the
     * {@link electrostatic4j.snaploader.LibraryInfo#LibraryInfo(DirectoryPath, DirectoryPath, String, DirectoryPath)}
     * and the {@link electrostatic4j.snaploader.NativeBinaryLoader}
     * APIs, it denotes the classpath routine for the {@link FileLocator} API.
     */
    public static final DirectoryPath CLASS_PATH = new DirectoryPath(null);

    private String path;

    /**
     * Instantiates a directory path from a string path (not null).
     *
     * @param path the directory path
     */
    public DirectoryPath(final String path) {
        this.path = path;
    }

    /**
     * Instantiates a directory path from a string path (not null) using
     * the platform-specific file separators.
     *
     * @param root the root directory path
     * @param entries the filesystem entries after the root path
     */
    public DirectoryPath(final String root, final String... entries) {
        this(root);
        for (String entry: entries) {
            if (entry == null) {
                continue;
            }
            path = getPath() + PropertiesProvider.FILE_SEPARATOR.getSystemProperty() + entry;
        }
    }

    /**
     * Retrieves the absolute path to the specified directory path.
     *
     * @return a path in strings.
     */
    public String getPath() {
        return path;
    }
}
