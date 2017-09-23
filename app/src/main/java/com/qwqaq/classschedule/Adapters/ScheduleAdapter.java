package com.qwqaq.classschedule.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qwqaq.classschedule.R;
import com.qwqaq.classschedule.Utils.DisplayUtil;
import com.qwqaq.classschedule.Views.ScheduleView;

import java.util.ArrayList;

/**
 * Created by Zneia on 2017/9/16.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private Context mContext;
    private ScheduleView mView;
    private int mColCount; // 列数
    private ArrayList<String> mScheduleData;
    private ItemClickListener mItemClickListener;

    /**
     * View Holder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;

        public CardView classCardView;
        public TextView classNameTextView;

        ViewHolder(View view) {
            super(view);
            itemView = view;
            classCardView = (CardView) view.findViewById(R.id.class_card);
            classNameTextView = (TextView) view.findViewById(R.id.class_name);
        }
    }

    public ScheduleAdapter(Context context, ScheduleView view, int colCount, ArrayList<String> scheduleData, ItemClickListener itemClickListener) {
        mContext = context;
        mView = view;
        mColCount = colCount;
        mScheduleData = scheduleData;
        mItemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String className = mScheduleData.get(position);

        int num = position + 1;
        if (num <= mColCount) {
            // 第一行
            holder.classNameTextView.getPaint().setFakeBoldText(true);
        } else {
            int currentNum = num - mColCount;
            // 当前列号 = 当前数字 % 总列数
            int colNum = currentNum % mColCount;
            if (colNum == 0) colNum = mColCount;
            // 当前行号 = ((总列数 - 当前列号) + 当前数字)) / 总列数
            int rowNum = ((mColCount - colNum) + currentNum) / mColCount;

            ScheduleView.ScheduleItemTag tagObj = new ScheduleView.ScheduleItemTag()
                    .setColNum(colNum)
                    .setRowNum(rowNum);

            holder.itemView.setTag(tagObj);

            if (!holder.itemView.hasOnClickListeners()) {
                holder.itemView.setOnClickListener(mItemClickListener);
                holder.itemView.setOnLongClickListener(mItemClickListener);
            }
        }

        if (mView.getIsInEditMode()) {
            // 显示 CardView 阴影
            holder.classCardView.setCardElevation(3);
        } else {
            holder.classCardView.setCardElevation(0);
        }

        holder.classNameTextView.setText(!className.trim().equals("") ? className.trim() : "-");
    }

    /**
     * return int 多少就会执行多少次 onCreateViewHolder
     */
    @Override
    public int getItemCount() {
        return mScheduleData.size();
    }

    /**
     * 点击监听
     */
    public static class ItemClickListener implements View.OnClickListener, View.OnLongClickListener {
        public void onClick(View v) {}
        public boolean onLongClick(View v) { return true; }
    }

    /**
     * 所有 Item 显示阴影
     */
    public void displayItemShadow(boolean isDisplay) {
        notifyItemRangeChanged(0, mScheduleData.size()); // 执行这个来刷新 Item View，将会重新执行 onCreateViewHolder 和 onBindViewHolder
    }
}