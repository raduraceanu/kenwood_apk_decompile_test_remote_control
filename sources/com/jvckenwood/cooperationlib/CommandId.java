package com.jvckenwood.cooperationlib;

/* JADX INFO: loaded from: classes.dex */
public class CommandId {
    public final int value;
    public static final CommandId CMD_CONNECT = new CommandId(1);
    public static final CommandId CMD_RETURN_CONNECT = new CommandId(32769);
    public static final CommandId CMD_NOTIFY_READY_CONNECT = new CommandId(2);
    public static final CommandId CMD_NOTIFY_RUNNING_STATE = new CommandId(65);
    public static final CommandId CMD_RETURN_RUNNING_STATE = new CommandId(32833);
    public static final CommandId CMD_LAUNCHER_APP_START = new CommandId(129);
    public static final CommandId CMD_RETURN_LAUNCHER_APP_START = new CommandId(32897);
    public static final CommandId CMD_NOTIFY_DISCONNECT = new CommandId(32915);
    public static final CommandId CMD_DISCONNECT = new CommandId(145);
    public static final CommandId CMD_RETURN_DISCONNECT = new CommandId(32913);
    public static final CommandId CMD_RETURN_NOT_SUPPORTED = new CommandId(33023);
    public static final CommandId CMD_GET_DEVICE_INFO = new CommandId(289);
    public static final CommandId CMD_RETURN_GET_DEVICE_INFO = new CommandId(33057);
    public static final CommandId CMD_GET_DEVICE_STAT = new CommandId(305);
    public static final CommandId CMD_NOTIFY_DEVICE_STAT = new CommandId(33073);
    public static final CommandId CMD_GET_CALIBRATION_DATA = new CommandId(338);
    public static final CommandId CMD_RETURN_GET_CALIBRATION_DATA = new CommandId(33106);
    public static final CommandId CMD_START_CALIBRATION = new CommandId(321);
    public static final CommandId CMD_RETURN_START_CALIBRATION = new CommandId(33089);
    public static final CommandId CMD_STOP_CALIBRATION = new CommandId(322);
    public static final CommandId CMD_RETURN_STOP_CALIBRATION = new CommandId(33090);
    public static final CommandId CMD_SAVE_CALIBRATION_DATA = new CommandId(337);
    public static final CommandId CMD_RETURN_SAVE_CALIBRATION_DATA = new CommandId(33105);
    public static final CommandId CMD_START_CALIBRATION_REQUEST = new CommandId(33088);
    public static final CommandId CMD_NOTIFY_CANCEL_CALIBRATION = new CommandId(33094);
    public static final CommandId CMD_SEND_KEY = new CommandId(353);
    public static final CommandId CMD_NOTIFY_LINK_APP = new CommandId(33138);

    public CommandId(int id) {
        this.value = id;
    }

    public byte getByte0() {
        return (byte) ((this.value >> 8) & 255);
    }

    public byte getByte1() {
        return (byte) (this.value & 255);
    }
}
