package com.jvckenwood.carconnectcontrol.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jvckenwood.HID_ThinClient.KWD.R;
import com.jvckenwood.applauncher.managers.AppListManager;
import com.jvckenwood.applauncher.tools.AppInfo;
import com.jvckenwood.tools.DisplayingSizeManager;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class GridAdapter extends BaseAdapter {
    Context context;
    private GridItems[] items;
    private LayoutInflater mInflater;

    public class ViewHolder {
        public ImageView imageView;
        public TextView textTitle;

        public ViewHolder() {
        }
    }

    public GridAdapter(Context context, int pageIndex, int itemCount) {
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.context = context;
        AppListManager applistManager = AppListManager.getInstance(context);
        applistManager.initInstalledAppInfos();
        int installedAppsCount = applistManager.getNumberOfInstalledApps();
        ArrayList<GridItems> itmLst = new ArrayList<>();
        int count = itemCount * pageIndex;
        int itemNumber = 0;
        while (count < installedAppsCount && itemNumber < itemCount) {
            GridItems itm = new GridItems(itemNumber, applistManager.getInstalledAppInfoAtPosition(count));
            itmLst.add(itm);
            count++;
            itemNumber++;
        }
        while (itemNumber < itemCount) {
            GridItems itm2 = new GridItems(itemNumber, new AppInfo(null, null, 7, null));
            itmLst.add(itm2);
            itemNumber++;
        }
        GridItems[] gp = new GridItems[0];
        this.items = (GridItems[]) itmLst.toArray(gp);
        DisplayingSizeManager.init(context);
    }

    public GridItems[] getItems() {
        return this.items;
    }

    public void setItems(GridItems[] items) {
        this.items = items;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        if (this.items != null) {
            return this.items.length;
        }
        return 0;
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        if (this.items == null || position < 0 || position >= getCount()) {
            return null;
        }
        return this.items[position];
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        if (this.items == null || position < 0 || position >= getCount()) {
            return 0L;
        }
        return this.items[position].id;
    }

    public void setItemsList(GridItems[] locations) {
        this.items = locations;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            int displaySize = DisplayingSizeManager.getLauncherIconSize();
            view = this.mInflater.inflate(R.layout.launcher_application_grid_item_view, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.grid_item_image);
            viewHolder.imageView.setLayoutParams(new LinearLayout.LayoutParams(displaySize, displaySize));
            viewHolder.textTitle = (TextView) view.findViewById(R.id.grid_item_label);
            viewHolder.textTitle.setWidth(displaySize);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        setCatImage(position, viewHolder, this.items[position]);
        return view;
    }

    private void setCatImage(int pos, ViewHolder viewHolder, GridItems gridItems) {
        int visibility = 4;
        if (gridItems.tag != null) {
            visibility = 0;
            viewHolder.textTitle.setText(gridItems.text);
            viewHolder.imageView.setImageDrawable(gridItems.icon);
            viewHolder.imageView.setTag(gridItems.tag);
        }
        viewHolder.textTitle.setVisibility(visibility);
        viewHolder.imageView.setVisibility(visibility);
    }
}
