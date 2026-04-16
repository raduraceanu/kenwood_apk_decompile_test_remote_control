package com.jvckenwood.android.launcherconnectionservice.interfaces;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.jvckenwood.carconnectcontrol.lib.api.info.IACommandInfo;
import com.jvckenwood.carconnectcontrol.lib.api.info.IAMultitouchInfo;
import com.jvckenwood.carconnectcontrol.lib.api.info.IAResponseInfo;
import com.jvckenwood.carconnectcontrol.lib.api.info.VCanInfo;
import com.jvckenwood.carconnectcontrol.lib.api.info.VDriveInfo;
import com.jvckenwood.carconnectcontrol.lib.api.info.VLocationInfo;
import com.jvckenwood.carconnectcontrol.lib.api.info.VSensorInfo;

/* JADX INFO: loaded from: classes.dex */
public interface INecConnectionHandler extends IInterface {
    void onCanInfoReceived(VCanInfo vCanInfo) throws RemoteException;

    void onCommandRequestReceived(IACommandInfo iACommandInfo) throws RemoteException;

    void onConnectionChanged(int i) throws RemoteException;

    void onDisplayChanged(int i) throws RemoteException;

    void onDriveInfoReceived(VDriveInfo vDriveInfo) throws RemoteException;

    void onIllumiStateChanged(boolean z) throws RemoteException;

    void onLocationInfoReceived(VLocationInfo vLocationInfo) throws RemoteException;

    void onResultAnnounce(IAResponseInfo iAResponseInfo) throws RemoteException;

    void onResultMultitouch(IAMultitouchInfo iAMultitouchInfo) throws RemoteException;

    void onResultRegisterDriveInfo(IAResponseInfo iAResponseInfo) throws RemoteException;

    void onResultRegisterMultitouch(IAResponseInfo iAResponseInfo) throws RemoteException;

    void onResultSendCommand(IAResponseInfo iAResponseInfo) throws RemoteException;

    void onResultSendSpInfo(IAResponseInfo iAResponseInfo) throws RemoteException;

    void onResultSwitchOffAudioSource(IAResponseInfo iAResponseInfo) throws RemoteException;

    void onResultSwitchOnAudioSource(IAResponseInfo iAResponseInfo) throws RemoteException;

    void onResultUnregisterDriveInfo(IAResponseInfo iAResponseInfo) throws RemoteException;

    void onResultUnregisterMultitouch(IAResponseInfo iAResponseInfo) throws RemoteException;

    void onRunningStateChanged(boolean z) throws RemoteException;

    void onSensorInfoReceived(VSensorInfo vSensorInfo) throws RemoteException;

    void onServiceConnected(int i) throws RemoteException;

    void onServiceDisconnected(int i) throws RemoteException;

    void onSyncImeReceived() throws RemoteException;

    public static abstract class Stub extends Binder implements INecConnectionHandler {
        private static final String DESCRIPTOR = "com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler";
        static final int TRANSACTION_onCanInfoReceived = 10;
        static final int TRANSACTION_onCommandRequestReceived = 5;
        static final int TRANSACTION_onConnectionChanged = 3;
        static final int TRANSACTION_onDisplayChanged = 4;
        static final int TRANSACTION_onDriveInfoReceived = 9;
        static final int TRANSACTION_onIllumiStateChanged = 22;
        static final int TRANSACTION_onLocationInfoReceived = 11;
        static final int TRANSACTION_onResultAnnounce = 18;
        static final int TRANSACTION_onResultMultitouch = 20;
        static final int TRANSACTION_onResultRegisterDriveInfo = 7;
        static final int TRANSACTION_onResultRegisterMultitouch = 14;
        static final int TRANSACTION_onResultSendCommand = 6;
        static final int TRANSACTION_onResultSendSpInfo = 13;
        static final int TRANSACTION_onResultSwitchOffAudioSource = 17;
        static final int TRANSACTION_onResultSwitchOnAudioSource = 16;
        static final int TRANSACTION_onResultUnregisterDriveInfo = 8;
        static final int TRANSACTION_onResultUnregisterMultitouch = 15;
        static final int TRANSACTION_onRunningStateChanged = 21;
        static final int TRANSACTION_onSensorInfoReceived = 12;
        static final int TRANSACTION_onServiceConnected = 1;
        static final int TRANSACTION_onServiceDisconnected = 2;
        static final int TRANSACTION_onSyncImeReceived = 19;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INecConnectionHandler asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof INecConnectionHandler)) {
                return (INecConnectionHandler) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0;
            IAMultitouchInfo _arg02;
            IAResponseInfo _arg03;
            IAResponseInfo _arg04;
            IAResponseInfo _arg05;
            IAResponseInfo _arg06;
            IAResponseInfo _arg07;
            IAResponseInfo _arg08;
            VSensorInfo _arg09;
            VLocationInfo _arg010;
            VCanInfo _arg011;
            VDriveInfo _arg012;
            IAResponseInfo _arg013;
            IAResponseInfo _arg014;
            IAResponseInfo _arg015;
            IACommandInfo _arg016;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onServiceConnected(data.readInt());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onServiceDisconnected(data.readInt());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    onConnectionChanged(data.readInt());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    onDisplayChanged(data.readInt());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg016 = IACommandInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg016 = null;
                    }
                    onCommandRequestReceived(_arg016);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg015 = IAResponseInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg015 = null;
                    }
                    onResultSendCommand(_arg015);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg014 = IAResponseInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg014 = null;
                    }
                    onResultRegisterDriveInfo(_arg014);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg013 = IAResponseInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg013 = null;
                    }
                    onResultUnregisterDriveInfo(_arg013);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg012 = VDriveInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg012 = null;
                    }
                    onDriveInfoReceived(_arg012);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg011 = VCanInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg011 = null;
                    }
                    onCanInfoReceived(_arg011);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg010 = VLocationInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg010 = null;
                    }
                    onLocationInfoReceived(_arg010);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg09 = VSensorInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg09 = null;
                    }
                    onSensorInfoReceived(_arg09);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg08 = IAResponseInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg08 = null;
                    }
                    onResultSendSpInfo(_arg08);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg07 = IAResponseInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg07 = null;
                    }
                    onResultRegisterMultitouch(_arg07);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = IAResponseInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    onResultUnregisterMultitouch(_arg06);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = IAResponseInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    onResultSwitchOnAudioSource(_arg05);
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = IAResponseInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    onResultSwitchOffAudioSource(_arg04);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = IAResponseInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    onResultAnnounce(_arg03);
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    onSyncImeReceived();
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = IAMultitouchInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    onResultMultitouch(_arg02);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    onRunningStateChanged(_arg0);
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    onIllumiStateChanged(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements INecConnectionHandler {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onServiceConnected(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onServiceDisconnected(int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onConnectionChanged(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onDisplayChanged(int kind) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(kind);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onCommandRequestReceived(IACommandInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onResultSendCommand(IAResponseInfo response) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (response != null) {
                        _data.writeInt(1);
                        response.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onResultRegisterDriveInfo(IAResponseInfo response) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (response != null) {
                        _data.writeInt(1);
                        response.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onResultUnregisterDriveInfo(IAResponseInfo response) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (response != null) {
                        _data.writeInt(1);
                        response.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onDriveInfoReceived(VDriveInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onCanInfoReceived(VCanInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onLocationInfoReceived(VLocationInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onSensorInfoReceived(VSensorInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onResultSendSpInfo(IAResponseInfo response) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (response != null) {
                        _data.writeInt(1);
                        response.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onResultRegisterMultitouch(IAResponseInfo response) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (response != null) {
                        _data.writeInt(1);
                        response.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onResultUnregisterMultitouch(IAResponseInfo response) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (response != null) {
                        _data.writeInt(1);
                        response.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onResultSwitchOnAudioSource(IAResponseInfo response) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (response != null) {
                        _data.writeInt(1);
                        response.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onResultSwitchOffAudioSource(IAResponseInfo response) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (response != null) {
                        _data.writeInt(1);
                        response.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onResultAnnounce(IAResponseInfo response) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (response != null) {
                        _data.writeInt(1);
                        response.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onSyncImeReceived() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onResultMultitouch(IAMultitouchInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onRunningStateChanged(boolean isRunning) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isRunning ? 1 : 0);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler
            public void onIllumiStateChanged(boolean state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state ? 1 : 0);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
