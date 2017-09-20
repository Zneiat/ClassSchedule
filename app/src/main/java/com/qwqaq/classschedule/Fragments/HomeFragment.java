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
import com.qwqaq.classschedule.Components.ScheduleGridLayoutManager;
import com.qwqaq.classschedule.Utils.StreamUtil;
import com.qwqaq.classschedule.Views.ScheduleView;

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

    @Override
    public String getFragmentTitle() {
        return "课程表";
    }

    /**
     * 顶部工具条操作按钮 选项菜单 初始化
     */
    public void onTopToolbarCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_toolbar_home_fragment, menu);
        syncToolbarEditModeMenu();
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
            mScheduleView.editModeExit(true);
        }
        if (id == R.id.action_give_up_edit) {
            // 放弃保存
            mScheduleView.editModeExit(false);
        }
    }

    private View mView;
    private Context context;
    private ArrayList<String> mScheduleData = new ArrayList<>();
    private ScheduleView mScheduleView;

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

    /**
     * 初始化课程表 界面
     */
    private void initScheduleView() {
        mScheduleView = mView.findViewById(R.id.schedule);

        ScheduleView.ScheduleOptions options = new ScheduleView.ScheduleOptions() {
            @Override
            public ArrayList<String> getData() {
                return mScheduleData;
            }
        };

        ScheduleView.ScheduleEvents events = new ScheduleView.ScheduleEvents() {
            @Override
            public void onItemClick(View itemView) {
                Snackbar.make(mView, "点击 " + itemView.getTag(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onItemLong(View itemView) {
                Snackbar.make(mView, "长按 " + itemView.getTag(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void afterEditModeEntry() {
                syncToolbarEditModeMenu(true);
                Snackbar.make(mView, "单击编辑内容 长按拖动位置", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public boolean afterEditModeExit(boolean needSaveData) {
                if (needSaveData) {
                    Snackbar.make(mView, "编辑已保存 [其实并未保存，以后再完成这个功能]", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(mView, "编辑未保存", Snackbar.LENGTH_LONG).show();
                }

                syncToolbarEditModeMenu(false);

                return true;
            }
        };

        mScheduleView.initSchedule(options, events);

    }

    /**
     * 同步 顶部 Toolbar 课程表 编辑模式
     */
    public void syncToolbarEditModeMenu() {
        syncToolbarEditModeMenu(mScheduleView.getIsInEditMode());
    }

    public void syncToolbarEditModeMenu(boolean isInEditMode) {
        MainActivity mainActivity = (MainActivity) getActivity();
        Toolbar toolbar = mainActivity.getTopToolBar();
        if (isInEditMode) {
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
     * 获取 ScheduleView
     */
    public ScheduleView getScheduleView() {
        return mScheduleView;
    }
}