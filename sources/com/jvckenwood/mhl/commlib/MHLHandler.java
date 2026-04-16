package com.jvckenwood.mhl.commlib;

import com.jvckenwood.mhl.commlib.internal.ConnectionHandlerThread;
import com.jvckenwood.mhl.commlib.internal.Message;
import com.jvckenwood.mhl.commlib.internal.MessageQueue;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class MHLHandler {
    private final ConnectionHandlerThread _connection;
    private MessageQueue _queue = null;
    private IMHLEventListener _clientListener = null;
    private final IMHLEventListener _wrapperListener = new IMHLEventListener() { // from class: com.jvckenwood.mhl.commlib.MHLHandler.1
        @Override // com.jvckenwood.mhl.commlib.IMHLEventListener
        public void onConnected(MHLHandler sender, Map<String, Object> properties) {
            if (MHLHandler.this._clientListener != null) {
                MHLHandler.this._clientListener.onConnected(sender, properties);
            }
        }

        @Override // com.jvckenwood.mhl.commlib.IMHLEventListener
        public void onDisconnected(MHLHandler sender) {
            if (MHLHandler.this._clientListener != null) {
                MHLHandler.this._clientListener.onDisconnected(sender);
            }
        }

        @Override // com.jvckenwood.mhl.commlib.IMHLEventListener
        public void onMessageReceived(MHLHandler sender, byte[] message) {
            if (MHLHandler.this._clientListener != null) {
                MHLHandler.this._clientListener.onMessageReceived(sender, message);
            }
        }

        @Override // com.jvckenwood.mhl.commlib.IMHLEventListener
        public void onError(MHLHandler sender, Exception exception) {
            if (MHLHandler.this._clientListener != null) {
                MHLHandler.this._clientListener.onError(sender, exception);
            }
        }
    };

    public MHLHandler(IMHLSessionListener listener) {
        this._connection = new ConnectionHandlerThread(this, listener);
    }

    public boolean startListening(IMHLEventListener listener) {
        if (this._connection.isAlive()) {
            return false;
        }
        this._clientListener = listener;
        this._queue = new MessageQueue();
        return this._connection.start(this._wrapperListener, this._queue);
    }

    public void stopListening() {
        if (this._connection.isAlive() || this._connection.isConnected()) {
            this._connection.cancel();
        }
    }

    public boolean isConnected() {
        return this._connection.isConnected();
    }

    public boolean isAlive() {
        if (this._connection.isAlive()) {
            return true;
        }
        return this._connection.isConnected();
    }

    public boolean sendMessage(byte[] message) {
        if (!isConnected() || this._queue == null) {
            return false;
        }
        this._queue.putMessage(new Message(message));
        return true;
    }

    public void cancelSending() {
        if (this._queue != null) {
            this._queue.clear();
        }
    }
}
