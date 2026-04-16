package com.jvckenwood.android.launcherconnectionservice.impl.sessions;

import android.bluetooth.BluetoothSocket;
import com.jvckenwood.mhl.commlib.IMHLSession;
import com.jvckenwood.mhl.commlib.tools.AppLog;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class BluetoothSession implements IMHLSession {
    private static final String TAG = BluetoothSession.class.getSimpleName();
    private InputStream _input;
    private OutputStream _output;
    private BluetoothSocket _socket;
    private final Object _syncObject = new Object();
    private final Map<String, Object> _properties = new HashMap();

    class InputStreamWrapper extends InputStream {
        private final String TAG = InputStreamWrapper.class.getSimpleName();
        private final InputStream _stream;

        public InputStreamWrapper(InputStream stream) {
            this._stream = stream;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            return this._stream.read();
        }

        @Override // java.io.InputStream
        public int read(byte[] buffer, int offset, int length) throws IOException {
            return this._stream.read(buffer, offset, length);
        }

        @Override // java.io.InputStream
        public int read(byte[] b) throws IOException {
            return this._stream.read(b);
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            synchronized (BluetoothSession.this._syncObject) {
                if (BluetoothSession.this._socket != null) {
                    BluetoothSession.this._socket.close();
                    BluetoothSession.this._socket = null;
                }
            }
        }
    }

    class OutputStreamWrapper extends OutputStream {
        private final String TAG = OutputStreamWrapper.class.getSimpleName();
        private final OutputStream _stream;

        public OutputStreamWrapper(OutputStream stream) {
            this._stream = stream;
        }

        @Override // java.io.OutputStream
        public void write(int value) throws IOException {
            this._stream.write(value);
        }

        @Override // java.io.OutputStream
        public void write(byte[] buffer, int offset, int count) throws IOException {
            this._stream.write(buffer, offset, count);
        }

        @Override // java.io.OutputStream
        public void write(byte[] buffer) throws IOException {
            this._stream.write(buffer);
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            this._stream.flush();
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            synchronized (BluetoothSession.this._syncObject) {
                if (BluetoothSession.this._socket != null) {
                    BluetoothSession.this._socket.close();
                    BluetoothSession.this._socket = null;
                }
            }
        }
    }

    public BluetoothSession(BluetoothSocket socket) {
        this._socket = null;
        this._input = null;
        this._output = null;
        try {
            this._socket = socket;
            this._input = new InputStreamWrapper(socket.getInputStream());
            this._output = new OutputStreamWrapper(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            AppLog.e(TAG, e.toString());
        }
    }

    @Override // com.jvckenwood.mhl.commlib.IMHLSession
    public InputStream getInputStream() {
        return this._input;
    }

    @Override // com.jvckenwood.mhl.commlib.IMHLSession
    public OutputStream getOutputStream() {
        return this._output;
    }

    @Override // com.jvckenwood.mhl.commlib.IMHLSession
    public Map<String, Object> getProperties() {
        return this._properties;
    }
}
