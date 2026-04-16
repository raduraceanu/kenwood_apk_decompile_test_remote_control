package com.jvckenwood.carconnectcontrol.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import com.jvckenwood.HID_ThinClient.KWD.R;
import com.jvckenwood.applauncher.managers.AppLaunchManager;

/* JADX INFO: loaded from: classes.dex */
public class GridFragment extends Fragment {
    private static final String ARG_GRID_ITEMS = "GridItems";
    private static final String ARG_GRID_ITEMS_MAX_COUNT = "GridItemsMaxCount";
    private static final String ARG_GRID_ITEMS_PAGE_INDEX = "GridItemsPageIndex";
    private PackageManager _packageManager;
    private Context mContext;
    private GridAdapter mGridAdapter;
    private GridItems[] mGridItems;
    private GridView mGridView;
    private int mMaxItemCount;
    private int mPageIndex;

    public static GridFragment newInstance(int pageIndex, int maxItemCount) {
        GridFragment f = new GridFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GRID_ITEMS_PAGE_INDEX, pageIndex);
        args.putInt(ARG_GRID_ITEMS_MAX_COUNT, maxItemCount);
        f.setArguments(args);
        return f;
    }

    public GridItems[] getGridItemData() {
        return null;
    }

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        this.mPageIndex = arguments.getInt(ARG_GRID_ITEMS_PAGE_INDEX);
        this.mMaxItemCount = arguments.getInt(ARG_GRID_ITEMS_MAX_COUNT);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.launcher_application_gird_view, container, false);
        this.mGridView = (GridView) view.findViewById(R.id.gridView);
        this.mContext = getActivity();
        this._packageManager = this.mContext.getPackageManager();
        return view;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.mContext != null) {
            this.mGridAdapter = new GridAdapter(this.mContext, this.mPageIndex, this.mMaxItemCount);
            if (this.mGridView != null) {
                this.mGridView.setAdapter((ListAdapter) this.mGridAdapter);
            }
            this.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.jvckenwood.carconnectcontrol.Fragments.GridFragment.1
                @Override // android.widget.AdapterView.OnItemClickListener
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    GridFragment.this.onGridItemClick((GridView) parent, view, position, id);
                }
            });
        }
    }

    public void onGridItemClick(GridView g, View v, int position, long id) {
        Intent intent;
        ImageView imgView = (ImageView) v.findViewById(R.id.grid_item_image);
        String appPackageName = (String) imgView.getTag();
        if (!TextUtils.isEmpty(appPackageName) && (intent = this._packageManager.getLaunchIntentForPackage(appPackageName)) != null) {
            AppLaunchManager.launchWhiteListApplication(this.mContext, intent);
        }
    }
}
