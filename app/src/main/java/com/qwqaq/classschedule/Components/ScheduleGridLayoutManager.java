package com.qwqaq.classschedule.Components;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * Created by Zneia on 2017/9/16.
 */

public class ScheduleGridLayoutManager extends GridLayoutManager {
    private boolean isScrollEnabled = true;

    public ScheduleGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        // Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
