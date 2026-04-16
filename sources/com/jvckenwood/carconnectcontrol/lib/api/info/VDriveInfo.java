package com.jvckenwood.carconnectcontrol.lib.api.info;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public class VDriveInfo implements Parcelable {
    public static final Parcelable.Creator<VDriveInfo> CREATOR;
    public static final int FLAG_CAR_INFO_APPLICATION = 512;
    public static final int FLAG_COMMAND_RESPONSE = 1;
    public static final int FLAG_DISTRIBUTOR_APPLICATION = 256;
    public static final int FLAG_DSSS_INFORMATION = 1024;
    public static final int FLAG_NOTIFY_DSRC_AREA = 8;
    public static final int FLAG_NOTIFY_ETC_AREA = 4;
    public static final int FLAG_NOTIFY_INFRARRED_BEACON_AREA = 16;
    public static final int FLAG_ONBOARD_INFORMATION = 2;
    public static final int FLAG_PUSH_APPLICATION = 128;
    public static final int FLAG_RESOURCE_INFORMATION = 64;
    public static final int FLAG_SPF_CERTIFICATION_INFORMATION = 32;
    public static final int ID_CAR_INFO_APPLICATION = 98;
    public static final int ID_COMMAND_RESPONSE = 255;
    public static final int ID_DISTRIBUTOR_APPLICATION = 97;
    public static final int ID_DSSS_INFORMATION = 113;
    public static final int ID_NOTIFY_DSRC_AREA = 18;
    public static final int ID_NOTIFY_ETC_AREA = 17;
    public static final int ID_NOTIFY_INFRARRED_BEACON_AREA = 19;
    public static final int ID_ONBOARD_INFORMATION = 1;
    public static final int ID_PUSH_APPLICATION = 66;
    public static final int ID_RESOURCE_INFORMATION = 65;
    public static final int ID_SPF_CERTIFICATION_INFORMATION = 49;
    public static final SparseIntArray mapIdToFlag = new SparseIntArray();
    private Date updateTime = new Date(0);
    private int commandId = -1;
    private byte[] frameData = new byte[0];

    static {
        mapIdToFlag.put(1, 1);
        mapIdToFlag.put(17, 2);
        mapIdToFlag.put(18, 4);
        mapIdToFlag.put(19, 8);
        mapIdToFlag.put(49, 16);
        mapIdToFlag.put(65, 32);
        mapIdToFlag.put(66, 64);
        mapIdToFlag.put(97, 128);
        mapIdToFlag.put(98, 256);
        mapIdToFlag.put(ID_DSSS_INFORMATION, 512);
        mapIdToFlag.put(255, 1024);
        CREATOR = new Parcelable.Creator<VDriveInfo>() { // from class: com.jvckenwood.carconnectcontrol.lib.api.info.VDriveInfo.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public VDriveInfo createFromParcel(Parcel p) {
                VDriveInfo o = new VDriveInfo();
                o.updateTime = new Date(p.readLong());
                o.commandId = p.readInt();
                o.frameData = new byte[p.readInt()];
                p.readByteArray(o.frameData);
                return o;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public VDriveInfo[] newArray(int size) {
                return new VDriveInfo[size];
            }
        };
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime() {
        this.updateTime = new Date();
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = new Date(updateTime);
    }

    public int getCommandId() {
        return this.commandId;
    }

    public byte[] getFrameData() {
        return this.frameData;
    }

    public void setFrameData(byte[] frameData) {
        if (frameData == null) {
            this.frameData = new byte[0];
        } else {
            this.frameData = new byte[frameData.length];
            System.arraycopy(frameData, 0, this.frameData, 0, frameData.length);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel p, int flags) {
        p.writeLong(this.updateTime.getTime());
        p.writeInt(this.commandId);
        p.writeInt(this.frameData.length);
        p.writeByteArray(this.frameData);
    }

    public boolean isFlagOn(int id, int flag) {
        return (mapIdToFlag.get(id) & flag) != 0;
    }
}
