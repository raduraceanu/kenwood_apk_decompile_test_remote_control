package com.jvckenwood.carconnectcontrol;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import com.jvckenwood.HID_ThinClient.KWD.R;
import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager;
import com.jvckenwood.applauncher.managers.SettingsManager;
import com.jvckenwood.cooperationlib.CommandId;
import com.jvckenwood.cooperationlib.CommandUtil;
import com.jvckenwood.tools.PrefsUtils;

/* JADX INFO: loaded from: classes.dex */
public class SettingsScreenActivity extends Activity {
    private static final int REQUEST_CODE_PERMISSION_OVERLAY = 100;
    private static final int REQUEST_CODE_PERMISSION_WRITE_SETTINGS = 101;
    private static final String ALREADY_CONFIRMED = SettingsScreenActivity.class.getName() + ".ALREADY_CONFIRMED";
    private static final String OVERLAY_CONFIRMED = SettingsScreenActivity.class.getName() + ".OVERLAY_CONFIRMED";
    private static final String WRITE_SETTING_CONFIRMED = SettingsScreenActivity.class.getName() + ".WRITE_SETTING_CONFIRMED";
    private ICooperationLib _ICoopeLib = null;
    private boolean mOverlayPermissiomComfirmed = false;
    private boolean mWriteSettingsPermissiomComfirmed = false;
    private boolean mIsPermissionCheckComplate = false;
    private AlertDialog mAlertDialog = null;
    private final BroadcastReceiver _onCooperatedReceiver = new BroadcastReceiver() { // from class: com.jvckenwood.carconnectcontrol.SettingsScreenActivity.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (IntentActions.HEAD_UNIT_CONNECTED.equals(action)) {
                    SettingsScreenActivity.this.updateCheckBoxAllowConnectionWithHeadUnit(true);
                    SettingsScreenActivity.this.updateButtonManualScreenCalibration(true);
                    return;
                }
                if (IntentActions.HEAD_UNIT_DISCONNECTED.equals(action)) {
                    SettingsScreenActivity.this.updateCheckBoxAllowConnectionWithHeadUnit(false);
                    SettingsScreenActivity.this.updateButtonManualScreenCalibration(false);
                    return;
                }
                if (IntentActions.CHANGE_COOPERATION_STATE.equals(action)) {
                    boolean isConnected = intent.getBooleanExtra("isConnected", false);
                    ICooperationLib.CooperationState cooperationStateMode = SettingsScreenActivity.this._ICoopeLib.getCooperationStateWithMode();
                    if (isConnected && cooperationStateMode.equals(ICooperationLib.CooperationState.CooperationBtHid)) {
                        SettingsScreenActivity.gotoActivity(context);
                        return;
                    }
                    return;
                }
                if (IntentActions.START_LAUNCHER.equals(action)) {
                    new Handler().postDelayed(new Runnable() { // from class: com.jvckenwood.carconnectcontrol.SettingsScreenActivity.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            SettingsScreenActivity.this.finish();
                        }
                    }, 1000L);
                }
            }
        }
    };
    private final View.OnClickListener _onAlowConnectionButtonClicked = new View.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.SettingsScreenActivity.3
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            Context thiz = SettingsScreenActivity.this;
            boolean isChecked = PrefsUtils.getBoolean(thiz, R.string.alow_connection_with_headunit_key, true);
            PrefsUtils.setBoolean(thiz, R.string.alow_connection_with_headunit_key, !isChecked);
            SettingsManager manager = SettingsManager.getInstance(thiz);
            manager.setCommEnabled(isChecked ? false : true);
            SettingsScreenActivity.this.refreshButtons();
        }
    };
    private final View.OnClickListener _onStartCalibrationButtonClicked = new View.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.SettingsScreenActivity.4
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            byte[] command = CommandUtil.createBasicReturnCommand(CommandId.CMD_START_CALIBRATION_REQUEST, 0);
            SettingsScreenActivity.this._ICoopeLib.sendMessage(command);
            AppLauncherApplication app = (AppLauncherApplication) SettingsScreenActivity.this.getApplication();
            int state = app.getCalibrationState();
            if (state == 0) {
                app.setCalibrationState(1);
            }
        }
    };
    private final View.OnClickListener _onBackButtonClicked = new View.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.SettingsScreenActivity.5
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            SettingsScreenActivity.this.finish();
        }
    };
    private Runnable mJudgeWriteSettingsPermissionRunnable = new Runnable() { // from class: com.jvckenwood.carconnectcontrol.SettingsScreenActivity.8
        @Override // java.lang.Runnable
        public void run() {
            SettingsScreenActivity.this.mOverlayPermissiomComfirmed = true;
            boolean isNextJudge = false;
            if (!SettingsScreenActivity.this.mWriteSettingsPermissiomComfirmed) {
                if (!SettingsScreenActivity.this.isFinishing()) {
                    SettingsScreenActivity.this.mAlertDialog = AppLauncherApplication.requestWriteSettingsPermissionDialog(SettingsScreenActivity.this, 101, new Runnable() { // from class: com.jvckenwood.carconnectcontrol.SettingsScreenActivity.8.1
                        @Override // java.lang.Runnable
                        public void run() {
                            SettingsScreenActivity.this.onActivityResult(101, -1, null);
                        }
                    });
                    if (SettingsScreenActivity.this.mAlertDialog != null) {
                        SettingsScreenActivity.this.mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.jvckenwood.carconnectcontrol.SettingsScreenActivity.8.2
                            @Override // android.content.DialogInterface.OnShowListener
                            public void onShow(DialogInterface dialog) {
                                SettingsScreenActivity.this._ICoopeLib.setApplicationState(ICooperationLib.ApplicationState.Foreground);
                            }
                        });
                        SettingsScreenActivity.this.mAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.jvckenwood.carconnectcontrol.SettingsScreenActivity.8.3
                            @Override // android.content.DialogInterface.OnDismissListener
                            public void onDismiss(DialogInterface dialog) {
                                SettingsScreenActivity.this._ICoopeLib.setApplicationState(ICooperationLib.ApplicationState.Background);
                            }
                        });
                        SettingsScreenActivity.this.mAlertDialog.show();
                        SettingsScreenActivity.this.mWriteSettingsPermissiomComfirmed = true;
                    } else {
                        isNextJudge = true;
                    }
                }
            } else {
                isNextJudge = true;
            }
            if (isNextJudge) {
                SettingsScreenActivity.this.onActivityResult(101, -1, null);
            }
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_settings);
        ActionBar bar = getActionBar();
        bar.hide();
        this.mOverlayPermissiomComfirmed = false;
        this.mWriteSettingsPermissiomComfirmed = false;
        this._ICoopeLib = AppLauncherApplication.getLib(this);
        Button button = (Button) findViewById(R.id.button_settings_allow_connection_with_headunit);
        button.setOnClickListener(this._onAlowConnectionButtonClicked);
        Button button2 = (Button) findViewById(R.id.button_settings_manual_screen_calibration);
        button2.setOnClickListener(this._onStartCalibrationButtonClicked);
        Button button3 = (Button) findViewById(R.id.button_settings_back);
        button3.setOnClickListener(this._onBackButtonClicked);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        OverlayViewManager.setForbidOverlayBtnState(this);
        boolean isShow = showScreenCalibrationCompletedDialog();
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentActions.HEAD_UNIT_CONNECTED);
        filter.addAction(IntentActions.HEAD_UNIT_DISCONNECTED);
        filter.addAction(IntentActions.CHANGE_COOPERATION_STATE);
        filter.addAction(IntentActions.START_LAUNCHER);
        registerReceiver(this._onCooperatedReceiver, filter);
        refreshButtons();
        if (!isShow) {
            ICooperationLib.CooperationState cooperationStateMode = this._ICoopeLib.getCooperationStateWithMode();
            if (!cooperationStateMode.equals(ICooperationLib.CooperationState.ConnectAppLink) && !cooperationStateMode.equals(ICooperationLib.CooperationState.CooperationAppLink) && !this.mIsPermissionCheckComplate) {
                judgeOverlayPermission();
            }
        }
        this.mIsPermissionCheckComplate = false;
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this._ICoopeLib.setApplicationState(ICooperationLib.ApplicationState.Foreground);
        if (requestCode == 100) {
            this.mOverlayPermissiomComfirmed = true;
            runOnUiThread(this.mJudgeWriteSettingsPermissionRunnable);
        } else if (requestCode == 101) {
            this.mOverlayPermissiomComfirmed = false;
            this.mWriteSettingsPermissiomComfirmed = false;
            this.mIsPermissionCheckComplate = true;
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        this.mIsPermissionCheckComplate = false;
        OverlayViewManager.setAllowOverlayBtnState(this);
        unregisterReceiver(this._onCooperatedReceiver);
        if (this.mAlertDialog != null) {
            this.mAlertDialog.dismiss();
        }
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putBoolean(OVERLAY_CONFIRMED, this.mOverlayPermissiomComfirmed);
            outState.putBoolean(WRITE_SETTING_CONFIRMED, this.mWriteSettingsPermissiomComfirmed);
        }
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        if (savedState != null) {
            this.mOverlayPermissiomComfirmed = savedState.getBoolean(OVERLAY_CONFIRMED);
            this.mWriteSettingsPermissiomComfirmed = savedState.getBoolean(WRITE_SETTING_CONFIRMED);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCheckBoxAllowConnectionWithHeadUnit(boolean isConnected) {
        Button button = (Button) findViewById(R.id.button_settings_allow_connection_with_headunit);
        button.setEnabled(!isConnected);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateButtonManualScreenCalibration(boolean isConnected) {
        Button button = (Button) findViewById(R.id.button_settings_manual_screen_calibration);
        button.setEnabled(isConnected);
    }

    private boolean showScreenCalibrationCompletedDialog() {
        String message = null;
        AppLauncherApplication app = (AppLauncherApplication) getApplication();
        int state = app.getCalibrationState();
        if (state == 2) {
            message = getString(R.string.calibration_failed_confirm_dialog_message);
        } else if (state == 3) {
            message = getString(R.string.calibration_success_confirm_dialog_message);
        }
        if (message == null) {
            return false;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.calibration_success_confirm_dialog_title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.SettingsScreenActivity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setCancelable(false);
        builder.create().show();
        app.setCalibrationState(0);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshButtons() {
        boolean isConnected = true;
        boolean isChecked = PrefsUtils.getBoolean(this, R.string.alow_connection_with_headunit_key, true);
        Button button = (Button) findViewById(R.id.button_settings_allow_connection_with_headunit);
        if (isChecked) {
            button.setBackgroundResource(R.drawable.button_settings_allow_connection_with_headunit_selected);
        } else {
            button.setBackgroundResource(R.drawable.button_settings_allow_connection_with_headunit_unselected);
        }
        ICooperationLib.CooperationState state = this._ICoopeLib.getCooperationState();
        if (!state.equals(ICooperationLib.CooperationState.Connect) && !state.equals(ICooperationLib.CooperationState.Cooperation)) {
            isConnected = false;
        }
        updateCheckBoxAllowConnectionWithHeadUnit(isConnected);
        updateButtonManualScreenCalibration(isConnected);
    }

    public static void gotoActivity(Context context) {
        gotoActivity(context, false);
    }

    public static void gotoActivity(Context context, boolean isCrearTask) {
        Intent intentActivity = new Intent(context, (Class<?>) SettingsScreenActivity.class);
        if (isCrearTask) {
            intentActivity.setFlags(32768);
        } else {
            intentActivity.setFlags(67108864);
        }
        context.startActivity(intentActivity);
    }

    private void judgeOverlayPermission() {
        boolean isNextJudge = false;
        if (!this.mOverlayPermissiomComfirmed) {
            if (!isFinishing()) {
                this.mAlertDialog = OverlayViewManager.requestOverlayPermissionDialog(this, 100, this.mJudgeWriteSettingsPermissionRunnable);
                if (this.mAlertDialog == null) {
                    isNextJudge = true;
                } else {
                    this.mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.jvckenwood.carconnectcontrol.SettingsScreenActivity.6
                        @Override // android.content.DialogInterface.OnShowListener
                        public void onShow(DialogInterface dialog) {
                            SettingsScreenActivity.this._ICoopeLib.setApplicationState(ICooperationLib.ApplicationState.Foreground);
                        }
                    });
                    this.mAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.jvckenwood.carconnectcontrol.SettingsScreenActivity.7
                        @Override // android.content.DialogInterface.OnDismissListener
                        public void onDismiss(DialogInterface dialog) {
                            SettingsScreenActivity.this._ICoopeLib.setApplicationState(ICooperationLib.ApplicationState.Background);
                        }
                    });
                    this.mAlertDialog.show();
                }
            }
        } else {
            isNextJudge = true;
        }
        if (isNextJudge) {
            runOnUiThread(this.mJudgeWriteSettingsPermissionRunnable);
        }
    }
}
