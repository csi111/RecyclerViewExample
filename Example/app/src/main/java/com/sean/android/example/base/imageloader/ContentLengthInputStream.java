package com.sean.android.example.base.imageloader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Seonil on 2017-03-14.
 */

public class ContentLengthInputStream extends InputStream {

    private final InputStream inputStream;
    private final int length;

    public ContentLengthInputStream(InputStream inputStream, int length) {
        this.inputStream = inputStream;
        this.length = length;
    }

    @Override
    public int available() throws IOException {
        return length;
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return inputStream.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return inputStream.read(b, off, len);
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        inputStream.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        inputStream.reset();
    }

    @Override
    public long skip(long n) throws IOException {
        return inputStream.skip(n);
    }

    @Override
    public boolean markSupported() {
        return inputStream.markSupported();
    }
}
