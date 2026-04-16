package com.jvckenwood.carconnectcontrol.lib.api.info;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class VCarSettingInfo implements Parcelable {
    public static final Parcelable.Creator<VCarSettingInfo> CREATOR = new Parcelable.Creator<VCarSettingInfo>() { // from class: com.jvckenwood.carconnectcontrol.lib.api.info.VCarSettingInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VCarSettingInfo createFromParcel(Parcel p) {
            VCarSettingInfo o = new VCarSettingInfo();
            o.carType = p.readByte();
            o.destination = p.readByte();
            o.country = p.readByte();
            o.language = p.readByte();
            return o;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VCarSettingInfo[] newArray(int size) {
            return new VCarSettingInfo[size];
        }
    };
    private byte carType;
    private byte country;
    private byte destination;
    private byte language;

    public byte getCarType() {
        return this.carType;
    }

    public void setCarType(byte carType) {
        this.carType = carType;
    }

    public byte getDestination() {
        return this.destination;
    }

    public void setDestination(byte destination) {
        this.destination = destination;
    }

    public byte getCountry() {
        return this.country;
    }

    public void setCountry(byte country) {
        this.country = country;
    }

    public byte getLanguage() {
        return this.language;
    }

    public void setLanguage(byte language) {
        this.language = language;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel p, int flags) {
        p.writeByte(this.carType);
        p.writeByte(this.destination);
        p.writeByte(this.country);
        p.writeByte(this.language);
    }
}
