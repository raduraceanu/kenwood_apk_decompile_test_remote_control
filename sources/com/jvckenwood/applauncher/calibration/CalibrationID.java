package com.jvckenwood.applauncher.calibration;

import android.support.v4.internal.view.SupportMenu;

/* JADX INFO: loaded from: classes.dex */
public class CalibrationID {
    public final int value;
    public static final CalibrationID CARIBRATION_TYPE_ID_SCREEN_LANDSCAPE = new CalibrationID(1);
    public static final CalibrationID CARIBRATION_TYPE_ID_SCREEN_PORTRAIT = new CalibrationID(2);
    public static final CalibrationID CARIBRATION_TYPE_ID_MOUSE_LANDSCAPE = new CalibrationID(3);
    public static final CalibrationID CARIBRATION_TYPE_ID_MOUSE_PORTRAIT = new CalibrationID(4);
    public static final CalibrationID CARIBRATION_TYPE_ID_KEY = new CalibrationID(5);
    public static final CalibrationID CARIBRATION_TOUCH_PRESSED = new CalibrationID(0);
    public static final CalibrationID CARIBRATION_TOUCH_RELEASED = new CalibrationID(2);
    public static final CalibrationID CALIBRATION_INVALID = new CalibrationID(SupportMenu.USER_MASK);

    public CalibrationID(int id) {
        this.value = id;
    }
}
