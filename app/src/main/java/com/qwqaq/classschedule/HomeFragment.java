package com.qwqaq.classschedule;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.qwqaq.classschedule.Adapter.ScheduleAdapter;
import com.qwqaq.classschedule.Base.BaseFragment;
import com.qwqaq.classschedule.Ui.ScheduleGridLayoutManager;
import com.qwqaq.classschedule.Util.DisplayUtil;
import com.qwqaq.classschedule.Util.StreamUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Zneia on 2017/9/14.
 */

public class HomeFragment extends BaseFragment {

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private View mView;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.home_fragment, container, false);
        context = mView.getContext();

        initTopBar(mView);

        try {
            initScheduleData();
            initSchedule();
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
    RecyclerView mScheduleView;

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

    private void initSchedule() {
        mScheduleView = mView.findViewById(R.id.schedule);

        // 设置布局
        int colCount = 6; // 指定列数
        ScheduleGridLayoutManager gridLayoutManager = new ScheduleGridLayoutManager(this.getContext(), colCount);
        gridLayoutManager.setScrollEnabled(false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1; // 单个项目 占一列
            }
        });

        mScheduleView.setLayoutManager(gridLayoutManager);

        // Adapter
        ScheduleAdapter adapter = new ScheduleAdapter(getContext(), colCount, mScheduleData, new ScheduleAdapter.ItemClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "点击 " + v.getTag(), Toast.LENGTH_LONG).show();
            }

            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "长按 " + v.getTag(), Toast.LENGTH_LONG).show();
                return true;
            }
        });
        mScheduleView.setAdapter(adapter);//设置数据
    }
}
