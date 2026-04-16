package com.jvckenwood.applauncher.tools;

import android.graphics.drawable.Drawable;
import com.jvckenwood.android.launcherconnectionservice.interfaces.IWhiteListRec;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AppInfo implements IWhiteListRec {
    private final String appName;
    private final List<String> certHash;
    private Drawable iconDrawable = null;
    private final String pName;
    private final int restrictLv;

    public AppInfo(String appName, String pName, int restrictLv, List<String> certHash) {
        this.appName = appName;
        this.pName = pName;
        this.restrictLv = restrictLv;
        this.certHash = certHash;
    }

    @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.IWhiteListRec
    public String getApplicationName() {
        return this.appName;
    }

    @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.IWhiteListRec
    public List<String> getCertHash() {
        return this.certHash;
    }

    @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.IWhiteListRec
    public String getPackageName() {
        return this.pName;
    }

    @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.IWhiteListRec
    public int getRestrictLv() {
        return this.restrictLv;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public Drawable getIconDrawable() {
        return this.iconDrawable;
    }
}
