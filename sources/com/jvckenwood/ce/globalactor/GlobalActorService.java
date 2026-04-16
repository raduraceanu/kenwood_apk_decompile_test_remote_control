package com.jvckenwood.ce.globalactor;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.accessibility.AccessibilityEvent;
import com.jvckenwood.tools.AppLog;

/* JADX INFO: loaded from: classes.dex */
public class GlobalActorService extends AccessibilityService {
    public static final String ACTION_BACKBUTTON = "com.jvckenwood.ce.globalactorservice.back";
    public static final String ACTION_HOMEBUTTON = "com.jvckenwood.ce.globalactorservice.home";
    public static final String ACTION_TASKBUTTON = "com.jvckenwood.ce.globalactorservice.task";
    private BroadcastCatcher mReceiver = new BroadcastCatcher();

    private class BroadcastCatcher extends BroadcastReceiver {
        private BroadcastCatcher() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GlobalActorService.ACTION_BACKBUTTON)) {
                GlobalActorService.this.performGlobalAction(1);
            } else if (intent.getAction().equals(GlobalActorService.ACTION_HOMEBUTTON)) {
                GlobalActorService.this.performGlobalAction(2);
            } else if (intent.getAction().equals(GlobalActorService.ACTION_TASKBUTTON)) {
                GlobalActorService.this.performGlobalAction(3);
            }
        }
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onAccessibilityEvent(AccessibilityEvent arg0) {
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onInterrupt() {
    }

    @Override // android.accessibilityservice.AccessibilityService
    protected void onServiceConnected() {
        AppLog.i("jvckenwood", "AccessibilityService:GlobalActorService connected");
        IntentFilter intent_filter = new IntentFilter();
        intent_filter.addAction(ACTION_BACKBUTTON);
        intent_filter.addAction(ACTION_HOMEBUTTON);
        intent_filter.addAction(ACTION_TASKBUTTON);
        registerReceiver(this.mReceiver, intent_filter);
    }

    @Override // android.app.Service
    public void onDestroy() {
        unregisterReceiver(this.mReceiver);
        super.onDestroy();
    }
}
