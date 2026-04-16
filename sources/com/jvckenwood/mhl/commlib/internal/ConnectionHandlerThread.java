package com.jvckenwood.mhl.commlib.internal;

import com.jvckenwood.mhl.commlib.IMHLEventListener;
import com.jvckenwood.mhl.commlib.IMHLSession;
import com.jvckenwood.mhl.commlib.IMHLSessionListener;
import com.jvckenwood.mhl.commlib.MHLHandler;
import com.jvckenwood.mhl.commlib.tools.AppLog;
import com.jvckenwood.mhl.commlib.tools.SyncObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ConnectionHandlerThread {
    private static final String TAG = ConnectionHandlerThread.class.getSimpleName();
    private final MHLHandler _handler;
    private final IMHLSessionListener _sessionListener;
    private final SyncObject<MessageQueue> _queue = new SyncObject<>();
    private IMHLEventListener _eventListener = null;
    private Thread _thread = null;
    private final SyncObject<InputStream> _inputStream = new SyncObject<>();
    private final SyncObject<OutputStream> _outputStream = new SyncObject<>();
    private volatile boolean _isConnected = false;
    private final Runnable _connectionProc = new Runnable() { // from class: com.jvckenwood.mhl.commlib.internal.ConnectionHandlerThread.1
        @Override // java.lang.Runnable
        public void run() {
            try {
                MessageQueue queue = (MessageQueue) ConnectionHandlerThread.this._queue.get();
                if (queue == null) {
                    ConnectionHandlerThread.this._eventListener.onDisconnected(ConnectionHandlerThread.this._handler);
                } else {
                    ConnectionHandlerThread.this._sessionListener.init();
                    IMHLSession session = ConnectionHandlerThread.this._sessionListener.listen();
                    if (session == null) {
                        ConnectionHandlerThread.this._eventListener.onDisconnected(ConnectionHandlerThread.this._handler);
                    } else {
                        InputStream input = session.getInputStream();
                        OutputStream output = session.getOutputStream();
                        if (input == null || output == null) {
                            ConnectionHandlerThread.this._eventListener.onDisconnected(ConnectionHandlerThread.this._handler);
                        } else {
                            ConnectionHandlerThread.this._inputStream.set(input);
                            ConnectionHandlerThread.this._outputStream.set(output);
                            ConnectionHandlerThread.this._wrapListener.onConnected(ConnectionHandlerThread.this._handler, session.getProperties());
                            FrameReceiverThread receiver = new FrameReceiverThread(ConnectionHandlerThread.this._handler, input, ConnectionHandlerThread.this._wrapListener);
                            FrameSenderThread sender = new FrameSenderThread(ConnectionHandlerThread.this._handler, output, queue, ConnectionHandlerThread.this._wrapListener);
                            receiver.start();
                            sender.start();
                        }
                    }
                }
            } catch (IOException e) {
                AppLog.d(ConnectionHandlerThread.TAG, e.getMessage());
                if (ConnectionHandlerThread.this._eventListener != null) {
                    ConnectionException exception = new ConnectionException(e);
                    ConnectionHandlerThread.this._eventListener.onError(ConnectionHandlerThread.this._handler, exception);
                }
            }
        }
    };
    private final IMHLEventListener _wrapListener = new IMHLEventListener() { // from class: com.jvckenwood.mhl.commlib.internal.ConnectionHandlerThread.2
        @Override // com.jvckenwood.mhl.commlib.IMHLEventListener
        public void onConnected(MHLHandler sender, Map<String, Object> properties) {
            ConnectionHandlerThread.this._isConnected = true;
            if (ConnectionHandlerThread.this._eventListener != null) {
                ConnectionHandlerThread.this._eventListener.onConnected(ConnectionHandlerThread.this._handler, properties);
            }
        }

        @Override // com.jvckenwood.mhl.commlib.IMHLEventListener
        public void onDisconnected(MHLHandler sender) {
            if (ConnectionHandlerThread.this._isConnected) {
                ConnectionHandlerThread.this._isConnected = false;
                MessageQueue queue = (MessageQueue) ConnectionHandlerThread.this._queue.get();
                if (queue != null) {
                    queue.putMessage(null);
                }
                InputStream input = (InputStream) ConnectionHandlerThread.this._inputStream.get();
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        AppLog.d(ConnectionHandlerThread.TAG, e.getMessage());
                    }
                }
                if (ConnectionHandlerThread.this._eventListener != null) {
                    ConnectionHandlerThread.this._eventListener.onDisconnected(ConnectionHandlerThread.this._handler);
                }
            }
        }

        @Override // com.jvckenwood.mhl.commlib.IMHLEventListener
        public void onMessageReceived(MHLHandler sender, byte[] message) {
            if (ConnectionHandlerThread.this._eventListener != null) {
                ConnectionHandlerThread.this._eventListener.onMessageReceived(ConnectionHandlerThread.this._handler, message);
            }
        }

        @Override // com.jvckenwood.mhl.commlib.IMHLEventListener
        public void onError(MHLHandler sender, Exception e) {
            if (ConnectionHandlerThread.this._eventListener != null) {
                ConnectionHandlerThread.this._eventListener.onError(ConnectionHandlerThread.this._handler, e);
            }
        }
    };

    public ConnectionHandlerThread(MHLHandler handler, IMHLSessionListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException();
        }
        this._handler = handler;
        this._sessionListener = listener;
    }

    public boolean start(IMHLEventListener listener, MessageQueue queue) {
        if (isAlive()) {
            return false;
        }
        this._eventListener = listener;
        this._isConnected = false;
        this._queue.set(queue);
        this._thread = new Thread(this._connectionProc);
        this._thread.setDaemon(true);
        this._thread.setName(TAG);
        this._thread.start();
        return true;
    }

    public void cancel() {
        if (isAlive() || isConnected()) {
            this._sessionListener.cancelListen();
            InputStream input = this._inputStream.get();
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    AppLog.e(TAG, e.getMessage());
                }
            }
            OutputStream output = this._outputStream.get();
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e2) {
                    AppLog.e(TAG, e2.getMessage());
                }
            }
            MessageQueue queue = this._queue.get();
            if (queue != null) {
                queue.putMessage(null);
            }
        }
    }

    public boolean isAlive() {
        if (this._thread == null) {
            return false;
        }
        return this._thread.isAlive();
    }

    public boolean isConnected() {
        return this._isConnected;
    }
}
