package com.jvckenwood.mhl.commlib.internal;

import com.jvckenwood.mhl.commlib.IMHLEventListener;
import com.jvckenwood.mhl.commlib.MHLHandler;
import com.jvckenwood.mhl.commlib.tools.AppLog;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public class FrameReceiverThread {
    private static final String TAG = FrameReceiverThread.class.getSimpleName();
    private final Runnable _frameReaderProc = new Runnable() { // from class: com.jvckenwood.mhl.commlib.internal.FrameReceiverThread.1
        @Override // java.lang.Runnable
        public void run() {
            while (true) {
                try {
                    try {
                        byte[] message = FrameReceiverThread.this._reader.read();
                        try {
                            if (message == null) {
                                try {
                                    break;
                                } catch (IOException e) {
                                    AppLog.e(FrameReceiverThread.TAG, e.getMessage());
                                    if (FrameReceiverThread.this._listener != null) {
                                        FrameReceiverThread.this._listener.onError(FrameReceiverThread.this._handler, e);
                                    }
                                    if (FrameReceiverThread.this._listener != null) {
                                        FrameReceiverThread.this._listener.onDisconnected(FrameReceiverThread.this._handler);
                                        return;
                                    }
                                    return;
                                }
                            }
                            if (FrameReceiverThread.this._listener != null) {
                                FrameReceiverThread.this._listener.onMessageReceived(FrameReceiverThread.this._handler, message);
                            }
                        } catch (Throwable th) {
                            if (FrameReceiverThread.this._listener != null) {
                                FrameReceiverThread.this._listener.onDisconnected(FrameReceiverThread.this._handler);
                            }
                            throw th;
                        }
                    } catch (Exception e2) {
                        if (FrameReceiverThread.this._listener != null) {
                            FrameReceiverThread.this._listener.onError(FrameReceiverThread.this._handler, e2);
                        }
                        try {
                            try {
                                FrameReceiverThread.this._reader.close();
                                if (FrameReceiverThread.this._listener != null) {
                                    FrameReceiverThread.this._listener.onDisconnected(FrameReceiverThread.this._handler);
                                    return;
                                }
                                return;
                            } catch (Throwable th2) {
                                if (FrameReceiverThread.this._listener != null) {
                                    FrameReceiverThread.this._listener.onDisconnected(FrameReceiverThread.this._handler);
                                }
                                throw th2;
                            }
                        } catch (IOException e3) {
                            AppLog.e(FrameReceiverThread.TAG, e3.getMessage());
                            if (FrameReceiverThread.this._listener != null) {
                                FrameReceiverThread.this._listener.onError(FrameReceiverThread.this._handler, e3);
                            }
                            if (FrameReceiverThread.this._listener != null) {
                                FrameReceiverThread.this._listener.onDisconnected(FrameReceiverThread.this._handler);
                                return;
                            }
                            return;
                        }
                    }
                } catch (Throwable th3) {
                    try {
                        try {
                            FrameReceiverThread.this._reader.close();
                            if (FrameReceiverThread.this._listener != null) {
                                FrameReceiverThread.this._listener.onDisconnected(FrameReceiverThread.this._handler);
                            }
                        } finally {
                            if (FrameReceiverThread.this._listener != null) {
                                FrameReceiverThread.this._listener.onDisconnected(FrameReceiverThread.this._handler);
                            }
                        }
                    } catch (IOException e4) {
                        AppLog.e(FrameReceiverThread.TAG, e4.getMessage());
                        if (FrameReceiverThread.this._listener != null) {
                            FrameReceiverThread.this._listener.onError(FrameReceiverThread.this._handler, e4);
                        }
                        if (FrameReceiverThread.this._listener != null) {
                            FrameReceiverThread.this._listener.onDisconnected(FrameReceiverThread.this._handler);
                        }
                    }
                    throw th3;
                }
            }
            FrameReceiverThread.this._reader.close();
        }
    };
    private final MHLHandler _handler;
    private final IMHLEventListener _listener;
    private final FrameReader _reader;
    private Thread _thread;

    public FrameReceiverThread(MHLHandler handler, InputStream stream, IMHLEventListener listener) {
        if (stream == null) {
            throw new IllegalArgumentException();
        }
        this._handler = handler;
        this._reader = new FrameReader(stream);
        this._listener = listener;
    }

    public boolean start() {
        if (this._thread != null && this._thread.isAlive()) {
            return false;
        }
        this._thread = new Thread(this._frameReaderProc);
        this._thread.setName(TAG);
        this._thread.setDaemon(true);
        this._thread.start();
        return true;
    }
}
