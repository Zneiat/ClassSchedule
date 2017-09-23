package com.qwqaq.classschedule.Activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.qwqaq.classschedule.Fragments.Fragment;
import com.qwqaq.classschedule.Fragments.HomeFragment;
import com.qwqaq.classschedule.Kernel;
import com.qwqaq.classschedule.R;
import com.qwqaq.classschedule.Fragments.WorkFragment;

import me.yokeyword.fragmentation.SupportActivity;

import static com.qwqaq.classschedule.Kernel.*;

/**
 * Created by Zneia on 2017/9/15.
 */

public abstract class Activity extends SupportActivity implements NavigationView.OnNavigationItemSelectedListener, Fragment.OnBackToFirstListener {

    /*
    |--------------------------------------------------------------------------
    | 关于 APP 中所有主要 Fragments 的操作
    |--------------------------------------------------------------------------
    */

    public Fragment[] mFragments;
    public Fragment mCurrentVisibleFragment;

    /**
     * 初始化 Fragments
     */
    protected void initFragments() {
        mFragments = Kernel.gFragments;
        Fragment firstFragment = findFragment(HomeFragment.class);
        // Fragment
        if (firstFragment == null) {
            mFragments[F_HOME] = HomeFragment.newInstance();
            mFragments[F_WORK] = WorkFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_container, F_HOME,
                    mFragments[F_HOME],
                    mFragments[F_WORK]);
        } else {
            mFragments[F_HOME] = firstFragment;
            mFragments[F_WORK] = findFragment(WorkFragment.class);
        }

        // 设置当前可见 Fragment
        mCurrentVisibleFragment = mFragments[F_HOME];
    }

    /**
     * 切换 Fragment
     */
    public void showFragment(Fragment fragmentClass) {
        showHideFragment(fragmentClass);
        // showHideFragment(SupportFragment showFragment, SupportFragment hideFragment);
        mCurrentVisibleFragment = fragmentClass;
        // 修改 TopToolbar
        mTopToolbar.setTitle(fragmentClass.getFragmentTitle());
        invalidateOptionsMenu();
    }

    /**
     * 获取当前用户可见的 Fragment Class
     */
    public Fragment getCurrentVisibleFragment() {
        return mCurrentVisibleFragment;
    }

    /**
     * 当回到主 Fragment 时执行
     */
    @Override
    public void onBackToFirstFragment() {
        showFragment(mFragments[0]); // showHideFragment() 无第二个参数返回栈，需要
        mLeftNavigationView.setCheckedItem(R.id.nav_home);
    }

    /*
    |--------------------------------------------------------------------------
    | 关于 APP 中主界面的控制
    |--------------------------------------------------------------------------
    */

    /**
     * 界面视图
     */
    protected DrawerLayout mLeftDrawer; // 左侧边栏
    protected NavigationView mLeftNavigationView; // 左侧边栏导航列表
    protected Toolbar mTopToolbar; // 顶部工具条

    protected void initView()
    {
        initLeftDrawer();
        initTopToolBar();
    }

    /**
     * 左侧边栏 初始化
     */
    protected void initLeftDrawer() {
        // Left Nav
        mLeftDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mLeftNavigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 获取 左侧边栏
     */
    public DrawerLayout getLeftDrawer() {
        return mLeftDrawer;
    }

    // 显示左侧边栏
    public void showLeftDrawer() {
        mLeftDrawer.closeDrawer(GravityCompat.END);
    }

    // 显示左侧边栏
    public void closeLeftDrawer() {
        mLeftDrawer.closeDrawer(GravityCompat.START);
    }

    /**
     * 顶部工具条 初始化
     */
    protected void initTopToolBar() {
        // Top Tool Bar
        mTopToolbar = (Toolbar) findViewById(R.id.toolbar);

        Drawable iconDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add_white_24dp);
        mTopToolbar.setOverflowIcon(iconDrawable); // 修改右上角 三点 more options 图标
        setSupportActionBar(mTopToolbar);

        // TopBar Left Part LeftDrawer Open/Hide Toggle Btn
        ActionBarDrawerToggle leftDrawerDisplayToggle = new ActionBarDrawerToggle(this, mLeftDrawer, mTopToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mLeftDrawer.setDrawerListener(leftDrawerDisplayToggle);
        leftDrawerDisplayToggle.syncState();

        // LeftDrawer Disable Elevation
        mLeftDrawer.setDrawerElevation(0);
    }

    /**
     * 获取 顶部工具条
     */
    public Toolbar getTopToolBar() {
        return mTopToolbar;
    }

    /**
     * 顶部工具条 右侧菜单 创建
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.activity_main_top_toolbar, menu);

        if (getCurrentVisibleFragment() != null) {
            getCurrentVisibleFragment().onTopToolbarCreateOptionsMenu(menu, getMenuInflater());
        }

        // 将每一个图标颜色改为白色
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
}