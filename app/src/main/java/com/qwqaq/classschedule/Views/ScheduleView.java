package com.qwqaq.classschedule.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.View;

import com.qwqaq.classschedule.Adapters.ScheduleAdapter;
import com.qwqaq.classschedule.Components.ScheduleGridLayoutManager;
import com.qwqaq.classschedule.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Zneia on 2017/9/19.
 */

public class ScheduleView extends RecyclerView {

    /**
     * TODO: ScheduleView 未来计划：
     * √ 1. 数据导入
     * √ 2. 自定义点击事件
     * 3. 精准获取 (x, y)
     * 4. (x, y) 转自定义规则内容
     * 5. 自定义更多事件
     * 6. 可用于桌面小部件
     * >>> 用来当做选择器 <<<
     */

    public ScheduleView(Context context) {
        super(context, null);
    }

    public ScheduleView(Context context, AttributeSet attri) {
        super(context, attri);
    }
    public ScheduleView(Context context, AttributeSet attri, int defStyle) {
        super(context, attri, defStyle);
    }

    private ScheduleOptions mOptions; // 配置
    private ArrayList<String> mData; // 数据
    private ScheduleEvents mEvents; // 自定义事件
    private ScheduleAdapter mAdapter; // 适配器
    private static int COL_COUNT = 6; // 指定列数
    private static boolean IN_EDIT_MODE = false; // 编辑模式

    public void initSchedule(ScheduleOptions options, ScheduleEvents events) {
        mOptions = options;
        mData = options.getData();
        mEvents = events;
        COL_COUNT = options.getColCount();

        initView();
    }

    /**
     * 初始化整个界面
     */
    private void initView() {
        // 设置 Recycler 布局
        ScheduleGridLayoutManager gridLayoutManager = new ScheduleGridLayoutManager(this.getContext(), COL_COUNT);
        gridLayoutManager.setScrollEnabled(false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1; // 单个项目 占一列
            }
        });

        this.setLayoutManager(gridLayoutManager);

        initAdapter();
        initEditMode();
    }

    /**
     * 初始化适配器
     */
    public void initAdapter() {
        // 项目点击监听
        ScheduleAdapter.ItemClickListener itemClickListener = new ScheduleAdapter.ItemClickListener() {
            @Override
            public void onClick(View itemView) {
                if (IN_EDIT_MODE) {
                    // 编辑模式单击
                    editModeItemOnClick(itemView);
                    return;
                }

                mEvents.onItemClick(itemView);
            }

            @Override
            public boolean onLongClick(View itemView) {
                if (IN_EDIT_MODE) {
                    return true;
                }

                mEvents.onItemLong(itemView);

                return true;
            }
        };

        mAdapter = new ScheduleAdapter(getContext(), COL_COUNT, mData, itemClickListener);
        this.setAdapter(mAdapter); // adapter 应用到 RecyclerView
    }

    /**
     * 获取 是否处于编辑模式
     */
    public boolean getIsInEditMode() {
        return IN_EDIT_MODE;
    }

    /**
     * 初始化编辑模式
     */
    public void initEditMode() {
        // 长按拖动 编辑操作
        int dragDirs = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        ItemTouchHelper.SimpleCallback editModeItemTouchCallback = new ItemTouchHelper.SimpleCallback(dragDirs, 0) {

            // 用户正在进行拖动操作时执行
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPosition = viewHolder.getAdapterPosition();
                final int toPosition = target.getAdapterPosition();

                // 如果是在第一行，不允许拖动更改位置
                if (fromPosition + 1 <= COL_COUNT || toPosition + 1 <= COL_COUNT) {
                    return false;
                }

                // 修改 数据列表 中的项目位置
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(mData, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(mData, i, i - 1);
                    }
                }

                mAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            // 拖动操作结束时执行
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                /*JSONArray jsonArr = new JSONArray();
                for (String item : mScheduleData) {
                    jsonArr.put(item);
                }
                Log.d("!!! DATA TEST !!!", jsonArr.toString());*/
            }

            // 允许拖动编辑
            @Override
            public boolean isLongPressDragEnabled() {
                return IN_EDIT_MODE;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {}
        };

        // itemTouchHelper 应用到 RecyclerView
        ItemTouchHelper editModeItemTouchHelper = new ItemTouchHelper(editModeItemTouchCallback);
        editModeItemTouchHelper.attachToRecyclerView(this);
    }

    /**
     * 课程表 编辑模式 单个 View 点击事件
     */
    private void editModeItemOnClick(View itemView) {
        // TODO: 这里将会有个 EditText 来编辑 这节课
        new AlertDialog.Builder(getContext())
                .setTitle("未完成的功能")
                .setMessage("这里将会有个 EditText 来编辑内容")
                .setPositiveButton("哦，我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    /**
     * 编辑模式 进入
     */
    public void editModeEntry() {

        IN_EDIT_MODE = true;

        mEvents.afterEditModeEntry();
    }

    /**
     * 编辑模式 退出
     * @param needSaveData 是否需要保存编辑数据
     */
    public void editModeExit(boolean needSaveData) {
        if (mEvents.afterEditModeExit(needSaveData)) {
            IN_EDIT_MODE = false;
        }
    }

    /**
     * 课程表配置
     */
    public static class ScheduleOptions {
        public ArrayList<String> getData() {
            return new ArrayList<String>();
        }

        public int getColCount() {
            return 6;
        }
    }

    /**
     * 课程表事件
     */
    public static class ScheduleEvents {
        // 项目单击
        public void onItemClick(View itemView) {}

        // 项目长按
        public void onItemLong(View itemView) {}

        // 编辑模式进入
        public void afterEditModeEntry() {}

        // 编辑模式退出
        public boolean afterEditModeExit(boolean needSaveData) { return true; }
    }
}