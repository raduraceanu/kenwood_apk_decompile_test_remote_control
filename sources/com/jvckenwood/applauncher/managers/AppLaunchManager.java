package com.jvckenwood.applauncher.managers;

import android.content.Context;
import android.content.Intent;

/* JADX INFO: loaded from: classes.dex */
public class AppLaunchManager {
    public static void launchApplication(Context context, Intent intent) {
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void launchWhiteListApplication(Context context, Intent intent) {
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setAction("android.intent.action.MAIN");
        intent.addFlags(270532608);
        launchApplication(context, intent);
    }
}
