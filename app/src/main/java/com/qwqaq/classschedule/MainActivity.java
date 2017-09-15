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

import com.qwqaq.classschedule.Base.BaseActivity;
import com.qwqaq.classschedule.Base.BaseFragment;

import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends BaseActivity implements BaseFragment.OnBackToFirstListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initView();
        initFragment();
    }

    /**
     * 初始化 视图
     */
    @Override
    protected void initView()
    {
        super.initView();
    }

    public static final int F_HOME = 0;
    public static final int F_WORK = 1;
    private SupportFragment[] mFragments = new SupportFragment[2];

    /**
     * 初始化 Fragment
     */
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

    /**
     * 返回键 点按事件
     */
    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    /**
     * 当回到主 Fragment 时执行
     */
    @Override
    public void onBackToFirstFragment() {
        showHideFragment(mFragments[0]); // showHideFragment() 无第二个参数返回栈，需要
        mLeftNavigationView.setCheckedItem(R.id.nav_home);
    }

    /**
     * 顶部工具条操作按钮 构建
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_toolbar_main_activity, menu);

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

    /**
     * 顶部工具条操作按钮 点击事件
     */
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

    /**
     * 侧边栏导航选项选中事件
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int position = 0;
        int id = item.getItemId();

        // 是否在该类别 Fragment 的主页
        SupportFragment currentFragment = mFragments[position];
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
            showHideFragment(mFragments[F_HOME]);
        }
        if (id == R.id.nav_work) {
            showHideFragment(mFragments[F_WORK]);
            // showHideFragment(SupportFragment showFragment, SupportFragment hideFragment);
        }
        if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);

            return false;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}