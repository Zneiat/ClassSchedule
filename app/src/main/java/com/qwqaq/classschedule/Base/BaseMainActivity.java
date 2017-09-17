package com.qwqaq.classschedule.Base;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import com.qwqaq.classschedule.R;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by Zneia on 2017/9/15.
 */

public abstract class BaseMainActivity extends SupportActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout mLeftDrawer; // 左侧边栏
    protected NavigationView mLeftNavigationView; // 左侧边栏导航列表

    protected void initView()
    {
        // Left Nav
        mLeftDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mLeftNavigationView.setNavigationItemSelectedListener(this);
    }

    public void showLeftDrawer() {
        mLeftDrawer.closeDrawer(GravityCompat.END);
    }

    public void closeLeftDrawer() {
        mLeftDrawer.closeDrawer(GravityCompat.START);
    }
}