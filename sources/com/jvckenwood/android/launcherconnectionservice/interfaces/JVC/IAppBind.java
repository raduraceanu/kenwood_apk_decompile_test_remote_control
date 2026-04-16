package com.jvckenwood.android.launcherconnectionservice.interfaces.JVC;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager;
import com.jvckenwood.android.launcherconnectionservice.interfaces.JVC.IApp;

/* JADX INFO: loaded from: classes.dex */
public interface IAppBind extends IInterface {
    IApp startBind() throws RemoteException;

    INecConnectionManager startBindForNec() throws RemoteException;

    public static abstract class Stub extends Binder implements IAppBind {
        private static final String DESCRIPTOR = "com.jvckenwood.android.launcherconnectionservice.interfaces.JVC.IAppBind";
        static final int TRANSACTION_startBind = 1;
        static final int TRANSACTION_startBindForNec = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAppBind asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAppBind)) {
                return (IAppBind) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IApp _result = startBind();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result != null ? _result.asBinder() : null);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    INecConnectionManager _result2 = startBindForNec();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result2 != null ? _result2.asBinder() : null);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IAppBind {
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

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.JVC.IAppBind
            public IApp startBind() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    IApp _result = IApp.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.JVC.IAppBind
            public INecConnectionManager startBindForNec() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    INecConnectionManager _result = INecConnectionManager.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
