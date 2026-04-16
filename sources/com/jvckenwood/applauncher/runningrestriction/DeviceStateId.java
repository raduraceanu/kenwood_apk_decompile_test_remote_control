package com.jvckenwood.applauncher.runningrestriction;

/* JADX INFO: loaded from: classes.dex */
public class DeviceStateId {
    public final int value;
    public static final DeviceStateId DEVICE_STATE_UNDETECTION = new DeviceStateId(0);
    public static final DeviceStateId DEVICE_STATE_ORIENTATION_PORTRAIT = new DeviceStateId(1);
    public static final DeviceStateId DEVICE_STATE_ORIENTATION_LANDSCAPE = new DeviceStateId(2);
    public static final DeviceStateId DEVICE_STATE_HDMI_CONNECT = new DeviceStateId(1);
    public static final DeviceStateId DEVICE_STATE_HDMI_DISCONNECT = new DeviceStateId(2);
    public static final DeviceStateId DEVICE_STATE_SCREEN_OFF = new DeviceStateId(1);
    public static final DeviceStateId DEVICE_STATE_SCREEN_ON = new DeviceStateId(2);

    public DeviceStateId(int id) {
        this.value = id;
    }
}
