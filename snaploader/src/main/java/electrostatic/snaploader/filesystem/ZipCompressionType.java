/*
 * Copyright (c) 2023, AvrSandbox, jSnapLoader
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
package com.avrsandbox.snaploader.filesystem;

import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

/**
 * A creational pattern that defines a zip compression type and creates
 * a compression object accordingly.
 * 
 * @author pavl_g
 */
public enum ZipCompressionType {
    
    /**
     * A {@link ZipFile} compression type.
     */
    ZIP(0x00, null),

    /**
     * A {@link JarFile} compression type.
     */
    JAR(0xFF, null);

    private final int compressionSymbol;
    private ZipFile compressionObject;

    /**
     * Defines a zip compression type with a symbol and a representative object.
     * 
     * @param compressionSymbol a unique hex code representing the compression type object
     * @param compressionObject the final required compression object
     * @see ZipCompressionType#createNewCompressionObject(String)
     */
    ZipCompressionType(final int compressionSymbol, final ZipFile compressionObject) {
        this.compressionSymbol = compressionSymbol;
        this.compressionObject = compressionObject;
    }
    
    /**
     * Creates a new zip compression object by its path based on the compression symbol.
     * 
     * @param directory the zip-filesystem absolute path
     * @return a new zip compression object based on the compression type specified by the compression symbol
     * @throws IOException if the zip filesystem is not found, or an interrupted I/O operation has occured
     */
    protected ZipFile createNewCompressionObject(String directory) throws IOException {
        if (compressionObject == null) {
            if (this.getCompressionSymbol() == ZipCompressionType.ZIP.getCompressionSymbol()) { 
                compressionObject = new ZipFile(directory);
                return compressionObject;
            }
            compressionObject = new JarFile(directory);
            return compressionObject;
        }
        return compressionObject;
    }

    /**
     * Retrieves the unique compression symbol for this compression type.
     * 
     * @return the compression type symbol in integers
     */
    protected int getCompressionSymbol() {
        return compressionSymbol;
    }

    /**
     * Retrieves the created zip compression object for this compression type.
     * 
     * @return a {@link ZipFile} compression object or null if {@link ZipCompressionType#createNewCompressionObject(String)} 
     *         isnot invoked on this compression type
     */
    public ZipFile getCompressionObject() {
        return compressionObject;
    }
}
