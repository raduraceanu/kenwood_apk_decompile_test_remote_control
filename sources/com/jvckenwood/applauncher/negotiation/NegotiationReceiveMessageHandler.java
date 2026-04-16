package com.jvckenwood.applauncher.negotiation;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.applauncher.calibration.CalibrationReceiveMessageHandler;
import com.jvckenwood.applauncher.runningrestriction.AppObserver;
import com.jvckenwood.carconnectcontrol.AppLauncherApplication;
import com.jvckenwood.cooperationlib.CommandId;
import com.jvckenwood.cooperationlib.CommandUtil;
import com.jvckenwood.tools.AppLog;
import com.jvckenwood.tools.DisplayUtils;

/* JADX INFO: loaded from: classes.dex */
public class NegotiationReceiveMessageHandler implements ICooperationLib.IReceiveMessageHandler {
    public static final String TAG = "NegotiationReceiveMessageHandler";
    private static Context _context;
    private static ICooperationLib _iCooperationLib;
    public static final int[] commands = {CommandId.CMD_GET_DEVICE_INFO.value, CommandId.CMD_GET_DEVICE_STAT.value, CommandId.CMD_GET_CALIBRATION_DATA.value};
    private AppObserver _appObserver = null;

    public static void register(Context context, ICooperationLib sender) {
        NegotiationReceiveMessageHandler handler = new NegotiationReceiveMessageHandler();
        handler.internalRegister(context, sender);
        _context = context;
        _iCooperationLib = AppLauncherApplication.getLib(context);
    }

    private void internalRegister(Context context, ICooperationLib sender) {
        this._appObserver = AppObserver.getInstance(context);
        int[] arr$ = commands;
        for (int id : arr$) {
            sender.addReceiveMessageHandler(id, this);
        }
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IReceiveMessageHandler
    public void onReceiveMessage(ICooperationLib sender, byte[] message) {
        int commandID = CommandUtil.readInt(message, 0, 2);
        if (commandID == CommandId.CMD_GET_DEVICE_INFO.value) {
            if (!sendDeviceInfo()) {
                AppLog.d(TAG, "send device info message failed");
            }
        } else if (commandID == CommandId.CMD_GET_DEVICE_STAT.value) {
            if (!this._appObserver.sendDeviceState(0L)) {
                AppLog.d(TAG, "send device state message failed");
            }
        } else if (commandID == CommandId.CMD_GET_CALIBRATION_DATA.value && !sendCalibrationData()) {
            AppLog.d(TAG, "send calibration message failed");
        }
    }

    private boolean sendDeviceInfo() {
        WindowManager wm = (WindowManager) _context.getSystemService("window");
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        try {
            byte[] basicCommand = CommandUtil.createBasicReturnCommand(CommandId.CMD_RETURN_GET_DEVICE_INFO, 0);
            int width = ((Integer) DisplayUtils.getDisplaySize(_context).first).intValue();
            byte[] widhtPX = CommandUtil.getBytes(width, 2);
            int height = ((Integer) DisplayUtils.getDisplaySize(_context).second).intValue();
            byte[] heightPX = CommandUtil.getBytes(height, 2);
            byte[] xdpi = CommandUtil.getFloatBytes(displayMetrics.xdpi);
            byte[] ydpi = CommandUtil.getFloatBytes(displayMetrics.ydpi);
            byte[] version = CommandUtil.getBytes(Build.VERSION.SDK_INT, 2);
            byte[] message = CommandUtil.join(basicCommand, widhtPX, heightPX, xdpi, ydpi, version);
            _iCooperationLib.sendMessage(message);
            return true;
        } catch (Exception e) {
            AppLog.e("CMD_GET_DEVICE_INFO", e.toString(), e);
            byte[] basicCommand2 = CommandUtil.createBasicReturnCommand(CommandId.CMD_RETURN_GET_DEVICE_INFO, 2);
            _iCooperationLib.sendMessage(basicCommand2);
            return false;
        }
    }

    private boolean sendCalibrationData() {
        try {
            byte[] command = CommandUtil.createBasicReturnCommand(CommandId.CMD_RETURN_GET_CALIBRATION_DATA, 0);
            byte[] calibrationData = CalibrationReceiveMessageHandler.getSavedCalibrationData();
            byte[] message = CommandUtil.join(command, calibrationData);
            _iCooperationLib.sendMessage(message);
            return true;
        } catch (Exception e) {
            AppLog.d(TAG, e.getMessage());
            byte[] basicCommand = CommandUtil.createBasicReturnCommand(CommandId.CMD_RETURN_GET_CALIBRATION_DATA, 2);
            _iCooperationLib.sendMessage(basicCommand);
            return false;
        }
    }
}
