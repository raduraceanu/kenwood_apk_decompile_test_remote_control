package com.jvckenwood.carconnectcontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.Display;
import android.view.WindowManager;
import com.jvckenwood.HID_ThinClient.KWD.R;
import com.jvckenwood.android.cooperationlib.CooperationLib;
import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.android.launcherconnectionservice.interfaces.IWhiteListRec;
import com.jvckenwood.applauncher.calibration.CalibrationID;
import com.jvckenwood.applauncher.calibration.CalibrationReceiveMessageHandler;
import com.jvckenwood.applauncher.log.LogManager;
import com.jvckenwood.applauncher.managers.AppListManager;
import com.jvckenwood.applauncher.managers.SettingsManager;
import com.jvckenwood.applauncher.negotiation.NegotiationReceiveMessageHandler;
import com.jvckenwood.applauncher.operationcooperation.OperationReceiveMessageHandler;
import com.jvckenwood.applauncher.runningrestriction.AppObserver;
import com.jvckenwood.tools.AppLog;
import com.jvckenwood.tools.BugReportExceptionHandler;
import com.jvckenwood.tools.CheckComfirmDialogBuilder;
import com.jvckenwood.tools.DisplayUtils;
import com.jvckenwood.tools.PrefsUtils;
import java.io.File;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AppLauncherApplication extends Application {
    private static final String SETTINGS_SYSTEM_POINTER_SPEED = "pointer_speed";
    private static final String TAG = "AppLauncherApplication";
    private static Context _context;
    private AppListManager _appListManager;
    private AppObserver _appObserver;
    private static int clock_ten_hour = 0;
    private static int clock_one_hour = 0;
    private static int clock_ten_minute = 0;
    private static int clock_one_minute = 0;
    public static int[] clock = {clock_ten_hour, clock_one_hour, clock_ten_minute, clock_one_minute};
    public static int waveStrength = 255;
    public static int batteryPower = 0;
    public static String ProtocolString = "";
    private ICooperationLib.CooperationState _lastCooperateState = ICooperationLib.CooperationState.None;
    private int _calibrationState = 0;
    private CalibrationID _lastCalibrationType = CalibrationID.CALIBRATION_INVALID;
    public final ICooperationLib cooperationLib = new CooperationLib();

    public AppLauncherApplication() {
        final Intent intentHideSpecialScreen = new Intent();
        intentHideSpecialScreen.setAction(IntentActions.HIDE_SPECIAL_SCREEN);
        this.cooperationLib.setStateChangeListener(new ICooperationLib.IStateChangeListener() { // from class: com.jvckenwood.carconnectcontrol.AppLauncherApplication.1
            @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IStateChangeListener
            public void onChangeState(ICooperationLib sender, ICooperationLib.IStateChangeListener.StateKind stateKind) {
                Intent intent = new Intent();
                switch (AnonymousClass4.$SwitchMap$com$jvckenwood$android$cooperationlib$ICooperationLib$IStateChangeListener$StateKind[stateKind.ordinal()]) {
                    case 1:
                        ICooperationLib.CooperationState currentState = sender.getCooperationState();
                        if (AppLauncherApplication.this._lastCooperateState == ICooperationLib.CooperationState.None || AppLauncherApplication.this._lastCooperateState != currentState) {
                            AppLauncherApplication.this._lastCooperateState = currentState;
                            boolean isCooperated = currentState == ICooperationLib.CooperationState.Cooperation;
                            if (isCooperated) {
                                ICooperationLib.IIdSettings _idSettings = sender.getIdSettings();
                                if (_idSettings == null) {
                                    AppLog.e(AppLauncherApplication.TAG, "前提条件違反(getIdSettings() が null です。)");
                                } else {
                                    int carType = _idSettings.getIdCarType();
                                    int shimuke = _idSettings.getShimukeID();
                                    int country = _idSettings.getIdCountry();
                                    int language = _idSettings.getIdLanguage();
                                    int market = _idSettings.getIdFunctionFragMarket();
                                    int model = _idSettings.getIdFunctionFragModel();
                                    int addtion = _idSettings.getIdFunctionFragAddtion();
                                    AppLauncherApplication.this._appListManager.updateWhiteListLocale(carType, shimuke, country, language, market, model, addtion);
                                    List<IWhiteListRec> whitelist = AppLauncherApplication.this._appListManager.getAllAppinfos();
                                    AppLauncherApplication.this.cooperationLib.setWhiteList(whitelist);
                                }
                            }
                            AppLauncherApplication.this.sendBroadcast(intentHideSpecialScreen);
                            intent.setAction(IntentActions.CHANGE_COOPERATION_STATE);
                            intent.putExtra("isConnected", isCooperated);
                            AppLauncherApplication.this.sendBroadcast(intent);
                            AppLauncherApplication.this.setAppObserver();
                            if (sender.getCooperationStateWithMode() == ICooperationLib.CooperationState.CooperationAppLink) {
                                AppLauncherApplication.this.sendBroadcast(new Intent(IntentActions.START_LAUNCHER));
                            }
                            AppLog.d(AppLauncherApplication.TAG, "CooperationState=" + sender.getCooperationState());
                            boolean isWriteSettingPermission = AppLauncherApplication.checkWriteSettingPermission(AppLauncherApplication._context);
                            ContentResolver resolver = AppLauncherApplication.this.getContentResolver();
                            if (!isCooperated) {
                                int speed = PrefsUtils.getInt(AppLauncherApplication._context, R.string.pref_key_pointer_speed, -999);
                                if (isWriteSettingPermission && speed != -999) {
                                    Settings.System.putInt(resolver, AppLauncherApplication.SETTINGS_SYSTEM_POINTER_SPEED, speed);
                                }
                                PrefsUtils.setInt(AppLauncherApplication._context, R.string.pref_key_pointer_speed, -999);
                                int rotation = PrefsUtils.getInt(AppLauncherApplication._context, R.string.pref_orientation, -999);
                                if (isWriteSettingPermission && rotation != -999) {
                                    Settings.System.putInt(resolver, "user_rotation", 0);
                                }
                                int accelerometer = PrefsUtils.getInt(AppLauncherApplication._context, R.string.pref_accelerometer, -999);
                                if (isWriteSettingPermission && accelerometer != -999) {
                                    Settings.System.putInt(resolver, "accelerometer_rotation", accelerometer);
                                }
                                PrefsUtils.setInt(AppLauncherApplication._context, R.string.pref_orientation, -999);
                                PrefsUtils.setInt(AppLauncherApplication._context, R.string.pref_accelerometer, -999);
                            } else {
                                try {
                                    int speed2 = Settings.System.getInt(resolver, AppLauncherApplication.SETTINGS_SYSTEM_POINTER_SPEED);
                                    if (isWriteSettingPermission) {
                                        Settings.System.putInt(resolver, AppLauncherApplication.SETTINGS_SYSTEM_POINTER_SPEED, 0);
                                    }
                                    PrefsUtils.setInt(AppLauncherApplication._context, R.string.pref_key_pointer_speed, speed2);
                                    WindowManager manager = (WindowManager) AppLauncherApplication.this.getSystemService("window");
                                    Display display = manager.getDefaultDisplay();
                                    int rotation2 = display.getRotation();
                                    Point size = new Point();
                                    display.getSize(size);
                                    int accelerometer2 = Settings.System.getInt(resolver, "accelerometer_rotation");
                                    if (isWriteSettingPermission) {
                                        if (((rotation2 == 0 || rotation2 == 2) && size.y < size.x) || ((rotation2 == 1 || rotation2 == 3) && size.y > size.x)) {
                                            Settings.System.putInt(AppLauncherApplication.this.getContentResolver(), "user_rotation", 0);
                                        } else {
                                            Settings.System.putInt(AppLauncherApplication.this.getContentResolver(), "user_rotation", 1);
                                        }
                                        Settings.System.putInt(AppLauncherApplication.this.getContentResolver(), "accelerometer_rotation", 0);
                                    }
                                    PrefsUtils.setInt(AppLauncherApplication._context, R.string.pref_orientation, rotation2);
                                    PrefsUtils.setInt(AppLauncherApplication._context, R.string.pref_accelerometer, accelerometer2);
                                } catch (Settings.SettingNotFoundException e) {
                                    AppLog.d(AppLauncherApplication.TAG, e.toString());
                                    return;
                                }
                            }
                        }
                        break;
                    case 2:
                        intent.setAction(IntentActions.CHANGE_RUNNING_STATE);
                        AppLog.d(AppLauncherApplication.TAG, "isRunning=" + sender.isRunning());
                        AppLauncherApplication.this.sendBroadcast(intent);
                        break;
                }
            }
        });
    }

    public int getCalibrationState() {
        return this._calibrationState;
    }

    public void setCalibrationState(int state) {
        this._calibrationState = state;
    }

    public CalibrationID getLastCalibrationType() {
        return this._lastCalibrationType;
    }

    public void setLastCalibrationType(int type) {
        if (type == CalibrationID.CARIBRATION_TYPE_ID_SCREEN_LANDSCAPE.value) {
            this._lastCalibrationType = CalibrationID.CARIBRATION_TYPE_ID_SCREEN_LANDSCAPE;
        } else if (type == CalibrationID.CARIBRATION_TYPE_ID_SCREEN_PORTRAIT.value) {
            this._lastCalibrationType = CalibrationID.CARIBRATION_TYPE_ID_SCREEN_PORTRAIT;
        }
    }

    /* JADX INFO: renamed from: com.jvckenwood.carconnectcontrol.AppLauncherApplication$4, reason: invalid class name */
    static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$jvckenwood$android$cooperationlib$ICooperationLib$IStateChangeListener$StateKind = new int[ICooperationLib.IStateChangeListener.StateKind.values().length];

        static {
            try {
                $SwitchMap$com$jvckenwood$android$cooperationlib$ICooperationLib$IStateChangeListener$StateKind[ICooperationLib.IStateChangeListener.StateKind.CooperationState.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$jvckenwood$android$cooperationlib$ICooperationLib$IStateChangeListener$StateKind[ICooperationLib.IStateChangeListener.StateKind.RunningState.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAppObserver() {
        if (this.cooperationLib.getCooperationState() == ICooperationLib.CooperationState.Cooperation) {
            this._appObserver.startObserve();
        } else {
            this._appObserver.stopObserve();
        }
    }

    public static ICooperationLib getLib(Context context) {
        Context ctxApp = context.getApplicationContext();
        if (!(ctxApp instanceof AppLauncherApplication)) {
            return null;
        }
        AppLauncherApplication app = (AppLauncherApplication) ctxApp;
        return app.cooperationLib;
    }

    @Override // android.app.Application
    public void onCreate() {
        if (DebugMode.isLoggingEnabled()) {
            File dirInternalStorage = Environment.getExternalStorageDirectory();
            File dirApp = new File(dirInternalStorage, getPackageName());
            File dirLog = new File(dirApp, "log");
            if (!dirLog.exists()) {
                dirLog.mkdirs();
            }
            LogManager.setup(LogManager.LogLevel.Debug, LogManager.LogLevel.Verbose, dirLog);
            LogManager.setTagOutput(true);
        }
        if (DebugMode.isBugMailEnabled()) {
            Thread.setDefaultUncaughtExceptionHandler(new BugReportExceptionHandler());
        }
        SettingsManager settings = SettingsManager.getInstance(this);
        try {
            AppLog.d(TAG, String.format("<<<%s v%s Start>>>", getPackageName(), settings.getVersionName()));
        } catch (Exception e) {
        }
        AppLog.d(TAG, "onCreate() in");
        super.onCreate();
        _context = this;
        ProtocolString = _context.getPackageName();
        this._appListManager = AppListManager.getInstance(this);
        List<IWhiteListRec> whitelist = this._appListManager.getAllAppinfos();
        this.cooperationLib.setWhiteList(whitelist);
        DisplayUtils.initializeDisplaySize(_context);
        this.cooperationLib.start(this, ProtocolString);
        this._appObserver = AppObserver.getInstance(_context);
        NegotiationReceiveMessageHandler.register(_context, this.cooperationLib);
        CalibrationReceiveMessageHandler.register(_context, this.cooperationLib);
        OperationReceiveMessageHandler.register(_context, this.cooperationLib);
        try {
            ContentResolver resolver = getContentResolver();
            int speed = Settings.System.getInt(resolver, SETTINGS_SYSTEM_POINTER_SPEED);
            PrefsUtils.setInt(_context, R.string.pref_key_pointer_speed, speed);
            int accelerometer = Settings.System.getInt(resolver, "accelerometer_rotation");
            PrefsUtils.setInt(_context, R.string.pref_accelerometer, accelerometer);
            PrefsUtils.setInt(_context, R.string.pref_orientation, -999);
        } catch (Settings.SettingNotFoundException e2) {
            AppLog.d(TAG, e2.toString());
        }
        AppLog.d(TAG, "onCreate() out");
    }

    @Override // android.app.Application
    public void onTerminate() {
        AppLog.d(TAG, "onTerminate() in");
        super.onTerminate();
        AppLog.d(TAG, "onTerminate() out");
    }

    public static boolean checkWriteSettingPermission(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return Settings.System.canWrite(context);
    }

    public static boolean isShouldCheckWriteettingsPermission(Context context) {
        return Build.VERSION.SDK_INT >= 23;
    }

    public static AlertDialog requestWriteSettingsPermissionDialog(final Activity activity, final int requestCode, final Runnable cancelListener) {
        if (checkWriteSettingPermission(activity) || !isShouldCheckWriteettingsPermission(activity)) {
            return null;
        }
        CheckComfirmDialogBuilder builder = new CheckComfirmDialogBuilder(activity);
        builder.setTitle(R.string.write_settings_confirm_dialog_title);
        builder.setMessage(R.string.write_settings_confirm_dialog_message);
        builder.setPositiveButton(R.string.Setting, new DialogInterface.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.AppLauncherApplication.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS", Uri.parse("package:" + activity.getApplicationContext().getPackageName()));
                activity.startActivityForResult(intent, requestCode);
            }
        });
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.AppLauncherApplication.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (cancelListener != null) {
                    new Handler().post(cancelListener);
                }
            }
        });
        builder.setCancelable(false);
        return builder.create();
    }
}
