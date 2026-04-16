package com.jvckenwood.applauncher.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.carconnectcontrol.AppLauncherApplication;

/* JADX INFO: loaded from: classes.dex */
public class SettingsManager {
    private static SettingsManager _instance = null;
    private final String KEY_PREFERENCE_AUTO_APP_CHANGE = "key_preference_auto_app_change";
    private ICooperationLib _ICoopLib;
    private Context _context;

    private SettingsManager(Context context) {
        this._ICoopLib = null;
        this._context = null;
        this._context = context;
        this._ICoopLib = AppLauncherApplication.getLib(context);
    }

    public static SettingsManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new SettingsManager(context);
        }
        return _instance;
    }

    public String getVersionName() throws PackageManager.NameNotFoundException {
        PackageManager pm = this._context.getPackageManager();
        PackageInfo pinfo = pm.getPackageInfo(this._context.getPackageName(), 1);
        String version = pinfo.versionName;
        return version;
    }

    public void setCommEnabled(boolean isEnabled) {
        this._ICoopLib.setCommEnabled(isEnabled);
    }

    public boolean getCommEnabled() {
        return this._ICoopLib.isCommEnabled();
    }

    public void setAutoAppChangerEnabled(boolean isEnabled) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this._context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("key_preference_auto_app_change", isEnabled);
        editor.commit();
    }

    public boolean getAutoAppChangerEnabled() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this._context);
        boolean result = sharedPreferences.getBoolean("key_preference_auto_app_change", true);
        return result;
    }
}
