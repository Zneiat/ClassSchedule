package com.qwqaq.classschedule.Fragments;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.qwqaq.classschedule.Activities.Activity;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Zneia on 2017/9/15.
 */

public abstract class Fragment extends SupportFragment {

    protected OnBackToFirstListener _mBackToFirstListener;

    /**
     * 定义 Fragment 标题
     */
    public String getFragmentTitle() {
        return getActivity().getTitle().toString();
    }

    /**
     * 当 Fragment 对用户可见时回调
     */
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }

    /**
     * 当 Fragment 与 Activity 发生关联时调用
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBackToFirstListener) {
            _mBackToFirstListener = (OnBackToFirstListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnBackToFirstListener");
        }
    }

    /**
     * 当 Fragment 与 Activity 关联被取消时调用（与 onAttach 对应）
     */
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

        if (((Activity) getActivity()).getLeftDrawer().isDrawerOpen(GravityCompat.START)) {
            // 隐藏侧边栏
            ((Activity) getActivity()).getLeftDrawer().closeDrawer(GravityCompat.START);
            return true;
        }

        if (getChildFragmentManager().getBackStackEntryCount() > 1) {
            popChild();
        } else {
            if (this instanceof HomeFragment) {
                // 如果是 第一个 Fragment 则执行退出 APP
                if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                    _mActivity.finish();
                } else {
                    TOUCH_TIME = System.currentTimeMillis();
                    Toast.makeText(getContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                }
            } else {
                // 如果不是，则回到第一个 Fragment
                _mBackToFirstListener.onBackToFirstFragment();
            }
        }
        return true;
    }

    public interface OnBackToFirstListener {
        void onBackToFirstFragment();
    }

    public void onTopToolbarCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    public void onTopToolbarOptionsItemSelected(MenuItem item) {
    }
}