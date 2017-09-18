package com.qwqaq.classschedule.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qwqaq.classschedule.R;

import java.util.ArrayList;

/**
 * Created by Zneia on 2017/9/16.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private Context mContext;
    private int mColCount; // 列数
    private ArrayList<String> mScheduleData;
    private ItemClickListener mItemClickListener;

    /**
     * View Holder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView classNameTextView;

        ViewHolder(View view) {
            super(view);
            itemView = view;
            classNameTextView = (TextView) view.findViewById(R.id.class_name);
        }
    }

    public ScheduleAdapter(Context context, int colCount, ArrayList<String> scheduleData, ItemClickListener itemClickListener) {
        mContext = context;
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

            holder.itemView.setTag("(" + colNum + ", " + rowNum + ")");
            holder.itemView.setOnClickListener(mItemClickListener);
            holder.itemView.setOnLongClickListener(mItemClickListener);
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
}