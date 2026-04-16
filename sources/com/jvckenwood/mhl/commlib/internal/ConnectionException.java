package com.jvckenwood.mhl.commlib.internal;

/* JADX INFO: loaded from: classes.dex */
public class ConnectionException extends Exception {
    private static final String TAG = ConnectionException.class.getSimpleName();
    private final Exception _innerExceptin;

    public ConnectionException(Exception inner) {
        this._innerExceptin = inner;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return this._innerExceptin == null ? super.getMessage() : TAG + ":" + this._innerExceptin.getMessage();
    }

    @Override // java.lang.Throwable
    public String toString() {
        return this._innerExceptin == null ? super.toString() : TAG + ":" + this._innerExceptin.toString();
    }
}
