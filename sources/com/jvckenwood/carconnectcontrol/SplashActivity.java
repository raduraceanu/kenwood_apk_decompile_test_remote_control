package com.jvckenwood.carconnectcontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import com.jvckenwood.HID_ThinClient.KWD.R;
import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager;
import com.jvckenwood.tools.AccessibilityUtils;
import com.jvckenwood.tools.AppLog;
import com.jvckenwood.tools.AppVersion;

/* JADX INFO: loaded from: classes.dex */
public class SplashActivity extends Activity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final String ALREADY_CONFIRMED = SplashActivity.class.getName() + ".ALREADY_CONFIRMED";
    private static final String ALREADY_BT_CONFIRMED = SplashActivity.class.getName() + ".ALREADY_BT_CONFIRMED";
    private ICooperationLib lib = null;
    private final int REQUEST_ENABLE_BT = 100;
    private final int REQUEST_ENABLE_ACCESSIBILITY = 200;
    private boolean mAlreadyComfirmed = false;
    private boolean mAlreadyBtComfirmed = false;
    BroadcastReceiver _receiver = new BroadcastReceiver() { // from class: com.jvckenwood.carconnectcontrol.SplashActivity.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context arg0, Intent arg1) {
            SplashActivity.this.unregisterReceiver(this);
            SplashActivity.this.judgeBtSetting();
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        setContentView(R.layout.activity_splash);
        this.lib = AppLauncherApplication.getLib(this);
        this.mAlreadyComfirmed = false;
        this.mAlreadyBtComfirmed = false;
        String version = AppVersion.getVersionString(this);
        TextView view = (TextView) findViewById(R.id.text_version);
        view.setText(getString(R.string.ver_prefix) + " " + version);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        this.lib.setApplicationState(ICooperationLib.ApplicationState.Foreground);
        OverlayViewManager.setForbidOverlayBtnState(this);
        if (this.lib.getCooperationState() == ICooperationLib.CooperationState.None) {
            registerReceiver(this._receiver, new IntentFilter(IntentActions.CHANGE_COOPERATION_STATE));
        } else {
            judgeBtSetting();
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        OverlayViewManager.setAllowOverlayBtnState(this);
        this.lib.setApplicationState(ICooperationLib.ApplicationState.Background);
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putBoolean(ALREADY_CONFIRMED, this.mAlreadyComfirmed);
            outState.putBoolean(ALREADY_BT_CONFIRMED, this.mAlreadyBtComfirmed);
        }
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        if (savedState != null) {
            this.mAlreadyComfirmed = savedState.getBoolean(ALREADY_CONFIRMED);
            this.mAlreadyBtComfirmed = savedState.getBoolean(ALREADY_BT_CONFIRMED);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startMainActivity() {
        runOnUiThread(new Runnable() { // from class: com.jvckenwood.carconnectcontrol.SplashActivity.2
            @Override // java.lang.Runnable
            public void run() {
                Class<?> cls = SettingsScreenActivity.class;
                if (AppLauncherApplication.getLib(SplashActivity.this).getCooperationStateWithMode() == ICooperationLib.CooperationState.CooperationAppLink) {
                    cls = ApplicationLauncherActivity.class;
                }
                Intent intentActivity = new Intent(SplashActivity.this, cls);
                intentActivity.setFlags(335544320);
                SplashActivity.this.startActivity(intentActivity);
                SplashActivity.this.finish();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void judgeBtSetting() {
        if (this.lib.getCooperationState() == ICooperationLib.CooperationState.DeviceOff) {
            if (!this.mAlreadyBtComfirmed) {
                Intent enableIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                startActivityForResult(enableIntent, 100);
                return;
            }
            return;
        }
        postJudgeAccessibilitySetting();
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            this.mAlreadyBtComfirmed = true;
            if (this.lib.getCooperationState() == ICooperationLib.CooperationState.DeviceOff) {
                showFailureDialog();
                return;
            } else {
                postJudgeAccessibilitySetting();
                return;
            }
        }
        if (requestCode == 200) {
            startMainActivity();
        } else {
            AppLog.d(TAG, "Unknown requset code: " + requestCode);
        }
    }

    private void showFailureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.bluetooth_setting_off_confirm_dialog_title);
        builder.setMessage(R.string.bluetooth_setting_off_confirm_dialog_message);
        builder.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.SplashActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                SplashActivity.this.finish();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    private void postJudgeAccessibilitySetting() {
        new Handler().postDelayed(new Runnable() { // from class: com.jvckenwood.carconnectcontrol.SplashActivity.4
            @Override // java.lang.Runnable
            public void run() {
                SplashActivity.this.judgeAccessibilitySetting();
            }
        }, 3000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void judgeAccessibilitySetting() {
        if (!this.mAlreadyComfirmed && !AccessibilityUtils.isEnabled(this)) {
            if (!isFinishing()) {
                showConfirmDialog();
                return;
            }
            return;
        }
        startMainActivity();
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.accessibility_confirm_dialog_title);
        builder.setMessage(R.string.accessibility_confirm_dialog_message);
        builder.setPositiveButton(R.string.Setting, new DialogInterface.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.SplashActivity.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
                SplashActivity.this.startActivityForResult(intent, 200);
                SplashActivity.this.mAlreadyComfirmed = true;
            }
        });
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.SplashActivity.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                SplashActivity.this.mAlreadyComfirmed = true;
                SplashActivity.this.startMainActivity();
            }
        });
        builder.setCancelable(false);
        AlertDialog accessibilityConfirmDialog = builder.create();
        accessibilityConfirmDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.jvckenwood.carconnectcontrol.SplashActivity.7
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialog) {
                SplashActivity.this.lib.setApplicationState(ICooperationLib.ApplicationState.Foreground);
            }
        });
        accessibilityConfirmDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.jvckenwood.carconnectcontrol.SplashActivity.8
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialog) {
                SplashActivity.this.lib.setApplicationState(ICooperationLib.ApplicationState.Background);
            }
        });
        accessibilityConfirmDialog.show();
    }
}
