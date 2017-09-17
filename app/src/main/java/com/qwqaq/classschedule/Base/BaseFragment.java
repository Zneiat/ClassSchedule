package com.qwqaq.classschedule.Base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
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

public abstract class BaseFragment extends SupportFragment {

    protected OnBackToFirstListener _mBackToFirstListener;

    // 定义 Fragment 标题
    public String getFragmentTitle() {
        return getActivity().getTitle().toString();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBackToFirstListener) {
            _mBackToFirstListener = (OnBackToFirstListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBackToFirstListener");
        }
    }

    @Override
    public void onDetach() {
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
    public boolean onBackPressedSupport() {
        // 隐藏软键盘
        hideSoftInput();

        if (((BaseMainActivity) getActivity()).mLeftDrawer.isDrawerOpen(GravityCompat.START)) {
            // 隐藏侧边栏
            ((BaseMainActivity) getActivity()).mLeftDrawer.closeDrawer(GravityCompat.START);
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

    public interface OnBackToFirstListener {
        void onBackToFirstFragment();
    }

    protected Toolbar mTopToolbar; // 顶部工具条

    /**
     * 初始化 顶部工具条
     */
    protected void initTopBar(View view, boolean hasOptionsMenu) {
        // TopBar
        mTopToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mTopToolbar.setTitle(getFragmentTitle()); // 设置标题

        Drawable iconDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_add_white_24dp);
        mTopToolbar.setOverflowIcon(iconDrawable); // 修改右上角 三点 more options 图标
        ((BaseMainActivity) getActivity()).setSupportActionBar(mTopToolbar);

        // TopBar Left Part LeftDrawer Open/Hide Toggle Btn
        ActionBarDrawerToggle leftDrawerDisplayToggle = new ActionBarDrawerToggle(getActivity(), ((BaseMainActivity) getActivity()).mLeftDrawer, mTopToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        ((BaseMainActivity) getActivity()).mLeftDrawer.setDrawerListener(leftDrawerDisplayToggle);
        leftDrawerDisplayToggle.syncState();

        // TopBar Right Part OptionsMenu
        setHasOptionsMenu(hasOptionsMenu);
    }

    /**
     * 顶部工具条 右侧菜单 创建
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // 将每一个图标颜色改为白色
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            Drawable drawable = item.getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }
}