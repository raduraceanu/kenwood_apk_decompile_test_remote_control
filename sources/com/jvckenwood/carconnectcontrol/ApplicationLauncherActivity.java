package com.jvckenwood.carconnectcontrol;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import com.jvckenwood.HID_ThinClient.KWD.R;
import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager;
import com.jvckenwood.applauncher.managers.AppListManager;
import com.jvckenwood.carconnectcontrol.Fragments.GridFragment;
import com.jvckenwood.carconnectcontrol.viewpagerindicator.PagerIndicator;
import com.jvckenwood.ce.globalactor.GlobalActorService;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ApplicationLauncherActivity extends FragmentActivity {
    private static final int MAX_ITME_COUNT_OF_PAGE = 6;
    private static final int MAX_PAGE_COUNT = 5;
    private static final int REQUEST_CODE_PERMISSION_OVERLAY = 100;
    private static final int REQUEST_CODE_PERMISSION_WRITE_SETTINGS = 101;
    private static AppListManager _applistManager;
    private ViewPager mAppListPager;
    public PagerIndicator mIndicator;
    private PagerAdapter pm;
    private static final String OVERLAY_CONFIRMED = ApplicationLauncherActivity.class.getName() + ".OVERLAY_CONFIRMED";
    private static final String WRITE_SETTING_CONFIRMED = ApplicationLauncherActivity.class.getName() + ".WRITE_SETTING_CONFIRMED";
    private Button mBtnLinkAppAdd = null;
    private Context mContext = null;
    private ICooperationLib _ICoopeLib = null;
    private List<GridFragment> mGridFragments = null;
    private ImageButton mBtnPrev = null;
    private ImageButton mBtnNext = null;
    private boolean mOverlayPermissiomComfirmed = false;
    private boolean mWriteSettingsPermissiomComfirmed = false;
    private boolean mIsPermissionCheckComplate = false;
    private AlertDialog mAlertDialog = null;
    private final BroadcastReceiver _onCooperatedReceiver = new BroadcastReceiver() { // from class: com.jvckenwood.carconnectcontrol.ApplicationLauncherActivity.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (IntentActions.CHANGE_COOPERATION_STATE.equals(action)) {
                    boolean isConnected = intent.getBooleanExtra("isConnected", false);
                    ICooperationLib.CooperationState cooperationStateMode = ApplicationLauncherActivity.this._ICoopeLib.getCooperationStateWithMode();
                    if (isConnected && cooperationStateMode.equals(ICooperationLib.CooperationState.CooperationBtHid)) {
                        SettingsScreenActivity.gotoActivity(context, true);
                    }
                }
            }
        }
    };
    private final BroadcastReceiver _onRunningStateChangeReceiver = new BroadcastReceiver() { // from class: com.jvckenwood.carconnectcontrol.ApplicationLauncherActivity.5
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (IntentActions.CHANGE_RUNNING_STATE.equals(action)) {
                    boolean isRunning = ApplicationLauncherActivity.this._ICoopeLib.isRunning();
                    ApplicationLauncherActivity.this.updateDisplayOfRunningState(isRunning);
                }
            }
        }
    };
    private final BroadcastReceiver _onWhiteListAppInstallStateChangeReceiver = new BroadcastReceiver() { // from class: com.jvckenwood.carconnectcontrol.ApplicationLauncherActivity.6
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (IntentActions.CHANGE_WHITELIST_APP_STATE.equals(action)) {
                    int viewPagePosition = ApplicationLauncherActivity.this.mAppListPager.getCurrentItem();
                    ApplicationLauncherActivity.this.setAppLaucherItem();
                    ApplicationLauncherActivity.this.pm.notifyDataSetChanged();
                    if (viewPagePosition >= ApplicationLauncherActivity.this.pm.getCount()) {
                        viewPagePosition = ApplicationLauncherActivity.this.pm.getCount() - 1;
                    }
                    ApplicationLauncherActivity.this.mAppListPager.setCurrentItem(viewPagePosition);
                }
            }
        }
    };
    private final View.OnClickListener _onBtnLinkAppAddClickListener = new View.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.ApplicationLauncherActivity.7
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            ApplicationLauncherActivity.this.mOverlayPermissiomComfirmed = true;
            ApplicationLauncherActivity.this.mWriteSettingsPermissiomComfirmed = true;
            Intent intent = new Intent(ApplicationLauncherActivity.this, (Class<?>) WhiteListApplicationListActiviry.class);
            ApplicationLauncherActivity.this.startActivity(intent);
        }
    };
    private final View.OnClickListener _onBtnMenuOnClickListener = new View.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.ApplicationLauncherActivity.8
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            ApplicationLauncherActivity.this.mOverlayPermissiomComfirmed = true;
            ApplicationLauncherActivity.this.mWriteSettingsPermissiomComfirmed = true;
            Intent intent = new Intent(ApplicationLauncherActivity.this, (Class<?>) SettingsScreenActivity.class);
            ApplicationLauncherActivity.this.startActivity(intent);
        }
    };
    private final View.OnClickListener _onBtnCloseOnClickListener = new View.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.ApplicationLauncherActivity.9
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            ApplicationLauncherActivity.this.mContext.sendBroadcast(new Intent(GlobalActorService.ACTION_HOMEBUTTON));
            ApplicationLauncherActivity.this.finish();
        }
    };
    private Runnable mJudgeWriteSettingsPermissionRunnable = new Runnable() { // from class: com.jvckenwood.carconnectcontrol.ApplicationLauncherActivity.12
        @Override // java.lang.Runnable
        public void run() {
            ApplicationLauncherActivity.this.mOverlayPermissiomComfirmed = true;
            boolean isNextJudge = false;
            if (!ApplicationLauncherActivity.this.mWriteSettingsPermissiomComfirmed) {
                if (!ApplicationLauncherActivity.this.isFinishing()) {
                    ApplicationLauncherActivity.this.mAlertDialog = AppLauncherApplication.requestWriteSettingsPermissionDialog(ApplicationLauncherActivity.this, 101, new Runnable() { // from class: com.jvckenwood.carconnectcontrol.ApplicationLauncherActivity.12.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ApplicationLauncherActivity.this.onActivityResult(101, -1, null);
                        }
                    });
                    if (ApplicationLauncherActivity.this.mAlertDialog != null) {
                        ApplicationLauncherActivity.this.mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.jvckenwood.carconnectcontrol.ApplicationLauncherActivity.12.2
                            @Override // android.content.DialogInterface.OnShowListener
                            public void onShow(DialogInterface dialog) {
                                ApplicationLauncherActivity.this._ICoopeLib.setApplicationState(ICooperationLib.ApplicationState.Foreground);
                            }
                        });
                        ApplicationLauncherActivity.this.mAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.jvckenwood.carconnectcontrol.ApplicationLauncherActivity.12.3
                            @Override // android.content.DialogInterface.OnDismissListener
                            public void onDismiss(DialogInterface dialog) {
                                ApplicationLauncherActivity.this._ICoopeLib.setApplicationState(ICooperationLib.ApplicationState.Background);
                            }
                        });
                        ApplicationLauncherActivity.this.mAlertDialog.show();
                        ApplicationLauncherActivity.this.mWriteSettingsPermissiomComfirmed = true;
                    } else {
                        isNextJudge = true;
                    }
                }
            } else {
                isNextJudge = true;
            }
            if (isNextJudge) {
                ApplicationLauncherActivity.this.onActivityResult(101, -1, null);
            }
        }
    };

    @Override // android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.hide();
        this._ICoopeLib = AppLauncherApplication.getLib(this);
        this.mContext = this;
        this.mOverlayPermissiomComfirmed = false;
        this.mWriteSettingsPermissiomComfirmed = false;
        LayoutInflater inflater = (LayoutInflater) getSystemService("layout_inflater");
        View view = inflater.inflate(R.layout.activity_application_launcher, (ViewGroup) null);
        setContentView(view);
        this.mAppListPager = (ViewPager) findViewById(R.id.applist_pager);
        this.mIndicator = (PagerIndicator) findViewById(R.id.pagerIndicator);
        this.mBtnPrev = (ImageButton) view.findViewById(R.id.btnLinkAppPrevPage);
        this.mBtnPrev.setOnClickListener(new View.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.ApplicationLauncherActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ApplicationLauncherActivity.this.mAppListPager.setCurrentItem(ApplicationLauncherActivity.this.mAppListPager.getCurrentItem() - 1);
            }
        });
        this.mBtnNext = (ImageButton) view.findViewById(R.id.btnLinkAppNextPage);
        this.mBtnNext.setOnClickListener(new View.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.ApplicationLauncherActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ApplicationLauncherActivity.this.mAppListPager.setCurrentItem(ApplicationLauncherActivity.this.mAppListPager.getCurrentItem() + 1);
            }
        });
        Button btnClose = (Button) view.findViewById(R.id.btnLauncherMenuClose);
        btnClose.setOnClickListener(this._onBtnCloseOnClickListener);
        this.mBtnLinkAppAdd = (Button) view.findViewById(R.id.btnAddApps);
        this.mBtnLinkAppAdd.setOnClickListener(this._onBtnLinkAppAddClickListener);
        Button btnMenu = (Button) view.findViewById(R.id.btnGotoSettings);
        btnMenu.setOnClickListener(this._onBtnMenuOnClickListener);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this._ICoopeLib.setApplicationState(ICooperationLib.ApplicationState.Foreground);
        OverlayViewManager.setForbidOverlayBtnState(this.mContext);
        boolean isRunning = this._ICoopeLib.isRunning();
        updateDisplayOfRunningState(isRunning);
        IntentFilter filter = new IntentFilter(IntentActions.CHANGE_RUNNING_STATE);
        registerReceiver(this._onRunningStateChangeReceiver, filter);
        IntentFilter filterCooperated = new IntentFilter();
        filterCooperated.addAction(IntentActions.CHANGE_COOPERATION_STATE);
        registerReceiver(this._onCooperatedReceiver, filterCooperated);
        registerReceiver(this._onWhiteListAppInstallStateChangeReceiver, new IntentFilter(IntentActions.CHANGE_WHITELIST_APP_STATE));
        _applistManager = AppListManager.getInstance(getApplicationContext());
        setAppLaucherItem();
        this.pm = new PagerAdapter(getSupportFragmentManager(), this.mGridFragments);
        this.mAppListPager.setAdapter(this.pm);
        this.mIndicator.setViewPager(this.mAppListPager);
        this.mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.jvckenwood.carconnectcontrol.ApplicationLauncherActivity.3
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int position) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ApplicationLauncherActivity.this.updateDisplayOfPageScroolBtn();
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int state) {
            }
        });
        if (!this.mIsPermissionCheckComplate) {
            judgeOverlayPermission();
        }
        this.mIsPermissionCheckComplate = false;
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this._ICoopeLib.setApplicationState(ICooperationLib.ApplicationState.Foreground);
        if (requestCode == 100) {
            this.mOverlayPermissiomComfirmed = true;
            runOnUiThread(this.mJudgeWriteSettingsPermissionRunnable);
        } else if (requestCode == 101) {
            this.mOverlayPermissiomComfirmed = false;
            this.mWriteSettingsPermissiomComfirmed = false;
            this.mIsPermissionCheckComplate = true;
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        OverlayViewManager.setAllowOverlayBtnState(this.mContext);
        this._ICoopeLib.setApplicationState(ICooperationLib.ApplicationState.Background);
        unregisterReceiver(this._onRunningStateChangeReceiver);
        unregisterReceiver(this._onCooperatedReceiver);
        unregisterReceiver(this._onWhiteListAppInstallStateChangeReceiver);
        if (this.mAlertDialog != null) {
            this.mAlertDialog.dismiss();
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putBoolean(OVERLAY_CONFIRMED, this.mOverlayPermissiomComfirmed);
            outState.putBoolean(WRITE_SETTING_CONFIRMED, this.mWriteSettingsPermissiomComfirmed);
        }
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        if (savedState != null) {
            this.mOverlayPermissiomComfirmed = savedState.getBoolean(OVERLAY_CONFIRMED);
            this.mWriteSettingsPermissiomComfirmed = savedState.getBoolean(WRITE_SETTING_CONFIRMED);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAppLaucherItem() {
        if (this.mGridFragments == null) {
            this.mGridFragments = new ArrayList();
        } else {
            this.mGridFragments.clear();
        }
        _applistManager.initInstalledAppInfos();
        int installedAppsCount = _applistManager.getNumberOfInstalledApps();
        int pageCount = installedAppsCount / 6;
        if (installedAppsCount % 6 > 0) {
            pageCount++;
        }
        for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
            this.mGridFragments.add(GridFragment.newInstance(pageIndex, 6));
        }
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {
        private List<GridFragment> fragments;

        public PagerAdapter(FragmentManager fm, List<GridFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override // android.support.v4.app.FragmentStatePagerAdapter
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return this.fragments.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDisplayOfRunningState(boolean isRunning) {
        this.mBtnLinkAppAdd.setEnabled(!isRunning);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDisplayOfPageScroolBtn() {
        boolean isPrevEnabled = false;
        boolean isNextEnabled = false;
        try {
            int pagelimit = this.pm.getCount();
            if (pagelimit >= 2) {
                int position = this.mAppListPager.getCurrentItem();
                if (position > 0) {
                    isPrevEnabled = true;
                }
                if (position < pagelimit - 1) {
                    isNextEnabled = true;
                }
            }
        } finally {
            this.mBtnPrev.setEnabled(false);
            this.mBtnNext.setEnabled(false);
        }
    }

    private void judgeOverlayPermission() {
        boolean isNextJudge = false;
        if (!this.mOverlayPermissiomComfirmed) {
            if (!isFinishing()) {
                this.mAlertDialog = OverlayViewManager.requestOverlayPermissionDialog(this, 100, this.mJudgeWriteSettingsPermissionRunnable);
                if (this.mAlertDialog == null) {
                    isNextJudge = true;
                } else {
                    this.mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.jvckenwood.carconnectcontrol.ApplicationLauncherActivity.10
                        @Override // android.content.DialogInterface.OnShowListener
                        public void onShow(DialogInterface dialog) {
                            ApplicationLauncherActivity.this._ICoopeLib.setApplicationState(ICooperationLib.ApplicationState.Foreground);
                        }
                    });
                    this.mAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.jvckenwood.carconnectcontrol.ApplicationLauncherActivity.11
                        @Override // android.content.DialogInterface.OnDismissListener
                        public void onDismiss(DialogInterface dialog) {
                            ApplicationLauncherActivity.this._ICoopeLib.setApplicationState(ICooperationLib.ApplicationState.Background);
                        }
                    });
                    this.mAlertDialog.show();
                }
            }
        } else {
            isNextJudge = true;
        }
        if (isNextJudge) {
            runOnUiThread(this.mJudgeWriteSettingsPermissionRunnable);
        }
    }
}
