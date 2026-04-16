package com.jvckenwood.android.cooperationlib;

import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.cooperationlib.CommandId;
import com.jvckenwood.cooperationlib.CommandUtil;
import com.jvckenwood.tools.AppLog;

/* JADX INFO: loaded from: classes.dex */
public class CommonCommandHandler {
    public static final String TAG = "CommonCommandHandler";
    private boolean _isCooperation = false;
    private boolean _isRunning = false;
    private ICooperationLib.IScreenMetrics _idScreenMetrics = null;
    private ICooperationLib.IIdSettings _idSettings = null;
    private Integer[] mSupportProtocolVersion = {2, 1};

    public void reset() {
        this._isCooperation = false;
        this._isRunning = false;
        this._idScreenMetrics = null;
        this._idSettings = null;
    }

    public boolean isCooperation() {
        return this._isCooperation;
    }

    public boolean isRunning() {
        return this._isRunning;
    }

    public ICooperationLib.IScreenMetrics getIdScreenMetrics() {
        return this._idScreenMetrics;
    }

    public ICooperationLib.IIdSettings getIdSettings() {
        return this._idSettings;
    }

    public static class TouchInfo {
        public static final TouchInfo Empty = new TouchInfo(0, 0, new ICooperationLib.IReceiveTouchHandler.ITouchPoint[0]);
        public final int touchKind;
        public final ICooperationLib.IReceiveTouchHandler.ITouchPoint[] touchPoints;
        public final long touchTime;

        public TouchInfo(int touchKind, long touchTime, ICooperationLib.IReceiveTouchHandler.ITouchPoint[] touchPoints) {
            this.touchKind = touchKind;
            this.touchTime = touchTime;
            this.touchPoints = touchPoints;
        }
    }

    public byte[] handle_CMD_CONNECT(byte[] message) throws Throwable {
        this._idScreenMetrics = null;
        this._idSettings = null;
        int verH = 0;
        int verL = 0;
        int resultCode = 0;
        if (message.length < 19) {
            resultCode = 2;
        } else {
            verH = message[2] & 240;
            verL = message[2] & 15;
            final int screenH = CommandUtil.readInt(message, 4, 2);
            final int screenW = CommandUtil.readInt(message, 6, 2);
            final float aspect = CommandUtil.readFloat(message, 8);
            final int protocolVersion = (verH | verL) & 255;
            final int carType = CommandUtil.readInt(message, 12, 1);
            final int shimuke = CommandUtil.readInt(message, 13, 1);
            final int country = CommandUtil.readInt(message, 14, 1);
            final int language = CommandUtil.readInt(message, 15, 1);
            final long function = CommandUtil.readInt(message, 16, 4);
            if (0 == 0) {
                resultCode = 1;
                Integer[] arr$ = this.mSupportProtocolVersion;
                int len$ = arr$.length;
                int i$ = 0;
                while (true) {
                    if (i$ >= len$) {
                        break;
                    }
                    Integer supportVersion = arr$[i$];
                    if (protocolVersion != supportVersion.intValue()) {
                        i$++;
                    } else {
                        resultCode = 0;
                        break;
                    }
                }
            }
            if (screenH < 1 || screenW < 1 || aspect <= 0.0f) {
                resultCode = 2;
            }
            if (resultCode == 0) {
                this._idScreenMetrics = new ICooperationLib.IScreenMetrics() { // from class: com.jvckenwood.android.cooperationlib.CommonCommandHandler.1
                    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IScreenMetrics
                    public int getWidth() {
                        return screenW;
                    }

                    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IScreenMetrics
                    public int getHeight() {
                        return screenH;
                    }

                    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IScreenMetrics
                    public float getAspect() {
                        return aspect;
                    }
                };
                this._idSettings = new ICooperationLib.IIdSettings() { // from class: com.jvckenwood.android.cooperationlib.CommonCommandHandler.2
                    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IIdSettings
                    public ICooperationLib.IIdSettings.ConnectMode getConnectMode() {
                        ICooperationLib.IIdSettings.ConnectMode mode = ICooperationLib.IIdSettings.ConnectMode.NONE;
                        if (protocolVersion > 1) {
                            ICooperationLib.IIdSettings.ConnectMode mode2 = ICooperationLib.IIdSettings.ConnectMode.APP_LINK;
                            return mode2;
                        }
                        if (protocolVersion == 1) {
                            ICooperationLib.IIdSettings.ConnectMode mode3 = ICooperationLib.IIdSettings.ConnectMode.BT_HID;
                            return mode3;
                        }
                        return mode;
                    }

                    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IIdSettings
                    public int getShimukeID() {
                        return shimuke;
                    }

                    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IIdSettings
                    public int getIdLanguage() {
                        return language;
                    }

                    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IIdSettings
                    public int getIdFunctionFragMarket() {
                        long flag = getIdFunctionFrag();
                        return (int) (((-16777216) & flag) >> 24);
                    }

                    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IIdSettings
                    public int getIdFunctionFragModel() {
                        long flag = getIdFunctionFrag();
                        return (int) ((16711680 & flag) >> 16);
                    }

                    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IIdSettings
                    public int getIdFunctionFragAddtion() {
                        long flag = getIdFunctionFrag();
                        return (int) (65535 & flag);
                    }

                    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IIdSettings
                    public long getIdFunctionFrag() {
                        return function;
                    }

                    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IIdSettings
                    public int getIdCountry() {
                        return country;
                    }

                    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IIdSettings
                    public int getIdCarType() {
                        return carType;
                    }
                };
            }
        }
        if (resultCode == 0) {
            this._isCooperation = true;
        }
        return CommandUtil.join(CommandUtil.createBasicReturnCommand(CommandId.CMD_RETURN_CONNECT, resultCode), new byte[]{(byte) ((verH | verL) & 255), 1});
    }

    public byte[] handle_CMD_DISCONNECT(byte[] message) {
        this._isCooperation = false;
        return CommandUtil.createBasicReturnCommand(CommandId.CMD_RETURN_DISCONNECT, 0);
    }

    public byte[] handle_CMD_NOTIFY_RUNNING_STATE(byte[] message) {
        this._isRunning = message[2] != 0;
        return CommandUtil.createBasicReturnCommand(CommandId.CMD_RETURN_RUNNING_STATE, 0);
    }

    public TouchInfo handle_CMD_NOTIFY_TOUCH_INFO(byte[] bArr) {
        TouchInfo info = TouchInfo.Empty;
        try {
            int i = bArr[2];
            long touchTime = CommandUtil.readLong(bArr, 3, 8);
            ICooperationLib.IReceiveTouchHandler.ITouchPoint[] points = new ICooperationLib.IReceiveTouchHandler.ITouchPoint[bArr[11]];
            for (int i2 = 0; i2 < points.length; i2++) {
                int offset = (i2 * 5) + 12;
                final int i3 = bArr[offset + 0];
                final int x = CommandUtil.readInt(bArr, offset + 1, 2);
                final int y = CommandUtil.readInt(bArr, offset + 3, 2);
                points[i2] = new ICooperationLib.IReceiveTouchHandler.ITouchPoint() { // from class: com.jvckenwood.android.cooperationlib.CommonCommandHandler.3
                    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IReceiveTouchHandler.ITouchPoint
                    public int getTouchId() {
                        return i3;
                    }

                    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IReceiveTouchHandler.ITouchPoint
                    public int getX() {
                        return x;
                    }

                    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IReceiveTouchHandler.ITouchPoint
                    public int getY() {
                        return y;
                    }
                };
            }
            TouchInfo info2 = new TouchInfo(i, touchTime, points);
            return info2;
        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage(), e);
            return info;
        }
    }

    public byte[] handle_CMD_LAUNCHER_APP_START(byte[] message) {
        return CommandUtil.createBasicReturnCommand(CommandId.CMD_RETURN_LAUNCHER_APP_START, 0);
    }

    public byte[] handle_UnsupportedCommand(byte[] message) {
        if (CommandUtil.readInt(message, 0, 2) == CommandId.CMD_RETURN_NOT_SUPPORTED.value) {
            return null;
        }
        return CommandUtil.join(CommandUtil.createBasicReturnCommand(CommandId.CMD_RETURN_NOT_SUPPORTED, 0), message);
    }

    public byte[] createDisconnectCommand() {
        return CommandUtil.createBasicReturnCommand(CommandId.CMD_NOTIFY_DISCONNECT, 0);
    }
}
