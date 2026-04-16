package com.jvckenwood.carconnectcontrol.lib.api.info;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class AppContentsInfo implements Parcelable {
    public static final Parcelable.Creator<AppContentsInfo> CREATOR = new Parcelable.Creator<AppContentsInfo>() { // from class: com.jvckenwood.carconnectcontrol.lib.api.info.AppContentsInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppContentsInfo createFromParcel(Parcel source) {
            AppContentsInfo o = new AppContentsInfo();
            return o;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppContentsInfo[] newArray(int size) {
            return new AppContentsInfo[size];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel p, int flg) {
    }
}
