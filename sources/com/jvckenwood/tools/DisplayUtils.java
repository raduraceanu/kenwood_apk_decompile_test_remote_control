package com.jvckenwood.tools;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.carconnectcontrol.AppLauncherApplication;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class DisplayUtils {
    private static Pair<Integer, Integer> _displaySize = null;
    private static Pair<Integer, Integer> _contentAreaSize = null;
    public static int BASE_DISPLAY_WIDTH = 800;
    public static int BASE_DISPLAY_HEIGHT = 480;
    private static Typeface _face = null;

    public static void initializeDisplaySize(Context context) {
        _displaySize = getDisplaySize(context);
    }

    public static void initialize(Context context, View layout) {
        ICooperationLib lib = AppLauncherApplication.getLib(context);
        ICooperationLib.IScreenMetrics screenMetrics = lib.getIdScreenMetrics();
        BASE_DISPLAY_HEIGHT = screenMetrics.getHeight();
        BASE_DISPLAY_WIDTH = screenMetrics.getWidth();
        if (_displaySize == null) {
            _displaySize = getDisplaySize(context);
        }
        if (layout != null) {
            _contentAreaSize = getHorizontalSize(layout.getWidth(), layout.getHeight());
        }
    }

    public static Pair<Integer, Integer> getHorizontalSize(int width, int height) {
        if (width < height) {
            width = height;
            height = width;
        }
        return new Pair<>(Integer.valueOf(width), Integer.valueOf(height));
    }

    public static Pair<Integer, Integer> getDisplaySize(Context context) {
        if (_displaySize == null) {
            WindowManager manager = (WindowManager) context.getSystemService("window");
            Display display = manager.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            _displaySize = getHorizontalSize(metrics.widthPixels, metrics.heightPixels);
            try {
                Method getRawW = Display.class.getMethod("getRawWidth", new Class[0]);
                Method getRawH = Display.class.getMethod("getRawHeight", new Class[0]);
                int width = ((Integer) getRawW.invoke(display, new Object[0])).intValue();
                int height = ((Integer) getRawH.invoke(display, new Object[0])).intValue();
                _displaySize = getHorizontalSize(width, height);
            } catch (IllegalAccessException e) {
            } catch (IllegalArgumentException e2) {
            } catch (NoSuchMethodException e3) {
            } catch (InvocationTargetException e4) {
            }
            try {
                Method getRealSize = Display.class.getMethod("getRealSize", Point.class);
                Point point = new Point();
                getRealSize.invoke(display, point);
                _displaySize = getHorizontalSize(point.x, point.y);
            } catch (IllegalAccessException e5) {
            } catch (IllegalArgumentException e6) {
            } catch (NoSuchMethodException e7) {
            } catch (InvocationTargetException e8) {
            }
        }
        return _displaySize;
    }

    public static Pair<Integer, Integer> getContentAreaSize(Context context) {
        return _contentAreaSize;
    }

    public static Pair<Integer, Integer> getDefaultDisplaySize(Context context) {
        Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return getHorizontalSize(size.x, size.y);
    }

    public static int getMeasuredHorizontalSize(int origin, int width) {
        return (origin * width) / BASE_DISPLAY_WIDTH;
    }

    public static int getMeasuredVerticalSize(int origin, int height) {
        return (origin * height) / BASE_DISPLAY_HEIGHT;
    }

    public static float getMesuredHorizontalScale(int width) {
        float scale = (1.0f * width) / BASE_DISPLAY_WIDTH;
        return scale;
    }

    public static float getMesuredVerticalScale(int height) {
        float scale = (1.0f * height) / BASE_DISPLAY_HEIGHT;
        return scale;
    }

    public static Typeface getTypeface() {
        return _face;
    }

    public static Pair<Integer, Integer> getDisplaySize() {
        return _displaySize;
    }
}
