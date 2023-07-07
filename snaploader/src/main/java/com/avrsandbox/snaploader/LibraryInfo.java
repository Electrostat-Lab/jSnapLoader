/*
 * Copyright (c) 2009-2023 jMonkeyEngine
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
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
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
package com.avrsandbox.snaploader;

/**
 * Provides a library placeholder with an adjustable baseName {@link LibraryInfo#baseName}.
 * 
 * @author pavl_g
 */
public enum LibraryInfo {
    
    /**
     * Represents the default basename to the native binary.
     */
    LIBRARY("");

    private String baseName;

    /**
     * Wraps information about the library.
     * 
     * @param baseName the library basename, for example: 'lib-basename.so'.
     */
    LibraryInfo(final String baseName) {
        this.baseName = baseName;
    }

    /**
     * Retrieves the dynamic library basename.
     * Default is [jmealloc].
     * 
     * @return the library base-name in string
     */
    public String getBaseName() {
        return baseName;
    }

    /**
     * Adjusts the library base name in a dynamic library name (lib-[basename]).
     * Default is [jmealloc].
     * 
     * @param baseName a new base-name to assign
     */
    public void setBaseName(final String baseName) {
        this.baseName = baseName;
    }
}
