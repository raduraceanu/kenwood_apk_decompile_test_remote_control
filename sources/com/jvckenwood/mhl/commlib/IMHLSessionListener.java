package com.jvckenwood.mhl.commlib;

import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public interface IMHLSessionListener {
    void cancelListen();

    void init() throws IOException;

    IMHLSession listen() throws IOException;
}
