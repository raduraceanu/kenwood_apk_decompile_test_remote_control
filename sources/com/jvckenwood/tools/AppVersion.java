package com.jvckenwood.tools;

import android.content.Context;
import android.content.pm.PackageManager;

/* JADX INFO: loaded from: classes.dex */
public class AppVersion {
    private static final String TAG = AppVersion.class.getSimpleName();
    private static String _appVersion = null;

    private static void init(Context context) {
        try {
            _appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            AppLog.d(TAG, e.toString());
        }
    }

    public static synchronized String getVersionString(Context context) {
        if (_appVersion == null) {
            init(context);
        }
        return _appVersion;
    }
}
