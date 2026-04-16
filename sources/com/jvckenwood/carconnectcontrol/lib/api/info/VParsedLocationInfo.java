package com.jvckenwood.carconnectcontrol.lib.api.info;

import android.os.Parcel;
import android.os.Parcelable;
import java.text.ParseException;

/* JADX INFO: loaded from: classes.dex */
public class VParsedLocationInfo implements Parcelable {
    public static final Parcelable.Creator<VParsedLocationInfo> CREATOR = new Parcelable.Creator<VParsedLocationInfo>() { // from class: com.jvckenwood.carconnectcontrol.lib.api.info.VParsedLocationInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VParsedLocationInfo createFromParcel(Parcel p) {
            VParsedLocationInfo o = new VParsedLocationInfo();
            o.latitude = p.readDouble();
            o.longitude = p.readDouble();
            o.time = p.readLong();
            o.speed = p.readFloat();
            o.bearing = p.readFloat();
            o.altitude = p.readDouble();
            o.horizontalAccuracy = p.readFloat();
            return o;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VParsedLocationInfo[] newArray(int size) {
            return new VParsedLocationInfo[size];
        }
    };
    private double latitude = 0.0d;
    private double longitude = 0.0d;
    private long time = 0;
    private float speed = 0.0f;
    private float bearing = 0.0f;
    private double altitude = 0.0d;
    private float horizontalAccuracy = 0.0f;

    public static VParsedLocationInfo parse(VLocationInfo info) throws ParseException {
        VParsedLocationInfo o = new VParsedLocationInfo();
        return o;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getBearing() {
        return this.bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public void setAltitude(double value) {
        this.altitude = value;
    }

    public float getAccuracy() {
        return this.horizontalAccuracy;
    }

    public void setAccuracy(float value) {
        this.horizontalAccuracy = value;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel p, int flags) {
        p.writeDouble(this.latitude);
        p.writeDouble(this.longitude);
        p.writeLong(this.time);
        p.writeFloat(this.speed);
        p.writeFloat(this.bearing);
        p.writeDouble(this.altitude);
        p.writeFloat(this.horizontalAccuracy);
    }
}
