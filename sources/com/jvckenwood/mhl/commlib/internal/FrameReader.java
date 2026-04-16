package com.jvckenwood.mhl.commlib.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public class FrameReader {
    private final InputStream _stream;

    public FrameReader(InputStream stream) {
        if (stream == null) {
            throw new IllegalArgumentException();
        }
        this._stream = stream;
    }

    public byte[] read() throws IOException {
        int data;
        do {
            data = this._stream.read();
            if (data == -1) {
                throw new IOException();
            }
        } while ((data & 255) != 254);
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        while (true) {
            int data2 = this._stream.read();
            if (data2 == -1) {
                throw new IOException();
            }
            if ((data2 & 255) != 252) {
                message.write(data2 & 255);
            } else {
                return new Frame(message.toByteArray()).escapeDecode();
            }
        }
    }

    public void close() throws IOException {
        this._stream.close();
    }
}
