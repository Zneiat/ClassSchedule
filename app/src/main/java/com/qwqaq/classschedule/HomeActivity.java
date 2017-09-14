package com.qwqaq.classschedule;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.qwqaq.classschedule.Ui.BaseMainFragment;
import com.qwqaq.classschedule.Ui.BottomBar;
import com.qwqaq.classschedule.Ui.BottomBarTab;
import com.qwqaq.classschedule.Utils.DisplayUtil;
import com.qwqaq.classschedule.Utils.StreamUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

public class HomeActivity extends SupportActivity implements NavigationView.OnNavigationItemSelectedListener, BaseMainFragment.OnBackToFirstListener {

    private static HomeActivity mHomeActivity;
    private NavigationView mLeftNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        mHomeActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mLeftNavView = (NavigationView) findViewById(R.id.nav_view);
        mLeftNavView.setNavigationItemSelectedListener(this);

        initFragment();
    }

    public static final int F_SCHEDULE = 0;
    public static final int F_HOMEWORK = 1;

    private SupportFragment[] mFragments = new SupportFragment[2];

    private void initFragment()
    {
        SupportFragment firstFragment = findFragment(ScheduleFragment.class);
        // Fragment
        if (firstFragment == null) {
            mFragments[F_SCHEDULE] = ScheduleFragment.newInstance();
            mFragments[F_HOMEWORK] = HomeworkFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_container, F_SCHEDULE,
                    mFragments[F_SCHEDULE],
                    mFragments[F_HOMEWORK]);
        } else {
            mFragments[F_SCHEDULE] = firstFragment;
            mFragments[F_HOMEWORK] = findFragment(HomeworkFragment.class);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);

        // 将图标颜色改为白色
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            Drawable drawable = item.getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/



    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public void onBackToFirstFragment() {
        mLeftNavView.setCheckedItem(R.id.nav_schedule);
        showHideFragment(mFragments[0], mFragments[prePosition]);
        prePosition = 0;
    }

    private int prePosition = 0;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int position = 0;
        int id = item.getItemId();

        if (id == R.id.nav_schedule) {
            position = F_SCHEDULE;
        } else if (id == R.id.nav_homework) {
            position = F_HOMEWORK;
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);

            return false;
        }

        SupportFragment currentFragment = mFragments[position];
        int count = currentFragment.getChildFragmentManager().getBackStackEntryCount();

        if (count > 1) {
            // 如果不在该类别Fragment的主页,则回到主页;
            if (currentFragment instanceof ScheduleFragment) {
                currentFragment.popToChild(ScheduleFragment.class, false);
            } else if (currentFragment instanceof HomeworkFragment) {
                currentFragment.popToChild(HomeworkFragment.class, false);
            }
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        showHideFragment(mFragments[position], mFragments[prePosition]);
        prePosition = position;

        return true;
    }

    public static HomeActivity getHomeActivity() {
        return mHomeActivity;
    }
}
