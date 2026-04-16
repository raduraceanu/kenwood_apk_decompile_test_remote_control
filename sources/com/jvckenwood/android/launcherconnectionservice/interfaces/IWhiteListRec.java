package com.jvckenwood.android.launcherconnectionservice.interfaces;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface IWhiteListRec {
    String getApplicationName();

    List<String> getCertHash();

    String getPackageName();

    int getRestrictLv();
}
