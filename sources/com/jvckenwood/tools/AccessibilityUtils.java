package com.jvckenwood.tools;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.view.accessibility.AccessibilityManager;
import com.jvckenwood.ce.globalactor.GlobalActorService;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AccessibilityUtils {
    private static final String TAG = AccessibilityUtils.class.getSimpleName();

    public static boolean isEnabled(Context context) {
        if (context == null) {
            AppLog.d(TAG, "The context is invalid.");
            return false;
        }
        AccessibilityManager manager = (AccessibilityManager) context.getSystemService("accessibility");
        if (manager == null) {
            AppLog.d(TAG, "The AccessibilityManager is invalid.");
            return false;
        }
        String aId = context.getPackageName() + "/" + GlobalActorService.class.getName();
        List<AccessibilityServiceInfo> runningServices = manager.getEnabledAccessibilityServiceList(-1);
        for (AccessibilityServiceInfo service : runningServices) {
            if (aId.equals(service.getId())) {
                return true;
            }
        }
        AppLog.d(TAG, "The AccessibilityManager is invalid.");
        return false;
    }
}
