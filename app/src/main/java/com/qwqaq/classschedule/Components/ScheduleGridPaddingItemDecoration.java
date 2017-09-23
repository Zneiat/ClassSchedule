package com.qwqaq.classschedule.Components;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.qwqaq.classschedule.Activities.MainActivity;
import com.qwqaq.classschedule.Kernel;
import com.qwqaq.classschedule.Utils.DisplayUtil;

/**
 * Created by Zneia on 2017/9/23.
 */

public class ScheduleGridPaddingItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private int mSpaceDp;
    private int mColCount;

    public ScheduleGridPaddingItemDecoration(Context context, int spaceDp, int colCount){
        mContext = context;
        mSpaceDp = spaceDp;
        mColCount = colCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);
        int itemCount = state.getItemCount(); // RecyclerView 中共用多少个项目，从 1 开始
        int lastPosition = itemCount - 1; // 整个 RecyclerView 最后一个 item 的 position
        int spanIndex = ((ScheduleGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex() + 1; // 列数 1 或 2

        // LEFT RIGHT
        if (spanIndex == 1) {
            // 每一列 第一个
            outRect.left = dipToPx(mSpaceDp);
            outRect.right = dipToPx(mSpaceDp);
        } else if (spanIndex == mColCount) {
            // 每一列 最后一个
            outRect.left = dipToPx(mSpaceDp);
            outRect.right = dipToPx(mSpaceDp);
        } else {
            outRect.left = dipToPx(mSpaceDp);
            outRect.right = dipToPx(mSpaceDp);
        }

        int topBottomPadding = 15;

        /*// TOP
        if (position + 1 <= mColCount) {
            // 第一排
            outRect.top = dipToPx(topBottomPadding);
        }

        // BOTTOM
        if (position > (lastPosition - mColCount)) {
            // 最后一排
            outRect.bottom = dipToPx(topBottomPadding);
        } else {
            outRect.bottom = dipToPx(1);
        }*/

    }

    private int dipToPx(int dip) {
        return DisplayUtil.dipToPx(mContext, dip);
    }
}
