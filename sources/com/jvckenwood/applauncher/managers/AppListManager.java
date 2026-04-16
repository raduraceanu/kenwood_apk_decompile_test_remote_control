package com.jvckenwood.applauncher.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.internal.view.SupportMenu;
import android.text.TextUtils;
import android.util.Log;
import com.jvckenwood.HID_ThinClient.KWD.R;
import com.jvckenwood.android.launcherconnectionservice.interfaces.IWhiteListRec;
import com.jvckenwood.applauncher.applist.AppListParser;
import com.jvckenwood.applauncher.tools.AppInfo;
import com.jvckenwood.carconnectcontrol.DebugMode;
import com.jvckenwood.tools.AppLog;
import com.jvckenwood.tools.PrefsUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class AppListManager {
    private static final String TAG = AppListManager.class.getSimpleName();
    private static AppListManager instance = null;
    private Context mContext;
    private AppListParser mParser;
    private Map<String, String> mRelationWhiteListMap;
    private final String mWhiteListDirectory;
    private List<AppInfo> mAllAppInfoList = null;
    private List<AppInfo> mInstalledAppInfoList = null;
    private List<AppInfo> mNewAppInfoList = null;
    private HashSet<String> mPermissionAppIDSet = null;
    private HashSet<String> mRunningRestrictionAppIDSet = null;
    private final int ITEM_NODATA_BYTE = 255;
    private final int ITEM_NODATA_WORD = SupportMenu.USER_MASK;
    private final String WHITELIST_DIRECTORY_NAME = "limits";
    private final String WHITELIST_ICON_DIRECTORY_NAME = "icon";
    private final String WHITELIST_RELATION_FILE_NAME = "relations.csv";

    private AppListManager(Context context) {
        this.mRelationWhiteListMap = null;
        this.mContext = context;
        if (!DebugMode.isLoggingEnabled()) {
            this.mWhiteListDirectory = "";
        } else {
            File dirInternalStorage = Environment.getExternalStorageDirectory();
            File dirApp = new File(dirInternalStorage, context.getPackageName());
            File dirLimits = new File(dirApp, "limits");
            if (dirLimits.isDirectory()) {
                this.mWhiteListDirectory = dirLimits.getPath();
            } else {
                this.mWhiteListDirectory = "";
            }
        }
        initAppListManager();
        this.mRelationWhiteListMap = loadRelationWhiteListMap();
    }

    private void initAppListManager() {
        initAppListParser();
        initAllAppInfos();
        initPermissionAppInfos();
        initRunningRestrictionAppInfos();
    }

    public static AppListManager getInstance(Context context) {
        if (instance == null) {
            instance = new AppListManager(context);
        }
        return instance;
    }

    public void updateWhiteListLocale(int carType, int shimuke, int country, int language, int functionMarket, int functionModel, int functionAddition) {
        setWhitelistLocale(carType, shimuke, country, language, functionMarket, functionModel, functionAddition);
        initAppListManager();
    }

    private void setWhitelistLocale(int carType, int shimuke, int country, int language, int functionMarket, int functionModel, int functionAddition) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(this.mContext.getString(R.string.pref_key_whitelist_car_type), carType);
        editor.putInt(this.mContext.getString(R.string.pref_key_whitelist_shimuke), shimuke);
        editor.putInt(this.mContext.getString(R.string.pref_key_whitelist_country), country);
        editor.putInt(this.mContext.getString(R.string.pref_key_whitelist_language), language);
        editor.putInt(this.mContext.getString(R.string.pref_key_whitelist_function_market), functionMarket);
        editor.putInt(this.mContext.getString(R.string.pref_key_whitelist_function_model), functionModel);
        editor.putInt(this.mContext.getString(R.string.pref_key_whitelist_function_addition), functionAddition);
        editor.commit();
    }

    private int getCurrentCarType() {
        return PrefsUtils.getInt(this.mContext, R.string.pref_key_whitelist_car_type, 255);
    }

    private int getCurrentShimuke() {
        return PrefsUtils.getInt(this.mContext, R.string.pref_key_whitelist_shimuke, 255);
    }

    private int getCurrentCountry() {
        return PrefsUtils.getInt(this.mContext, R.string.pref_key_whitelist_country, 255);
    }

    private int getCurrentLanguage() {
        return PrefsUtils.getInt(this.mContext, R.string.pref_key_whitelist_language, 255);
    }

    private int getCurrentFunctionMarket() {
        return PrefsUtils.getInt(this.mContext, R.string.pref_key_whitelist_function_market, 255);
    }

    private int getCurrentFunctionModel() {
        return PrefsUtils.getInt(this.mContext, R.string.pref_key_whitelist_function_model, 255);
    }

    private int getCurrentFunctionAddition() {
        return PrefsUtils.getInt(this.mContext, R.string.pref_key_whitelist_function_addition, SupportMenu.USER_MASK);
    }

    private InputStream getWhiteListStream() throws IOException {
        int carType = getCurrentCarType();
        int shimuke = getCurrentShimuke();
        int country = getCurrentCountry();
        int language = getCurrentLanguage();
        int functionMarket = getCurrentFunctionMarket();
        int functionModel = getCurrentFunctionModel();
        int functionAddition = getCurrentFunctionAddition();
        String fileName = null;
        try {
            fileName = buildFileNameString(carType, shimuke, country, language, functionMarket, functionModel, functionAddition);
            return buildWhitelitStream(fileName);
        } catch (Exception e) {
            AppLog.d("AppListManager", "dont have best list");
            try {
                if (!this.mRelationWhiteListMap.containsKey(fileName)) {
                    throw new FileNotFoundException("No Relation Whitelist File");
                }
                return buildWhitelitStream(this.mRelationWhiteListMap.get(fileName));
            } catch (Exception e2) {
                AppLog.d(TAG, "dont have better list");
                String fileName2 = buildFileNameString(255, 255, 255, 255, 255, 255, SupportMenu.USER_MASK);
                return buildWhitelitStream(fileName2);
            }
        }
    }

    private InputStream buildWhitelitStream(String fileName) throws IOException {
        InputStream inputStream = buildInputStream(fileName);
        setSelectedWhiteListName(fileName);
        return inputStream;
    }

    private InputStream buildInputStream(String fileName) throws IOException {
        if (TextUtils.isEmpty(this.mWhiteListDirectory)) {
            AssetManager am = this.mContext.getResources().getAssets();
            InputStream inputStream = am.open("limits" + File.separator + fileName);
            return inputStream;
        }
        File list = new File(this.mWhiteListDirectory + File.separator + fileName);
        InputStream inputStream2 = new FileInputStream(list);
        return inputStream2;
    }

    private String buildFileNameString(int carType, int shimuke, int country, int language, int functionMarket, int functionModel, int functionAddition) {
        String result = String.format("%02x-%02x-%02x-%02x-%02x-%02x-%04x.list", Integer.valueOf(carType), Integer.valueOf(shimuke), Integer.valueOf(country), Integer.valueOf(language), Integer.valueOf(functionMarket), Integer.valueOf(functionModel), Integer.valueOf(functionAddition)).toLowerCase();
        setWhitelistLocale(carType, shimuke, country, language, functionMarket, functionModel, functionAddition);
        return result;
    }

    private void initAppListParser() {
        try {
            InputStream inputStream = getWhiteListStream();
            this.mParser = new AppListParser(inputStream);
            inputStream.close();
        } catch (Exception e) {
            AppLog.d("AppListParser", "Can not open Whitelist!!");
        }
    }

    public void initAllAppInfos() {
        Log.d("debug", "infos nothing");
        List<AppInfo> list = new ArrayList<>();
        int i = 1;
        while (true) {
            try {
                AppInfo info = this.mParser.getListContents(i);
                if (info == null) {
                    break;
                }
                if (!info.getPackageName().startsWith("*")) {
                    list.add(info);
                } else {
                    AppLog.d(TAG, "");
                }
                i++;
            } catch (Exception e) {
            }
        }
        this.mAllAppInfoList = list;
    }

    public void initInstalledAppInfos() {
        PackageManager pm = this.mContext.getPackageManager();
        List<AppInfo> list = new ArrayList<>();
        List<AppInfo> listNew = new ArrayList<>();
        for (AppInfo info : this.mAllAppInfoList) {
            try {
                ApplicationInfo ai = pm.getApplicationInfo(info.getPackageName(), 0);
                if (ai != null) {
                    AppInfo installedAppInfo = new AppInfo(ai.loadLabel(pm).toString(), ai.packageName, info.getRestrictLv(), info.getCertHash());
                    installedAppInfo.setIconDrawable(pm.getApplicationIcon(ai.packageName));
                    list.add(installedAppInfo);
                }
            } catch (PackageManager.NameNotFoundException e) {
                info.setIconDrawable(getWhiteListAppIcon(info.getPackageName()));
                listNew.add(info);
            } catch (Exception e2) {
            }
        }
        this.mInstalledAppInfoList = list;
        this.mNewAppInfoList = listNew;
    }

    public void initPermissionAppInfos() {
        int count = getNumberOfAllApps();
        HashSet<String> appIDSet = new HashSet<>();
        String myAppName = this.mContext.getPackageName();
        appIDSet.add(myAppName);
        for (int i = 0; i < count; i++) {
            try {
                AppInfo info = getAllAppInfoAtPostition(i);
                int restrictLv = info.getRestrictLv();
                if (restrictLv > 4) {
                    appIDSet.add(info.getPackageName());
                }
            } catch (Exception e) {
            }
        }
        this.mPermissionAppIDSet = appIDSet;
    }

    public void initRunningRestrictionAppInfos() {
        int count = getNumberOfAllApps();
        HashSet<String> appIDSet = new HashSet<>();
        String myAppName = this.mContext.getPackageName();
        appIDSet.add(myAppName);
        for (int i = 0; i < count; i++) {
            try {
                AppInfo info = getAllAppInfoAtPostition(i);
                int restrictLv = info.getRestrictLv();
                if (restrictLv == 7 || restrictLv == 5) {
                    appIDSet.add(info.getPackageName());
                }
            } catch (Exception e) {
            }
        }
        this.mRunningRestrictionAppIDSet = appIDSet;
    }

    public List<IWhiteListRec> getAllAppinfos() {
        Log.d("debug", "infos nothing");
        List<IWhiteListRec> list = new ArrayList<>();
        if (this.mAllAppInfoList == null) {
            initAllAppInfos();
        }
        List<AppInfo> listSrc = this.mAllAppInfoList;
        for (int i = 0; i < this.mAllAppInfoList.size(); i++) {
            try {
                list.add(listSrc.get(i));
            } catch (Exception e) {
            }
        }
        return list;
    }

    public HashSet<String> getPermissionAppIDSet() {
        return this.mPermissionAppIDSet;
    }

    public HashSet<String> getRunningRestrictionAppIDSet() {
        return this.mRunningRestrictionAppIDSet;
    }

    public int getNumberOfAllApps() {
        return this.mAllAppInfoList.size();
    }

    public int getNumberOfInstalledApps() {
        return this.mInstalledAppInfoList.size();
    }

    public int getNumberOfNewApps() {
        return this.mNewAppInfoList.size();
    }

    public AppInfo getAllAppInfoAtPackageName(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        if (this.mAllAppInfoList == null) {
            initAllAppInfos();
        }
        for (AppInfo appInfo : this.mAllAppInfoList) {
            if (packageName.equals(appInfo.getPackageName())) {
                return appInfo;
            }
        }
        return null;
    }

    public AppInfo getAllAppInfoAtPostition(int position) {
        try {
            AppInfo info = this.mAllAppInfoList.get(position);
            return info;
        } catch (Exception e) {
            return null;
        }
    }

    public AppInfo getInstalledAppInfoAtPosition(int position) {
        return getAppInfoAtPosition(this.mInstalledAppInfoList, position);
    }

    public AppInfo getNewAppInfoAtPosition(int position) {
        return getAppInfoAtPosition(this.mNewAppInfoList, position);
    }

    private AppInfo getAppInfoAtPosition(List<AppInfo> appInfo, int position) {
        try {
            AppInfo info = appInfo.get(position);
            return info;
        } catch (Exception e) {
            return null;
        }
    }

    public void setSelectedWhiteListName(String name) {
        PrefsUtils.setString(this.mContext, R.string.pref_key_whitelist_select_name, name);
    }

    private Map<String, String> loadRelationWhiteListMap() {
        InputStream inputStream;
        Map<String, String> relationMap = new HashMap<>();
        try {
            if (TextUtils.isEmpty(this.mWhiteListDirectory)) {
                AssetManager am = this.mContext.getResources().getAssets();
                inputStream = am.open("limits" + File.separator + "relations.csv");
            } else {
                File list = new File(this.mWhiteListDirectory + File.separator + "relations.csv");
                InputStream inputStream2 = new FileInputStream(list);
                inputStream = inputStream2;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                String strLine = br.readLine();
                if (strLine != null) {
                    String strLine2 = strLine.trim();
                    if (!TextUtils.isEmpty(strLine2) && !strLine2.startsWith("#")) {
                        StringTokenizer strTokenizer = new StringTokenizer(strLine2, ",");
                        if (strTokenizer.countTokens() == 2) {
                            String key = strTokenizer.nextToken();
                            String value = strTokenizer.nextToken();
                            if (isValidateWhiteListFileName(key) && isValidateWhiteListFileName(value)) {
                                relationMap.put(key, value);
                            }
                        }
                    }
                } else {
                    br.close();
                    return relationMap;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppLog.d(TAG, "WhiteList Relation File: Load error!!");
            return new HashMap<>();
        }
    }

    private boolean isValidateWhiteListFileName(String fileName) {
        Pattern ptn = Pattern.compile("^([0-9a-f]{2}\\-){6}[0-9a-f]{4}\\.list$");
        Matcher m = ptn.matcher(fileName);
        return m.find();
    }

    private Drawable getWhiteListAppIcon(String packageName) {
        Bitmap bitmapIcom;
        try {
            InputStream is = buildWhitelitIconStream(packageName + ".png");
            bitmapIcom = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            try {
                InputStream is2 = buildWhitelitIconStream("ic_launcher.png");
                bitmapIcom = BitmapFactory.decodeStream(is2);
            } catch (IOException e2) {
                bitmapIcom = null;
            }
        }
        if (bitmapIcom == null) {
            return null;
        }
        return new BitmapDrawable(this.mContext.getResources(), bitmapIcom);
    }

    private InputStream buildWhitelitIconStream(String fileName) throws IOException {
        return buildInputStream("icon" + File.separator + fileName);
    }
}
