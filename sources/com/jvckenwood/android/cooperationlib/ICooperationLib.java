package com.jvckenwood.android.cooperationlib;

import android.content.Context;
import com.jvckenwood.android.launcherconnectionservice.interfaces.IWhiteListRec;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface ICooperationLib {

    public enum ApplicationState {
        None,
        Foreground,
        Background,
        Sleep
    }

    public enum CooperationState {
        None,
        DeviceOff,
        DeviceOn,
        Listen,
        Connect,
        Cooperation,
        ConnectBtHid,
        ConnectAppLink,
        CooperationBtHid,
        CooperationAppLink
    }

    public interface IIdSettings {

        public enum ConnectMode {
            NONE,
            BT_HID,
            APP_LINK
        }

        ConnectMode getConnectMode();

        int getIdCarType();

        int getIdCountry();

        long getIdFunctionFrag();

        int getIdFunctionFragAddtion();

        int getIdFunctionFragMarket();

        int getIdFunctionFragModel();

        int getIdLanguage();

        int getShimukeID();
    }

    public interface ILauncherActionHandler {

        public enum LauncherAction {
            None,
            ShowAuthSpecialScreen,
            ShowCalibrationLandscapeSpecialScreen,
            ShowCalibrationPortraitSpecialScreen,
            HideSpecialScreen
        }

        void onHandleAction(ICooperationLib iCooperationLib, LauncherAction launcherAction);
    }

    public interface IReceiveMessageHandler {
        void onReceiveMessage(ICooperationLib iCooperationLib, byte[] bArr);
    }

    public interface IReceiveTouchHandler {

        public interface ITouchPoint {
            int getTouchId();

            int getX();

            int getY();
        }

        void onReceiveTouch(ICooperationLib iCooperationLib, int i, long j, ITouchPoint[] iTouchPointArr);
    }

    public interface IScreenMetrics {
        float getAspect();

        int getHeight();

        int getWidth();
    }

    public interface IStateChangeListener {

        public enum StateKind {
            None,
            CooperationState,
            RunningState
        }

        void onChangeState(ICooperationLib iCooperationLib, StateKind stateKind);
    }

    void addReceiveMessageHandler(int i, IReceiveMessageHandler iReceiveMessageHandler);

    ApplicationState getApplicationState();

    CooperationState getCooperationState();

    CooperationState getCooperationStateWithMode();

    int getDebugMode();

    IScreenMetrics getIdScreenMetrics();

    IIdSettings getIdSettings();

    List<IWhiteListRec> getWhiteList();

    boolean isCommEnabled();

    boolean isRunning();

    void notifyFinishLauncherAction(ILauncherActionHandler.LauncherAction launcherAction);

    boolean sendMessage(byte[] bArr);

    void setApplicationState(ApplicationState applicationState);

    void setCommEnabled(boolean z);

    boolean setDebugMode(int i);

    void setLauncherAction(ILauncherActionHandler iLauncherActionHandler);

    void setReceiveTouchHandler(IReceiveTouchHandler iReceiveTouchHandler);

    void setStateChangeListener(IStateChangeListener iStateChangeListener);

    void setWhiteList(List<IWhiteListRec> list);

    void start(Context context, String str);

    void stop();

    void stopCooperation();
}
