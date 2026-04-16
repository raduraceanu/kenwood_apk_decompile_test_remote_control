package com.jvckenwood.mhl.commlib.internal;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
public class FrameWriter {
    private final BufferedOutputStream _stream;

    public FrameWriter(OutputStream stream) {
        if (stream == null) {
            throw new IllegalArgumentException();
        }
        this._stream = new BufferedOutputStream(stream);
    }

    public void write(byte[] message) throws IOException {
        this._stream.write(MHLSpecs.START_SYNC);
        this._stream.write(new Frame(message).escapeEncode());
        this._stream.write(MHLSpecs.END_SYNC);
        this._stream.flush();
    }

    public void close() throws IOException {
        this._stream.close();
    }
}
