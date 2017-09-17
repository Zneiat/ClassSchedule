package com.qwqaq.classschedule;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.qwqaq.classschedule.Base.BaseMainActivity;
import com.qwqaq.classschedule.Base.BaseFragment;

import java.util.Timer;
import java.util.TimerTask;

import me.yokeyword.fragmentation.SupportFragment;

import static com.qwqaq.classschedule.MainApplication.*;

public class MainActivity extends BaseMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initFragments();
        initView();
    }

    /**
     * 顶部工具条操作按钮 点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // 调用当前用户可见 Fragment 的操作
        getCurrentVisibleFragment().onTopToolbarOptionsItemSelected(item);

        return true;
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
            showFragment(mFragments[F_HOME]);
        }
        if (id == R.id.nav_work) {
            showFragment(mFragments[F_WORK]);

        }
        if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);

            // 延迟执行，解决卡顿
            (new Timer()).schedule(new TimerTask() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeLeftDrawer();
                        }
                    });
                }
            }, 500);

            return false;
        }

        closeLeftDrawer();
        return true;
    }
}