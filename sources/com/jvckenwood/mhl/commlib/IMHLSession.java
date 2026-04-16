package com.jvckenwood.mhl.commlib;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface IMHLSession {
    InputStream getInputStream();

    OutputStream getOutputStream();

    Map<String, Object> getProperties();
}
