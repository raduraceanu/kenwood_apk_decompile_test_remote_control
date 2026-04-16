package com.jvckenwood.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractNotifier {
    private final int NOTIFICATION_ID;
    private boolean isForeground = false;
    private NotificationManager manager;
    private Service service;

    protected abstract Notification buildNotification();

    public AbstractNotifier(int id, Service service) {
        this.service = null;
        this.manager = null;
        this.NOTIFICATION_ID = id;
        this.service = service;
        this.manager = (NotificationManager) service.getSystemService("notification");
    }

    public void dismiss() {
        this.manager.cancelAll();
        this.service.stopForeground(true);
        this.isForeground = false;
    }

    public void update() {
        Notification notification = buildNotification();
        if (notification != null) {
            if (this.isForeground) {
                this.manager.notify(this.NOTIFICATION_ID, notification);
            } else {
                this.isForeground = true;
                this.service.startForeground(this.NOTIFICATION_ID, notification);
            }
        }
    }
}
