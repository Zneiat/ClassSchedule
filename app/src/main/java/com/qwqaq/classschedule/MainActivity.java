package com.qwqaq.classschedule;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.qwqaq.classschedule.Ui.BaseMainFragment;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends SupportActivity implements NavigationView.OnNavigationItemSelectedListener, BaseMainFragment.OnBackToFirstListener {

    private static MainActivity mMainActivity;
    private NavigationView mLeftNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mMainActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // 修改右上角 三点 more options 图标
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add_white_24dp);
        toolbar.setOverflowIcon(drawable);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mLeftNavView = (NavigationView) findViewById(R.id.nav_view);
        mLeftNavView.setNavigationItemSelectedListener(this);

        initFragment();
    }

    public static final int F_HOME = 0;
    public static final int F_WORK = 1;

    private SupportFragment[] mFragments = new SupportFragment[2];

    private void initFragment()
    {
        SupportFragment firstFragment = findFragment(HomeFragment.class);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_main_activity, menu);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_new_reminder) {

        }
        if (id == R.id.action_new_todo) {

        }
        if (id == R.id.action_new_note) {

        }

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
        mLeftNavView.setCheckedItem(R.id.nav_home);
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

        if (id == R.id.nav_home) {
            position = F_HOME;
        } else if (id == R.id.nav_work) {
            position = F_WORK;
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);

            return false;
        }

        SupportFragment currentFragment = mFragments[position];
        int count = currentFragment.getChildFragmentManager().getBackStackEntryCount();

        if (count > 1) {
            // 如果不在该类别Fragment的主页,则回到主页;
            if (currentFragment instanceof HomeFragment) {
                currentFragment.popToChild(HomeFragment.class, false);
            } else if (currentFragment instanceof WorkFragment) {
                currentFragment.popToChild(WorkFragment.class, false);
            }
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        showHideFragment(mFragments[position], mFragments[prePosition]);
        prePosition = position;

        return true;
    }

    public static MainActivity getMainActivity() {
        return mMainActivity;
    }
}
