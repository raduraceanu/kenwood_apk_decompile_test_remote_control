package com.jvckenwood.mhl.commlib.tools;

/* JADX INFO: loaded from: classes.dex */
public class SyncObject<T> {
    private T _object = null;

    public synchronized T get() {
        return this._object;
    }

    public synchronized void set(T object) {
        this._object = object;
    }
}
