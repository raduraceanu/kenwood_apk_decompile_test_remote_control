package com.jvckenwood.carconnectcontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.jvckenwood.HID_ThinClient.KWD.R;
import com.jvckenwood.android.cooperationlib.ICooperationLib;
import com.jvckenwood.android.launcherconnectionservice.overlay.OverlayViewManager;
import com.jvckenwood.applauncher.managers.AppLaunchManager;
import com.jvckenwood.applauncher.managers.AppListManager;
import com.jvckenwood.applauncher.tools.AppInfo;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class WhiteListApplicationListActiviry extends AppCompatActivity {
    private static AppListManager _applistManager;
    private static final String URI_BASE = WhiteListApplicationListActiviry.class.getCanonicalName() + ".";
    private static final String DIALOG_TAG_NOT_EXIIST_WHITELIST_APPLICATION = URI_BASE + "DIALOG_TAG_NOT_EXIIST_WHITELIST_APPLICATION";
    private ICooperationLib _ICoopeLib = null;
    private Context mContext = null;
    private WhiteListAdapter mWhiteListAdapter = null;
    private List<AppInfo> mListItemObjects = null;
    private final BroadcastReceiver _onCooperatedReceiver = new BroadcastReceiver() { // from class: com.jvckenwood.carconnectcontrol.WhiteListApplicationListActiviry.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (IntentActions.CHANGE_COOPERATION_STATE.equals(action)) {
                    boolean isConnected = intent.getBooleanExtra("isConnected", false);
                    ICooperationLib.CooperationState cooperationStateMode = WhiteListApplicationListActiviry.this._ICoopeLib.getCooperationStateWithMode();
                    if (isConnected && cooperationStateMode.equals(ICooperationLib.CooperationState.CooperationBtHid)) {
                        SettingsScreenActivity.gotoActivity(context, true);
                    }
                }
            }
        }
    };
    private final BroadcastReceiver _onRunningStateChangeReceiver = new BroadcastReceiver() { // from class: com.jvckenwood.carconnectcontrol.WhiteListApplicationListActiviry.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (IntentActions.CHANGE_RUNNING_STATE.equals(action)) {
                    boolean isRunning = WhiteListApplicationListActiviry.this._ICoopeLib.isRunning();
                    WhiteListApplicationListActiviry.this.updateDisplayOfRunningState(isRunning);
                }
            }
        }
    };
    private final BroadcastReceiver _onWhiteListAppInstallStateChangeReceiver = new BroadcastReceiver() { // from class: com.jvckenwood.carconnectcontrol.WhiteListApplicationListActiviry.5
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (IntentActions.CHANGE_WHITELIST_APP_STATE.equals(action)) {
                    WhiteListApplicationListActiviry.this.setListItemObjects();
                    WhiteListApplicationListActiviry.this.mWhiteListAdapter.notifyDataSetChanged();
                    if (WhiteListApplicationListActiviry.this.mListItemObjects.isEmpty()) {
                        WhiteListApplicationListActiviry.this.displayToNotExistWhiteListApplicationsDialog();
                    }
                }
            }
        }
    };

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_list_application_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mContext = this;
        this._ICoopeLib = AppLauncherApplication.getLib(this.mContext);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.jvckenwood.carconnectcontrol.WhiteListApplicationListActiviry.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WhiteListApplicationListActiviry.this.launchPlayStore((String) view.getTag());
            }
        });
        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.WhiteListApplicationListActiviry.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                WhiteListApplicationListActiviry.this.finish();
            }
        });
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        OverlayViewManager.setForbidOverlayBtnState(this.mContext);
        this._ICoopeLib.setApplicationState(ICooperationLib.ApplicationState.Foreground);
        IntentFilter filter = new IntentFilter(IntentActions.CHANGE_RUNNING_STATE);
        registerReceiver(this._onRunningStateChangeReceiver, filter);
        IntentFilter filterCooperated = new IntentFilter();
        filterCooperated.addAction(IntentActions.CHANGE_COOPERATION_STATE);
        registerReceiver(this._onCooperatedReceiver, filterCooperated);
        registerReceiver(this._onWhiteListAppInstallStateChangeReceiver, new IntentFilter(IntentActions.CHANGE_WHITELIST_APP_STATE));
        _applistManager = AppListManager.getInstance(getApplicationContext());
        _applistManager.initInstalledAppInfos();
        setListItemObjects();
        this.mWhiteListAdapter = new WhiteListAdapter(this.mContext, 0, this.mListItemObjects);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter((ListAdapter) this.mWhiteListAdapter);
        if (this.mListItemObjects.isEmpty()) {
            displayToNotExistWhiteListApplicationsDialog();
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
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class WhiteListAdapter extends ArrayAdapter<AppInfo> {
        private Context mContext;
        private LayoutInflater mLayoutInflater;

        public WhiteListAdapter(Context context, int textViewResourceId, List<AppInfo> objects) {
            super(context, textViewResourceId, objects);
            this.mContext = null;
            this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
            this.mContext = context;
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            AppInfo item = getItem(position);
            if (convertView == null) {
                convertView = this.mLayoutInflater.inflate(R.layout.white_list_row, (ViewGroup) null);
            }
            boolean isRunning = WhiteListApplicationListActiviry.this._ICoopeLib.isRunning();
            convertView.setEnabled(!isRunning);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imgViewAppIcon);
            imageView.setImageDrawable(item.getIconDrawable());
            TextView textView = (TextView) convertView.findViewById(R.id.txtViewAppName);
            textView.setText(item.getApplicationName());
            textView.setEnabled(isRunning ? false : true);
            convertView.setTag(item.getPackageName());
            return convertView;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setListItemObjects() {
        if (this.mListItemObjects == null) {
            this.mListItemObjects = new ArrayList();
        } else {
            this.mListItemObjects.clear();
        }
        for (int i = 0; i < _applistManager.getNumberOfNewApps(); i++) {
            this.mListItemObjects.add(_applistManager.getNewAppInfoAtPosition(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchPlayStore(String id) {
        Uri uri = Uri.parse("market://details?id=" + id);
        Intent marketIntent = new Intent("android.intent.action.VIEW", uri);
        AppLaunchManager.launchApplication(this, marketIntent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDisplayOfRunningState(boolean isRunning) {
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setEnabled(!isRunning);
        this.mWhiteListAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayToNotExistWhiteListApplicationsDialog() {
        Handler handler = new Handler();
        handler.post(new Runnable() { // from class: com.jvckenwood.carconnectcontrol.WhiteListApplicationListActiviry.6
            @Override // java.lang.Runnable
            public void run() {
                DialogFragment dialogFragment = new NotExistWhiteListApplicationsDialogFragment();
                dialogFragment.show(WhiteListApplicationListActiviry.this.getFragmentManager(), WhiteListApplicationListActiviry.DIALOG_TAG_NOT_EXIIST_WHITELIST_APPLICATION);
            }
        });
    }

    public static class NotExistWhiteListApplicationsDialogFragment extends DialogFragment {
        @Override // android.app.DialogFragment
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.not_exist_whitelist_application_dialog_title);
            builder.setMessage(R.string.not_exist_whitelist_application_dialog_message).setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() { // from class: com.jvckenwood.carconnectcontrol.WhiteListApplicationListActiviry.NotExistWhiteListApplicationsDialogFragment.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int id) {
                    NotExistWhiteListApplicationsDialogFragment.this.getActivity().finish();
                }
            });
            setCancelable(false);
            return builder.create();
        }
    }
}
