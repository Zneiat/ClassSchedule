package com.qwqaq.classschedule.Views;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
     * 1. 数据导入
     * 2. 自定义点击事件
     * 3. 精准获取 (x, y)
     * 4. (x, y) 转自定义规则内容
     * 5. 自定义更多事件
     * 6. 可用于桌面小部件
     * >>> 用来当做选择器 <<<
     */

    public ScheduleView(Context context) {
        super(context, null);
    }

    private ArrayList<String> mData = new ArrayList<>(); // 数据 TODO: 数据导入自定义
    private ScheduleAdapter mAdapter; // 适配器
    private static final int COL_COUNT = 6; // 指定列数
    private static boolean EDIT_MODE = false; // 编辑模式

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
                // TODO: 单击事件（做成自定义事件）
            }

            @Override
            public boolean onLongClick(View itemView) {
                // TODO: 长按事件（做成自定义事件）
                return true;
            }
        };

        mAdapter = new ScheduleAdapter(getContext(), COL_COUNT, mData, itemClickListener);
        this.setAdapter(mAdapter); // adapter 应用到 RecyclerView
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
                return EDIT_MODE;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {}
        };

        // itemTouchHelper 应用到 RecyclerView
        ItemTouchHelper editModeItemTouchHelper = new ItemTouchHelper(editModeItemTouchCallback);
        editModeItemTouchHelper.attachToRecyclerView(this);
    }

    /**
     * 编辑模式 进入
     */
    public void scheduleEditModeInto() {

        EDIT_MODE = true;
        // TODO: 修改 TopNavBar 状态内容（自定义事件）

        Snackbar.make(getRootView(), "单击编辑内容 长按拖动位置", Snackbar.LENGTH_LONG).show();
    }

    /**
     * 编辑模式 退出
     * @param saveData 是否保存编辑数据
     */
    public void scheduleEditModeExit(boolean saveData) {
        if (saveData) {
            Snackbar.make(getRootView(), "编辑已保存 [其实并未保存，以后再完成这个功能]", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(getRootView(), "编辑未保存", Snackbar.LENGTH_LONG).show();
        }

        // TODO: 保存操作

        EDIT_MODE = false;
        // TODO: 修改 TopNavBar 状态内容（自定义事件）
    }
}
