package com.jvckenwood.android.launcherconnectionservice.impl.sessions;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Looper;
import android.text.TextUtils;
import com.jvckenwood.mhl.commlib.IMHLSession;
import com.jvckenwood.mhl.commlib.IMHLSessionListener;
import com.jvckenwood.tools.AppLog;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class BluetoothSessionListener implements IMHLSessionListener {
    private static final String TAG = BluetoothSessionListener.class.getSimpleName();
    private final UUID SPP_SERVICE_UUID;
    private final String SSP_SERVICE_NAME;
    private final ServerSocketVariable _serverSocket = new ServerSocketVariable();

    public BluetoothSessionListener(String name, String uuid) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(uuid)) {
            throw new IllegalArgumentException();
        }
        this.SSP_SERVICE_NAME = name;
        this.SPP_SERVICE_UUID = UUID.fromString(uuid);
    }

    class ServerSocketVariable {
        private BluetoothServerSocket _serverSocket = null;

        ServerSocketVariable() {
        }

        public synchronized void init() throws IOException {
            clear();
            this._serverSocket = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord(BluetoothSessionListener.this.SSP_SERVICE_NAME, BluetoothSessionListener.this.SPP_SERVICE_UUID);
        }

        public synchronized void clear() {
            BluetoothServerSocket last = this._serverSocket;
            this._serverSocket = null;
            if (last != null) {
                try {
                    last.close();
                } catch (IOException e) {
                    AppLog.e(BluetoothSessionListener.TAG, e.toString());
                }
            }
        }

        public synchronized BluetoothServerSocket get() {
            return this._serverSocket;
        }
    }

    @Override // com.jvckenwood.mhl.commlib.IMHLSessionListener
    public void init() throws IOException {
        Looper.prepare();
        this._serverSocket.init();
    }

    @Override // com.jvckenwood.mhl.commlib.IMHLSessionListener
    public IMHLSession listen() throws IOException {
        AppLog.d(TAG, "listen() in");
        try {
            BluetoothServerSocket sockServer = this._serverSocket.get();
            if (sockServer == null) {
                throw new IOException("Connection Listener Init Error");
            }
            AppLog.d(TAG, "Before accept");
            BluetoothSocket socket = sockServer.accept();
            AppLog.d(TAG, "After accept");
            this._serverSocket.clear();
            BluetoothSession session = new BluetoothSession(socket);
            BluetoothDevice device = socket.getRemoteDevice();
            if (device != null) {
                Map<String, Object> props = session.getProperties();
                props.put("Name", device.getName());
                props.put("Address", device.getAddress());
                props.put("BluetoothClass", device.getBluetoothClass());
                props.put("BondState", Integer.valueOf(device.getBondState()));
            }
            return session;
        } finally {
            AppLog.d(TAG, "listen() out");
            this._serverSocket.clear();
        }
    }

    @Override // com.jvckenwood.mhl.commlib.IMHLSessionListener
    public void cancelListen() {
        this._serverSocket.clear();
    }
}
