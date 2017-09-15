package com.qwqaq.classschedule;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.qwqaq.classschedule.Ui.BaseMainFragment;
import com.qwqaq.classschedule.Utils.DisplayUtil;
import com.qwqaq.classschedule.Utils.StreamUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Zneia on 2017/9/14.
 */

public class HomeFragment extends BaseMainFragment {

    public static HomeFragment newInstance()
    {
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private View mView;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.home_fragment, container, false);
        context = mView.getContext();

        try {
            initTableLayout();
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

    private ArrayList<String[]> tableData = new ArrayList<>();

    private void initTableLayout() throws JSONException {
        tableData.add(new String[]{"周一", "周二", "周三", "周四", "周五", "周六"});

        // JSON 导入
        String json = StreamUtils.get(context, R.raw.classes_default_data);
        JSONArray jsonArr = new JSONArray(json);

        for (int i = 0; i < jsonArr.length(); i++) {

            JSONArray itemJsonArr = jsonArr.getJSONArray(i);
            String[] itemArr = new String[itemJsonArr.length()];

            for (int o = 0; o < itemJsonArr.length(); o++) {
                itemArr[o] = itemJsonArr.getString(o);
            }

            tableData.add(itemArr);
        }

        // 控件
        TableLayout layout = (TableLayout) mView.findViewById(R.id.class_schedule);

        int itemIndex = 0;
        for (String[] item : tableData) {
            TableRow row = new TableRow(context);

            if (itemIndex == 0) {
                row.setPadding(0, 0, 0, DisplayUtil.dipToPx(context, 10));
            }
            if (itemIndex == 5) {
                row.setPadding(0, DisplayUtil.dipToPx(context, 10), 0, 0);
            }
            for (int i = 0; i < item.length; i++) {
                // TextView 控件
                TextView text = new TextView(context);
                text.setGravity(Gravity.CENTER);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
                text.setHeight(DisplayUtil.dipToPx(context, 40));
                text.setText(item[i]);
                if (itemIndex == 0) {
                    text.getPaint().setFakeBoldText(true);
                    // text.setTextColor(Color.parseColor("#00a2ed"));
                    text.setHeight(DisplayUtil.dipToPx(context, 25));
                }

                // 向 TableRow 中添加 TextView 控件
                row.addView(text, i);
            }

            layout.addView(row); // 向布局控件添加新视图
            itemIndex++;
        }
    }

}
