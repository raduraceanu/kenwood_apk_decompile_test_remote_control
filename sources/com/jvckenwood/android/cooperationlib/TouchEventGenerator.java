package com.jvckenwood.android.cooperationlib;

import android.app.Instrumentation;
import android.graphics.Point;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import com.jvckenwood.carconnectcontrol.lib.api.info.IAMultitouchInfo;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public class TouchEventGenerator {
    private Instrumentation _instrumentation = new Instrumentation();
    private IAMultitouchInfo _lastInfo = null;
    private int _moovingPointerId = -1;
    private static final ExecutorService _queue = Executors.newFixedThreadPool(1);
    private static final int[] _pointerIdTable = {1, 0};

    public void sendMultitouchEvent(IAMultitouchInfo info) {
        if (Build.VERSION.SDK_INT < 14) {
            Log.w("TouchEventSender", "android build version invalid: " + Build.VERSION.SDK_INT);
            return;
        }
        int num = info.getTouchPoints();
        if (num <= 0 || 2 < num) {
            Log.e("TouchEventSender", "parameter invalid: touch point is " + num);
            return;
        }
        if (info.getRemark() == 1 && !Build.MODEL.equals("SC-02C")) {
            sendCancelEvent(info.getPoint1());
        }
        switch (info.getAction()) {
            case 1:
                if (num == 1) {
                    sendUpEvent(new Point[]{info.getPoint1()});
                } else {
                    sendUpEvent(new Point[]{info.getPoint1(), info.getPoint2()});
                }
                this._moovingPointerId = -1;
                break;
            case 2:
                if (num == 1) {
                    sendDownEvent(new Point[]{info.getPoint1()});
                } else {
                    sendDownEvent(new Point[]{info.getPoint1(), info.getPoint2()});
                }
                this._moovingPointerId = -1;
                break;
            case 4:
                if (num == 1) {
                    sendMoveEvent(new Point[]{info.getPoint1()});
                } else {
                    sendMoveEvent(new Point[]{info.getPoint1(), info.getPoint2()});
                }
                break;
            case 8:
                sendCancelEvent(info.getPoint1());
                break;
        }
        this._lastInfo = info;
    }

    private void sendCancelEvent(Point point) {
        MotionEvent.PointerProperties[] properties = createProperties(1);
        MotionEvent.PointerCoords[] pointerCoords = createPointerCoods(new Point[]{point});
        MotionEvent event = createMotionEvent(3, 1, properties, pointerCoords, new Point[]{point});
        postPointerEvent(event);
    }

    private void sendSingleMoveEvent(IAMultitouchInfo prev, Point point) {
        Point prevPoint1 = prev.getPoint1();
        Point prevPoint2 = prev.getPoint2();
        double prev1 = calcDistance(prevPoint1.x, prevPoint1.y, point.x, point.y);
        double prev2 = calcDistance(prevPoint2.x, prevPoint2.y, point.x, point.y);
        if (prev1 < prev2) {
            this._moovingPointerId = 0;
            sendReleaseEvent(1, new Point[]{point, prevPoint2});
        } else {
            this._moovingPointerId = 1;
            sendReleaseEvent(0, new Point[]{prevPoint1, point});
        }
    }

    private static double calcDistance(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    private void sendDownEvent(Point[] points) {
        int action = 0;
        int pointerCount = points.length;
        MotionEvent.PointerProperties[] properties = createProperties(pointerCount);
        MotionEvent.PointerCoords[] pointerCoords = createPointerCoods(points);
        for (int count = 1; count <= points.length; count++) {
            if (count != 1) {
                action = 261;
            }
            Point point = points[count - 1];
            MotionEvent event = createMotionEvent(action, count, properties, pointerCoords, new Point[]{point});
            postPointerEvent(event);
        }
    }

    private void sendMoveEvent(Point[] points) {
        int action;
        int count = points.length;
        MotionEvent.PointerProperties[] properties = createProperties(count);
        MotionEvent.PointerCoords[] pointerCoords = createPointerCoods(points);
        if (count == 1) {
            if (this._lastInfo != null && this._lastInfo.getTouchPoints() == 2 && this._lastInfo.getAction() == 4) {
                sendSingleMoveEvent(this._lastInfo, points[0]);
            }
            action = 2;
            if (this._moovingPointerId != -1) {
                properties[0].id = this._moovingPointerId;
            }
        } else {
            if (this._lastInfo != null && this._lastInfo.getTouchPoints() == 1 && this._lastInfo.getAction() == 4) {
                sendMultiMoveEvent(_pointerIdTable[this._moovingPointerId], points);
            }
            action = 2;
        }
        MotionEvent event = createMotionEvent(action, count, properties, pointerCoords, points);
        postPointerEvent(event);
    }

    private void sendUpEvent(Point[] points) {
        int action = 262;
        int pointerCount = points.length;
        MotionEvent.PointerProperties[] properties = createProperties(pointerCount);
        MotionEvent.PointerCoords[] pointerCoords = createPointerCoods(points);
        for (int count = points.length; count > 0; count--) {
            if (count == 1) {
                action = 1;
            }
            Point point = points[count - 1];
            MotionEvent event = createMotionEvent(action, count, properties, pointerCoords, new Point[]{point});
            postPointerEvent(event);
        }
    }

    private void sendMultiMoveEvent(int pointerId, Point[] points) {
        int action = (pointerId << 8) | 5;
        MotionEvent.PointerProperties[] properties = createProperties(2);
        MotionEvent.PointerCoords[] pointerCoords = createPointerCoods(points);
        MotionEvent event = createMotionEvent(action, 2, properties, pointerCoords, points);
        postPointerEvent(event);
    }

    private void sendReleaseEvent(int pointerId, Point[] points) {
        int action = (pointerId << 8) | 6;
        MotionEvent.PointerProperties[] properties = createProperties(2);
        MotionEvent.PointerCoords[] pointerCoords = createPointerCoods(points);
        MotionEvent event = createMotionEvent(action, 2, properties, pointerCoords, points);
        postPointerEvent(event);
    }

    private void postPointerEvent(final MotionEvent event) {
        _queue.execute(new Runnable() { // from class: com.jvckenwood.android.cooperationlib.TouchEventGenerator.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    TouchEventGenerator.this._instrumentation.sendPointerSync(event);
                    event.recycle();
                } catch (Exception e1) {
                    Log.e(getClass().getSimpleName(), e1.getMessage(), e1);
                }
            }
        });
    }

    private MotionEvent createMotionEvent(int action, int pointerCount, MotionEvent.PointerProperties[] properties, MotionEvent.PointerCoords[] pointerCoords, Point[] points) {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, action, pointerCount, properties, pointerCoords, 0, 0, 1.0f, 1.0f, 0, 0, 0, 0);
        return event;
    }

    private MotionEvent.PointerProperties[] createProperties(int num) {
        MotionEvent.PointerProperties[] properties = new MotionEvent.PointerProperties[num];
        for (int i = 0; i < num; i++) {
            MotionEvent.PointerProperties pp = new MotionEvent.PointerProperties();
            pp.id = i;
            pp.toolType = 1;
            properties[i] = pp;
        }
        return properties;
    }

    private MotionEvent.PointerCoords[] createPointerCoods(Point[] points) {
        MotionEvent.PointerCoords[] pointerCoords = new MotionEvent.PointerCoords[points.length];
        for (int i = 0; i < points.length; i++) {
            MotionEvent.PointerCoords pc = new MotionEvent.PointerCoords();
            pc.x = points[i].x;
            pc.y = points[i].y;
            pc.pressure = 1.0f;
            pc.size = 1.0f;
            pointerCoords[i] = pc;
        }
        return pointerCoords;
    }
}
