package com.jvckenwood.android.launcherconnectionservice.impl;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import com.jvckenwood.HID_ThinClient.KWD.R;
import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.carconnectcontrol.AppLauncherApplication;
import com.jvckenwood.carconnectcontrol.ApplicationLauncherActivity;
import com.jvckenwood.carconnectcontrol.SettingsScreenActivity;
import com.jvckenwood.tools.AbstractNotifier;

/* JADX INFO: loaded from: classes.dex */
public class StatusNotifier extends AbstractNotifier {
    private final Context _context;
    private int _iconId;
    private String _message;
    private long _notificationId;

    public StatusNotifier(Service service, int id, int iconId, int messageId) {
        super(id, service);
        this._notificationId = 0L;
        this._iconId = R.drawable.icon_status;
        this._message = "";
        this._context = service;
        this._notificationId = System.currentTimeMillis();
        this._iconId = iconId;
        this._message = service.getString(messageId);
    }

    @Override // com.jvckenwood.tools.AbstractNotifier
    protected Notification buildNotification() {
        Class<?> cls = ApplicationLauncherActivity.class;
        if (AppLauncherApplication.getLib(this._context).getCooperationStateWithMode() != ICooperationLib.CooperationState.CooperationAppLink) {
            cls = SettingsScreenActivity.class;
        }
        Intent intent = new Intent(this._context, cls);
        PendingIntent pending = PendingIntent.getActivity(this._context, 0, intent, 0);
        Notification.Builder builder = new Notification.Builder(this._context);
        builder.setWhen(this._notificationId);
        builder.setContentTitle(this._message);
        builder.setSmallIcon(this._iconId);
        builder.setTicker(this._message);
        builder.setContentIntent(pending);
        return builder.build();
    }

    public void update(int iconId, String message) {
        this._iconId = iconId;
        this._message = message;
        this._notificationId = System.currentTimeMillis();
        super.update();
    }
}
