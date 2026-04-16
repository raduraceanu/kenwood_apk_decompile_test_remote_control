package com.jvckenwood.android.cooperationlib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.SparseArray;
import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.android.launcherconnectionservice.interfaces.ConnectionServiceBindIntent;
import com.jvckenwood.android.launcherconnectionservice.interfaces.IAppCallback;
import com.jvckenwood.android.launcherconnectionservice.interfaces.IWhiteListRec;
import com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp;
import com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IAppBind;
import com.jvckenwood.android.launcherconnectionservice.interfaces.ParcelableWhiteList;
import com.jvckenwood.cooperationlib.CommandId;
import com.jvckenwood.cooperationlib.CommandUtil;
import com.jvckenwood.tools.AppLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class CooperationLib implements ICooperationLib {
    private static final List<IWhiteListRec> EMPTY_WHITE_LIST = new ArrayList();
    private static final String TAG = "CooperationEngine";
    private String _protocolString;
    private final CooperationLib outer = this;
    private IApp _service = null;
    private Intent _serviceIntent = null;
    private Handler _handler = new Handler();
    private Context _context = null;
    private boolean _bCooperation = false;
    private ICooperationLib.IScreenMetrics _idScreenMetrics = null;
    private ICooperationLib.IIdSettings _idSettings = null;
    private boolean _isRunning = false;
    private final SparseArray<ICooperationLib.IReceiveMessageHandler> _mapReceiveHandler = new SparseArray<>();
    private ICooperationLib.IStateChangeListener _stateChangeListener = null;
    private List<IWhiteListRec> _whiteList = EMPTY_WHITE_LIST;
    private ICooperationLib.ILauncherActionHandler _launcherActionHandler = null;
    private final HashMap<ICooperationLib.ILauncherActionHandler.LauncherAction, Runnable> _mapLauncherActionFinishHandler = new HashMap<>();
    private ICooperationLib.ApplicationState _appState = ICooperationLib.ApplicationState.None;
    private ServiceConnection _serviceConnection = new ServiceConnection() { // from class: com.jvckenwood.android.cooperationlib.CooperationLib.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                IAppBind bind = IAppBind.Stub.asInterface(service);
                CooperationLib.this._service = bind.startBind();
                CooperationLib.this._service.registerApp(CooperationLib.this._protocolString, CooperationLib.this._appCallback);
                if (CooperationLib.this._whiteList != CooperationLib.EMPTY_WHITE_LIST) {
                    CooperationLib.this._service.uplodadWhiteList(new ParcelableWhiteList(CooperationLib.this._whiteList));
                }
                CooperationLib.this.fireStateChange(ICooperationLib.IStateChangeListener.StateKind.CooperationState);
            } catch (Exception e) {
                AppLog.e(CooperationLib.TAG, e.getMessage(), e);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            try {
                CooperationLib.this._service = null;
            } catch (Exception e) {
                AppLog.e(CooperationLib.TAG, e.getMessage(), e);
            }
        }
    };
    private IAppCallback _appCallback = new IAppCallback.Stub() { // from class: com.jvckenwood.android.cooperationlib.CooperationLib.4
        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.IAppCallback
        public void onChangeCommState() throws RemoteException {
            CooperationLib.this._handler.post(new Runnable() { // from class: com.jvckenwood.android.cooperationlib.CooperationLib.4.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        CooperationLib.this._bCooperation = false;
                        CooperationLib.this.fireStateChange(ICooperationLib.IStateChangeListener.StateKind.CooperationState);
                    } catch (Exception e) {
                        AppLog.e(CooperationLib.TAG, e.getMessage(), e);
                    }
                }
            });
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.IAppCallback
        public void onReceiveCommand(byte[] command) throws RemoteException {
            AppLog.v(CooperationLib.TAG, "onReceiveCommand() in");
            try {
                int cmdId = CommandUtil.readInt(command, 0, 2);
                ICooperationLib.IReceiveMessageHandler handler = (ICooperationLib.IReceiveMessageHandler) CooperationLib.this._mapReceiveHandler.get(cmdId);
                if (handler != null) {
                    handler.onReceiveMessage(CooperationLib.this.outer, command);
                } else {
                    CooperationLib.this.sendMessage(CommandUtil.join(CommandUtil.createBasicReturnCommand(CommandId.CMD_RETURN_NOT_SUPPORTED, 0), command));
                }
            } catch (Exception e) {
                AppLog.e(CooperationLib.TAG, e.getMessage(), e);
            }
            AppLog.v(CooperationLib.TAG, "onReceiveCommand() out");
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.IAppCallback
        public void onDisconnectRequest(int reason) throws RemoteException {
            AppLog.d(CooperationLib.TAG, "onDisconnectRequest() in");
            try {
                CooperationLib.this._bCooperation = false;
                CooperationLib.this._service.unregisterApp();
                CooperationLib.this._service.registerApp(CooperationLib.this._protocolString, CooperationLib.this._appCallback);
                CooperationLib.this.fireStateChange(ICooperationLib.IStateChangeListener.StateKind.CooperationState);
            } catch (Exception e) {
                AppLog.e(CooperationLib.TAG, e.getMessage(), e);
            }
            AppLog.d(CooperationLib.TAG, "onDisconnectRequest() out");
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.IAppCallback
        public void onDisconnect() throws RemoteException {
            AppLog.d(CooperationLib.TAG, "onDisconnect() in");
            try {
                CooperationLib.this.setRunningState(false);
            } catch (Exception e) {
                AppLog.e(CooperationLib.TAG, e.getMessage(), e);
            }
            AppLog.d(CooperationLib.TAG, "onDisconnect() out");
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.IAppCallback
        public void onConnect() throws RemoteException {
            AppLog.d(CooperationLib.TAG, "onConnect() in");
            AppLog.d(CooperationLib.TAG, "onConnect() out");
        }
    };
    private final CommonCommandHandler _cch = new CommonCommandHandler();

    public CooperationLib() {
        this._mapReceiveHandler.put(CommandId.CMD_CONNECT.value, new ICooperationLib.IReceiveMessageHandler() { // from class: com.jvckenwood.android.cooperationlib.CooperationLib.5
            @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IReceiveMessageHandler
            public void onReceiveMessage(ICooperationLib sender, byte[] message) throws Throwable {
                byte[] cmdReceive = CooperationLib.this._cch.handle_CMD_CONNECT(message);
                CooperationLib.this.outer._bCooperation = CooperationLib.this._cch.isCooperation();
                if (CooperationLib.this.outer._bCooperation) {
                    CooperationLib.this.outer._idScreenMetrics = CooperationLib.this._cch.getIdScreenMetrics();
                    CooperationLib.this.outer._idSettings = CooperationLib.this._cch.getIdSettings();
                }
                if (CooperationLib.this._service != null && cmdReceive != null) {
                    try {
                        CooperationLib.this._service.sendCommand(cmdReceive);
                    } catch (Exception e) {
                        AppLog.e(CooperationLib.TAG, e.getMessage(), e);
                    }
                }
            }
        });
        this._mapReceiveHandler.put(CommandId.CMD_NOTIFY_READY_CONNECT.value, new ICooperationLib.IReceiveMessageHandler() { // from class: com.jvckenwood.android.cooperationlib.CooperationLib.6
            @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IReceiveMessageHandler
            public void onReceiveMessage(ICooperationLib sender, byte[] message) {
                CooperationLib.this.fireStateChange(ICooperationLib.IStateChangeListener.StateKind.CooperationState);
            }
        });
        this._mapReceiveHandler.put(CommandId.CMD_DISCONNECT.value, new ICooperationLib.IReceiveMessageHandler() { // from class: com.jvckenwood.android.cooperationlib.CooperationLib.7
            @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IReceiveMessageHandler
            public void onReceiveMessage(ICooperationLib sender, byte[] message) {
                byte[] cmdReceive = CooperationLib.this._cch.handle_CMD_DISCONNECT(message);
                if (cmdReceive != null) {
                    sender.sendMessage(cmdReceive);
                    CooperationLib.this.outer._bCooperation = CooperationLib.this._cch.isCooperation();
                    if (!CooperationLib.this.outer._bCooperation) {
                        CooperationLib.this.fireStateChange(ICooperationLib.IStateChangeListener.StateKind.CooperationState);
                    }
                }
            }
        });
        this._mapReceiveHandler.put(CommandId.CMD_NOTIFY_RUNNING_STATE.value, new ICooperationLib.IReceiveMessageHandler() { // from class: com.jvckenwood.android.cooperationlib.CooperationLib.8
            @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IReceiveMessageHandler
            public void onReceiveMessage(ICooperationLib sender, byte[] message) {
                byte[] cmdReceive;
                if (CooperationLib.this.chkAppLinkModeSupportedCommand(sender, message) && (cmdReceive = CooperationLib.this._cch.handle_CMD_NOTIFY_RUNNING_STATE(message)) != null) {
                    sender.sendMessage(cmdReceive);
                    CooperationLib.this.setRunningState(CooperationLib.this._cch.isRunning());
                }
            }
        });
        this._mapReceiveHandler.put(CommandId.CMD_LAUNCHER_APP_START.value, new ICooperationLib.IReceiveMessageHandler() { // from class: com.jvckenwood.android.cooperationlib.CooperationLib.9
            @Override // com.jvckenwood.android.cooperationlib.ICooperationLib.IReceiveMessageHandler
            public void onReceiveMessage(ICooperationLib sender, byte[] message) {
                if (CooperationLib.this.chkAppLinkModeSupportedCommand(sender, message)) {
                    try {
                        byte[] cmdReceive = CooperationLib.this._cch.handle_CMD_LAUNCHER_APP_START(message);
                        if (cmdReceive != null) {
                            sender.sendMessage(cmdReceive);
                            if (CooperationLib.this._service != null) {
                                CooperationLib.this._service.startLauncherApp();
                            }
                        }
                    } catch (Exception e) {
                        AppLog.e(CooperationLib.TAG, e.getMessage(), e);
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireStateChange(final ICooperationLib.IStateChangeListener.StateKind stateKind) {
        if (this._stateChangeListener != null) {
            this._handler.post(new Runnable() { // from class: com.jvckenwood.android.cooperationlib.CooperationLib.2
                @Override // java.lang.Runnable
                public void run() {
                    CooperationLib.this._stateChangeListener.onChangeState(CooperationLib.this.outer, stateKind);
                }
            });
        }
    }

    private void fireLauncherAction(final ICooperationLib.ILauncherActionHandler.LauncherAction action) {
        if (this._launcherActionHandler != null) {
            this._handler.post(new Runnable() { // from class: com.jvckenwood.android.cooperationlib.CooperationLib.3
                @Override // java.lang.Runnable
                public void run() {
                    CooperationLib.this._launcherActionHandler.onHandleAction(CooperationLib.this.outer, action);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean chkAppLinkModeSupportedCommand(ICooperationLib sender, byte[] message) {
        if (this._cch.getIdSettings().getConnectMode() == ICooperationLib.IIdSettings.ConnectMode.APP_LINK) {
            return true;
        }
        byte[] cmdReceive = this._cch.handle_UnsupportedCommand(message);
        sender.sendMessage(cmdReceive);
        return false;
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public void start(Context context, String protocolString) {
        this._protocolString = protocolString;
        this._context = context.getApplicationContext();
        this._handler = new Handler();
        this._serviceIntent = new ConnectionServiceBindIntent(context);
        this._bCooperation = false;
        if (!this._context.bindService(this._serviceIntent, this._serviceConnection, 1)) {
            AppLog.e(TAG, "bind失敗");
        }
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public void stop() {
        if (this._service != null) {
            try {
                this._service.stopBind();
            } catch (Exception e) {
                AppLog.e(TAG, e.getMessage(), e);
            }
        }
        if (this._context != null) {
            this._service = null;
            this._context.unbindService(this._serviceConnection);
            this._context = null;
        }
        this._handler = null;
        this._serviceIntent = null;
        this._bCooperation = false;
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public void addReceiveMessageHandler(int commandId, ICooperationLib.IReceiveMessageHandler handler) {
        this._mapReceiveHandler.put(commandId, handler);
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0006, code lost:
    
        r2 = com.jvckenwood.android.cooperationlib.ICooperationLib.CooperationState.None;
     */
    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.jvckenwood.android.cooperationlib.ICooperationLib.CooperationState getCooperationState() {
        /*
            r4 = this;
            com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp r2 = r4._service     // Catch: java.lang.Exception -> L26
            if (r2 != 0) goto L7
            com.jvckenwood.android.cooperationlib.ICooperationLib$CooperationState r2 = com.jvckenwood.android.cooperationlib.ICooperationLib.CooperationState.None     // Catch: java.lang.Exception -> L26
        L6:
            return r2
        L7:
            com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp r2 = r4._service     // Catch: java.lang.Exception -> L26
            int r0 = r2.getCommState()     // Catch: java.lang.Exception -> L26
            switch(r0) {
                case 0: goto L13;
                case 1: goto L16;
                case 2: goto L19;
                case 3: goto L1c;
                default: goto L10;
            }
        L10:
            com.jvckenwood.android.cooperationlib.ICooperationLib$CooperationState r2 = com.jvckenwood.android.cooperationlib.ICooperationLib.CooperationState.None
            goto L6
        L13:
            com.jvckenwood.android.cooperationlib.ICooperationLib$CooperationState r2 = com.jvckenwood.android.cooperationlib.ICooperationLib.CooperationState.DeviceOff     // Catch: java.lang.Exception -> L26
            goto L6
        L16:
            com.jvckenwood.android.cooperationlib.ICooperationLib$CooperationState r2 = com.jvckenwood.android.cooperationlib.ICooperationLib.CooperationState.DeviceOn     // Catch: java.lang.Exception -> L26
            goto L6
        L19:
            com.jvckenwood.android.cooperationlib.ICooperationLib$CooperationState r2 = com.jvckenwood.android.cooperationlib.ICooperationLib.CooperationState.Listen     // Catch: java.lang.Exception -> L26
            goto L6
        L1c:
            boolean r2 = r4._bCooperation     // Catch: java.lang.Exception -> L26
            if (r2 == 0) goto L23
            com.jvckenwood.android.cooperationlib.ICooperationLib$CooperationState r2 = com.jvckenwood.android.cooperationlib.ICooperationLib.CooperationState.Cooperation     // Catch: java.lang.Exception -> L26
            goto L6
        L23:
            com.jvckenwood.android.cooperationlib.ICooperationLib$CooperationState r2 = com.jvckenwood.android.cooperationlib.ICooperationLib.CooperationState.Connect     // Catch: java.lang.Exception -> L26
            goto L6
        L26:
            r1 = move-exception
            java.lang.String r2 = "CooperationEngine"
            java.lang.String r3 = r1.getMessage()
            com.jvckenwood.tools.AppLog.e(r2, r3, r1)
            goto L10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jvckenwood.android.cooperationlib.CooperationLib.getCooperationState():com.jvckenwood.android.cooperationlib.ICooperationLib$CooperationState");
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public ICooperationLib.CooperationState getCooperationStateWithMode() {
        ICooperationLib.IIdSettings.ConnectMode connectMode = ICooperationLib.IIdSettings.ConnectMode.NONE;
        ICooperationLib.IIdSettings idSettings = getIdSettings();
        if (idSettings != null) {
            connectMode = idSettings.getConnectMode();
        }
        ICooperationLib.CooperationState cooperationState = getCooperationState();
        if (cooperationState == ICooperationLib.CooperationState.Connect) {
            if (connectMode == ICooperationLib.IIdSettings.ConnectMode.APP_LINK) {
                return ICooperationLib.CooperationState.ConnectAppLink;
            }
            if (connectMode == ICooperationLib.IIdSettings.ConnectMode.BT_HID) {
                return ICooperationLib.CooperationState.ConnectBtHid;
            }
            return cooperationState;
        }
        if (cooperationState == ICooperationLib.CooperationState.Cooperation) {
            if (connectMode == ICooperationLib.IIdSettings.ConnectMode.APP_LINK) {
                return ICooperationLib.CooperationState.CooperationAppLink;
            }
            if (connectMode == ICooperationLib.IIdSettings.ConnectMode.BT_HID) {
                return ICooperationLib.CooperationState.CooperationBtHid;
            }
            return cooperationState;
        }
        return cooperationState;
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public boolean isRunning() {
        return this._isRunning;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setRunningState(boolean isNewRunning) {
        if (this._isRunning == isNewRunning) {
            return false;
        }
        this._isRunning = isNewRunning;
        fireStateChange(ICooperationLib.IStateChangeListener.StateKind.RunningState);
        return true;
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public void stopCooperation() {
        if (getCooperationState() == ICooperationLib.CooperationState.Cooperation) {
            sendMessage(this._cch.createDisconnectCommand());
            this.outer._bCooperation = false;
            fireStateChange(ICooperationLib.IStateChangeListener.StateKind.CooperationState);
        }
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public boolean sendMessage(byte[] message) {
        if (this._service == null || getCooperationState() != ICooperationLib.CooperationState.Cooperation) {
            return false;
        }
        try {
            return this._service.sendCommand(message);
        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public boolean isCommEnabled() {
        try {
            if (this._service == null) {
                return false;
            }
            return this._service.isCommEnabled();
        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public void setCommEnabled(boolean sw) {
        try {
            if (this._service != null) {
                this._service.setCommEnabled(sw);
            }
        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage(), e);
        }
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public void setWhiteList(List<IWhiteListRec> whiteList) {
        this._whiteList = whiteList;
        if (this._service != null) {
            try {
                ParcelableWhiteList tmp = new ParcelableWhiteList(whiteList);
                this._service.uplodadWhiteList(tmp);
            } catch (Exception e) {
                AppLog.e(TAG, e.getMessage(), e);
            }
        }
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public List<IWhiteListRec> getWhiteList() {
        return this._whiteList;
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public ICooperationLib.IScreenMetrics getIdScreenMetrics() {
        return this._idScreenMetrics;
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public ICooperationLib.IIdSettings getIdSettings() {
        return this._idSettings;
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public void setReceiveTouchHandler(ICooperationLib.IReceiveTouchHandler handler) {
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public void setStateChangeListener(ICooperationLib.IStateChangeListener listener) {
        this._stateChangeListener = listener;
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public void setLauncherAction(ICooperationLib.ILauncherActionHandler handler) {
        this._launcherActionHandler = handler;
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public void notifyFinishLauncherAction(final ICooperationLib.ILauncherActionHandler.LauncherAction action) {
        if (this._launcherActionHandler != null) {
            this._handler.post(new Runnable() { // from class: com.jvckenwood.android.cooperationlib.CooperationLib.10
                @Override // java.lang.Runnable
                public void run() {
                    Runnable f = (Runnable) CooperationLib.this._mapLauncherActionFinishHandler.get(action);
                    if (f != null) {
                        CooperationLib.this._mapLauncherActionFinishHandler.remove(action);
                        f.run();
                    }
                }
            });
        }
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public void setApplicationState(ICooperationLib.ApplicationState state) {
        this._appState = state;
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public ICooperationLib.ApplicationState getApplicationState() {
        return this._appState;
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public int getDebugMode() {
        try {
            if (this._service == null) {
                return 0;
            }
            return this._service.getDebugMode();
        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage(), e);
            return 0;
        }
    }

    @Override // com.jvckenwood.android.cooperationlib.ICooperationLib
    public boolean setDebugMode(int value) {
        try {
            if (this._service == null) {
                return false;
            }
            return this._service.setDebugMode(value);
        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage(), e);
            return false;
        }
    }
}
