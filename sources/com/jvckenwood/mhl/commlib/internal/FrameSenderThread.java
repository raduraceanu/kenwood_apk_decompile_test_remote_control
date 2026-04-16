package com.jvckenwood.mhl.commlib.internal;

import com.jvckenwood.mhl.commlib.IMHLEventListener;
import com.jvckenwood.mhl.commlib.MHLHandler;
import com.jvckenwood.mhl.commlib.tools.AppLog;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
public class FrameSenderThread {
    private static final String TAG = FrameSenderThread.class.getSimpleName();
    private final MHLHandler _handler;
    private final IMHLEventListener _listener;
    private final MessageQueue _queue;
    private final FrameWriter _writer;
    private Thread _thread = null;
    private final Runnable _frameWriterProc = new Runnable() { // from class: com.jvckenwood.mhl.commlib.internal.FrameSenderThread.1
        @Override // java.lang.Runnable
        public void run() {
            while (true) {
                try {
                    try {
                        Message message = FrameSenderThread.this._queue.getMessage();
                        try {
                            if (message == null) {
                                try {
                                    break;
                                } catch (IOException e) {
                                    AppLog.e(FrameSenderThread.TAG, e.getMessage());
                                    if (FrameSenderThread.this._listener != null) {
                                        FrameSenderThread.this._listener.onError(FrameSenderThread.this._handler, e);
                                    }
                                    if (FrameSenderThread.this._listener != null) {
                                        FrameSenderThread.this._listener.onDisconnected(FrameSenderThread.this._handler);
                                        return;
                                    }
                                    return;
                                }
                            }
                            FrameSenderThread.this._writer.write(message.getMessage());
                        } catch (Throwable th) {
                            if (FrameSenderThread.this._listener != null) {
                                FrameSenderThread.this._listener.onDisconnected(FrameSenderThread.this._handler);
                            }
                            throw th;
                        }
                    } catch (Exception e2) {
                        AppLog.e(FrameSenderThread.TAG, e2.getMessage());
                        if (FrameSenderThread.this._listener != null) {
                            FrameSenderThread.this._listener.onError(FrameSenderThread.this._handler, e2);
                        }
                        try {
                            try {
                                FrameSenderThread.this._writer.close();
                                if (FrameSenderThread.this._listener != null) {
                                    FrameSenderThread.this._listener.onDisconnected(FrameSenderThread.this._handler);
                                    return;
                                }
                                return;
                            } catch (IOException e3) {
                                AppLog.e(FrameSenderThread.TAG, e3.getMessage());
                                if (FrameSenderThread.this._listener != null) {
                                    FrameSenderThread.this._listener.onError(FrameSenderThread.this._handler, e3);
                                }
                                if (FrameSenderThread.this._listener != null) {
                                    FrameSenderThread.this._listener.onDisconnected(FrameSenderThread.this._handler);
                                    return;
                                }
                                return;
                            }
                        } catch (Throwable th2) {
                            if (FrameSenderThread.this._listener != null) {
                                FrameSenderThread.this._listener.onDisconnected(FrameSenderThread.this._handler);
                            }
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    try {
                        try {
                            FrameSenderThread.this._writer.close();
                            if (FrameSenderThread.this._listener != null) {
                                FrameSenderThread.this._listener.onDisconnected(FrameSenderThread.this._handler);
                            }
                        } catch (IOException e4) {
                            AppLog.e(FrameSenderThread.TAG, e4.getMessage());
                            if (FrameSenderThread.this._listener != null) {
                                FrameSenderThread.this._listener.onError(FrameSenderThread.this._handler, e4);
                            }
                            if (FrameSenderThread.this._listener != null) {
                                FrameSenderThread.this._listener.onDisconnected(FrameSenderThread.this._handler);
                            }
                        }
                        throw th3;
                    } finally {
                        if (FrameSenderThread.this._listener != null) {
                            FrameSenderThread.this._listener.onDisconnected(FrameSenderThread.this._handler);
                        }
                    }
                }
            }
            FrameSenderThread.this._writer.close();
        }
    };

    public FrameSenderThread(MHLHandler handler, OutputStream stream, MessageQueue queue, IMHLEventListener listener) {
        if (stream == null || queue == null) {
            throw new IllegalArgumentException();
        }
        this._handler = handler;
        this._writer = new FrameWriter(stream);
        this._queue = queue;
        this._listener = listener;
    }

    public boolean start() {
        if (this._thread != null && this._thread.isAlive()) {
            return false;
        }
        this._thread = new Thread(this._frameWriterProc);
        this._thread.setName(TAG);
        this._thread.setDaemon(true);
        this._thread.start();
        return true;
    }
}
