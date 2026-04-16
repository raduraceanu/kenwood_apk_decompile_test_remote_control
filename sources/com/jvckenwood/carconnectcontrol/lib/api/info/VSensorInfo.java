package com.jvckenwood.carconnectcontrol.lib.api.info;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public class VSensorInfo implements Parcelable, Cloneable {
    public static final Parcelable.Creator<VSensorInfo> CREATOR = new Parcelable.Creator<VSensorInfo>() { // from class: com.jvckenwood.carconnectcontrol.lib.api.info.VSensorInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VSensorInfo createFromParcel(Parcel p) {
            VSensorInfo o = new VSensorInfo();
            o.updateTime = p.readLong();
            o.updateFlag = p.readInt();
            o.interval = p.readLong();
            int len = p.readInt();
            o.gsensorX = new int[len];
            p.readIntArray(o.gsensorX);
            int len2 = p.readInt();
            o.gsensorY = new int[len2];
            p.readIntArray(o.gsensorY);
            int len3 = p.readInt();
            o.gsensorZ = new int[len3];
            p.readIntArray(o.gsensorZ);
            int len4 = p.readInt();
            o.gyroYaw = new int[len4];
            p.readIntArray(o.gyroYaw);
            int len5 = p.readInt();
            o.gyroPitch = new int[len5];
            p.readIntArray(o.gyroPitch);
            int len6 = p.readInt();
            o.gyroRoll = new int[len6];
            p.readIntArray(o.gyroRoll);
            return o;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VSensorInfo[] newArray(int size) {
            return new VSensorInfo[size];
        }
    };
    public static final int FLAG_GSENSOR_X = 1;
    public static final int FLAG_GSENSOR_Y = 2;
    public static final int FLAG_GSENSOR_Z = 4;
    public static final int FLAG_GYRO_PITCH = 16;
    public static final int FLAG_GYRO_ROLL = 32;
    public static final int FLAG_GYRO_YAW = 8;
    public static final int GSENSOR_SIZE = 8;
    public static final int GYRO_SIZE = 8;
    private long interval;
    private int updateFlag;
    private long updateTime;
    private int[] gsensorX = new int[0];
    private int[] gsensorY = new int[0];
    private int[] gsensorZ = new int[0];
    private int[] gyroYaw = new int[0];
    private int[] gyroPitch = new int[0];
    private int[] gyroRoll = new int[0];

    public Date getUpdateTime() {
        return new Date(this.updateTime);
    }

    public void setUpdateTime() {
        setUpdateTime(new Date().getTime());
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getUpdateFlag() {
        return this.updateFlag;
    }

    public void setUpdateFlag(int updateFlag) {
        this.updateFlag = updateFlag;
    }

    public int[] getGsensorX() {
        return this.gsensorX;
    }

    public void setGsensorX(int[] gsensorX) {
        this.gsensorX = gsensorX;
    }

    public int[] getGsensorY() {
        return this.gsensorY;
    }

    public void setGsensorY(int[] gsensorY) {
        this.gsensorY = gsensorY;
    }

    public int[] getGsensorZ() {
        return this.gsensorZ;
    }

    public void setGsensorZ(int[] gsensorZ) {
        this.gsensorZ = gsensorZ;
    }

    public int[] getGyroYaw() {
        return this.gyroYaw;
    }

    public void setGyroYaw(int[] gyro) {
        this.gyroYaw = gyro;
    }

    public int[] getGyroPitch() {
        return this.gyroPitch;
    }

    public void setGyroPitch(int[] gyro) {
        this.gyroPitch = gyro;
    }

    public int[] getGyroRoll() {
        return this.gyroRoll;
    }

    public void setGyroRoll(int[] gyro) {
        this.gyroRoll = gyro;
    }

    public long getInterval() {
        return this.interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel p, int flags) {
        p.writeLong(this.updateTime);
        p.writeInt(this.updateFlag);
        p.writeLong(this.interval);
        p.writeInt(this.gsensorX.length);
        p.writeIntArray(this.gsensorX);
        p.writeInt(this.gsensorY.length);
        p.writeIntArray(this.gsensorY);
        p.writeInt(this.gsensorZ.length);
        p.writeIntArray(this.gsensorZ);
        p.writeInt(this.gyroYaw.length);
        p.writeIntArray(this.gyroYaw);
        p.writeInt(this.gyroPitch.length);
        p.writeIntArray(this.gyroPitch);
        p.writeInt(this.gyroRoll.length);
        p.writeIntArray(this.gyroRoll);
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public VSensorInfo m7clone() {
        VSensorInfo o = new VSensorInfo();
        o.updateTime = this.updateTime;
        o.updateFlag = this.updateFlag;
        o.interval = this.interval;
        o.gsensorX = this.gsensorX;
        o.gsensorX = this.gsensorX;
        o.gsensorX = this.gsensorX;
        o.gyroYaw = this.gyroYaw;
        o.gyroPitch = this.gyroPitch;
        o.gyroRoll = this.gyroRoll;
        return o;
    }

    public static void update(int updateFlag, VSensorInfo sensorInfo, VSensorInfo info) {
        if ((updateFlag & 1) != 0) {
            sensorInfo.setGsensorX(info.gsensorX);
        }
        if ((updateFlag & 2) != 0) {
            sensorInfo.setGsensorY(info.gsensorY);
        }
        if ((updateFlag & 4) != 0) {
            sensorInfo.setGsensorZ(info.gsensorZ);
        }
        if ((updateFlag & 8) != 0) {
            sensorInfo.setGyroYaw(info.gyroYaw);
        }
        if ((updateFlag & 16) != 0) {
            sensorInfo.setGyroPitch(info.gyroPitch);
        }
        if ((updateFlag & 32) != 0) {
            sensorInfo.setGyroRoll(info.gyroRoll);
        }
        sensorInfo.setUpdateFlag(updateFlag);
        sensorInfo.setUpdateTime(info.getUpdateTime().getTime());
        sensorInfo.setInterval(info.getInterval());
    }
}
