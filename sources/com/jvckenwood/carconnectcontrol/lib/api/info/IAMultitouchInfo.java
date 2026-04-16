package com.jvckenwood.carconnectcontrol.lib.api.info;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class IAMultitouchInfo implements Parcelable, Cloneable {
    public static final byte ACTION_CANCEL = 8;
    public static final byte ACTION_DOWN = 2;
    public static final byte ACTION_MOVE = 4;
    public static final byte ACTION_UP = 1;
    public static final Parcelable.Creator<IAMultitouchInfo> CREATOR = new Parcelable.Creator<IAMultitouchInfo>() { // from class: com.jvckenwood.carconnectcontrol.lib.api.info.IAMultitouchInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IAMultitouchInfo createFromParcel(Parcel p) {
            IAMultitouchInfo o = new IAMultitouchInfo();
            o.point1.x = p.readInt();
            o.point1.y = p.readInt();
            o.point2.x = p.readInt();
            o.point2.y = p.readInt();
            o.action = p.readByte();
            o.remark = p.readByte();
            o.touchPoints = p.readInt();
            o.time = p.readLong();
            return o;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IAMultitouchInfo[] newArray(int size) {
            return new IAMultitouchInfo[size];
        }
    };
    public static final byte REMARK_OFF = 0;
    public static final byte REMARK_ON = 1;
    private Point point1 = new Point();
    private Point point2 = new Point();
    private byte action = 0;
    private byte remark = 0;
    private int touchPoints = 0;
    private long time = 0;

    public Point getPoint1() {
        Point p = new Point();
        p.x = this.point1.x;
        p.y = this.point1.y;
        return p;
    }

    public void setPoint1(int x, int y) {
        this.point1.x = x;
        this.point1.y = y;
    }

    public Point getPoint2() {
        Point p = new Point();
        p.x = this.point2.x;
        p.y = this.point2.y;
        return p;
    }

    public void setPoint2(int x, int y) {
        this.point2.x = x;
        this.point2.y = y;
    }

    public byte getAction() {
        return this.action;
    }

    public void setAction(byte action) {
        this.action = action;
    }

    public byte getRemark() {
        return this.remark;
    }

    public void setRemark(byte remark) {
        this.remark = remark;
    }

    public int getTouchPoints() {
        return this.touchPoints;
    }

    public void setTouchPoints(int touchPoints) {
        this.touchPoints = touchPoints;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel p, int flags) {
        p.writeInt(this.point1.x);
        p.writeInt(this.point1.y);
        p.writeInt(this.point2.x);
        p.writeInt(this.point2.y);
        p.writeByte(this.action);
        p.writeByte(this.remark);
        p.writeInt(this.touchPoints);
        p.writeLong(this.time);
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public IAMultitouchInfo m4clone() {
        IAMultitouchInfo o = new IAMultitouchInfo();
        o.point1.x = this.point1.x;
        o.point1.y = this.point1.y;
        o.point2.x = this.point2.x;
        o.point2.y = this.point2.y;
        o.action = this.action;
        o.remark = this.remark;
        o.touchPoints = this.touchPoints;
        o.time = this.time;
        return o;
    }
}
