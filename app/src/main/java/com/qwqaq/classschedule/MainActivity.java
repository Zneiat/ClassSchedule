package com.qwqaq.classschedule;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.qwqaq.classschedule.Base.BaseMainActivity;
import com.qwqaq.classschedule.Base.BaseFragment;

import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends BaseMainActivity implements BaseFragment.OnBackToFirstListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initView();
        initFragment();
    }

    public SupportFragment[] sFragments;
    public static final int F_HOME = 0;
    public static final int F_WORK = 1;

    /**
     * 初始化 Fragment
     */
    private void initFragment() {
        sFragments = MainApplication.gFragments;
        SupportFragment firstFragment = findFragment(HomeFragment.class);
        // Fragment
        if (firstFragment == null) {
            sFragments[F_HOME] = HomeFragment.newInstance();
            sFragments[F_WORK] = WorkFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_container, F_HOME,
                    sFragments[F_HOME],
                    sFragments[F_WORK]);
        } else {
            sFragments[F_HOME] = firstFragment;
            sFragments[F_WORK] = findFragment(WorkFragment.class);
        }
    }

    /**
     * 当回到主 Fragment 时执行
     */
    @Override
    public void onBackToFirstFragment() {
        showHideFragment(sFragments[0]); // showHideFragment() 无第二个参数返回栈，需要
        mLeftNavigationView.setCheckedItem(R.id.nav_home);
    }

    /**
     * 初始化 视图
     */
    @Override
    protected void initView() {
        super.initView();
    }

    /**
     * 侧边栏导航选项选中事件
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int position = 0;
        int id = item.getItemId();

        // 是否在该类别 Fragment 的主页
        SupportFragment currentFragment = sFragments[position];
        int count = currentFragment.getChildFragmentManager().getBackStackEntryCount();
        if (count > 1) {
            // 如果不在该类别 Fragment 的主页，则回到主页;
            if (currentFragment instanceof HomeFragment) {
                currentFragment.popToChild(HomeFragment.class, false);
            } else if (currentFragment instanceof WorkFragment) {
                currentFragment.popToChild(WorkFragment.class, false);
            }
            return true;
        }

        // 切换 Fragment
        if (id == R.id.nav_home) {
            showHideFragment(sFragments[F_HOME]);
        }
        if (id == R.id.nav_work) {
            showHideFragment(sFragments[F_WORK]);
            // showHideFragment(SupportFragment showFragment, SupportFragment hideFragment);
        }
        if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);

            closeLeftDrawer();
            return false;
        }

        closeLeftDrawer();
        return true;
    }
}