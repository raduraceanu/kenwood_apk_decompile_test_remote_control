package com.jvckenwood.android.launcherconnectionservice.impl;

import com.jvckenwood.tools.AppLog;

/* JADX INFO: loaded from: classes.dex */
public class CommunicationLog {
    public static void writeSendLog(byte[] data) {
        writeLog("Send", data);
    }

    public static void writeReceiveLog(byte[] data) {
        writeLog("Recv", data);
    }

    private static void writeLog(String title, byte[] data) {
        StringBuilder buf = new StringBuilder();
        buf.append(title);
        buf.append(":");
        for (byte b : data) {
            buf.append(String.format("%02X ", Integer.valueOf(b & 255)));
        }
        AppLog.v("CommunicationLog", buf.toString());
    }
}
