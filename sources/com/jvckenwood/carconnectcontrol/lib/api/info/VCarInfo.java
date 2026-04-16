package com.jvckenwood.carconnectcontrol.lib.api.info;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class VCarInfo implements Parcelable {
    public static final Parcelable.Creator<VCarInfo> CREATOR = new Parcelable.Creator<VCarInfo>() { // from class: com.jvckenwood.carconnectcontrol.lib.api.info.VCarInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VCarInfo createFromParcel(Parcel source) {
            VCarInfo o = new VCarInfo();
            return o;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VCarInfo[] newArray(int size) {
            return new VCarInfo[size];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flag) {
    }

    public enum CARINFO_TYPE {
        CAN,
        LOCATION,
        SENSOR,
        SETTINGS;

        public int getBit() {
            return 1 << ordinal();
        }
    }
}
