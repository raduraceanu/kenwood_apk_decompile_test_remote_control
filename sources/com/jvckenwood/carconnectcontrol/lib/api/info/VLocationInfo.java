package com.jvckenwood.carconnectcontrol.lib.api.info;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public class VLocationInfo implements Parcelable, Cloneable {
    public static final Parcelable.Creator<VLocationInfo> CREATOR = new Parcelable.Creator<VLocationInfo>() { // from class: com.jvckenwood.carconnectcontrol.lib.api.info.VLocationInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VLocationInfo createFromParcel(Parcel p) {
            VLocationInfo o = new VLocationInfo();
            o.updateTime = new Date(p.readLong());
            o.updateFlag = p.readInt();
            o.deadReckoning = p.readInt();
            o.gPRMC = p.readString();
            o.pGRME = p.readString();
            String[] tmp = new String[p.readInt()];
            p.readStringArray(tmp);
            o.setGPGSV(tmp);
            o.gPGSVSentences = p.readInt();
            o.gGPGGA = p.readString();
            return o;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VLocationInfo[] newArray(int size) {
            return new VLocationInfo[size];
        }
    };
    public static final int FLAG_DEAD_RECKONING = 1;
    public static final int FLAG_GPGGA = 16;
    public static final int FLAG_GPGSV = 8;
    public static final int FLAG_GPRMC = 2;
    public static final int FLAG_PGRME = 4;
    public static final int MAX_SENTENCES = 5;
    private Date updateTime = new Date(0);
    private int updateFlag = 0;
    private int deadReckoning = 0;
    private String gPRMC = "";
    private String pGRME = "";
    private int gPGSVSentences = 0;
    private String gGPGGA = "";
    private final String[] gPGSV = new String[5];

    public VLocationInfo() {
        for (int i = 0; i < this.gPGSV.length; i++) {
            this.gPGSV[i] = "";
        }
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

    public int getUpdateFlag() {
        return this.updateFlag;
    }

    public void setUpdateFlag(int updateFlag) {
        this.updateFlag = updateFlag;
    }

    public int getDeadReckoning() {
        return this.deadReckoning;
    }

    public void setDeadReckoning(int deadReckoning) {
        this.deadReckoning = deadReckoning;
    }

    public String getGPRMC() {
        return this.gPRMC;
    }

    public void setGPRMC(String gprmc) {
        if (gprmc == null) {
            gprmc = "";
        }
        this.gPRMC = gprmc;
    }

    public String getPGRME() {
        return this.pGRME;
    }

    public void setPGRME(String pgrme) {
        if (pgrme == null) {
            pgrme = "";
        }
        this.pGRME = pgrme;
    }

    public String[] getGPGSV() {
        return this.gPGSV;
    }

    public void setGPGSV(String[] gpgsv) {
        if (gpgsv == null) {
            gpgsv = new String[5];
        }
        for (int i = 0; i < this.gPGSV.length; i++) {
            if (i < gpgsv.length) {
                this.gPGSV[i] = gpgsv[i];
            } else {
                this.gPGSV[i] = "";
            }
        }
    }

    public void setGPGSV(String gpgsv, int index) {
        this.gPGSV[index] = gpgsv;
    }

    public int getGPGSVSentences() {
        return this.gPGSVSentences;
    }

    public void setGPGSVSentences(int gpgsvSentences) {
        this.gPGSVSentences = gpgsvSentences;
    }

    public String getGPGGA() {
        return this.gGPGGA;
    }

    public void setGPGGA(String value) {
        if (value == null) {
            value = "";
        }
        this.gGPGGA = value;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel p, int flags) {
        p.writeLong(this.updateTime.getTime());
        p.writeInt(this.updateFlag);
        p.writeInt(this.deadReckoning);
        p.writeString(this.gPRMC);
        p.writeString(this.pGRME);
        p.writeInt(this.gPGSV.length);
        p.writeStringArray(this.gPGSV);
        p.writeInt(this.gPGSVSentences);
        p.writeString(this.gGPGGA);
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public VLocationInfo m6clone() {
        VLocationInfo o = new VLocationInfo();
        o.updateTime = (Date) this.updateTime.clone();
        o.updateFlag = this.updateFlag;
        o.deadReckoning = this.deadReckoning;
        o.gPRMC = this.gPRMC;
        o.pGRME = this.gPRMC;
        System.arraycopy(this.gPGSV, 0, o.gPGSV, 0, this.gPGSV.length);
        o.gPGSVSentences = this.gPGSVSentences;
        o.gGPGGA = this.gGPGGA;
        return o;
    }

    public static void update(int updateFlag, VLocationInfo locationInfo, VLocationInfo info) {
        if ((updateFlag & 1) != 0) {
            locationInfo.setDeadReckoning(info.getDeadReckoning());
        }
        if ((updateFlag & 2) != 0) {
            locationInfo.setGPRMC(info.getGPRMC());
        }
        if ((updateFlag & 4) != 0) {
            locationInfo.setPGRME(info.getPGRME());
        }
        if ((updateFlag & 8) != 0) {
            locationInfo.setGPGSVSentences(info.getGPGSVSentences());
            locationInfo.setGPGSV(info.getGPGSV());
        }
        if ((updateFlag & 16) != 0) {
            locationInfo.setGPGGA(info.getGPGGA());
        }
        locationInfo.setUpdateFlag(updateFlag);
        locationInfo.setUpdateTime(info.getUpdateTime().getTime());
    }
}
