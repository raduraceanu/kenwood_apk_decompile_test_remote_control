package com.jvckenwood.carconnectcontrol.lib.api.info;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class IAResponseInfo implements Parcelable {
    public static final Parcelable.Creator<IAResponseInfo> CREATOR = new Parcelable.Creator<IAResponseInfo>() { // from class: com.jvckenwood.carconnectcontrol.lib.api.info.IAResponseInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IAResponseInfo createFromParcel(Parcel source) {
            IAResponseInfo o = new IAResponseInfo(RESULT.get(source.readInt()));
            return o;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IAResponseInfo[] newArray(int size) {
            return new IAResponseInfo[size];
        }
    };
    private RESULT result;

    public IAResponseInfo(RESULT result) {
        this.result = result;
    }

    public RESULT getResult() {
        return this.result;
    }

    public enum RESULT {
        NG,
        OK,
        TIME_OUT;

        static RESULT get(int ordinal) {
            if (ordinal == OK.ordinal()) {
                return OK;
            }
            if (ordinal == TIME_OUT.ordinal()) {
                return TIME_OUT;
            }
            return NG;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel p, int flags) {
        p.writeInt(getResult().ordinal());
    }
}
