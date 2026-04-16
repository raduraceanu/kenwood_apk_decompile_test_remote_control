package com.jvckenwood.carconnectcontrol.Fragments;

import android.graphics.drawable.Drawable;
import com.jvckenwood.applauncher.tools.AppInfo;

/* JADX INFO: loaded from: classes.dex */
public class GridItems {
    public Drawable icon;
    public int id;
    public String tag;
    public String text;

    public GridItems(int id, AppInfo appInfo) {
        this.id = id;
        this.tag = appInfo.getPackageName();
        this.text = appInfo.getApplicationName();
        this.icon = appInfo.getIconDrawable();
    }
}
