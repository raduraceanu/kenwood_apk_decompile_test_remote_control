package com.jvckenwood.android.launcherconnectionservice.interfaces;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;
import com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionHandler;
import com.jvckenwood.carconnectcontrol.lib.api.info.AppContentsInfo;
import com.jvckenwood.carconnectcontrol.lib.api.info.IACommandInfo;
import com.jvckenwood.carconnectcontrol.lib.api.info.IAResponseInfo;
import com.jvckenwood.carconnectcontrol.lib.api.info.VCanInfo;
import com.jvckenwood.carconnectcontrol.lib.api.info.VCarSettingInfo;
import com.jvckenwood.carconnectcontrol.lib.api.info.VLocationInfo;
import com.jvckenwood.carconnectcontrol.lib.api.info.VParsedLocationInfo;
import com.jvckenwood.carconnectcontrol.lib.api.info.VSensorInfo;

/* JADX INFO: loaded from: classes.dex */
public interface INecConnectionManager extends IInterface {
    boolean bindCoordinateService(String str, INecConnectionHandler iNecConnectionHandler) throws RemoteException;

    void cancelSyncIme() throws RemoteException;

    int checkSyncIme(CharSequence charSequence, CharSequence charSequence2) throws RemoteException;

    VCanInfo getCanInfo() throws RemoteException;

    VCarSettingInfo getCarSettingInfo() throws RemoteException;

    int getConnectionState() throws RemoteException;

    long getEnabledFlagCanInfo1() throws RemoteException;

    long getEnabledFlagCanInfo2() throws RemoteException;

    long getEnabledFlagCanInfo3() throws RemoteException;

    long getEnabledFlagCanInfo4() throws RemoteException;

    int getEnabledFlagDriveInfo() throws RemoteException;

    int getEnabledFlagLocationInfo() throws RemoteException;

    int getEnabledFlagSensorInfo() throws RemoteException;

    boolean getIllumiState() throws RemoteException;

    VLocationInfo getLocationInfo() throws RemoteException;

    boolean getRunningState() throws RemoteException;

    VSensorInfo getSensorInfo() throws RemoteException;

    boolean hideKeyboard() throws RemoteException;

    boolean isChangeValue(CharSequence charSequence) throws RemoteException;

    VParsedLocationInfo parseLocationInfo(VLocationInfo vLocationInfo) throws RemoteException;

    boolean registerCarInfo(int i, int i2) throws RemoteException;

    boolean registerDriveInfo(boolean z) throws RemoteException;

    boolean registerMultitouch() throws RemoteException;

    boolean sendAnnounce(int i, String str) throws RemoteException;

    boolean sendCommand(IACommandInfo iACommandInfo) throws RemoteException;

    boolean sendCommandResponse(IAResponseInfo iAResponseInfo) throws RemoteException;

    boolean sendSpInfo(AppContentsInfo appContentsInfo) throws RemoteException;

    boolean showKeyboard(int i) throws RemoteException;

    void startSyncIme() throws RemoteException;

    void switchOffAudioSource() throws RemoteException;

    boolean switchOnAudioSource(int i) throws RemoteException;

    void unbindCoordinateService(String str) throws RemoteException;

    void unregisterCarInfo() throws RemoteException;

    void unregisterDriveInfo() throws RemoteException;

    void unregisterMultitouch() throws RemoteException;

    public static abstract class Stub extends Binder implements INecConnectionManager {
        private static final String DESCRIPTOR = "com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager";
        static final int TRANSACTION_bindCoordinateService = 1;
        static final int TRANSACTION_cancelSyncIme = 32;
        static final int TRANSACTION_checkSyncIme = 30;
        static final int TRANSACTION_getCanInfo = 11;
        static final int TRANSACTION_getCarSettingInfo = 10;
        static final int TRANSACTION_getConnectionState = 35;
        static final int TRANSACTION_getEnabledFlagCanInfo1 = 15;
        static final int TRANSACTION_getEnabledFlagCanInfo2 = 16;
        static final int TRANSACTION_getEnabledFlagCanInfo3 = 17;
        static final int TRANSACTION_getEnabledFlagCanInfo4 = 18;
        static final int TRANSACTION_getEnabledFlagDriveInfo = 7;
        static final int TRANSACTION_getEnabledFlagLocationInfo = 19;
        static final int TRANSACTION_getEnabledFlagSensorInfo = 20;
        static final int TRANSACTION_getIllumiState = 34;
        static final int TRANSACTION_getLocationInfo = 12;
        static final int TRANSACTION_getRunningState = 33;
        static final int TRANSACTION_getSensorInfo = 14;
        static final int TRANSACTION_hideKeyboard = 28;
        static final int TRANSACTION_isChangeValue = 31;
        static final int TRANSACTION_parseLocationInfo = 13;
        static final int TRANSACTION_registerCarInfo = 8;
        static final int TRANSACTION_registerDriveInfo = 5;
        static final int TRANSACTION_registerMultitouch = 22;
        static final int TRANSACTION_sendAnnounce = 26;
        static final int TRANSACTION_sendCommand = 3;
        static final int TRANSACTION_sendCommandResponse = 4;
        static final int TRANSACTION_sendSpInfo = 21;
        static final int TRANSACTION_showKeyboard = 27;
        static final int TRANSACTION_startSyncIme = 29;
        static final int TRANSACTION_switchOffAudioSource = 25;
        static final int TRANSACTION_switchOnAudioSource = 24;
        static final int TRANSACTION_unbindCoordinateService = 2;
        static final int TRANSACTION_unregisterCarInfo = 9;
        static final int TRANSACTION_unregisterDriveInfo = 6;
        static final int TRANSACTION_unregisterMultitouch = 23;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INecConnectionManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof INecConnectionManager)) {
                return (INecConnectionManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            CharSequence _arg0;
            CharSequence _arg02;
            CharSequence _arg1;
            AppContentsInfo _arg03;
            VLocationInfo _arg04;
            IAResponseInfo _arg05;
            IACommandInfo _arg06;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    INecConnectionHandler _arg12 = INecConnectionHandler.Stub.asInterface(data.readStrongBinder());
                    boolean _result = bindCoordinateService(_arg07, _arg12);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    unbindCoordinateService(_arg08);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = IACommandInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    boolean _result2 = sendCommand(_arg06);
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = IAResponseInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    boolean _result3 = sendCommandResponse(_arg05);
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg09 = data.readInt() != 0;
                    boolean _result4 = registerDriveInfo(_arg09);
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterDriveInfo();
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _result5 = getEnabledFlagDriveInfo();
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg010 = data.readInt();
                    int _arg13 = data.readInt();
                    boolean _result6 = registerCarInfo(_arg010, _arg13);
                    reply.writeNoException();
                    reply.writeInt(_result6 ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterCarInfo();
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    VCarSettingInfo _result7 = getCarSettingInfo();
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    VCanInfo _result8 = getCanInfo();
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    VLocationInfo _result9 = getLocationInfo();
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        _result9.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = VLocationInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    VParsedLocationInfo _result10 = parseLocationInfo(_arg04);
                    reply.writeNoException();
                    if (_result10 != null) {
                        reply.writeInt(1);
                        _result10.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    VSensorInfo _result11 = getSensorInfo();
                    reply.writeNoException();
                    if (_result11 != null) {
                        reply.writeInt(1);
                        _result11.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    long _result12 = getEnabledFlagCanInfo1();
                    reply.writeNoException();
                    reply.writeLong(_result12);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    long _result13 = getEnabledFlagCanInfo2();
                    reply.writeNoException();
                    reply.writeLong(_result13);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    long _result14 = getEnabledFlagCanInfo3();
                    reply.writeNoException();
                    reply.writeLong(_result14);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    long _result15 = getEnabledFlagCanInfo4();
                    reply.writeNoException();
                    reply.writeLong(_result15);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    int _result16 = getEnabledFlagLocationInfo();
                    reply.writeNoException();
                    reply.writeInt(_result16);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    int _result17 = getEnabledFlagSensorInfo();
                    reply.writeNoException();
                    reply.writeInt(_result17);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = AppContentsInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    boolean _result18 = sendSpInfo(_arg03);
                    reply.writeNoException();
                    reply.writeInt(_result18 ? 1 : 0);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result19 = registerMultitouch();
                    reply.writeNoException();
                    reply.writeInt(_result19 ? 1 : 0);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterMultitouch();
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    boolean _result20 = switchOnAudioSource(_arg011);
                    reply.writeNoException();
                    reply.writeInt(_result20 ? 1 : 0);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    switchOffAudioSource();
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    String _arg14 = data.readString();
                    boolean _result21 = sendAnnounce(_arg012, _arg14);
                    reply.writeNoException();
                    reply.writeInt(_result21 ? 1 : 0);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    boolean _result22 = showKeyboard(_arg013);
                    reply.writeNoException();
                    reply.writeInt(_result22 ? 1 : 0);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result23 = hideKeyboard();
                    reply.writeNoException();
                    reply.writeInt(_result23 ? 1 : 0);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    startSyncIme();
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    int _result24 = checkSyncIme(_arg02, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result24);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    boolean _result25 = isChangeValue(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result25 ? 1 : 0);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    cancelSyncIme();
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result26 = getRunningState();
                    reply.writeNoException();
                    reply.writeInt(_result26 ? 1 : 0);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result27 = getIllumiState();
                    reply.writeNoException();
                    reply.writeInt(_result27 ? 1 : 0);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    int _result28 = getConnectionState();
                    reply.writeNoException();
                    reply.writeInt(_result28);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements INecConnectionManager {
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

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public boolean bindCoordinateService(String packageName, INecConnectionHandler handler) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(handler != null ? handler.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public void unbindCoordinateService(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public boolean sendCommand(IACommandInfo info) throws RemoteException {
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
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public boolean sendCommandResponse(IAResponseInfo info) throws RemoteException {
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
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public boolean registerDriveInfo(boolean isSpeaker) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isSpeaker ? 1 : 0);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public void unregisterDriveInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public int getEnabledFlagDriveInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public boolean registerCarInfo(int type, int canInterval) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(canInterval);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public void unregisterCarInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public VCarSettingInfo getCarSettingInfo() throws RemoteException {
                VCarSettingInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VCarSettingInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public VCanInfo getCanInfo() throws RemoteException {
                VCanInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VCanInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public VLocationInfo getLocationInfo() throws RemoteException {
                VLocationInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VLocationInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public VParsedLocationInfo parseLocationInfo(VLocationInfo info) throws RemoteException {
                VParsedLocationInfo _result;
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
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VParsedLocationInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public VSensorInfo getSensorInfo() throws RemoteException {
                VSensorInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VSensorInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public long getEnabledFlagCanInfo1() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public long getEnabledFlagCanInfo2() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public long getEnabledFlagCanInfo3() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public long getEnabledFlagCanInfo4() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public int getEnabledFlagLocationInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public int getEnabledFlagSensorInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public boolean sendSpInfo(AppContentsInfo info) throws RemoteException {
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
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public boolean registerMultitouch() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public void unregisterMultitouch() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public boolean switchOnAudioSource(int priority) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(priority);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public void switchOffAudioSource() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public boolean sendAnnounce(int priority, String message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(priority);
                    _data.writeString(message);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public boolean showKeyboard(int lang) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(lang);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public boolean hideKeyboard() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public void startSyncIme() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public int checkSyncIme(CharSequence s, CharSequence checker) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (s != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(s, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (checker != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(checker, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public boolean isChangeValue(CharSequence s) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (s != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(s, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public void cancelSyncIme() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public boolean getRunningState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public boolean getIllumiState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager
            public int getConnectionState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
