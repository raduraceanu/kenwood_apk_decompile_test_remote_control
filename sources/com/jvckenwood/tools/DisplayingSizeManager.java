package com.jvckenwood.tools;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import com.jvckenwood.applauncher.tools.SdkCompat;

/* JADX INFO: loaded from: classes.dex */
public class DisplayingSizeManager {
    private static final int BASE_DISPLAY_SIZE = 1280;
    private static final String TAG = DisplayingSizeManager.class.getSimpleName();
    private static Context mContext = null;
    private static float mCoefficient = 0.0f;
    private static int mLauncherIconSize = -1;

    public static void init(Context context) {
        mContext = context;
        WindowManager manager = (WindowManager) context.getSystemService("window");
        Display display = manager.getDefaultDisplay();
        Point displayPoint = new Point();
        SdkCompat.displayGetSize(display, displayPoint);
        int width = displayPoint.x;
        int height = displayPoint.y;
        if (width <= height) {
            width = height;
        }
        float size = width;
        mCoefficient = size / 1280.0f;
    }

    public static int calcDisplayingSize(int basePixels) {
        return (int) (basePixels * mCoefficient);
    }

    public static int getLauncherIconSize() {
        if (mLauncherIconSize < 0) {
            mLauncherIconSize = calcDisplayingSize(196);
        }
        return mLauncherIconSize;
    }
}
