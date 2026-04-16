package com.jvckenwood.mhl.commlib.internal;

import java.io.ByteArrayOutputStream;

/* JADX INFO: loaded from: classes.dex */
public class Frame {
    private final byte[] _frame;

    public Frame(byte[] frame) {
        if (frame == null) {
            throw new IllegalArgumentException();
        }
        this._frame = frame;
    }

    public byte[] escapeDecode() {
        ByteArrayOutputStream decode = new ByteArrayOutputStream();
        int i = 0;
        while (i < this._frame.length) {
            int data = this._frame[i] & 255;
            if (data == 253) {
                if (i < this._frame.length - 1) {
                    switch (this._frame[i + 1] & 255) {
                        case 92:
                            data = MHLSpecs.END_SYNC;
                            i++;
                            decode.write(data);
                            break;
                        case 93:
                            data = MHLSpecs.ESC_HEAD;
                            i++;
                            decode.write(data);
                            break;
                        case 94:
                            data = MHLSpecs.START_SYNC;
                            i++;
                            decode.write(data);
                            break;
                    }
                } else {
                    return decode.toByteArray();
                }
            } else {
                decode.write(data);
            }
            i++;
        }
        return decode.toByteArray();
    }

    public byte[] escapeEncode() {
        ByteArrayOutputStream encode = new ByteArrayOutputStream();
        for (int i = 0; i < this._frame.length; i++) {
            int data = this._frame[i] & 255;
            switch (data) {
                case MHLSpecs.END_SYNC /* 252 */:
                    encode.write(MHLSpecs.ESC_HEAD);
                    encode.write(92);
                    break;
                case MHLSpecs.ESC_HEAD /* 253 */:
                    encode.write(MHLSpecs.ESC_HEAD);
                    encode.write(93);
                    break;
                case MHLSpecs.START_SYNC /* 254 */:
                    encode.write(MHLSpecs.ESC_HEAD);
                    encode.write(94);
                    break;
                default:
                    encode.write(data);
                    break;
            }
        }
        return encode.toByteArray();
    }
}
