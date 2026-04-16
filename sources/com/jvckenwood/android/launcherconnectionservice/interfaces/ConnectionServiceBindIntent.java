package com.jvckenwood.android.launcherconnectionservice.interfaces;

import android.content.Context;
import android.content.Intent;
import com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class ConnectionServiceBindIntent extends Intent {
    public ConnectionServiceBindIntent(Context context) {
        super(IApp.class.getName());
        setPackage(context.getApplicationContext().getPackageName());
        putExtra("bindID", UUID.randomUUID().toString());
    }
}
