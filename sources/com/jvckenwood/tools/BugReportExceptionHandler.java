package com.jvckenwood.tools;

import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread;

/* JADX INFO: loaded from: classes.dex */
public class BugReportExceptionHandler implements Thread.UncaughtExceptionHandler {
    public static final String FILE_NAME = "bug_com.jvckenwood.HID_ThinClient.KWD.txt";
    private Thread.UncaughtExceptionHandler _defaultHandler;

    public BugReportExceptionHandler() {
        this._defaultHandler = null;
        this._defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable ex) throws Throwable {
        saveExceptionInfo(ex);
        this._defaultHandler.uncaughtException(thread, ex);
    }

    private boolean saveExceptionInfo(Throwable ex) throws Throwable {
        PrintWriter writer;
        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + FILE_NAME);
            PrintWriter writer2 = null;
            try {
                try {
                    writer = new PrintWriter(new FileOutputStream(file));
                } catch (Throwable th) {
                    th = th;
                }
            } catch (IOException e) {
            } catch (Exception e2) {
            }
            try {
                ex.printStackTrace(writer);
                if (writer == null) {
                    return true;
                }
                writer.close();
                return true;
            } catch (IOException e3) {
                writer2 = writer;
                if (file.exists()) {
                    file.delete();
                }
                if (writer2 != null) {
                    writer2.close();
                }
                return false;
            } catch (Exception e4) {
                writer2 = writer;
                if (file.exists()) {
                    file.delete();
                }
                if (writer2 != null) {
                    writer2.close();
                }
                return false;
            } catch (Throwable th2) {
                th = th2;
                writer2 = writer;
                if (writer2 != null) {
                    writer2.close();
                }
                throw th;
            }
        } catch (Exception e5) {
            return false;
        }
    }
}
