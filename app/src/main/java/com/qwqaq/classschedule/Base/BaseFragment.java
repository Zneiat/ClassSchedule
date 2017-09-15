package com.qwqaq.classschedule.Base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.qwqaq.classschedule.HomeFragment;
import com.qwqaq.classschedule.R;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Zneia on 2017/9/15.
 */

public abstract class BaseFragment extends SupportFragment
{
    protected OnBackToFirstListener _mBackToFirstListener;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnBackToFirstListener) {
            _mBackToFirstListener = (OnBackToFirstListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBackToFirstListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        _mBackToFirstListener = null;
    }

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    /**
     * 返回键 点按事件
     */
    @Override
    public boolean onBackPressedSupport()
    {
        // 隐藏软键盘
        hideSoftInput();

        if (((BaseActivity)getActivity()).mLeftDrawer.isDrawerOpen(GravityCompat.START)) {
            // 隐藏侧边栏
            ((BaseActivity)getActivity()).mLeftDrawer.closeDrawer(GravityCompat.START);
            return true;
        }

        if (getChildFragmentManager().getBackStackEntryCount() > 1) {
            popChild();
        } else {
            if (this instanceof HomeFragment) {
                // 如果是 第一个Fragment 则执行退出app
                if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                    _mActivity.finish();
                } else {
                    TOUCH_TIME = System.currentTimeMillis();
                    Toast.makeText(getContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                }
            } else {
                // 如果不是,则回到第一个Fragment
                _mBackToFirstListener.onBackToFirstFragment();
            }
        }
        return true;
    }

    public interface OnBackToFirstListener
    {
        void onBackToFirstFragment();
    }

    protected Toolbar mTopToolbar; // 顶部工具条

    /**
     * 初始化 顶部工具条
     */
    protected void initTopBar(View view)
    {
        // Top Bar
        mTopToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        Drawable iconDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_add_white_24dp);
        mTopToolbar.setOverflowIcon(iconDrawable); // 修改右上角 三点 more options 图标
        ((BaseActivity)getActivity()).setSupportActionBar(mTopToolbar);

        // Left Nav
        ActionBarDrawerToggle leftDrawerDisplayToggle = new ActionBarDrawerToggle(getActivity(), ((BaseActivity)getActivity()).mLeftDrawer, mTopToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        ((BaseActivity)getActivity()).mLeftDrawer.setDrawerListener(leftDrawerDisplayToggle);
        leftDrawerDisplayToggle.syncState();
    }
}