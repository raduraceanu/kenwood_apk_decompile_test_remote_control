package com.jvckenwood.android.launcherconnectionservice.impl;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/* JADX INFO: loaded from: classes.dex */
public class ConnectionServiceModel {
    private static final int C_DEBUG_MODE_DEFAULT_VALUE = 0;
    private static final String C_DEBUG_MODE_PREFS_KEY = "debugMode";
    private static final boolean C_IS_COMM_ENABLED_DEFAULT_VALUE = true;
    private static final String C_IS_COMM_ENABLED_PREFS_KEY = "isCommEnabled";
    private Context _context = null;
    private boolean _isCommEnabled = C_IS_COMM_ENABLED_DEFAULT_VALUE;
    private int _debugMode = 0;

    public void initialize(Context context) {
        this._context = context;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this._context);
        this._isCommEnabled = prefs.getBoolean(C_IS_COMM_ENABLED_PREFS_KEY, C_IS_COMM_ENABLED_DEFAULT_VALUE);
        this._debugMode = prefs.getInt(C_DEBUG_MODE_PREFS_KEY, 0);
    }

    public void terminate() {
        this._context = null;
    }

    public boolean isCommEnabled() {
        return this._isCommEnabled;
    }

    public boolean setCommEnabled(boolean sw) {
        boolean z = C_IS_COMM_ENABLED_DEFAULT_VALUE;
        if (sw == this._isCommEnabled) {
            return false;
        }
        this._isCommEnabled = sw;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this._context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(C_IS_COMM_ENABLED_PREFS_KEY, this._isCommEnabled);
        if (editor.commit()) {
            return C_IS_COMM_ENABLED_DEFAULT_VALUE;
        }
        if (sw) {
            z = false;
        }
        this._isCommEnabled = z;
        return false;
    }

    public boolean isBluetoothEnabled() {
        BluetoothAdapter adap = BluetoothAdapter.getDefaultAdapter();
        if (adap == null) {
            return false;
        }
        return adap.isEnabled();
    }

    public int getDebugMode() {
        return this._debugMode;
    }

    public boolean setDebugMode(int value) {
        if (value == this._debugMode) {
            return false;
        }
        int orgValue = this._debugMode;
        this._debugMode = value;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this._context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(C_DEBUG_MODE_PREFS_KEY, this._debugMode);
        if (editor.commit()) {
            return C_IS_COMM_ENABLED_DEFAULT_VALUE;
        }
        this._debugMode = orgValue;
        return false;
    }
}
