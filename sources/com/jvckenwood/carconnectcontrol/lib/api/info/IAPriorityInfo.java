package com.jvckenwood.carconnectcontrol.lib.api.info;

/* JADX INFO: loaded from: classes.dex */
public class IAPriorityInfo {

    public enum PRIORITY {
        NONE,
        LOW,
        HIGH;

        int getBit() {
            return ordinal();
        }
    }
}
