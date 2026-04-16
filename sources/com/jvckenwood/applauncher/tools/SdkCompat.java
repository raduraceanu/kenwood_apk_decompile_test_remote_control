package com.jvckenwood.applauncher.tools;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;

/* JADX INFO: loaded from: classes.dex */
public class SdkCompat {
    public static void displayGetSize(Display display, Point outSize) {
        if (display != null && outSize != null) {
            if (Build.VERSION.SDK_INT >= 13) {
                displayGetSizeNew(display, outSize);
            } else {
                displayGetSizeLegacy(display, outSize);
            }
        }
    }

    @TargetApi(13)
    private static void displayGetSizeNew(Display display, Point outSize) {
        display.getSize(outSize);
    }

    @Deprecated
    private static void displayGetSizeLegacy(Display display, Point outSize) {
        outSize.set(display.getWidth(), display.getHeight());
    }
}
