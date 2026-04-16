package com.jvckenwood.android.launcherconnectionservice.impl;

import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.SparseArray;
import com.jvckenwood.android.launcherconnectionservice.interfaces.IAppCallback;
import com.jvckenwood.android.launcherconnectionservice.interfaces.IWhiteListRec;
import com.jvckenwood.android.launcherconnectionservice.interfaces.ParcelableWhiteList;
import com.jvckenwood.cooperationlib.CommandUtil;
import com.jvckenwood.mhl.commlib.MHLHandler;
import com.jvckenwood.tools.AppLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes.dex */
public class SessionManager {
    private static final byte CMD_SESSION_0 = -15;
    private static final byte CMD_SESSION_1 = 1;
    private static final byte CONNECTION_CLOSE = 6;
    private static final byte CONNECTION_NOTIFY_ESTABLISHED = 7;
    private static final byte SESSION_COMMAND = 4;
    private static final byte SESSION_NOOP = 5;
    private static final byte SESSION_NOTIFY_CONNECT = 1;
    private static final byte SESSION_NOTIFY_DISCONNECT = 2;
    private static final byte SESSION_REQUEST_DISCONNECT = 3;
    private static final String TAG = "SessionContainer";
    private static final String UTF8 = "UTF-8";
    private final MHLHandler _commForStatus;
    private final ConnectionServiceModel _connectionServiceModel;
    public final IServiceFunctions serviceFunctions;
    private String _launcherPackageName = "com.example.applauncher";
    private int _launcherAppSessionId = -1;
    private Timer _timer = new Timer();
    private Handler _handler = new Handler();
    private MHLHandler _comm = null;
    private boolean _bIdConnectionReady = false;
    private final HashMap<IBinder, SessionItem> _stubMap = new HashMap<>();
    private final HashMap<String, SessionItem> _connStrMap = new HashMap<>();
    private final SparseArray<SessionItem> _sarraySession = new SparseArray<>(256);
    private ParcelableWhiteList _whiteList = new ParcelableWhiteList();

    public enum DispatchReceviedCommandResult {
        OK,
        NG,
        DisconnectTransportRequest
    }

    public interface ILauncherCarInfoHookHandler {

        public enum WaitType {
            Location,
            Can
        }

        void onReadDebugComStat(boolean z);

        void onReadDebugWait(WaitType waitType, int i);

        void onReceiveCarInfo(Object obj);

        void resumeLastCarInfo(Object obj);
    }

    public interface ILauncherCommandHookHandler {
        void onReceiveLauncherCommand(long j, byte[] bArr);

        void resumeLastCommand(long j, byte[] bArr);
    }

    public interface IServiceFunctions {
        void startLauncher();
    }

    public SessionManager(MHLHandler commForStatus, ConnectionServiceModel model, IServiceFunctions serviceFunctions) {
        this._connectionServiceModel = model;
        this._commForStatus = commForStatus;
        this.serviceFunctions = serviceFunctions;
    }

    public void setup(String launcherPackageName) {
        this._launcherPackageName = launcherPackageName;
    }

    public void connect(MHLHandler comm) {
        try {
            AppLog.d(TAG, "connect in");
            this._comm = comm;
            this._bIdConnectionReady = false;
        } catch (Exception e) {
            AppLog.e(TAG, "connect", e);
        } finally {
            AppLog.d(TAG, "connect out");
        }
    }

    public void disconnect() {
        this._comm = null;
        this._bIdConnectionReady = false;
        try {
            resetSessionTimer();
            notifyChangeCommState();
            this._launcherAppSessionId = -1;
            int count = this._sarraySession.size();
            for (int i = 0; i < count; i++) {
                SessionItem item = this._sarraySession.valueAt(i);
                try {
                    item.callback.onDisconnect();
                    item.endSession();
                } catch (RemoteException e) {
                    AppLog.e(TAG, "disconnect", e);
                }
            }
        } catch (Exception e2) {
            AppLog.e(TAG, "disconnect", e2);
        } finally {
            this._sarraySession.clear();
        }
    }

    public boolean register(IBinder stub, String connectionString, IAppCallback callback) {
        try {
            AppLog.d(TAG, "register in " + connectionString);
            if (this._connStrMap.containsKey(connectionString)) {
                AppLog.d(TAG, "_connStrMap.containsKey() " + connectionString);
                return false;
            }
            if (this._stubMap.containsKey(stub)) {
                AppLog.d(TAG, "_stubMap.containsKey() " + connectionString);
                return false;
            }
            SessionItem item = new SessionItem(connectionString, callback);
            this._stubMap.put(stub, item);
            this._connStrMap.put(connectionString, item);
            AppLog.d(TAG, "registered " + connectionString);
            if (isConnect()) {
                int sessionID = getNextSessionID();
                if (this._launcherPackageName.equals(item.connectionString)) {
                    this._launcherAppSessionId = sessionID;
                }
                item.startSession(sessionID);
                this._sarraySession.put(sessionID, item);
                sendSessionCommand(1, sessionID, item.connectionString.getBytes(UTF8));
                try {
                    item.callback.onConnect();
                } catch (RemoteException e) {
                    AppLog.e(TAG, "connect", e);
                }
            }
            restartSessionTimer();
            return true;
        } catch (Exception e2) {
            AppLog.d(TAG, "register out " + connectionString);
            return false;
        } finally {
            AppLog.d(TAG, "register out");
        }
    }

    public boolean unregister(IBinder stub) {
        try {
            AppLog.d(TAG, "unregister in");
            SessionItem item = null;
            if (this._stubMap.containsKey(stub)) {
                SessionItem item2 = this._stubMap.get(stub);
                item = item2;
            }
            if (item == null) {
                AppLog.d(TAG, "unregister item not found");
                return false;
            }
            AppLog.d(TAG, "unregister session item found " + item.connectionString);
            if (item.isSessionStarted()) {
                int sessionID = item.getSessionID();
                if (this._launcherPackageName.equals(item.connectionString)) {
                    this._launcherAppSessionId = -1;
                }
                item.endSession();
                if (this._sarraySession.indexOfKey(sessionID) >= 0) {
                    this._sarraySession.remove(sessionID);
                }
                if (isConnect()) {
                    sendSessionCommand(2, sessionID, null);
                }
            }
            this._stubMap.remove(stub);
            this._connStrMap.remove(item.connectionString);
            restartSessionTimer();
            return true;
        } catch (Exception e) {
            AppLog.e(TAG, "unregister", e);
            return false;
        } finally {
            AppLog.d(TAG, "unregister out");
        }
    }

    public boolean reRegister(IBinder stub, String connectionString, IAppCallback callback) {
        Iterator<IBinder> it = this._stubMap.keySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            IBinder o = it.next();
            SessionItem item = this._stubMap.get(o);
            if (item.connectionString.equals(connectionString)) {
                if (!unregister(o)) {
                    AppLog.d(TAG, "reRegister unregister error. package=" + connectionString);
                }
            }
        }
        return register(stub, connectionString, callback);
    }

    public void unregisterAll() {
        AppLog.d(TAG, "unregisterAll in");
        ArrayList<IBinder> keys = new ArrayList<>();
        for (IBinder key : this._stubMap.keySet()) {
            keys.add(key);
        }
        for (IBinder key2 : keys) {
            unregister(key2);
        }
        AppLog.d(TAG, "unregisterAll out");
    }

    public int getSessionState(IBinder stub) {
        SessionItem item = this._stubMap.get(stub);
        if (item == null) {
            return 0;
        }
        if (!item.isSessionStarted()) {
            return 1;
        }
        return 2;
    }

    public boolean notifyChangeCommState() {
        try {
            for (SessionItem item : this._stubMap.values()) {
                try {
                    item.callback.onChangeCommState();
                } catch (RemoteException e) {
                    AppLog.e(TAG, "notifyChangeCommState() for " + item.connectionString, e);
                }
            }
            return true;
        } catch (Exception e2) {
            AppLog.e(TAG, "notifyChangeCommState", e2);
            return false;
        }
    }

    public DispatchReceviedCommandResult dispatchReceivedCommand(byte[] sessionCommand) {
        if (sessionCommand[0] != -15 || sessionCommand[1] != 1) {
            return DispatchReceviedCommandResult.NG;
        }
        int subCommand = getWord(sessionCommand, 2);
        int sessionID = getWord(sessionCommand, 4);
        if (subCommand == 6) {
            return DispatchReceviedCommandResult.DisconnectTransportRequest;
        }
        if (!this._bIdConnectionReady && subCommand == 7) {
            this._bIdConnectionReady = true;
            connectProc();
            return DispatchReceviedCommandResult.OK;
        }
        SessionItem item = this._sarraySession.get(sessionID);
        if (item == null) {
            return DispatchReceviedCommandResult.NG;
        }
        byte[] appCommand = new byte[Math.max(sessionCommand.length - 6, 0)];
        if (appCommand.length > 0) {
            System.arraycopy(sessionCommand, 6, appCommand, 0, appCommand.length);
        }
        boolean rc = false;
        long now = System.currentTimeMillis();
        switch (subCommand) {
            case 3:
                try {
                    item.callback.onDisconnectRequest(1);
                } catch (RemoteException e) {
                    AppLog.e(TAG, "dispatchReceiveCommand");
                }
                rc = true;
                break;
            case 4:
                try {
                    item.callback.onReceiveCommand(appCommand);
                } catch (RemoteException e2) {
                    AppLog.e(TAG, "dispatchReceiveCommand");
                }
                if (sessionID == this._launcherAppSessionId) {
                    int cmdId = CommandUtil.readInt(appCommand, 0, 2);
                    int count = this._sarraySession.size();
                    for (int i = 0; i < count; i++) {
                        SessionItem o = this._sarraySession.valueAt(i);
                        ILauncherCommandHookHandler handler = o.launcherHook.get(cmdId);
                        if (handler != null) {
                            try {
                                handler.onReceiveLauncherCommand(now, appCommand);
                            } catch (Exception e3) {
                                AppLog.e(TAG, "onReceiveLauncherCommand(" + o.connectionString + ")", e3);
                            }
                        }
                    }
                }
                rc = true;
                break;
            case 5:
                rc = true;
                item.refreshReceiveTimeout(new Date().getTime());
                restartSessionTimer();
                break;
        }
        return rc ? DispatchReceviedCommandResult.OK : DispatchReceviedCommandResult.NG;
    }

    public boolean sendAppCommand(IBinder stub, byte[] command) {
        SessionItem item = this._stubMap.get(stub);
        if (item != null && item.isSessionStarted()) {
            return sendSessionCommand(4, item.getSessionID(), command);
        }
        return false;
    }

    public boolean registerLauncherCommandHookHandler(IBinder stub, ILauncherCommandHookHandler handler, int[] commandIds) {
        SessionItem item = this._stubMap.get(stub);
        if (item == null) {
            return false;
        }
        for (int commandId : commandIds) {
            item.launcherHook.put(commandId, handler);
        }
        return true;
    }

    public List<IWhiteListRec> getWhiteList() {
        return this._whiteList;
    }

    public int getCommState() {
        if (!this._connectionServiceModel.isBluetoothEnabled()) {
            return 0;
        }
        if (!this._commForStatus.isAlive()) {
            return 1;
        }
        if (!this._commForStatus.isConnected()) {
            return 2;
        }
        return 3;
    }

    public void updateWhiteList(List<IWhiteListRec> whiteList) {
        this._whiteList.copyFrom(whiteList);
    }

    public Handler getHandler() {
        return this._handler;
    }

    private static class SessionItem {
        private static final long C_RECEIVE_TIMEOUT = 20000;
        private static final long C_SEND_TIMEOUT = 10000;
        public final IAppCallback callback;
        public final String connectionString;
        public final SparseArray<ILauncherCommandHookHandler> launcherHook = new SparseArray<>();
        private int _sessionID = 0;
        private long _tmSendTimeout = 0;
        private long _tmReceiveTimeout = 0;

        public SessionItem(String connectionString, IAppCallback callback) {
            this.connectionString = connectionString;
            this.callback = callback;
        }

        public void clearTimeout() {
            AppLog.d(SessionManager.TAG, "Clear Timeout");
            this._tmSendTimeout = 0L;
            this._tmReceiveTimeout = 0L;
        }

        public long getSendTimeout() {
            return this._tmSendTimeout;
        }

        public long getReceiveTimeout() {
            return this._tmReceiveTimeout;
        }

        public void refreshSendTimeout(long tmNow) {
            this._tmSendTimeout = C_SEND_TIMEOUT + tmNow;
            AppLog.d(SessionManager.TAG, String.format("Set SendTimeout = %s", new Date(this._tmSendTimeout)));
        }

        public void refreshReceiveTimeout(long tmNow) {
            this._tmReceiveTimeout = C_RECEIVE_TIMEOUT + tmNow;
            AppLog.v(SessionManager.TAG, String.format("Set ReseiveTimeout = %s", new Date(this._tmReceiveTimeout)));
        }

        public boolean isSendTimeout(long tmNow) {
            return 0 < this._tmSendTimeout && this._tmSendTimeout <= tmNow;
        }

        public boolean isReceiveTimeout(long tmNow) {
            return 0 < this._tmReceiveTimeout && this._tmReceiveTimeout <= tmNow;
        }

        public void startSession(int sessionID) {
            this._sessionID = sessionID;
            long tmNow = new Date().getTime();
            refreshReceiveTimeout(tmNow);
            refreshSendTimeout(tmNow);
        }

        public void endSession() {
            this._sessionID = 0;
            clearTimeout();
        }

        public int getSessionID() {
            return this._sessionID;
        }

        public boolean isSessionStarted() {
            return this._sessionID > 0;
        }
    }

    private boolean isConnect() {
        return this._comm != null && this._bIdConnectionReady;
    }

    private static byte[] join(byte[] a, byte[] b) {
        byte[] buf = new byte[a.length + b.length];
        System.arraycopy(a, 0, buf, 0, a.length);
        System.arraycopy(b, 0, buf, a.length, b.length);
        return buf;
    }

    private int getNextSessionID() {
        for (int id = 1; id < 65535; id++) {
            if (this._sarraySession.indexOfKey(id) < 0) {
                return id;
            }
        }
        return 0;
    }

    private boolean sendSessionCommand(int subCmd, int sessionID, byte[] extraBytes) {
        byte[] data;
        if (!isConnect()) {
            return false;
        }
        byte[] cmdHead = {CMD_SESSION_0, 1, getHiByte(subCmd), getLoByte(subCmd), getHiByte(sessionID), getLoByte(sessionID)};
        if (extraBytes == null || extraBytes.length < 1) {
            data = cmdHead;
        } else {
            data = join(cmdHead, extraBytes);
        }
        boolean rc = this._comm.sendMessage(data);
        CommunicationLog.writeSendLog(data);
        return rc;
    }

    private void restartSessionTimer() {
        resetSessionTimer();
        long tmNext = Long.MAX_VALUE;
        int count = this._sarraySession.size();
        for (int i = 0; i < count; i++) {
            SessionItem item = this._sarraySession.valueAt(i);
            long tm = item.getReceiveTimeout();
            if (0 < tm && tm < tmNext) {
                tmNext = tm;
            }
            long tm2 = item.getSendTimeout();
            if (0 < tm2 && tm2 < tmNext) {
                tmNext = tm2;
            }
        }
        if (tmNext < Long.MAX_VALUE) {
            this._timer.schedule(new TimerTask() { // from class: com.jvckenwood.android.launcherconnectionservice.impl.SessionManager.1
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    SessionManager.this._handler.post(new Runnable() { // from class: com.jvckenwood.android.launcherconnectionservice.impl.SessionManager.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            SessionManager.this.expireTimer();
                        }
                    });
                }
            }, new Date(tmNext));
        }
    }

    private void resetSessionTimer() {
        this._timer.cancel();
        this._timer = new Timer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void expireTimer() {
        int killSessionsCount;
        try {
            int[] killSessions = new int[256];
            long tmNow = new Date().getTime();
            int count = this._sarraySession.size();
            int i = 0;
            int killSessionsCount2 = 0;
            while (i < count) {
                SessionItem item = this._sarraySession.valueAt(i);
                if (item.isReceiveTimeout(tmNow)) {
                    AppLog.d(TAG, "ReceiveTimeout!");
                    killSessionsCount = killSessionsCount2 + 1;
                    killSessions[killSessionsCount2] = item.getSessionID();
                } else {
                    if (item.isSendTimeout(tmNow)) {
                        AppLog.d(TAG, "SendTimeout!");
                        sendSessionCommand(5, item.getSessionID(), null);
                        item.refreshSendTimeout(tmNow);
                    }
                    killSessionsCount = killSessionsCount2;
                }
                i++;
                killSessionsCount2 = killSessionsCount;
            }
            for (int i2 = 0; i2 < killSessionsCount2; i2++) {
                int sessionID = killSessions[i2];
                SessionItem item2 = this._sarraySession.get(sessionID);
                item2.clearTimeout();
                try {
                    item2.callback.onDisconnectRequest(2);
                } catch (RemoteException e) {
                    AppLog.e(TAG, "expireTimer", e);
                }
            }
        } catch (Exception e2) {
            AppLog.e(TAG, "expireTimer", e2);
        } finally {
            restartSessionTimer();
        }
    }

    private static int getWord(byte[] buf, int index) {
        return ((buf[index] & 255) << 8) | (buf[index + 1] & 255);
    }

    private static void setWord(byte[] buf, int index, int value) {
        buf[index] = getHiByte(value);
        buf[index + 1] = getLoByte(value);
    }

    private static byte getHiByte(int value) {
        return (byte) ((value >> 8) & 255);
    }

    private static byte getLoByte(int value) {
        return (byte) (value & 255);
    }

    private void connectProc() {
        notifyChangeCommState();
        this._sarraySession.clear();
        for (SessionItem item : this._stubMap.values()) {
            int sessionID = getNextSessionID();
            if (this._launcherPackageName.equals(item.connectionString)) {
                this._launcherAppSessionId = sessionID;
            }
            item.startSession(sessionID);
            this._sarraySession.put(sessionID, item);
            boolean rc = false;
            try {
                rc = sendSessionCommand(1, sessionID, item.connectionString.getBytes(UTF8));
            } catch (Exception e) {
                AppLog.e(TAG, e.getMessage(), e);
            }
            if (!rc) {
                AppLog.e(TAG, "Can not sent SESSION_NOTIFY_CONNECT command to #" + sessionID + " for " + item.connectionString);
            }
            try {
                item.callback.onConnect();
            } catch (RemoteException e2) {
                AppLog.e(TAG, "connect", e2);
            }
        }
        restartSessionTimer();
    }
}
