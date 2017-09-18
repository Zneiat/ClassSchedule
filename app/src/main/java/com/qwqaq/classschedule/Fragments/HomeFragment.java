package com.qwqaq.classschedule.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.qwqaq.classschedule.Adapters.ScheduleAdapter;
import com.qwqaq.classschedule.Activities.MainActivity;
import com.qwqaq.classschedule.R;
import com.qwqaq.classschedule.Widgets.ScheduleGridLayoutManager;
import com.qwqaq.classschedule.Utils.StreamUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Zneia on 2017/9/14.
 */

public class HomeFragment extends Fragment {

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private View mView;
    private Context context;

    @Override
    public String getFragmentTitle() {
        return "课程表";
    }

    /**
     * 顶部工具条操作按钮 选项菜单 初始化
     */
    public void onTopToolbarCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_toolbar_home_fragment, menu);
        syncTopToolbarScheduleEditModeMenu();
    }

    /**
     * 顶部工具条操作按钮 点击事件
     */
    @Override
    public void onTopToolbarOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_new_reminder) {
            Snackbar.make(mView, "添加提醒 将来开发", Snackbar.LENGTH_LONG).show();
        }
        if (id == R.id.action_new_todo) {
            Snackbar.make(mView, "添加待办 即将开发", Snackbar.LENGTH_LONG).show();
        }
        if (id == R.id.action_new_note) {
            Snackbar.make(mView, "添加笔记 敬请期待", Snackbar.LENGTH_LONG).show();
        }

        // 课程表 编辑模式
        if (id == R.id.action_save_edit) {
            // 保存
            scheduleEditModeExit(true);
        }
        if (id == R.id.action_give_up_edit) {
            // 放弃保存
            scheduleEditModeExit(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        context = mView.getContext();

        try {
            initScheduleData();
            initScheduleView();
        } catch (Exception e) {
            Log.e("初始化课程表", "发生错误", e);
            new AlertDialog.Builder(context)
                    .setTitle("课程表 无法启动")
                    .setMessage("你可以将下列问题通过QQ反馈给我：\n\n" + e.getMessage())
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    }).show();
        }

        return mView;
    }

    private ArrayList<String> mScheduleData = new ArrayList<>();

    /**
     * 初始化课程表 数据
     */
    private void initScheduleData() throws JSONException {
        for (String item : new String[]{"周一", "周二", "周三", "周四", "周五", "周六"}) {
            mScheduleData.add(item);
        }

        // JSON 导入
        String allWeekClassesJson = StreamUtil.get(context, R.raw.classes_default_data);
        JSONArray allWeekClasses = new JSONArray(allWeekClassesJson);
        for (int i = 0; i < allWeekClasses.length(); i++) {
            String className = allWeekClasses.getString(i);
            mScheduleData.add(className);
        }
    }

    private RecyclerView mScheduleRecyclerView;
    private ScheduleAdapter mScheduleAdapter;

    /**
     * 初始化课程表 界面
     */
    private void initScheduleView() {
        mScheduleRecyclerView = mView.findViewById(R.id.schedule);

        // 设置 Recycler 布局
        ScheduleGridLayoutManager gridLayoutManager = new ScheduleGridLayoutManager(this.getContext(), RECYCLER_COL_COUNT);
        gridLayoutManager.setScrollEnabled(false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1; // 单个项目 占一列
            }
        });
        mScheduleRecyclerView.setLayoutManager(gridLayoutManager);

        initScheduleAdapter();
        initScheduleEditMode();
    }

    /**
     * 初始化课程表 Adapter
     */
    private void initScheduleAdapter() {
        // 项目点击监听
        ScheduleAdapter.ItemClickListener itemClickListener = new ScheduleAdapter.ItemClickListener() {
            @Override
            public void onClick(View itemView) {
                if (RECYCLER_EDIT_MODE) {
                    // 编辑模式单击
                    scheduleEditModeItemOnClick(itemView);
                    return;
                }

                Snackbar.make(mView, "点击 " + itemView.getTag(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public boolean onLongClick(View itemView) {
                if (RECYCLER_EDIT_MODE) {
                    // 编辑模式长按
                    return true;
                }

                Snackbar.make(mView, "长按 " + itemView.getTag(), Snackbar.LENGTH_LONG).show();
                return true;
            }
        };

        mScheduleAdapter = new ScheduleAdapter(getContext(), RECYCLER_COL_COUNT, mScheduleData, itemClickListener);
        mScheduleRecyclerView.setAdapter(mScheduleAdapter); // adapter 应用到 RecyclerView
    }

    /*
    |--------------------------------------------------------------------------
    | 课程表 编辑模式
    |--------------------------------------------------------------------------
    |
    | All About Schedule Edit Mode...
    |
    */
    private static final int RECYCLER_COL_COUNT = 6; // 指定列数
    private static boolean RECYCLER_EDIT_MODE = false; // 编辑模式

    /**
     * 编辑模式 进入
     */
    public void scheduleEditModeInto() {

        RECYCLER_EDIT_MODE = true;
        syncTopToolbarScheduleEditModeMenu(); // 必须保持在后

        Snackbar.make(mView, "单击编辑内容 长按拖动位置", Snackbar.LENGTH_LONG).show();
    }

    /**
     * 编辑模式 退出
     * @param saveData 是否保存编辑数据
     */
    public void scheduleEditModeExit(boolean saveData) {
        if (saveData) {
            Snackbar.make(mView, "编辑已保存 [其实并未保存，以后再完成这个功能]", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(mView, "编辑未保存", Snackbar.LENGTH_LONG).show();
        }

        // TODO: 保存操作

        RECYCLER_EDIT_MODE = false;
        syncTopToolbarScheduleEditModeMenu();
    }

    /**
     * 同步 顶部 Toolbar 课程表 编辑模式
     */
    public void syncTopToolbarScheduleEditModeMenu() {
        MainActivity mainActivity = (MainActivity) getActivity();
        Toolbar toolbar = mainActivity.getTopToolBar();
        if (RECYCLER_EDIT_MODE) {
            toolbar.setTitle("编辑模式");
            toolbar.getMenu().setGroupVisible(R.id.normal_group, false);
            toolbar.getMenu().setGroupVisible(R.id.schedule_edit_mode_group, true);
        } else {
            toolbar.setTitle(getFragmentTitle());
            toolbar.getMenu().setGroupVisible(R.id.normal_group, true);
            toolbar.getMenu().setGroupVisible(R.id.schedule_edit_mode_group, false);
        }
    }

    /**
     * 初始化课程表 编辑模式
     */
    private void initScheduleEditMode() {
        // 长按拖动 编辑操作
        int dragDirs = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        ItemTouchHelper.SimpleCallback editModeItemTouchCallback = new ItemTouchHelper.SimpleCallback(dragDirs, 0) {

            // 用户正在进行拖动操作时执行
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPosition = viewHolder.getAdapterPosition();
                final int toPosition = target.getAdapterPosition();

                // 如果是在第一行，不允许拖动更改位置
                if (fromPosition + 1 <= RECYCLER_COL_COUNT || toPosition + 1 <= RECYCLER_COL_COUNT) {
                    return false;
                }

                // 修改 数据列表 中的项目位置
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(mScheduleData, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(mScheduleData, i, i - 1);
                    }
                }

                mScheduleAdapter.notifyItemMoved(fromPosition, toPosition);
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
                return RECYCLER_EDIT_MODE;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {}
        };

        // itemTouchHelper 应用到 RecyclerView
        ItemTouchHelper editModeItemTouchHelper = new ItemTouchHelper(editModeItemTouchCallback);
        editModeItemTouchHelper.attachToRecyclerView(mScheduleRecyclerView);
    }

    /**
     * 课程表 编辑模式 单个 View 点击事件
     */
    private void scheduleEditModeItemOnClick(View itemView) {
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
}