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
package com.avrsandbox.snaploader.platform;

/**
 * Provides platform-dependent system properties for the current running machine.
 * 
 * @author pavl_g
 */
public enum PropertiesProvider {
    
    /**
     * Provides a string representation for the absolute directory
     * of the current user directory.
     */
    USER_DIR(System.getProperty("user.dir")), 

    /**
     * Provides a string representation for the absolute directory 
     * of the user home.
     */
    USER_HOME(System.getProperty("user.home")),

    /**
     * Provides a string representation for the platform-dependent file separator.
     */
    FILE_SEPARATOR(System.getProperty("file.separator")),

    /**
     * Provides a string representation for the file separator of the Zip specification.
     */
    ZIP_FILE_SEPARATOR("/"),

    /**
     * Provides a string representation for the absolute path of the 
     * java interpreter binary.
     */
    JAVA_HOME(System.getProperty("java.home"));
    
    private final String systemProperty;

    /**
     * Instantiates a platform-dependent property object.
     * 
     * @param systemProperty the string representation of the platform-dependent property
     */
    PropertiesProvider(String systemProperty) {
        this.systemProperty = systemProperty;
    }

    /**
     * Retrieves the platform-dependent property.
     * 
     * @return a string representation for the platform-dependent property
     */
    public String getSystemProperty() {
        return systemProperty;
    }
}
