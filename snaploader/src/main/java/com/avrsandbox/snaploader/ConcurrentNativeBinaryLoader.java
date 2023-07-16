package com.avrsandbox.snaploader;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentNativeBinaryLoader extends NativeBinaryLoader {

    protected final ReentrantLock lock = new ReentrantLock();

    public ConcurrentNativeBinaryLoader(LibraryInfo libraryInfo) {
        super(libraryInfo);
    }
    
    @Override
    protected void cleanExtractBinary(NativeDynamicLibrary library) throws IOException {
        try {
            lock.lock();
            super.cleanExtractBinary(library);
        } finally {
            lock.unlock();
        }
    }
}
