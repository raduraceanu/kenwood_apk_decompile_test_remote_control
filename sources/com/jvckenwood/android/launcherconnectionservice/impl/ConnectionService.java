package com.jvckenwood.android.launcherconnectionservice.impl;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import com.jvckenwood.HID_ThinClient.KWD.R;
import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.android.launcherconnectionservice.impl.SessionManager;
import com.jvckenwood.android.launcherconnectionservice.impl.sessions.BluetoothSessionListener;
import com.jvckenwood.android.launcherconnectionservice.interfaces.IAppCallback;
import com.jvckenwood.android.launcherconnectionservice.interfaces.INecConnectionManager;
import com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp;
import com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IAppBind;
import com.jvckenwood.android.launcherconnectionservice.interfaces.ParcelableWhiteList;
import com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager;
import com.jvckenwood.carconnectcontrol.AppLauncherApplication;
import com.jvckenwood.carconnectcontrol.IntentActions;
import com.jvckenwood.mhl.commlib.IMHLEventListener;
import com.jvckenwood.mhl.commlib.MHLHandler;
import com.jvckenwood.mhl.commlib.internal.ConnectionException;
import com.jvckenwood.tools.AppLog;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ConnectionService extends Service {
    public static final String SPP_SERVICE_NAME = "Launcher Application";
    public static final String SPP_UUID = "160b3720-7f28-11e3-ba0e-0002a5d5c51b";
    private static final Class<ConnectionService> _clazz = ConnectionService.class;
    private static final String TAG = _clazz.getSimpleName();
    private Handler _handler = new Handler();
    private PowerManager.WakeLock _wakeLock = null;
    private MHLHandler _comm = new MHLHandler(new BluetoothSessionListener(SPP_SERVICE_NAME, "160b3720-7f28-11e3-ba0e-0002a5d5c51b"));
    private StatusNotifier _notifier = null;
    private final ConnectionServiceModel _model = new ConnectionServiceModel();
    private SessionManager.IServiceFunctions _serviceForSessionManager = new SessionManager.IServiceFunctions() { // from class: com.jvckenwood.android.launcherconnectionservice.impl.ConnectionService.1
        @Override // com.jvckenwood.android.launcherconnectionservice.impl.SessionManager.IServiceFunctions
        public void startLauncher() {
            ConnectionService.this.sendBroadcast(ConnectionService.this._startLauncherAppBloadcastIntent);
        }
    };
    private SessionManager _sessionContainer = new SessionManager(this._comm, this._model, this._serviceForSessionManager);
    private final Intent _connectBloadcastIntent = new Intent(IntentActions.HEAD_UNIT_CONNECTED);
    private final Intent _disconnectBroadcastIntent = new Intent(IntentActions.HEAD_UNIT_DISCONNECTED);
    private final Intent _startLauncherAppBloadcastIntent = new Intent(IntentActions.START_LAUNCHER);
    private OverlayViewManager _overlayManager = null;
    private String _piaAddress = null;
    private final BroadcastReceiver _onCooperatedReceiver = new BroadcastReceiver() { // from class: com.jvckenwood.android.launcherconnectionservice.impl.ConnectionService.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null && ConnectionService.this._overlayManager != null) {
                String action = intent.getAction();
                if (IntentActions.CHANGE_COOPERATION_STATE.equals(action)) {
                    boolean isConnected = intent.getBooleanExtra("isConnected", false);
                    if (!isConnected) {
                        if (ConnectionService.this._overlayManager.isEnabled()) {
                            ConnectionService.this._notifier.update(R.drawable.icon_status, ConnectionService.this.getString(R.string.notification_connecting));
                        }
                        ConnectionService.this._overlayManager.hide();
                        return;
                    } else {
                        if (AppLauncherApplication.getLib(context).getCooperationStateWithMode() != ICooperationLib.CooperationState.CooperationAppLink) {
                            if (!ConnectionService.this._overlayManager.isEnabled()) {
                                ConnectionService.this._overlayManager.setEnabled(true);
                            }
                            ConnectionService.this._overlayManager.show();
                        }
                        ConnectionService.this._notifier.update(R.drawable.icon_status, ConnectionService.this.getString(R.string.notification_coordinating));
                        return;
                    }
                }
                if (IntentActions.CHANGE_RUNNING_STATE.equals(action) && ConnectionService.this._overlayManager.isDisplayingShowMode()) {
                    ConnectionService.this._overlayManager.updateOverlayButtonViews();
                }
            }
        }
    };
    private final BroadcastReceiver _onAllowOverlayButtonReceiver = new BroadcastReceiver() { // from class: com.jvckenwood.android.launcherconnectionservice.impl.ConnectionService.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null && ConnectionService.this._overlayManager != null) {
                String action = intent.getAction();
                if (IntentActions.ALLOW_OVERLAY_BUTTON.equals(action)) {
                    boolean isAlowed = intent.getBooleanExtra("allow", false);
                    ConnectionService.this._overlayManager.setEnabled(isAlowed);
                }
            }
        }
    };
    private IMHLEventListener _mhlEventListener = new AnonymousClass5();

    public enum ActionCommand {
        RefreshService,
        ResetListener
    }

    @Override // android.app.Service
    public void onCreate() {
        AppLog.d(TAG, "onCreate() in");
        try {
            super.onCreate();
            this._model.initialize(this);
            this._sessionContainer.setup(getPackageName());
            this._notifier = new StatusNotifier(this, R.id.notification_statusbar, R.drawable.icon_status, R.string.notification_connecting);
            IntentFilter filter = new IntentFilter(IntentActions.CHANGE_COOPERATION_STATE);
            filter.addAction(IntentActions.CHANGE_RUNNING_STATE);
            registerReceiver(this._onCooperatedReceiver, filter);
            registerReceiver(this._onAllowOverlayButtonReceiver, new IntentFilter(IntentActions.ALLOW_OVERLAY_BUTTON));
            this._overlayManager = new OverlayViewManager(this);
        } catch (Exception e) {
            e.printStackTrace();
            AppLog.e(TAG, e.getMessage(), e);
        }
        AppLog.d(TAG, "onCreate() out");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseWakeLock() {
        if (this._wakeLock != null) {
            if (this._wakeLock.isHeld()) {
                this._wakeLock.release();
            }
            this._wakeLock = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void aquireWakeLock(int lockType) {
        if (this._wakeLock != null) {
            releaseWakeLock();
        }
        this._wakeLock = ((PowerManager) getSystemService("power")).newWakeLock(lockType, TAG);
        this._wakeLock.acquire();
    }

    @Override // android.app.Service
    public void onDestroy() {
        AppLog.d(TAG, "onDestroy() in");
        try {
            if (this._comm.isAlive()) {
                this._comm.stopListening();
            }
            this._model.terminate();
            releaseWakeLock();
            if (this._notifier != null) {
                this._notifier.dismiss();
            }
            super.onDestroy();
            unregisterReceiver(this._onCooperatedReceiver);
            unregisterReceiver(this._onAllowOverlayButtonReceiver);
            this._overlayManager.hide();
            this._overlayManager.stopPermissionObserve();
        } catch (Exception e) {
            e.printStackTrace();
            AppLog.e(TAG, e.getMessage(), e);
        }
        AppLog.d(TAG, "onDestroy() out");
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        String sAction;
        AppLog.d(TAG, "onStartCommand() in");
        try {
            ActionCommand action = ActionCommand.RefreshService;
            if (intent != null && (sAction = intent.getAction()) != null && sAction.length() > 0) {
                try {
                    action = ActionCommand.valueOf(sAction);
                } catch (IllegalArgumentException e) {
                }
            }
            if (this._comm.isAlive()) {
                if (!this._model.isBluetoothEnabled() || !this._model.isCommEnabled()) {
                    this._comm.stopListening();
                    this._sessionContainer.notifyChangeCommState();
                    AppLog.d(TAG, "onStartCommand().stopListening()");
                } else if (this._comm.isConnected() && action == ActionCommand.ResetListener) {
                    Bundle extras = intent.getExtras();
                    String disconnectedPia = extras.getString("extra");
                    if (disconnectedPia != null && this._piaAddress != null && disconnectedPia.equalsIgnoreCase(this._piaAddress)) {
                        this._comm.stopListening();
                        this._comm.startListening(this._mhlEventListener);
                        AppLog.d(TAG, "onStartCommand().disconnect()");
                    } else {
                        AppLog.d(TAG, "ACL_DISCONNECTED by others");
                    }
                }
            } else if (this._model.isBluetoothEnabled() && this._model.isCommEnabled()) {
                this._comm.startListening(this._mhlEventListener);
                this._sessionContainer.notifyChangeCommState();
                AppLog.d(TAG, "onStartCommand().startListening()");
            }
            if (!this._comm.isAlive()) {
                stopSelf();
                AppLog.d(TAG, "onStartCommand().stopSelf()");
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            AppLog.e(TAG, e2.getMessage(), e2);
        }
        AppLog.d(TAG, "onStartCommand() out");
        return 1;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        IBinder binder = null;
        AppLog.d(TAG, "onBind() in");
        try {
            IBinder binder2 = new IAppBind.Stub() { // from class: com.jvckenwood.android.launcherconnectionservice.impl.ConnectionService.4
                @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IAppBind
                public IApp startBind() throws RemoteException {
                    ConnectionService.sendAction(ConnectionService.this, ActionCommand.RefreshService);
                    return ConnectionService.this.new AppBindStub();
                }

                @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IAppBind
                public INecConnectionManager startBindForNec() throws RemoteException {
                    return null;
                }
            };
            binder = binder2;
        } catch (Exception e) {
            e.printStackTrace();
            AppLog.e(TAG, e.getMessage(), e);
        }
        AppLog.d(TAG, "onBind() out");
        return binder;
    }

    @Override // android.app.Service
    public void onRebind(Intent intent) {
        AppLog.d(TAG, "onRebind() in");
        try {
            super.onRebind(intent);
        } catch (Exception e) {
            e.printStackTrace();
            AppLog.e(TAG, e.getMessage(), e);
        }
        AppLog.d(TAG, "onRebind() out");
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        AppLog.d(TAG, "onUnbbind() in");
        boolean rc = false;
        try {
            super.onUnbind(intent);
            this._sessionContainer.unregisterAll();
            rc = true;
        } catch (Exception e) {
            e.printStackTrace();
            AppLog.e(TAG, e.getMessage(), e);
        }
        AppLog.d(TAG, "onUnbbind() out");
        return rc;
    }

    /* JADX INFO: renamed from: com.jvckenwood.android.launcherconnectionservice.impl.ConnectionService$5, reason: invalid class name */
    class AnonymousClass5 implements IMHLEventListener {
        private boolean _onProcessConnected = false;
        private boolean _onReceiveDisconnected = false;

        AnonymousClass5() {
        }

        @Override // com.jvckenwood.mhl.commlib.IMHLEventListener
        public void onConnected(final MHLHandler comm, final Map<String, Object> prop1) {
            AppLog.d(ConnectionService.TAG, "onConnected(Async) in");
            this._onProcessConnected = true;
            this._onReceiveDisconnected = false;
            ConnectionService.this._handler.post(new Runnable() { // from class: com.jvckenwood.android.launcherconnectionservice.impl.ConnectionService.5.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        AppLog.d(ConnectionService.TAG, "onConnected(Sync) in");
                        if (AnonymousClass5.this._onReceiveDisconnected) {
                            AppLog.d(ConnectionService.TAG, "diconnect on connect");
                            AnonymousClass5.this._onProcessConnected = false;
                            return;
                        }
                        if (!comm.isConnected()) {
                            AppLog.d(ConnectionService.TAG, "!isConnected(). retry");
                            AnonymousClass5.this.onConnected(comm, prop1);
                            return;
                        }
                        if (prop1 == null || !prop1.containsKey("Address")) {
                            ConnectionService.this._piaAddress = null;
                        } else {
                            ConnectionService.this._piaAddress = (String) prop1.get("Address");
                        }
                        AnonymousClass5.this._onProcessConnected = false;
                        ConnectionService.this._sessionContainer.connect(comm);
                        ConnectionService.this.sendBroadcast(ConnectionService.this._connectBloadcastIntent);
                        ConnectionService.this._notifier.update(R.drawable.icon_status, ConnectionService.this.getString(R.string.notification_connecting));
                        ConnectionService.this.aquireWakeLock(6);
                    } catch (Exception e) {
                        e.printStackTrace();
                        AppLog.e(ConnectionService.TAG, e.getMessage(), e);
                    } finally {
                        AppLog.d(ConnectionService.TAG, "onConnected(Sync) out");
                    }
                }
            });
            AppLog.d(ConnectionService.TAG, "onConnected(Async) out");
        }

        @Override // com.jvckenwood.mhl.commlib.IMHLEventListener
        public void onDisconnected(MHLHandler comm) {
            AppLog.d(ConnectionService.TAG, "onDisconnected(Async) in");
            this._onReceiveDisconnected = true;
            if (!this._onProcessConnected) {
                ConnectionService.this._handler.post(new Runnable() { // from class: com.jvckenwood.android.launcherconnectionservice.impl.ConnectionService.5.2
                    @Override // java.lang.Runnable
                    public void run() {
                        AppLog.d(ConnectionService.TAG, "onDisconnected(Sync) in");
                        try {
                            ConnectionService.this._piaAddress = null;
                            ConnectionService.this._sessionContainer.disconnect();
                            ConnectionService.this._notifier.dismiss();
                            ConnectionService.this._overlayManager.setEnabled(false);
                            ConnectionService.this.sendBroadcast(ConnectionService.this._disconnectBroadcastIntent);
                            ConnectionService.this.releaseWakeLock();
                        } catch (Exception e) {
                            AppLog.e(ConnectionService.TAG, e.getMessage(), e);
                        }
                        AppLog.d(ConnectionService.TAG, "onDisconnected(Sync) out");
                    }
                });
                if (ConnectionService.this._model.isBluetoothEnabled() && ConnectionService.this._model.isCommEnabled()) {
                    comm.startListening(ConnectionService.this._mhlEventListener);
                }
                AppLog.d(ConnectionService.TAG, "onDisconnected(Async) out");
            }
        }

        @Override // com.jvckenwood.mhl.commlib.IMHLEventListener
        public void onError(MHLHandler comm, final Exception exception) {
            ConnectionService.this._handler.post(new Runnable() { // from class: com.jvckenwood.android.launcherconnectionservice.impl.ConnectionService.5.3
                @Override // java.lang.Runnable
                public void run() {
                    AppLog.d(ConnectionService.TAG, "onError() in");
                    if (exception instanceof ConnectionException) {
                        AppLog.e(ConnectionService.TAG, exception.getMessage(), exception);
                    } else {
                        AppLog.d(ConnectionService.TAG, exception.getMessage(), exception);
                    }
                    ConnectionService.this._piaAddress = null;
                    ConnectionService.this._sessionContainer.disconnect();
                    ConnectionService.this._notifier.dismiss();
                    ConnectionService.this._overlayManager.setEnabled(false);
                    ConnectionService.this.sendBroadcast(ConnectionService.this._disconnectBroadcastIntent);
                    ConnectionService.this.releaseWakeLock();
                    AppLog.d(ConnectionService.TAG, "onError() out");
                }
            });
        }

        @Override // com.jvckenwood.mhl.commlib.IMHLEventListener
        public void onMessageReceived(final MHLHandler comm, final byte[] data) {
            CommunicationLog.writeReceiveLog(data);
            ConnectionService.this._handler.post(new Runnable() { // from class: com.jvckenwood.android.launcherconnectionservice.impl.ConnectionService.5.4
                @Override // java.lang.Runnable
                public void run() {
                    AppLog.v(ConnectionService.TAG, "onMessageReceived() in");
                    try {
                        SessionManager.DispatchReceviedCommandResult rc = ConnectionService.this._sessionContainer.dispatchReceivedCommand(data);
                        switch (AnonymousClass6.$SwitchMap$com$jvckenwood$android$launcherconnectionservice$impl$SessionManager$DispatchReceviedCommandResult[rc.ordinal()]) {
                            case 1:
                                comm.stopListening();
                                comm.startListening(ConnectionService.this._mhlEventListener);
                                break;
                            case 3:
                                AppLog.e(ConnectionService.TAG, "dispatchReceivedCommand error");
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        AppLog.e(ConnectionService.TAG, e.getMessage(), e);
                    }
                    AppLog.v(ConnectionService.TAG, "onMessageReceived() out");
                }
            });
        }
    }

    /* JADX INFO: renamed from: com.jvckenwood.android.launcherconnectionservice.impl.ConnectionService$6, reason: invalid class name */
    static /* synthetic */ class AnonymousClass6 {
        static final /* synthetic */ int[] $SwitchMap$com$jvckenwood$android$launcherconnectionservice$impl$SessionManager$DispatchReceviedCommandResult = new int[SessionManager.DispatchReceviedCommandResult.values().length];

        static {
            try {
                $SwitchMap$com$jvckenwood$android$launcherconnectionservice$impl$SessionManager$DispatchReceviedCommandResult[SessionManager.DispatchReceviedCommandResult.DisconnectTransportRequest.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$jvckenwood$android$launcherconnectionservice$impl$SessionManager$DispatchReceviedCommandResult[SessionManager.DispatchReceviedCommandResult.OK.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$jvckenwood$android$launcherconnectionservice$impl$SessionManager$DispatchReceviedCommandResult[SessionManager.DispatchReceviedCommandResult.NG.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public static void sendAction(Context context, ActionCommand action) {
        sendActionExtra(context, action, null);
    }

    public static void sendActionExtra(Context context, ActionCommand action, String extra) {
        AppLog.d(TAG, "startService() in");
        try {
            Intent intent = new Intent(context, _clazz);
            intent.setAction(action.name());
            if (extra != null) {
                intent.putExtra("extra", extra);
            }
            context.startService(intent);
        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage(), e);
        }
        AppLog.d(TAG, "startService() out");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int _getCommState() {
        if (!this._model.isBluetoothEnabled()) {
            return 0;
        }
        if (!this._comm.isAlive()) {
            return 1;
        }
        if (!this._comm.isConnected()) {
            return 2;
        }
        return 3;
    }

    private class AppBindStub extends IApp.Stub {
        private static final String TAG = "AppBindStub";

        public AppBindStub() {
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp
        public int getCommState() throws RemoteException {
            AppLog.d(TAG, "getCommState() in");
            try {
                return ConnectionService.this._getCommState();
            } catch (Exception e) {
                AppLog.e(TAG, e.getMessage(), e);
                AppLog.d(TAG, "getCommState() out");
                return -1;
            }
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp
        public int getSessionState() throws RemoteException {
            return 0;
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp
        public boolean registerApp(String protocolStr, IAppCallback callback) throws RemoteException {
            boolean rc = false;
            AppLog.d(TAG, "registerApp() in");
            try {
                rc = ConnectionService.this._sessionContainer.register(this, protocolStr, callback);
            } catch (Exception e) {
                AppLog.e(TAG, e.getMessage(), e);
            }
            AppLog.d(TAG, "registerApp() out");
            return rc;
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp
        public boolean unregisterApp() throws RemoteException {
            boolean rc = false;
            AppLog.d(TAG, "unregisterApp() in");
            try {
                rc = ConnectionService.this._sessionContainer.unregister(this);
            } catch (Exception e) {
                AppLog.e(TAG, e.getMessage(), e);
            }
            AppLog.d(TAG, "unregisterApp() out");
            return rc;
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp
        public boolean sendCommand(byte[] command) throws RemoteException {
            boolean rc = false;
            AppLog.d(TAG, "sendCommand() in");
            try {
                rc = ConnectionService.this._sessionContainer.sendAppCommand(this, command);
            } catch (Exception e) {
                AppLog.e(TAG, e.getMessage(), e);
            }
            AppLog.d(TAG, "sendCommand() out");
            return rc;
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp
        public boolean isCommEnabled() throws RemoteException {
            return ConnectionService.this._model.isCommEnabled();
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp
        public boolean setCommEnabled(boolean sw) throws RemoteException {
            boolean rc = false;
            AppLog.d(TAG, "setCommEnabled() in");
            try {
                if (ConnectionService.this._model.setCommEnabled(sw)) {
                    ConnectionService.sendAction(ConnectionService.this.getApplicationContext(), ActionCommand.RefreshService);
                    rc = true;
                }
            } catch (Exception e) {
                AppLog.e(TAG, e.getMessage(), e);
            }
            AppLog.d(TAG, "setCommEnabled() out");
            return rc;
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp
        public boolean startLauncherApp() throws RemoteException {
            ConnectionService.this.sendBroadcast(ConnectionService.this._startLauncherAppBloadcastIntent);
            return true;
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp
        public boolean stopBind() throws RemoteException {
            boolean rc = false;
            AppLog.d(TAG, "stopBind() in");
            try {
                ConnectionService.this._sessionContainer.unregister(this);
                rc = true;
            } catch (Exception e) {
                AppLog.e(TAG, e.getMessage(), e);
            }
            AppLog.d(TAG, "stopBind() out");
            return rc;
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp
        public boolean uplodadWhiteList(ParcelableWhiteList whiteList) throws RemoteException {
            boolean rc = false;
            AppLog.d(TAG, "uplodadWhiteList() in");
            try {
                ConnectionService.this._sessionContainer.updateWhiteList(whiteList);
                rc = true;
            } catch (Exception e) {
                AppLog.e(TAG, e.getMessage(), e);
            }
            AppLog.d(TAG, "uplodadWhiteList() out");
            return rc;
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp
        public int getDebugMode() {
            return ConnectionService.this._model.getDebugMode();
        }

        @Override // com.jvckenwood.android.launcherconnectionservice.interfaces.KWD.IApp
        public boolean setDebugMode(int value) {
            return false;
        }
    }
}
