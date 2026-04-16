package com.jvckenwood.applauncher.calibration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Base64;
import com.jvckenwood.HID_ThinClient.KWD.R;
import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.carconnectcontrol.AppLauncherApplication;
import com.jvckenwood.carconnectcontrol.IntentActions;
import com.jvckenwood.carconnectcontrol.ScreenCalibrationActivity;
import com.jvckenwood.cooperationlib.CommandId;
import com.jvckenwood.cooperationlib.CommandUtil;
import com.jvckenwood.tools.AppLog;
import com.jvckenwood.tools.PrefsUtils;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class CalibrationReceiveMessageHandler implements ICooperationLib.IReceiveMessageHandler {
    private static final long DELAY_SEND_STOP_RESPONSE = 500;
    public static final String KEY_CALIBRATION_SAVED_DATA = "KEY_CALIBRATION_SAVED_DATA";
    private static final String TAG = "CalibrationReceiveMessageHandler";
    private static Context _context;
    private static ICooperationLib _iCooperationLib;
    private static Handler _asyncProcessor = null;
    public static final int[] commands = {CommandId.CMD_START_CALIBRATION.value, CommandId.CMD_STOP_CALIBRATION.value, CommandId.CMD_SAVE_CALIBRATION_DATA.value};

    public static void register(Context context, ICooperationLib sender) {
        CalibrationReceiveMessageHandler handler = new CalibrationReceiveMessageHandler();
        handler.internalRegister(context, sender);
        _context = context;
        _iCooperationLib = sender;
        _asyncProcessor = new Handler();
    }

    private void internalRegister(Context context, ICooperationLib sender) {
        int[] arr$ = commands;
        for (int id : arr$) {
            sender.addReceiveMessageHandler(id, this);
        }
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IReceiveMessageHandler
    public void onReceiveMessage(ICooperationLib sender, byte[] message) {
        Intent intentHideSpecialScreen = new Intent();
        intentHideSpecialScreen.setAction(IntentActions.HIDE_SPECIAL_SCREEN);
        _context.sendBroadcast(intentHideSpecialScreen);
        int commandID = CommandUtil.readInt(message, 0, 2);
        if (commandID == CommandId.CMD_SAVE_CALIBRATION_DATA.value) {
            byte[] data = Arrays.copyOfRange(message, 2, message.length);
            if (saveCalibrationData(data)) {
                AppLog.d(TAG, "send device info message successed");
                byte[] basicCommand = CommandUtil.createBasicReturnCommand(CommandId.CMD_RETURN_SAVE_CALIBRATION_DATA, 0);
                _iCooperationLib.sendMessage(basicCommand);
                return;
            } else {
                AppLog.d(TAG, "send device info message failed");
                byte[] basicCommand2 = CommandUtil.createBasicReturnCommand(CommandId.CMD_RETURN_SAVE_CALIBRATION_DATA, 4);
                _iCooperationLib.sendMessage(basicCommand2);
                return;
            }
        }
        if (commandID == CommandId.CMD_START_CALIBRATION.value) {
            PrefsUtils.setBoolean(_context, R.string.pref_stop_calibration_received, false);
            int calibrationType = CommandUtil.readInt(message, 2, 1);
            showCalibrationScreen(calibrationType);
            ((AppLauncherApplication) _context).setLastCalibrationType(calibrationType);
            return;
        }
        if (commandID == CommandId.CMD_STOP_CALIBRATION.value) {
            PrefsUtils.setBoolean(_context, R.string.pref_stop_calibration_received, true);
            intentHideSpecialScreen.setAction(IntentActions.HIDE_SPECIAL_SCREEN);
            _context.sendBroadcast(intentHideSpecialScreen);
            boolean needDelay = false;
            if (((AppLauncherApplication) _context).getLastCalibrationType() == CalibrationID.CARIBRATION_TYPE_ID_SCREEN_LANDSCAPE) {
                needDelay = true;
            } else {
                int state = ((AppLauncherApplication) _context).getCalibrationState();
                if (state == 2) {
                    needDelay = true;
                }
            }
            if (needDelay) {
                _asyncProcessor.postDelayed(new Runnable() { // from class: com.jvckenwood.applauncher.calibration.CalibrationReceiveMessageHandler.1
                    @Override // java.lang.Runnable
                    public void run() {
                        byte[] command = CommandUtil.createBasicReturnCommand(CommandId.CMD_RETURN_STOP_CALIBRATION, 0);
                        CalibrationReceiveMessageHandler._iCooperationLib.sendMessage(command);
                    }
                }, DELAY_SEND_STOP_RESPONSE);
            } else {
                byte[] command = CommandUtil.createBasicReturnCommand(CommandId.CMD_RETURN_STOP_CALIBRATION, 0);
                _iCooperationLib.sendMessage(command);
            }
        }
    }

    private void showCalibrationScreen(int calibrationType) {
        if (calibrationType == CalibrationID.CARIBRATION_TYPE_ID_SCREEN_LANDSCAPE.value) {
            Intent intentActivity = new Intent(_context.getApplicationContext(), (Class<?>) ScreenCalibrationActivity.class);
            intentActivity.putExtra("CALIBRATION_TYPE", calibrationType);
            intentActivity.setFlags(268435456);
            _context.startActivity(intentActivity);
            return;
        }
        if (calibrationType == CalibrationID.CARIBRATION_TYPE_ID_SCREEN_PORTRAIT.value) {
            Intent intentActivity2 = new Intent(_context.getApplicationContext(), (Class<?>) ScreenCalibrationActivity.class);
            intentActivity2.putExtra("CALIBRATION_TYPE", calibrationType);
            intentActivity2.setFlags(268435456);
            _context.startActivity(intentActivity2);
            return;
        }
        AppLog.d(TAG, "Unknown calibration type: " + calibrationType);
    }

    private boolean saveCalibrationData(byte[] saveData) {
        String decode = new String(Base64.encodeToString(saveData, 0));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CALIBRATION_SAVED_DATA, decode);
        editor.commit();
        return editor.commit();
    }

    public static byte[] getSavedCalibrationData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String dataStr = sharedPreferences.getString(KEY_CALIBRATION_SAVED_DATA, "");
        byte[] dataByte = Base64.decode(dataStr, 0);
        return dataByte;
    }
}
