package com.jvckenwood.carconnectcontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.jvckenwood.android.cooperationlib.ICooperationLib;

/* JADX INFO: loaded from: classes.dex */
public class ConnectionReciver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        ICooperationLib lib = AppLauncherApplication.getLib(context);
        if (lib.getCooperationStateWithMode() == ICooperationLib.CooperationState.CooperationAppLink) {
            switch (lib.getCooperationState()) {
                case None:
                    Intent intentActivity = new Intent(context, (Class<?>) SplashActivity.class);
                    intentActivity.setFlags(335544320);
                    context.startActivity(intentActivity);
                    break;
                case Cooperation:
                    if (lib.getApplicationState() != ICooperationLib.ApplicationState.Foreground) {
                        Intent intentActivity2 = new Intent(context, (Class<?>) ApplicationLauncherActivity.class);
                        intentActivity2.setFlags(335544320);
                        context.startActivity(intentActivity2);
                    }
                    break;
                default:
                    if (lib.getApplicationState() != ICooperationLib.ApplicationState.Foreground) {
                        Intent intentActivity3 = new Intent(context, (Class<?>) SplashActivity.class);
                        intentActivity3.setFlags(335544320);
                        context.startActivity(intentActivity3);
                    }
                    break;
            }
        }
    }
}
