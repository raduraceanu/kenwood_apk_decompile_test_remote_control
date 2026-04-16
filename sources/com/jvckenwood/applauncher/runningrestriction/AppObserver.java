package com.jvckenwood.applauncher.runningrestriction;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;
import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.android.launcherconnectionservice.interfaces.IWhiteListRec;
import com.jvckenwood.applauncher.managers.AppListManager;
import com.jvckenwood.carconnectcontrol.AppLauncherApplication;
import com.jvckenwood.carconnectcontrol.IntentActions;
import com.jvckenwood.cooperationlib.CommandId;
import com.jvckenwood.cooperationlib.CommandUtil;
import com.jvckenwood.tools.AppLog;
import java.util.HashSet;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AppObserver {
    private static final int FG_APP_FORCE_NOTIFY_TIME = 3000;
    private static final int FG_APP_MONITORING_COUNT = 4;
    private static final int MONITORING_INTERVAL = 750;
    private static final String TAG = "AppObserver";
    private static ContentResolver _contentResolver;
    private static AppObserver _observer;
    private AppListManager _appListManager;
    private Context _context;
    private ICooperationLib _iCoopertationLib;
    private OnOrientationChangedListener _onOrientationChanged = null;
    private WatchingHandler _watchingHandler = null;
    private final HashSet<String> _whitelistPackageNames = new HashSet<>();
    public PhoneStateListener phoneStateListener = new PhoneStateListener() { // from class: com.jvckenwood.applauncher.runningrestriction.AppObserver.1
        private int getAntennaCountForGSM(int waveLevel) {
            if (Build.VERSION.SDK_INT < 8) {
                if (waveLevel <= 0 || waveLevel == 99) {
                    return 0;
                }
                if (waveLevel >= 16) {
                    return 4;
                }
                if (waveLevel >= 8) {
                    return 3;
                }
                if (waveLevel >= 4) {
                    return 2;
                }
                if (waveLevel < 1) {
                    return 255;
                }
                return 1;
            }
            if (waveLevel <= 2 || waveLevel == 99) {
                return 0;
            }
            if (waveLevel >= 12) {
                return 4;
            }
            if (waveLevel >= 8) {
                return 3;
            }
            if (waveLevel >= 5) {
                return 2;
            }
            if (waveLevel < 3) {
                return 255;
            }
            return 1;
        }

        private int getAntennaCountForCdma(int waveLevel, boolean isDbm) {
            if (isDbm) {
                if (waveLevel >= -75) {
                    return 4;
                }
                if (waveLevel >= -85) {
                    return 3;
                }
                if (waveLevel >= -95) {
                    return 2;
                }
                if (waveLevel >= -100) {
                    return 1;
                }
                return 0;
            }
            if (waveLevel >= -90) {
                return 4;
            }
            if (waveLevel >= -110) {
                return 3;
            }
            if (waveLevel >= -130) {
                return 2;
            }
            if (waveLevel >= -150) {
                return 1;
            }
            return 0;
        }

        private int getAntennaCountForEvo(int waveLevel, boolean isEcio) {
            if (isEcio) {
                if (waveLevel >= -65) {
                    return 4;
                }
                if (waveLevel >= -75) {
                    return 3;
                }
                if (waveLevel >= -90) {
                    return 2;
                }
                if (waveLevel >= -105) {
                    return 1;
                }
                return 0;
            }
            if (waveLevel >= 7) {
                return 4;
            }
            if (waveLevel >= 5) {
                return 3;
            }
            if (waveLevel >= 3) {
                return 2;
            }
            if (waveLevel >= 1) {
                return 1;
            }
            return 0;
        }

        @Override // android.telephony.PhoneStateListener
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            int antenna = 255;
            if (signalStrength.isGsm()) {
                int value = signalStrength.getGsmSignalStrength();
                antenna = getAntennaCountForGSM(value);
            } else if (signalStrength.getCdmaDbm() < 0) {
                int dbm = signalStrength.getCdmaDbm();
                int ecio = signalStrength.getCdmaEcio();
                if (dbm < ecio) {
                    antenna = getAntennaCountForCdma(dbm, true);
                } else {
                    antenna = getAntennaCountForCdma(ecio, false);
                }
            } else if (signalStrength.getEvdoDbm() < 0) {
                int ecio2 = signalStrength.getEvdoEcio();
                int snr = signalStrength.getEvdoSnr();
                if (ecio2 < snr) {
                    antenna = getAntennaCountForEvo(ecio2, true);
                } else {
                    antenna = getAntennaCountForEvo(snr, false);
                }
            }
            AppLauncherApplication.waveStrength = antenna;
            Intent intent = new Intent();
            intent.setAction(IntentActions.WAVE_STRENGTH_CHANGED);
            AppObserver.this._context.sendBroadcast(intent);
        }
    };
    private static int mFGAppMonitoringCount = 0;
    private static String _lastFGAppName = "";
    private static int _lastPointerSpeed = 0;
    private static String _lastLanguage = "";
    private static int _lastOrientation = 2;
    private static int _lastHdmiConnection = 0;
    private static int _lastSleepState = 0;
    private static String _lastCountry = "";
    private static int _lastWaveStrength = 255;
    private static HashSet<String> _homeAppSet = new HashSet<>();

    public interface OnOrientationChangedListener {
        void onOrientationChanged(int i);
    }

    static /* synthetic */ int access$1108() {
        int i = mFGAppMonitoringCount;
        mFGAppMonitoringCount = i + 1;
        return i;
    }

    public static synchronized AppObserver getInstance(Context context) {
        if (_observer == null) {
            _observer = new AppObserver(context);
        }
        return _observer;
    }

    public void setOnOrientationChangedListener(OnOrientationChangedListener listener) {
        this._onOrientationChanged = listener;
        if (this._watchingHandler != null) {
            this._watchingHandler.setOnOrientationChangedListener(listener);
        }
    }

    private AppObserver(Context context) {
        this._context = context;
        this._iCoopertationLib = AppLauncherApplication.getLib(context);
        this._appListManager = AppListManager.getInstance(context);
        _contentResolver = this._context.getContentResolver();
    }

    public void startObserve() {
        List<IWhiteListRec> whitelist = this._iCoopertationLib.getWhiteList();
        this._whitelistPackageNames.clear();
        for (IWhiteListRec rec : whitelist) {
            this._whitelistPackageNames.add(rec.getPackageName());
        }
        _lastFGAppName = "";
        mFGAppMonitoringCount = -1;
        if (this._watchingHandler == null) {
            this._watchingHandler = new WatchingHandler(this._context, this._onOrientationChanged);
            this._watchingHandler.sendMessageStart();
            this._watchingHandler.sleep(0L);
        }
        TelephonyManager telephonyManager = (TelephonyManager) this._context.getSystemService("phone");
        telephonyManager.listen(this.phoneStateListener, 256);
        _homeAppSet.clear();
        try {
            PackageManager pm = this._context.getPackageManager();
            Intent it = new Intent("android.intent.action.MAIN");
            it.addCategory("android.intent.category.HOME");
            List<ResolveInfo> list = pm.queryIntentActivities(it, 0);
            for (ResolveInfo ri : list) {
                ActivityInfo ai = ri.activityInfo;
                _homeAppSet.add(ai.packageName);
            }
        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage(), e);
        }
    }

    public void stopObserve() {
        if (this._watchingHandler != null) {
            this._watchingHandler.stop();
            this._watchingHandler = null;
        }
        TelephonyManager telephonyManager = (TelephonyManager) this._context.getSystemService("phone");
        telephonyManager.listen(this.phoneStateListener, 0);
    }

    private boolean isAppPermission(String appID) {
        HashSet<String> appList = this._appListManager.getRunningRestrictionAppIDSet();
        if (!appList.contains(appID)) {
            return false;
        }
        return true;
    }

    public boolean sendApplicationID() {
        try {
            byte[] cmdData = {2};
            if (isAppPermission(_lastFGAppName)) {
                cmdData[0] = 1;
            }
            byte[] command = CommandUtil.createBasicReturnCommand(CommandId.CMD_NOTIFY_LINK_APP, 0);
            byte[] message = CommandUtil.join(command, cmdData);
            this._iCoopertationLib.sendMessage(message);
            return true;
        } catch (Exception e) {
            AppLog.d("notify fg app exception", e.toString());
            return false;
        }
    }

    public void startSendMessage() {
        if (this._watchingHandler == null) {
            return;
        }
        this._watchingHandler.sendMessageStart();
    }

    public boolean sendDeviceState(long stateMask) {
        try {
            byte[] command = CommandUtil.createBasicReturnCommand(CommandId.CMD_NOTIFY_DEVICE_STAT, 0);
            byte[] changedMask = CommandUtil.getBytes(stateMask, 2);
            byte[] orientation = CommandUtil.getBytes(_lastOrientation, 1);
            byte[] hdmi = CommandUtil.getBytes(_lastHdmiConnection, 1);
            byte[] sleep = CommandUtil.getBytes(_lastSleepState, 1);
            byte[] speed = CommandUtil.getBytes(_lastPointerSpeed, 1);
            byte[] country = getLastCountryByte();
            byte[] language = getLastLanguage();
            byte[] wave = CommandUtil.getBytes(_lastWaveStrength, 1);
            byte[] space = CommandUtil.getBytes(0L, 1);
            byte[] message = CommandUtil.join(command, changedMask, orientation, hdmi, sleep, speed, country, language, wave, space);
            this._iCoopertationLib.sendMessage(message);
            return true;
        } catch (Exception e) {
            AppLog.d("send device state exception", e.toString());
            byte[] command2 = CommandUtil.createBasicReturnCommand(CommandId.CMD_NOTIFY_DEVICE_STAT, 2);
            this._iCoopertationLib.sendMessage(command2);
            return false;
        }
    }

    private byte[] getLastCountryByte() {
        byte[] countryByte = {0, 0, 0, 0};
        try {
            byte[] countryByte2 = CommandUtil.getStringByteWithSize(_lastCountry, 4);
            return countryByte2;
        } catch (Exception e) {
            AppLog.d(TAG, e.getMessage());
            return countryByte;
        }
    }

    private byte[] getLastLanguage() {
        byte[] languageByte = {0, 0, 0, 0};
        try {
            byte[] languageByte2 = CommandUtil.getStringByteWithSize(_lastLanguage, 4);
            return languageByte2;
        } catch (Exception e) {
            AppLog.d(TAG, e.getMessage());
            return languageByte;
        }
    }

    private static class WatchingHandler extends Handler {
        private ActivityManager _am;
        private Configuration _configuration;
        private Context _context;
        private ICooperationLib.CooperationState _cooperationStateMode;
        private OnOrientationChangedListener _listener;
        private boolean _isStop = false;
        private boolean _isSendStart = false;

        public WatchingHandler(Context context, OnOrientationChangedListener listener) {
            initDeviceState();
            this._context = context;
            this._am = (ActivityManager) context.getSystemService("activity");
            this._cooperationStateMode = AppObserver._observer._iCoopertationLib.getCooperationStateWithMode();
            this._configuration = this._context.getResources().getConfiguration();
            this._listener = listener;
        }

        public void setOnOrientationChangedListener(OnOrientationChangedListener listener) {
            this._listener = listener;
        }

        private void initDeviceState() {
            int unused = AppObserver._lastOrientation = DeviceStateId.DEVICE_STATE_ORIENTATION_LANDSCAPE.value;
            int unused2 = AppObserver._lastHdmiConnection = getCurrentHdmiConnection();
            int unused3 = AppObserver._lastSleepState = getCurrentSleepState();
            int unused4 = AppObserver._lastPointerSpeed = getCurrentPointerSpeed();
            String unused5 = AppObserver._lastCountry = getCurrentCountry();
            String unused6 = AppObserver._lastLanguage = getCurrentLanguage();
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj.toString().equals("observe")) {
                AppLog.d("handler", "observe!!");
                if (this._isSendStart) {
                    checkForeGroundApp();
                    checkDeviceState();
                }
            }
            if (!this._isStop) {
                sleep(750L);
            }
        }

        private void checkForeGroundApp() {
            if (this._cooperationStateMode == ICooperationLib.CooperationState.CooperationAppLink) {
                List<ActivityManager.RunningTaskInfo> taskInfo = this._am.getRunningTasks(3);
                String currentAppName = taskInfo.get(0).topActivity.getPackageName();
                AppObserver.access$1108();
                if (!AppObserver._lastFGAppName.equals(currentAppName) || AppObserver.mFGAppMonitoringCount >= 4) {
                    AppLog.d(AppObserver.TAG, "ForeGroundApp: " + currentAppName + (AppObserver._lastFGAppName.equals(currentAppName) ? "(Re-Notify)" : ""));
                    String unused = AppObserver._lastFGAppName = currentAppName;
                    int unused2 = AppObserver.mFGAppMonitoringCount = 0;
                    AppObserver._observer.sendApplicationID();
                }
            }
        }

        private void checkDeviceState() {
            long maskValue = 0;
            int orientation = getCurrentOrientation();
            if (AppObserver._lastOrientation != orientation) {
                int unused = AppObserver._lastOrientation = orientation;
                maskValue = 0 | 1;
                if (this._listener != null) {
                    this._listener.onOrientationChanged(orientation);
                }
            }
            int hdmiConnection = getCurrentHdmiConnection();
            if (AppObserver._lastHdmiConnection != hdmiConnection) {
                int unused2 = AppObserver._lastHdmiConnection = hdmiConnection;
                maskValue |= 2;
            }
            int sleepState = getCurrentSleepState();
            if (AppObserver._lastSleepState != sleepState) {
                int unused3 = AppObserver._lastSleepState = sleepState;
                maskValue |= 4;
            }
            int pointerSpeed = getCurrentPointerSpeed();
            if (AppObserver._lastPointerSpeed != pointerSpeed) {
                int unused4 = AppObserver._lastPointerSpeed = pointerSpeed;
                maskValue |= 8;
            }
            String country = getCurrentCountry();
            if (!AppObserver._lastCountry.equals(country)) {
                String unused5 = AppObserver._lastCountry = country;
                maskValue |= 16;
            }
            String language = getCurrentLanguage();
            if (!AppObserver._lastLanguage.equals(language)) {
                String unused6 = AppObserver._lastLanguage = language;
                maskValue |= 32;
            }
            if (AppObserver._lastWaveStrength != AppLauncherApplication.waveStrength) {
                int unused7 = AppObserver._lastWaveStrength = AppLauncherApplication.waveStrength;
                maskValue |= 64;
            }
            if (maskValue != 0) {
                AppObserver._observer.sendDeviceState(maskValue);
            }
        }

        private int getCurrentOrientation() {
            int rotation;
            try {
                if (this._configuration.orientation == 1) {
                    rotation = DeviceStateId.DEVICE_STATE_ORIENTATION_PORTRAIT.value;
                } else {
                    rotation = DeviceStateId.DEVICE_STATE_ORIENTATION_LANDSCAPE.value;
                }
                return rotation;
            } catch (Exception e) {
                int rotation2 = DeviceStateId.DEVICE_STATE_UNDETECTION.value;
                return rotation2;
            }
        }

        private int getCurrentHdmiConnection() {
            try {
                int hdmi = DeviceStateId.DEVICE_STATE_UNDETECTION.value;
                return hdmi;
            } catch (Exception e) {
                int hdmi2 = DeviceStateId.DEVICE_STATE_UNDETECTION.value;
                return hdmi2;
            }
        }

        private int getCurrentSleepState() {
            int sleep;
            try {
                boolean isScreenOn = isDisplaySreenOn();
                if (!isScreenOn) {
                    sleep = DeviceStateId.DEVICE_STATE_SCREEN_OFF.value;
                } else {
                    sleep = DeviceStateId.DEVICE_STATE_SCREEN_ON.value;
                }
                return sleep;
            } catch (Exception e) {
                int sleep2 = DeviceStateId.DEVICE_STATE_UNDETECTION.value;
                return sleep2;
            }
        }

        private boolean isDisplaySreenOn() {
            if (Build.VERSION.SDK_INT < 20) {
                PowerManager pm = (PowerManager) AppObserver._observer._context.getSystemService("power");
                return pm.isScreenOn();
            }
            WindowManager wm = (WindowManager) this._context.getSystemService("window");
            Display display = wm.getDefaultDisplay();
            return display.getState() == 2;
        }

        private int getCurrentPointerSpeed() {
            try {
                int speed = Settings.System.getInt(AppObserver._contentResolver, "pointer_speed");
                int currentSpeed = speed + 8;
                return currentSpeed;
            } catch (Exception e) {
                AppLog.d("pointer_spped!!!!", "cant get pointer_speed");
                int currentSpeed2 = DeviceStateId.DEVICE_STATE_UNDETECTION.value;
                return currentSpeed2;
            }
        }

        private String getCurrentCountry() {
            return "";
        }

        private String getCurrentLanguage() {
            String language = AppObserver._observer._context.getResources().getConfiguration().locale.getLanguage();
            return language;
        }

        public void sleep(long delay) {
            removeMessages(0);
            Message msg = new Message();
            msg.obj = "observe";
            sendMessageDelayed(msg, delay);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sendMessageStart() {
            this._isSendStart = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void stop() {
            this._isStop = true;
        }
    }
}
