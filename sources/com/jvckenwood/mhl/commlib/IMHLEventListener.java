package com.jvckenwood.mhl.commlib;

import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface IMHLEventListener {
    void onConnected(MHLHandler mHLHandler, Map<String, Object> map);

    void onDisconnected(MHLHandler mHLHandler);

    void onError(MHLHandler mHLHandler, Exception exc);

    void onMessageReceived(MHLHandler mHLHandler, byte[] bArr);
}
