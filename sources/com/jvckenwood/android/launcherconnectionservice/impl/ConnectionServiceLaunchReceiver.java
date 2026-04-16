package com.jvckenwood.android.launcherconnectionservice.impl;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.jvckenwood.android.launcherconnectionservice.impl.ConnectionService;
import com.jvckenwood.applauncher.managers.AppListManager;
import com.jvckenwood.carconnectcontrol.IntentActions;
import com.jvckenwood.tools.AppLog;

/* JADX INFO: loaded from: classes.dex */
public class ConnectionServiceLaunchReceiver extends BroadcastReceiver {
    private static final String TAG = ConnectionServiceLaunchReceiver.class.getSimpleName();

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
            AppLog.d(TAG, ">>>>> ACTION_BOOT_COMPLETED received <<<<<");
            ConnectionService.sendAction(context, ConnectionService.ActionCommand.RefreshService);
            return;
        }
        if ("android.intent.action.PACKAGE_ADDED".equals(action) || "android.intent.action.PACKAGE_FULLY_REMOVED".equals(action)) {
            String targetPackageName = intent.getDataString();
            if (!TextUtils.isEmpty(targetPackageName)) {
                if (targetPackageName.startsWith("package:")) {
                    targetPackageName = targetPackageName.substring("package:".length());
                }
                AppListManager applistManager = AppListManager.getInstance(context);
                if (applistManager.getAllAppInfoAtPackageName(targetPackageName) != null) {
                    context.sendBroadcast(new Intent(IntentActions.CHANGE_WHITELIST_APP_STATE));
                    return;
                }
                return;
            }
            return;
        }
        if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(action)) {
            AppLog.d(TAG, ">>>>> ACTION_STATE_CHANGED received <<<<<");
            ConnectionService.sendAction(context, ConnectionService.ActionCommand.RefreshService);
        } else if ("android.bluetooth.device.action.ACL_DISCONNECTED".equals(action)) {
            AppLog.d(TAG, ">>>>> ACTION_ACL_DISCONNECTED received <<<<<");
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            if (device != null) {
                String piaAddress = device.getAddress();
                if (!TextUtils.isEmpty(piaAddress)) {
                    ConnectionService.sendActionExtra(context, ConnectionService.ActionCommand.ResetListener, piaAddress);
                }
            }
        }
    }
}
