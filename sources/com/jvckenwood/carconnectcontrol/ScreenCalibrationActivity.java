package com.jvckenwood.carconnectcontrol;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.jvckenwood.HID_ThinClient.KWD.R;
import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager;
import com.jvckenwood.applauncher.calibration.CalibrationID;
import com.jvckenwood.cooperationlib.CommandId;
import com.jvckenwood.cooperationlib.CommandUtil;
import com.jvckenwood.tools.DisplayUtils;
import com.jvckenwood.tools.PrefsUtils;

/* JADX INFO: loaded from: classes.dex */
public class ScreenCalibrationActivity extends Activity {
    private static final int MONITOR_INTERVAL_MS = 300;
    private LinearLayout _layout;
    private ICooperationLib _lib;
    private Handler _screenSizeMonitor = null;
    private Handler _screenAppearMonitor = null;
    private boolean _isFailed = false;
    private boolean _isSentReturn = false;
    private int _calibrationType = 0;
    private Pair<Integer, Integer> _contentSize = null;
    private final Runnable _onScreenSizeTimer = new Runnable() { // from class: com.jvckenwood.carconnectcontrol.ScreenCalibrationActivity.1
        @Override // java.lang.Runnable
        public void run() {
            if (!ScreenCalibrationActivity.this.isFinishing() && !ScreenCalibrationActivity.this._isFailed) {
                if (ScreenCalibrationActivity.this._contentSize == null) {
                    ScreenCalibrationActivity.this._screenSizeMonitor.postDelayed(ScreenCalibrationActivity.this._onScreenSizeTimer, 300L);
                    return;
                }
                Pair<Integer, Integer> fullSize = DisplayUtils.getHorizontalSize(ScreenCalibrationActivity.this._layout.getWidth(), ScreenCalibrationActivity.this._layout.getHeight());
                if (fullSize == null) {
                    ScreenCalibrationActivity.this._screenSizeMonitor.postDelayed(ScreenCalibrationActivity.this._onScreenSizeTimer, 300L);
                } else if (((Integer) fullSize.first).intValue() != ((Integer) ScreenCalibrationActivity.this._contentSize.first).intValue() || ((Integer) fullSize.second).intValue() != ((Integer) ScreenCalibrationActivity.this._contentSize.second).intValue()) {
                    ScreenCalibrationActivity.this.sendCalibrationCancelNotify();
                    ScreenCalibrationActivity.this.sendCalibrationStartResponse();
                } else {
                    ScreenCalibrationActivity.this._screenSizeMonitor.postDelayed(ScreenCalibrationActivity.this._onScreenSizeTimer, 300L);
                }
            }
        }
    };
    BroadcastReceiver _rcvHideAction = new BroadcastReceiver() { // from class: com.jvckenwood.carconnectcontrol.ScreenCalibrationActivity.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            ScreenCalibrationActivity.this.finish();
        }
    };
    private final Runnable _runnable = new Runnable() { // from class: com.jvckenwood.carconnectcontrol.ScreenCalibrationActivity.3
        @Override // java.lang.Runnable
        public void run() {
            if (!ScreenCalibrationActivity.this.isFinishing()) {
                ScreenCalibrationActivity.this.sendCalibrationStartResponse();
            }
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this._lib = AppLauncherApplication.getLib(this);
        this._layout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_scrren_calibration, (ViewGroup) null);
        setContentView(this._layout);
        this._layout.setSystemUiVisibility(2);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        getWindow().addFlags(1024);
        Intent intent = getIntent();
        this._calibrationType = intent.getIntExtra("CALIBRATION_TYPE", 0);
        if (this._calibrationType == CalibrationID.CARIBRATION_TYPE_ID_SCREEN_LANDSCAPE.value) {
            setRequestedOrientation(0);
        } else if (this._calibrationType == CalibrationID.CARIBRATION_TYPE_ID_SCREEN_PORTRAIT.value) {
            setRequestedOrientation(1);
        }
        this._screenSizeMonitor = new Handler();
        this._screenAppearMonitor = new Handler();
        View view = new CalibrationView(this);
        this._layout.addView(view);
    }

    class CalibrationView extends View {
        public CalibrationView(Context context) {
            super(context);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (ScreenCalibrationActivity.this._contentSize == null) {
                ScreenCalibrationActivity.this._contentSize = DisplayUtils.getHorizontalSize(getWidth(), getHeight());
            }
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        registerReceiver(this._rcvHideAction, new IntentFilter(IntentActions.HIDE_SPECIAL_SCREEN));
        OverlayViewManager.setForbidOverlayBtnState(this);
        this._screenSizeMonitor.postDelayed(this._onScreenSizeTimer, 300L);
        this._screenAppearMonitor.postDelayed(this._runnable, 2000L);
        this._contentSize = null;
        boolean isStopped = PrefsUtils.getBoolean(this, R.string.pref_stop_calibration_received, false);
        if (isStopped) {
            PrefsUtils.setBoolean(this, R.string.pref_stop_calibration_received, false);
            finish();
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        unregisterReceiver(this._rcvHideAction);
        OverlayViewManager.setAllowOverlayBtnState(this);
        this._screenSizeMonitor.removeCallbacks(this._onScreenSizeTimer);
        this._screenAppearMonitor.removeCallbacks(this._runnable);
        if (!this._isSentReturn) {
            sendCalibrationCancelNotify();
            sendCalibrationStartResponse();
        }
        PrefsUtils.setBoolean(this, R.string.pref_stop_calibration_received, false);
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        if (savedState != null) {
            this._isFailed = savedState.getBoolean("_isFailed");
            this._isSentReturn = savedState.getBoolean("_isSentReturn");
            this._calibrationType = savedState.getInt("_calibrationType");
        }
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putBoolean("_isFailed", this._isFailed);
            outState.putBoolean("_isSentReturn", this._isSentReturn);
            outState.putInt("_calibrationType", this._calibrationType);
        }
    }

    @Override // android.app.Activity
    public void onBackPressed() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendCalibrationStartResponse() {
        if (!this._isSentReturn) {
            byte[] command = CommandUtil.createBasicReturnCommand(CommandId.CMD_RETURN_START_CALIBRATION, 0);
            this._lib.sendMessage(command);
            this._isSentReturn = true;
            AppLauncherApplication app = (AppLauncherApplication) getApplication();
            int state = app.getCalibrationState();
            if (state != 0 && state != 2) {
                app.setCalibrationState(this._calibrationType == CalibrationID.CARIBRATION_TYPE_ID_SCREEN_LANDSCAPE.value ? 3 : 4);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendCalibrationCancelNotify() {
        if (!this._isFailed) {
            byte[] command = CommandUtil.createBasicReturnCommand(CommandId.CMD_NOTIFY_CANCEL_CALIBRATION, 0);
            byte[] message = CommandUtil.join(command, new byte[]{(byte) this._calibrationType});
            this._lib.sendMessage(message);
            this._isFailed = true;
            AppLauncherApplication app = (AppLauncherApplication) getApplication();
            app.setCalibrationState(2);
        }
    }

    private void showFailureDialog() {
        if (!this._isFailed) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.calibration_failed_confirm_dialog_title);
            builder.setMessage(R.string.calibration_failed_confirm_dialog_message);
            builder.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.ScreenCalibrationActivity.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        }
    }
}
