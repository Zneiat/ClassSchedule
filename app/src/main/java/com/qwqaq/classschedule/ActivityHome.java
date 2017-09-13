package com.qwqaq.classschedule;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.qwqaq.classschedule.Utils.DisplayUtil;
import com.qwqaq.classschedule.Utils.StreamUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ActivityHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            initTableLayout();
        } catch (Exception e) {
            Log.e("初始化课程表", "发生错误", e);
            new AlertDialog.Builder(this)
                    .setTitle("课程表 无法启动")
                    .setMessage("你可以将下列问题通过QQ反馈给我：\n\n" + e.getMessage())
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityHome.this.finish();
                        }
                    }).show();
        }
    }

    private ArrayList<String[]> tableData = new ArrayList<>();

    private void initTableLayout() throws JSONException {
        tableData.add(new String[]{"周一", "周二", "周三", "周四", "周五"});

        // JSON 导入
        String json = StreamUtils.get(this, R.raw.classes_default_data);
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
        TableLayout layout = (TableLayout) findViewById(R.id.class_schedule);

        for (String[] item : tableData) {
            TableRow row = new TableRow(this);

            for (int i = 0; i < item.length; i++) {
                // TextView 控件
                TextView text = new TextView(this);
                text.setGravity(Gravity.CENTER);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
                text.setHeight(DisplayUtil.dipToPx(this, 50));
                text.setText(item[i]);
                // 向 TableRow 中添加 TextView 控件
                row.addView(text, i);
            }

            layout.addView(row); // 向布局控件添加新视图
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_homework_list) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
