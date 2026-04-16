package com.jvckenwood.android.launcherconnectionservice.impl.sessions;

import com.jvckenwood.mhl.commlib.IMHLSession;
import com.jvckenwood.mhl.commlib.IMHLSessionListener;
import com.jvckenwood.mhl.commlib.tools.SyncObject;
import com.jvckenwood.tools.AppLog;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class TcpIpSessionListener implements IMHLSessionListener {
    private static final String TAG = TcpIpSessionListener.class.getSimpleName();
    private final int SERVER_SOCKET_PORT;
    private final SyncObject<ServerSocket> _serverSocket = new SyncObject<>();

    public TcpIpSessionListener(int port) {
        this.SERVER_SOCKET_PORT = port;
    }

    @Override // com.jvckenwood.mhl.commlib.IMHLSessionListener
    public void init() throws IOException {
        ServerSocket socket = this._serverSocket.get();
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                AppLog.e(TAG, e.toString());
            }
        }
        this._serverSocket.set(new ServerSocket(this.SERVER_SOCKET_PORT));
    }

    @Override // com.jvckenwood.mhl.commlib.IMHLSessionListener
    public IMHLSession listen() throws IOException {
        ServerSocket socket = null;
        try {
            socket = this._serverSocket.get();
            if (socket == null) {
                throw new IOException();
            }
            final Socket accept = socket.accept();
            return new IMHLSession() { // from class: com.jvckenwood.android.launcherconnectionservice.impl.sessions.TcpIpSessionListener.1
                @Override // com.jvckenwood.mhl.commlib.IMHLSession
                public OutputStream getOutputStream() {
                    try {
                        return accept.getOutputStream();
                    } catch (IOException e) {
                        AppLog.e(TcpIpSessionListener.TAG, e.getMessage());
                        return null;
                    }
                }

                @Override // com.jvckenwood.mhl.commlib.IMHLSession
                public InputStream getInputStream() {
                    try {
                        return accept.getInputStream();
                    } catch (IOException e) {
                        AppLog.e(TcpIpSessionListener.TAG, e.getMessage());
                        return null;
                    }
                }

                @Override // com.jvckenwood.mhl.commlib.IMHLSession
                public Map<String, Object> getProperties() {
                    return null;
                }
            };
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    @Override // com.jvckenwood.mhl.commlib.IMHLSessionListener
    public void cancelListen() {
        ServerSocket socket = this._serverSocket.get();
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                AppLog.e(TAG, e.toString());
            }
        }
    }
}
