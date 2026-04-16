package com.jvckenwood.android.launcherconnectionservice.interfaces;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ParcelableWhiteList extends ArrayList<IWhiteListRec> implements Parcelable {
    public static final Parcelable.Creator<ParcelableWhiteList> CREATOR = new Parcelable.Creator<ParcelableWhiteList>() { // from class: com.jvckenwood.android.launcherconnectionservice.interfaces.ParcelableWhiteList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParcelableWhiteList createFromParcel(Parcel p) {
            ParcelableWhiteList o = new ParcelableWhiteList();
            int count = p.readInt();
            for (int i = 0; i < count; i++) {
                o.add(new ParcelableWhiteListRec(p));
            }
            return o;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParcelableWhiteList[] newArray(int size) {
            return new ParcelableWhiteList[size];
        }
    };
    private static final long serialVersionUID = 1;

    public ParcelableWhiteList() {
    }

    public ParcelableWhiteList(List<IWhiteListRec> whiteList) {
        copyFrom(whiteList);
    }

    public void copyFrom(List<IWhiteListRec> whiteList) {
        clear();
        for (IWhiteListRec rec : whiteList) {
            add(new ParcelableWhiteListRec(rec));
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel p, int flags) {
        int count = size();
        p.writeInt(count);
        for (IWhiteListRec rec : this) {
            ParcelableWhiteListRec.writeToParcel(p, rec);
        }
    }

    private static class ParcelableWhiteListRec implements IWhiteListRec {
        private String _ApplicationName;
        private final ArrayList<String> _CertHash = new ArrayList<>();
        private String _PackageName;
        private int _RestrictLv;

        public ParcelableWhiteListRec(IWhiteListRec o) {
            this._ApplicationName = "";
            this._PackageName = "";
            this._RestrictLv = 0;
            this._ApplicationName = o.getApplicationName();
            this._PackageName = o.getPackageName();
            this._RestrictLv = o.getRestrictLv();
            for (String hash : o.getCertHash()) {
                this._CertHash.add(hash);
            }
        }

        public ParcelableWhiteListRec(Parcel p) {
            this._ApplicationName = "";
            this._PackageName = "";
            this._RestrictLv = 0;
            this._ApplicationName = p.readString();
            this._PackageName = p.readString();
            this._RestrictLv = p.readInt();
            int cntHash = p.readInt();
            for (int i = 0; i < cntHash; i++) {
                this._CertHash.add(p.readString());
            }
        }

        public static void writeToParcel(Parcel p, IWhiteListRec o) {
            p.writeString(o.getApplicationName());
            p.writeString(o.getPackageName());
            p.writeInt(o.getRestrictLv());
            List<String> hashList = o.getCertHash();
            p.writeInt(hashList.size());
            for (String hash : hashList) {
                p.writeString(hash);
            }
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.IWhiteListRec
        public String getApplicationName() {
            return this._ApplicationName;
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.IWhiteListRec
        public String getPackageName() {
            return this._PackageName;
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.IWhiteListRec
        public int getRestrictLv() {
            return this._RestrictLv;
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.IWhiteListRec
        public List<String> getCertHash() {
            return this._CertHash;
        }
    }
}
