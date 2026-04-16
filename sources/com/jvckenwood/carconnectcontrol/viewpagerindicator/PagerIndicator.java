package com.jvckenwood.carconnectcontrol.viewpagerindicator;

import android.support.v4.view.ViewPager;

/* JADX INFO: loaded from: classes.dex */
public interface PagerIndicator extends ViewPager.OnPageChangeListener {
    void notifyDataSetChanged();

    void setCurrentItem(int i);

    void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener);

    void setViewPager(ViewPager viewPager);

    void setViewPager(ViewPager viewPager, int i);
}
