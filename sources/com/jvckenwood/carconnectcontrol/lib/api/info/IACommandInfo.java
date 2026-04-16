package com.jvckenwood.carconnectcontrol.lib.api.info;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class IACommandInfo implements Parcelable {
    public static final Parcelable.Creator<IACommandInfo> CREATOR = new Parcelable.Creator<IACommandInfo>() { // from class: com.jvckenwood.carconnectcontrol.lib.api.info.IACommandInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IACommandInfo createFromParcel(Parcel source) {
            IACommandInfo o = new IACommandInfo();
            return o;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IACommandInfo[] newArray(int size) {
            return new IACommandInfo[size];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel p, int flags) {
    }
}
