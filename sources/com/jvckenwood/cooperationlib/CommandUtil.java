package com.jvckenwood.cooperationlib;

import com.jvckenwood.tools.AppLog;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;

/* JADX INFO: loaded from: classes.dex */
public class CommandUtil {
    private static final String TAG = "CommandUtil";

    public static byte[] join(byte[]... prms) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (byte[] prm : prms) {
            try {
                out.write(prm);
            } catch (Exception e) {
                AppLog.e(TAG, e.getMessage());
            }
        }
        try {
            out.flush();
            out.close();
        } catch (Exception e2) {
            AppLog.e(TAG, e2.getMessage());
        }
        return out.toByteArray();
    }

    public static long readLong(byte[] bytes, int offset, int length) {
        long val = 0;
        for (int i = 0; i < length; i++) {
            byte b = bytes[offset + i];
            val = (val << 8) | ((long) (b & 255));
        }
        return val;
    }

    public static int readInt(byte[] bytes, int offset, int length) {
        return (int) readLong(bytes, offset, length);
    }

    public static float readFloat(byte[] bytes, int offset) throws Throwable {
        DataInputStream st;
        float f = 0.0f;
        DataInputStream st2 = null;
        try {
            try {
                st = new DataInputStream(new ByteArrayInputStream(bytes, offset, 4));
            } catch (Exception e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            f = st.readFloat();
            if (st != null) {
                try {
                    st.close();
                } catch (Exception e2) {
                }
            }
        } catch (Exception e3) {
            e = e3;
            st2 = st;
            AppLog.e(TAG, e.getMessage(), e);
            if (st2 != null) {
                try {
                    st2.close();
                } catch (Exception e4) {
                }
            }
        } catch (Throwable th2) {
            th = th2;
            st2 = st;
            if (st2 != null) {
                try {
                    st2.close();
                } catch (Exception e5) {
                }
            }
            throw th;
        }
        return f;
    }

    public static String readString(byte[] command, int pos) throws UnsupportedEncodingException {
        int len = 0;
        for (int i = pos; i < command.length && command[i] != 0; i++) {
            len++;
        }
        return new String(command, pos, len, "UTF-8");
    }

    public static String readString(byte[] command, int pos, int limitLength) throws IndexOutOfBoundsException, UnsupportedEncodingException {
        int len = 0;
        for (int i = pos; i < command.length && command[i] != 0; i++) {
            len++;
            if (limitLength <= len) {
                throw new IndexOutOfBoundsException();
            }
        }
        return new String(command, pos, len, "UTF-8");
    }

    public static byte[] getBytes(long val, int byteSize) {
        byte[] buf = new byte[byteSize];
        for (int i = byteSize - 1; i >= 0; i--) {
            buf[i] = (byte) (255 & val);
            val >>= 8;
        }
        return buf;
    }

    public static byte[] getFloatBytes(float val) {
        byte[] rc = {0, 0, 0, 0};
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(buf);
        try {
            try {
                dataOutputStream.writeFloat(val);
                rc = buf.toByteArray();
            } catch (Exception e) {
                AppLog.d(TAG, e.getMessage());
                try {
                    buf.close();
                } catch (Exception e2) {
                    AppLog.d(TAG, e2.getMessage());
                }
            }
            return rc;
        } finally {
            try {
                buf.close();
            } catch (Exception e22) {
                AppLog.d(TAG, e22.getMessage());
            }
        }
    }

    public static byte[] getBytes(String str) throws UnsupportedEncodingException {
        return (str == null || str.length() < 1) ? new byte[]{0} : (str + "\u0000").getBytes("UTF-8");
    }

    public static byte[] getStringByteWithSize(String str, int byteSize) throws UnsupportedEncodingException {
        byte[] container = new byte[byteSize];
        byte[] strByte = str.getBytes("UTF-8");
        if (strByte.length < byteSize - 1) {
            System.arraycopy(strByte, 0, container, 0, strByte.length);
        } else {
            System.arraycopy(strByte, 0, container, 0, byteSize - 1);
        }
        AppLog.d(TAG, "length = " + container.length);
        return container;
    }

    public static byte[] createBasicReturnCommand(CommandId cmdId, int resultCode) {
        byte[] command = {cmdId.getByte0(), cmdId.getByte1(), (byte) resultCode, 0, 0, 0, 0, 1};
        return command;
    }
}
