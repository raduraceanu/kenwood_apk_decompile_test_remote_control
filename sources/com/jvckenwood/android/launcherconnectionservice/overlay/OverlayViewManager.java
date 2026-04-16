package com.jvckenwood.android.launcherconnectionservice.overlay;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import com.jvckenwood.HID_ThinClient.KWD.R;
import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.applauncher.runningrestriction.AppObserver;
import com.jvckenwood.applauncher.runningrestriction.DeviceStateId;
import com.jvckenwood.carconnectcontrol.AppLauncherApplication;
import com.jvckenwood.carconnectcontrol.IntentActions;
import com.jvckenwood.carconnectcontrol.SettingsScreenActivity;
import com.jvckenwood.ce.globalactor.GlobalActorService;
import com.jvckenwood.tools.AccessibilityUtils;
import com.jvckenwood.tools.AppLog;
import com.jvckenwood.tools.CheckComfirmDialogBuilder;
import com.jvckenwood.tools.DimensionUtils;
import com.jvckenwood.tools.DisplayUtils;
import com.jvckenwood.tools.PrefsUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class OverlayViewManager {
    private static final int PREF_GRAVITY_BOTTOM_OR_LEFT = 1;
    private static final int PREF_GRAVITY_TOP_OR_RIGHT = 0;
    private static final int STATE_HIDE_BUTTONS = 1;
    private static final int STATE_INVALID = 3;
    private static final int STATE_SHOW_BUTTONS = 0;
    private static final int STATE_SHOW_DIALOG = 2;
    private static final int TIMEOUT_MS = 5000;
    private ICooperationLib _ICoopLib;
    private Handler _autoHideHandler;
    private Context _context;
    private ViewEventManager _eventManager;
    private Pair<Integer, Integer> mOverlayBtnSizeLandscape;
    private Pair<Integer, Integer> mOverlayBtnSizePortrait;
    private static final String TAG = OverlayViewManager.class.getSimpleName();
    private static final int[] BUTTON_IDS_SHOW_MODE = {R.id.button_overlay_close, R.id.button_overlay_settings, R.id.button_overlay_tasks, R.id.button_overlay_home, R.id.button_overlay_back};
    private static final List<Integer> BUTTON_IDS_SHOW_MODE_RUNNING_RESTRICTION = new ArrayList(Arrays.asList(Integer.valueOf(R.id.button_overlay_tasks), Integer.valueOf(R.id.button_overlay_home)));
    private static final int[] BUTTON_IDS_HIDE_MODE = {R.id.button_overlay_open};
    private WindowManager _manager = null;
    private AppOpsManager _appOpsManager = null;
    private WindowManager.LayoutParams _showParams = null;
    private WindowManager.LayoutParams _hideParams = null;
    private WindowManager.LayoutParams _dialogParams = null;
    private View _showView = null;
    private View _hideView = null;
    private View _confirmDialog = null;
    private int _currentState = 3;
    private int _stateBeforeShowingDialog = 3;
    private boolean _isEnabled = false;
    private int _currentOrientatin = 2;
    private final View.OnClickListener _onCloseButtonClicked = new View.OnClickListener() { // from class: com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager.3
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            OverlayViewManager.this.addView(1, true);
        }
    };
    private final View.OnClickListener _onSettingsButtonClicked = new View.OnClickListener() { // from class: com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager.4
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (OverlayViewManager.this._ICoopLib.getCooperationStateWithMode() == ICooperationLib.CooperationState.CooperationAppLink) {
                OverlayViewManager.this._context.sendBroadcast(new Intent(IntentActions.START_LAUNCHER));
            } else {
                Intent intent = new Intent(OverlayViewManager.this._context, (Class<?>) SettingsScreenActivity.class);
                intent.setFlags(335544320);
                OverlayViewManager.this._context.startActivity(intent);
            }
            OverlayViewManager.this.startAutoHideTimer();
        }
    };
    private final View.OnClickListener _onTasksButtonClicked = new View.OnClickListener() { // from class: com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager.5
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (AccessibilityUtils.isEnabled(OverlayViewManager.this._context)) {
                OverlayViewManager.this._context.sendBroadcast(new Intent(GlobalActorService.ACTION_TASKBUTTON));
                OverlayViewManager.this.startAutoHideTimer();
            } else {
                OverlayViewManager.this.showConfirmSetupDialog();
            }
        }
    };
    private final View.OnClickListener _onHomeButtonClicked = new View.OnClickListener() { // from class: com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager.6
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (AccessibilityUtils.isEnabled(OverlayViewManager.this._context)) {
                OverlayViewManager.this._context.sendBroadcast(new Intent(GlobalActorService.ACTION_HOMEBUTTON));
                OverlayViewManager.this.startAutoHideTimer();
            } else {
                OverlayViewManager.this.showConfirmSetupDialog();
            }
        }
    };
    private final View.OnClickListener _onBackButtonClicked = new View.OnClickListener() { // from class: com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager.7
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (AccessibilityUtils.isEnabled(OverlayViewManager.this._context)) {
                OverlayViewManager.this._context.sendBroadcast(new Intent(GlobalActorService.ACTION_BACKBUTTON));
                OverlayViewManager.this.startAutoHideTimer();
            } else {
                OverlayViewManager.this.showConfirmSetupDialog();
            }
        }
    };
    private final View.OnClickListener _onOpenButtonClicked = new View.OnClickListener() { // from class: com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager.8
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            OverlayViewManager.this.addView(0, true);
        }
    };
    private final View.OnClickListener _onDialogOkButtonClicked = new View.OnClickListener() { // from class: com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager.9
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
            intent.setFlags(335544320);
            OverlayViewManager.this._context.startActivity(intent);
            OverlayViewManager.this.addView(OverlayViewManager.this._stateBeforeShowingDialog, false);
        }
    };
    private final View.OnClickListener _onDialogCancelButtonClicked = new View.OnClickListener() { // from class: com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager.10
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            OverlayViewManager.this.addView(OverlayViewManager.this._stateBeforeShowingDialog, false);
        }
    };
    private final Runnable _onTimeoutAutoHideTimer = new Runnable() { // from class: com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager.11
        @Override // java.lang.Runnable
        public void run() {
            if (OverlayViewManager.this._currentState == 0 || OverlayViewManager.this._currentState == 1) {
                OverlayViewManager.this.addView(OverlayViewManager.this._currentState, false);
            }
        }
    };
    private AppOpsManager.OnOpChangedListener mOnOpChangedListener = null;

    public OverlayViewManager(Context context) {
        this._context = null;
        this._autoHideHandler = null;
        this._eventManager = null;
        this._ICoopLib = null;
        this.mOverlayBtnSizePortrait = null;
        this.mOverlayBtnSizeLandscape = null;
        this._context = context;
        this._ICoopLib = AppLauncherApplication.getLib(context);
        this._autoHideHandler = new Handler();
        this._eventManager = new ViewEventManager();
        Resources res = context.getResources();
        this.mOverlayBtnSizePortrait = getResourceSize(res, R.drawable.btn_3home2_n);
        this.mOverlayBtnSizeLandscape = getResourceSize(res, R.drawable.btn_3home_n);
        setupViews();
        if (Build.VERSION.SDK_INT >= 23) {
            startPermissionObserve(context);
        }
    }

    private void setupViews() {
        this._manager = (WindowManager) this._context.getSystemService("window");
        this._showParams = new WindowManager.LayoutParams(-2, -2, 2002, 262184, -3);
        this._hideParams = new WindowManager.LayoutParams(-2, -2, 2002, 262184, -3);
        this._dialogParams = new WindowManager.LayoutParams(DimensionUtils.convertDpToPixel(300.0f, this._context), DimensionUtils.convertDpToPixel(200.0f, this._context), 2002, 262184, -3);
        LayoutInflater inflater = (LayoutInflater) this._context.getSystemService("layout_inflater");
        this._showView = inflater.inflate(R.layout.overlay_view_buttons_shown, (ViewGroup) null);
        LinearLayout layout = (LinearLayout) this._showView.findViewById(R.id.layout_overlay_shown);
        layout.setOnTouchListener(new View.OnTouchListener() { // from class: com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v, MotionEvent event) {
                OverlayViewManager.this.makeButtonsVisible(OverlayViewManager.BUTTON_IDS_SHOW_MODE, OverlayViewManager.this._showView, OverlayViewManager.this._isEnabled);
                OverlayViewManager.this.startAutoHideTimer();
                return false;
            }
        });
        View.OnClickListener[] shownListeners = {this._onCloseButtonClicked, this._onSettingsButtonClicked, this._onTasksButtonClicked, this._onHomeButtonClicked, this._onBackButtonClicked};
        setupListeners(BUTTON_IDS_SHOW_MODE, shownListeners, this._showView);
        this._hideView = inflater.inflate(R.layout.overlay_view_buttons_hid, (ViewGroup) null);
        LinearLayout layout2 = (LinearLayout) this._hideView.findViewById(R.id.layout_overlay_hide);
        layout2.setOnTouchListener(new View.OnTouchListener() { // from class: com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v, MotionEvent event) {
                OverlayViewManager.this.makeButtonsVisible(OverlayViewManager.BUTTON_IDS_HIDE_MODE, OverlayViewManager.this._hideView, OverlayViewManager.this._isEnabled);
                OverlayViewManager.this.startAutoHideTimer();
                return false;
            }
        });
        View.OnClickListener[] hidListeners = {this._onOpenButtonClicked};
        setupListeners(BUTTON_IDS_HIDE_MODE, hidListeners, this._hideView);
        Button button = (Button) this._hideView.findViewById(R.id.button_overlay_open);
        button.setOnLongClickListener(this._eventManager);
        button.setOnTouchListener(this._eventManager);
        this._confirmDialog = inflater.inflate(R.layout.overlay_dialog_confirm_setup, (ViewGroup) null);
        int[] dialogIds = {R.id.button_overlay_dialog_ok, R.id.button_overlay_dialog_cancel};
        View.OnClickListener[] dialogListeners = {this._onDialogOkButtonClicked, this._onDialogCancelButtonClicked};
        setupListeners(dialogIds, dialogListeners, this._confirmDialog);
        initializeOverlayButtons();
        AppObserver observer = AppObserver.getInstance(this._context);
        observer.setOnOrientationChangedListener(this._eventManager);
    }

    private int getResourceWidth(Resources res, int resId) {
        Pair<Integer, Integer> resSize = getResourceSize(res, resId);
        if (resSize == null) {
            return 0;
        }
        return ((Integer) resSize.first).intValue();
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x003d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private android.util.Pair<java.lang.Integer, java.lang.Integer> getResourceSize(android.content.res.Resources r7, int r8) {
        /*
            r6 = this;
            r2 = 0
            r0 = 0
            android.graphics.Bitmap r0 = android.graphics.BitmapFactory.decodeResource(r7, r8)     // Catch: java.lang.OutOfMemoryError -> L2a java.lang.Throwable -> L3a
            if (r0 != 0) goto Le
            if (r0 == 0) goto Ld
            r0.recycle()
        Ld:
            return r2
        Le:
            android.util.Pair r3 = new android.util.Pair     // Catch: java.lang.OutOfMemoryError -> L2a java.lang.Throwable -> L3a
            int r4 = r0.getWidth()     // Catch: java.lang.OutOfMemoryError -> L2a java.lang.Throwable -> L3a
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch: java.lang.OutOfMemoryError -> L2a java.lang.Throwable -> L3a
            int r5 = r0.getHeight()     // Catch: java.lang.OutOfMemoryError -> L2a java.lang.Throwable -> L3a
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch: java.lang.OutOfMemoryError -> L2a java.lang.Throwable -> L3a
            r3.<init>(r4, r5)     // Catch: java.lang.OutOfMemoryError -> L2a java.lang.Throwable -> L3a
            if (r0 == 0) goto L28
            r0.recycle()
        L28:
            r2 = r3
            goto Ld
        L2a:
            r1 = move-exception
            java.lang.String r3 = com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager.TAG     // Catch: java.lang.Throwable -> L3a
            java.lang.String r4 = r1.toString()     // Catch: java.lang.Throwable -> L3a
            com.jvckenwood.tools.AppLog.e(r3, r4)     // Catch: java.lang.Throwable -> L3a
            if (r0 == 0) goto Ld
            r0.recycle()
            goto Ld
        L3a:
            r2 = move-exception
            if (r0 == 0) goto L40
            r0.recycle()
        L40:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager.getResourceSize(android.content.res.Resources, int):android.util.Pair");
    }

    private void initializeOverlayButtons() {
        int lastGravity = PrefsUtils.getInt(this._context, R.string.pref_key_overlay_buttons_gravity, 0);
        int lastOffset = PrefsUtils.getInt(this._context, R.string.pref_key_overlay_buttons_offset, 0);
        Resources res = this._context.getResources();
        Configuration config = res.getConfiguration();
        Pair<Integer, Integer> size = DisplayUtils.getDefaultDisplaySize(this._context);
        if (config.orientation == 1) {
            this._showParams.gravity = 81;
        } else {
            int width = getResourceWidth(res, R.drawable.btn_3open2_n);
            if (lastOffset + (width / 2) < ((Integer) size.first).intValue() / 2) {
                this._showParams.gravity = 19;
            } else {
                this._showParams.gravity = 21;
            }
        }
        if (config.orientation == 1) {
            if (lastGravity == 0) {
                this._hideParams.gravity = 53;
            } else {
                this._hideParams.gravity = 51;
            }
            this._hideParams.y = lastOffset;
            this._hideParams.x = 0;
        } else {
            if (lastGravity == 0) {
                this._hideParams.gravity = 51;
            } else {
                this._hideParams.gravity = 83;
            }
            this._hideParams.x = lastOffset;
            this._hideParams.y = 0;
        }
        LinearLayout layout = (LinearLayout) this._showView.findViewById(R.id.layout_overlay_shown);
        if (config.orientation == 1) {
            layout.setOrientation(0);
        } else {
            layout.setOrientation(1);
        }
        changeButtonBackgroundAndOrder(config.orientation);
        changeButtonWhenClosedBackground(config.orientation);
        this._currentOrientatin = config.orientation;
    }

    private void setupListeners(int[] ids, View.OnClickListener[] listeners, View parent) {
        for (int i = 0; i < ids.length; i++) {
            Button button = (Button) parent.findViewById(ids[i]);
            button.setOnClickListener(listeners[i]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAutoHideTimer() {
        stopAutoHideTimer();
        this._autoHideHandler.postDelayed(this._onTimeoutAutoHideTimer, 5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopAutoHideTimer() {
        this._autoHideHandler.removeCallbacks(this._onTimeoutAutoHideTimer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showConfirmSetupDialog() {
        if (this._currentState == 0) {
            stopAutoHideTimer();
            addView(2, false);
        }
    }

    public void show() {
        initializeOverlayButtons();
        addView(0, true);
    }

    public void hide() {
        removeView();
        this._currentState = 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addView(int state, boolean showOpenButton) {
        boolean isOverlayPermission = checkOverlayPermission(this._context);
        if (!isOverlayPermission) {
            hide();
            return;
        }
        removeView();
        if (state == 0) {
            this._manager.addView(this._showView, this._showParams);
            makeButtonsVisible(BUTTON_IDS_SHOW_MODE, this._showView, showOpenButton && this._isEnabled);
            if (showOpenButton) {
                startAutoHideTimer();
            }
        } else if (state == 1) {
            this._manager.addView(this._hideView, this._hideParams);
            makeButtonsVisible(BUTTON_IDS_HIDE_MODE, this._hideView, showOpenButton && this._isEnabled);
            if (showOpenButton) {
                startAutoHideTimer();
            }
        } else if (state == 2) {
            this._stateBeforeShowingDialog = this._currentState;
            this._manager.addView(this._confirmDialog, this._dialogParams);
            this._confirmDialog.setVisibility(0);
        }
        this._currentState = state;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeView() {
        WindowManager manager = (WindowManager) this._context.getSystemService("window");
        AppLog.d(TAG, "currentState = " + this._currentState);
        if (this._currentState == 0) {
            manager.removeView(this._showView);
        } else if (this._currentState == 1) {
            manager.removeView(this._hideView);
        } else if (this._currentState == 2) {
            manager.removeView(this._confirmDialog);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void makeButtonsVisible(int[] ids, View parent, boolean isVisible) {
        ICooperationLib.CooperationState cooperationStateMode = this._ICoopLib.getCooperationStateWithMode();
        boolean isRunning = this._ICoopLib.isRunning();
        for (int id : ids) {
            Button button = (Button) parent.findViewById(id);
            int viewVisibility = isVisible ? 0 : 8;
            if (cooperationStateMode == ICooperationLib.CooperationState.CooperationAppLink && isRunning && BUTTON_IDS_SHOW_MODE_RUNNING_RESTRICTION.contains(Integer.valueOf(id))) {
                viewVisibility = 8;
            }
            button.setVisibility(viewVisibility);
        }
    }

    public void setEnabled(boolean isEnabled) {
        this._isEnabled = isEnabled;
        refreshViews();
    }

    public boolean isEnabled() {
        return this._isEnabled;
    }

    private void refreshViews() {
        if (!this._isEnabled) {
            if (this._currentState == 0 || this._currentState == 1 || this._currentState == 2) {
                addView(this._currentState, false);
                return;
            }
            return;
        }
        if (this._currentState == 2) {
            addView(this._stateBeforeShowingDialog, false);
        }
    }

    public void updateOverlayButtonViews() {
        addView(0, true);
    }

    private class ViewEventManager implements AppObserver.OnOrientationChangedListener, View.OnLongClickListener, View.OnTouchListener {
        private int _deltaX;
        private int _deltaY;
        private boolean _isDragging = false;
        private int _viewPreviousPosX;
        private int _viewPreviousPosY;

        public ViewEventManager() {
        }

        @Override // com.jvckenwood.applauncher.runningrestriction.AppObserver.OnOrientationChangedListener
        public void onOrientationChanged(int orientation) {
            int orientation2 = OverlayViewManager.this.changeAppOrientataion(orientation);
            if (orientation2 != OverlayViewManager.this._currentOrientatin) {
                ICooperationLib.CooperationState state = OverlayViewManager.this._ICoopLib.getCooperationState();
                if (state == ICooperationLib.CooperationState.Cooperation) {
                    OverlayViewManager.this._currentOrientatin = orientation2;
                    LinearLayout layout = (LinearLayout) OverlayViewManager.this._showView.findViewById(R.id.layout_overlay_shown);
                    OverlayViewManager.this.removeView();
                    OverlayViewManager.this._currentState = 3;
                    if (orientation2 == 1) {
                        if ((OverlayViewManager.this._hideParams.gravity & 48) == 48) {
                            OverlayViewManager.this._hideParams.gravity = 53;
                        } else {
                            OverlayViewManager.this._hideParams.gravity = 51;
                        }
                        OverlayViewManager.this._hideParams.y = OverlayViewManager.this._hideParams.x;
                        OverlayViewManager.this._hideParams.x = 0;
                        OverlayViewManager.this._showParams.gravity = 81;
                        layout.setOrientation(0);
                    } else {
                        if ((OverlayViewManager.this._hideParams.gravity & 5) == 5) {
                            OverlayViewManager.this._hideParams.gravity = 51;
                        } else {
                            OverlayViewManager.this._hideParams.gravity = 83;
                        }
                        OverlayViewManager.this._hideParams.x = OverlayViewManager.this._hideParams.y;
                        OverlayViewManager.this._hideParams.y = 0;
                        int x = OverlayViewManager.this._hideParams.x + (OverlayViewManager.this._hideParams.width / 2);
                        Pair<Integer, Integer> size = DisplayUtils.getDefaultDisplaySize(OverlayViewManager.this._context);
                        if (x < ((Integer) size.first).intValue() / 2) {
                            OverlayViewManager.this._showParams.gravity = 19;
                        } else {
                            OverlayViewManager.this._showParams.gravity = 21;
                        }
                        layout.setOrientation(1);
                    }
                    OverlayViewManager.this.addView(1, true);
                    OverlayViewManager.this.changeButtonBackgroundAndOrder(orientation2);
                    OverlayViewManager.this.changeButtonWhenClosedBackground(orientation2);
                }
            }
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View v) {
            this._isDragging = true;
            this._viewPreviousPosX = -1;
            this._viewPreviousPosY = -1;
            OverlayViewManager.this._hideParams.gravity = 51;
            OverlayViewManager.this.stopAutoHideTimer();
            return true;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent event) {
            int newGravity;
            if (this._isDragging) {
                int viewW = OverlayViewManager.this._hideView.getWidth() / 2;
                int viewH = OverlayViewManager.this._hideView.getHeight() / 2;
                int action = event.getAction();
                if (action != 2) {
                    int x = OverlayViewManager.this._hideParams.x + viewW;
                    int y = OverlayViewManager.this._hideParams.y + viewH;
                    Resources res = OverlayViewManager.this._context.getResources();
                    Configuration config = res.getConfiguration();
                    Pair<Integer, Integer> size = DisplayUtils.getDefaultDisplaySize(OverlayViewManager.this._context);
                    LinearLayout layout = (LinearLayout) OverlayViewManager.this._showView.findViewById(R.id.layout_overlay_shown);
                    if (config.orientation == 1) {
                        if (x < ((Integer) size.second).intValue() / 2) {
                            OverlayViewManager.this._hideParams.gravity = 51;
                            newGravity = 1;
                        } else {
                            OverlayViewManager.this._hideParams.gravity = 53;
                            newGravity = 0;
                        }
                        OverlayViewManager.this._hideParams.x = 0;
                        OverlayViewManager.this._showParams.gravity = 81;
                        layout.setOrientation(0);
                    } else {
                        if (y < ((Integer) size.second).intValue() / 2) {
                            OverlayViewManager.this._hideParams.gravity = 51;
                            newGravity = 0;
                        } else {
                            OverlayViewManager.this._hideParams.gravity = 83;
                            newGravity = 1;
                        }
                        OverlayViewManager.this._hideParams.y = 0;
                        if (x < ((Integer) size.first).intValue() / 2) {
                            OverlayViewManager.this._showParams.gravity = 19;
                        } else {
                            OverlayViewManager.this._showParams.gravity = 21;
                        }
                        layout.setOrientation(1);
                    }
                    OverlayViewManager.this.changeButtonWhenClosedBackground(config.orientation);
                    OverlayViewManager.this._manager.updateViewLayout(OverlayViewManager.this._hideView, OverlayViewManager.this._hideParams);
                    this._isDragging = false;
                    this._viewPreviousPosX = -1;
                    this._viewPreviousPosY = -1;
                    OverlayViewManager.this.startAutoHideTimer();
                    PrefsUtils.setInt(OverlayViewManager.this._context, R.string.pref_key_overlay_buttons_offset, config.orientation == 1 ? OverlayViewManager.this._hideParams.y : OverlayViewManager.this._hideParams.x);
                    PrefsUtils.setInt(OverlayViewManager.this._context, R.string.pref_key_overlay_buttons_gravity, newGravity);
                    return false;
                }
                if (this._viewPreviousPosX == -1) {
                    int[] location = new int[2];
                    OverlayViewManager.this._hideView.getLocationOnScreen(location);
                    this._viewPreviousPosX = location[0];
                    this._viewPreviousPosY = location[1];
                    OverlayViewManager.this._hideParams.x = location[0];
                    OverlayViewManager.this._hideParams.y = location[1];
                    this._deltaX = ((int) event.getRawX()) - (location[0] + viewW);
                    this._deltaY = ((int) event.getRawY()) - (location[1] + viewH);
                    return true;
                }
                int rawX = (((int) event.getRawX()) - viewW) - this._deltaX;
                int rawY = (((int) event.getRawY()) - viewH) - this._deltaY;
                int deltaX = rawX - this._viewPreviousPosX;
                int deltaY = rawY - this._viewPreviousPosY;
                this._viewPreviousPosX = rawX;
                this._viewPreviousPosY = rawY;
                OverlayViewManager.this._hideParams.x += deltaX;
                OverlayViewManager.this._hideParams.y += deltaY;
                OverlayViewManager.this._manager.updateViewLayout(OverlayViewManager.this._hideView, OverlayViewManager.this._hideParams);
                return true;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeButtonBackgroundAndOrder(int orientation) {
        int[] backgrounds;
        Pair<Integer, Integer> resSize;
        boolean isAppLinkMode = this._ICoopLib.getCooperationStateWithMode() == ICooperationLib.CooperationState.CooperationAppLink;
        int[] horizontalBackground = new int[5];
        horizontalBackground[0] = R.drawable.button_close_overly_buttons_horizontal;
        horizontalBackground[1] = isAppLinkMode ? R.drawable.btn_3lauhome2 : R.drawable.button_settings_horizontal;
        horizontalBackground[2] = R.drawable.button_tasks_horizontal;
        horizontalBackground[3] = R.drawable.button_home_horizontal;
        horizontalBackground[4] = R.drawable.button_back_horizontal;
        int[] verticalBackground = new int[5];
        verticalBackground[0] = R.drawable.button_close_overly_buttons_vertical;
        verticalBackground[1] = isAppLinkMode ? R.drawable.btn_3lauhome : R.drawable.button_settings_vertical;
        verticalBackground[2] = R.drawable.button_tasks_vertical;
        verticalBackground[3] = R.drawable.button_home_vertical;
        verticalBackground[4] = R.drawable.button_back_vertical;
        if (orientation == 1) {
            backgrounds = horizontalBackground;
            resSize = this.mOverlayBtnSizePortrait;
        } else {
            backgrounds = verticalBackground;
            resSize = this.mOverlayBtnSizeLandscape;
        }
        for (int i = 0; i < BUTTON_IDS_SHOW_MODE.length; i++) {
            Button button = (Button) this._showView.findViewById(BUTTON_IDS_SHOW_MODE[i]);
            button.setBackgroundResource(backgrounds[i]);
            if (resSize != null) {
                ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
                layoutParams.width = ((Integer) resSize.first).intValue();
                layoutParams.height = ((Integer) resSize.second).intValue();
                button.setLayoutParams(layoutParams);
            }
        }
        int[] VIEW_IDS_LANDSCAPE = {R.id.button_overlay_close, R.id.button_overlay_settings, R.id.button_overlay_tasks, R.id.button_overlay_home, R.id.button_overlay_back};
        int[] VIEW_IDS_PORTRAIT = {R.id.button_overlay_back, R.id.button_overlay_home, R.id.button_overlay_tasks, R.id.button_overlay_settings, R.id.button_overlay_close};
        int[] viewIds = orientation == 1 ? VIEW_IDS_PORTRAIT : VIEW_IDS_LANDSCAPE;
        View[] views = new View[viewIds.length];
        ICooperationLib.CooperationState cooperationStateMode = this._ICoopLib.getCooperationStateWithMode();
        boolean isRunning = this._ICoopLib.isRunning();
        for (int i2 = 0; i2 < viewIds.length; i2++) {
            views[i2] = this._showView.findViewById(viewIds[i2]);
            int viewVisibility = 0;
            if (cooperationStateMode == ICooperationLib.CooperationState.CooperationAppLink && isRunning && BUTTON_IDS_SHOW_MODE_RUNNING_RESTRICTION.contains(Integer.valueOf(viewIds[i2]))) {
                viewVisibility = 8;
            }
            views[i2].setVisibility(viewVisibility);
        }
        LinearLayout layout = (LinearLayout) this._showView.findViewById(R.id.layout_overlay_shown);
        layout.removeAllViews();
        for (View view : views) {
            layout.addView(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeButtonWhenClosedBackground(int orientation) {
        Button button = (Button) this._hideView.findViewById(R.id.button_overlay_open);
        if (orientation == 1) {
            if ((this._hideParams.gravity & 3) == 3) {
                button.setBackgroundResource(R.drawable.button_open_overlay_buttons_rightward);
                return;
            } else {
                button.setBackgroundResource(R.drawable.button_open_overlay_buttons_leftward);
                return;
            }
        }
        if ((this._hideParams.gravity & 80) == 80) {
            button.setBackgroundResource(R.drawable.button_open_overlay_buttons_upward);
        } else {
            button.setBackgroundResource(R.drawable.button_open_overlay_buttons_downward);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int changeAppOrientataion(int orientation) {
        if (orientation == DeviceStateId.DEVICE_STATE_ORIENTATION_PORTRAIT.value) {
            return 1;
        }
        return 2;
    }

    public boolean isDisplayingShowMode() {
        boolean isDisplay = false;
        if (this._currentState != 0) {
            return false;
        }
        Button btnBack = (Button) this._showView.findViewById(R.id.button_overlay_back);
        if (btnBack != null && btnBack.getVisibility() == 0) {
            isDisplay = true;
        }
        return isDisplay;
    }

    public static void setAllowOverlayBtnState(Context context) {
        setOverlayBtnAllowState(context, true);
    }

    public static void setForbidOverlayBtnState(Context context) {
        setOverlayBtnAllowState(context, false);
    }

    private static void setOverlayBtnAllowState(Context context, Boolean isAllow) {
        Intent intent = new Intent(IntentActions.ALLOW_OVERLAY_BUTTON);
        intent.putExtra("allow", isAllow);
        context.sendBroadcast(intent);
    }

    public static boolean checkOverlayPermission(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        boolean isCanDrawOverlay = Settings.canDrawOverlays(context);
        if (isCanDrawOverlay) {
            setChekingOverlayPermission(context);
            return isCanDrawOverlay;
        }
        return isCanDrawOverlay;
    }

    public static boolean isShouldCheckPermission(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return false;
        }
        return PrefsUtils.getBoolean(context, R.string.pref_key_check_overlay_permission, true);
    }

    public static AlertDialog requestOverlayPermissionDialog(final Activity activity, final int requestCode, final Runnable cancelLister) {
        if (checkOverlayPermission(activity) || !isShouldCheckPermission(activity)) {
            return null;
        }
        CheckComfirmDialogBuilder builder = new CheckComfirmDialogBuilder(activity);
        builder.setTitle(R.string.overlay_draw_confirm_dialog_title);
        builder.setMessage(R.string.overlay_draw_confirm_dialog_message);
        builder.setPositiveButton(R.string.Setting, new DialogInterface.OnClickListener() { // from class: com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + activity.getApplicationContext().getPackageName()));
                activity.startActivityForResult(intent, requestCode);
            }
        });
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { // from class: com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager.13
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (cancelLister != null) {
                    new Handler().post(cancelLister);
                }
            }
        });
        builder.setCancelable(false);
        builder.setPreferecesCheck(R.string.pref_key_check_overlay_permission);
        return builder.create();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setChekingOverlayPermission(Context context) {
        if (!PrefsUtils.getBoolean(context, R.string.pref_key_check_overlay_permission, true)) {
            PrefsUtils.setBoolean(context, R.string.pref_key_check_overlay_permission, true);
        }
    }

    @TargetApi(23)
    public void startPermissionObserve(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            this.mOnOpChangedListener = new AppOpsManager.OnOpChangedListener() { // from class: com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager.14
                @Override // android.app.AppOpsManager.OnOpChangedListener
                public void onOpChanged(String op, String packageName) {
                    Context context2 = OverlayViewManager.this._context.getApplicationContext();
                    if ("android:system_alert_window".equals(op) && OverlayViewManager.this._context.getApplicationContext().getPackageName().equals(packageName)) {
                        OverlayViewManager.setChekingOverlayPermission(context2);
                    }
                }
            };
            this._appOpsManager = (AppOpsManager) context.getSystemService("appops");
            this._appOpsManager.startWatchingMode("android:system_alert_window", context.getApplicationContext().getPackageName(), this.mOnOpChangedListener);
        }
    }

    @TargetApi(23)
    public void stopPermissionObserve() {
        if (this._appOpsManager != null) {
            this._appOpsManager.stopWatchingMode(this.mOnOpChangedListener);
        }
    }
}
