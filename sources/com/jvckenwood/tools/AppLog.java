package com.jvckenwood.tools;

/* JADX INFO: loaded from: classes.dex */
public class AppLog {
    private static IImplement _Implement = null;

    public interface IImplement {
        int d(String str, String str2);

        int d(String str, String str2, Throwable th);

        int e(String str, String str2);

        int e(String str, String str2, Throwable th);

        int i(String str, String str2);

        int i(String str, String str2, Throwable th);

        int v(String str, String str2);

        int v(String str, String str2, Throwable th);

        int w(String str, String str2);

        int w(String str, String str2, Throwable th);
    }

    public static IImplement setImplement(IImplement impl) {
        IImplement old = _Implement;
        _Implement = impl;
        return old;
    }

    public static IImplement getImplement() {
        return _Implement;
    }

    public static int e(String tag, String msg) {
        if (_Implement == null) {
            return 0;
        }
        return _Implement.e(tag, msg);
    }

    public static int w(String tag, String msg) {
        if (_Implement == null) {
            return 0;
        }
        return _Implement.w(tag, msg);
    }

    public static int i(String tag, String msg) {
        if (_Implement == null) {
            return 0;
        }
        return _Implement.i(tag, msg);
    }

    public static int d(String tag, String msg) {
        if (_Implement == null) {
            return 0;
        }
        return _Implement.d(tag, msg);
    }

    public static int v(String tag, String msg) {
        if (_Implement == null) {
            return 0;
        }
        return _Implement.v(tag, msg);
    }

    public static int e(String tag, String msg, Throwable err) {
        if (_Implement == null) {
            return 0;
        }
        return _Implement.e(tag, msg, err);
    }

    public static int w(String tag, String msg, Throwable err) {
        if (_Implement == null) {
            return 0;
        }
        return _Implement.w(tag, msg, err);
    }

    public static int i(String tag, String msg, Throwable err) {
        if (_Implement == null) {
            return 0;
        }
        return _Implement.i(tag, msg, err);
    }

    public static int d(String tag, String msg, Throwable err) {
        if (_Implement == null) {
            return 0;
        }
        return _Implement.d(tag, msg, err);
    }

    public static int v(String tag, String msg, Throwable err) {
        if (_Implement == null) {
            return 0;
        }
        return _Implement.v(tag, msg, err);
    }
}
