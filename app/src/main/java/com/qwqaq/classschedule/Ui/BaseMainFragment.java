package com.qwqaq.classschedule.Ui;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.Toast;

import com.qwqaq.classschedule.HomeActivity;
import com.qwqaq.classschedule.R;
import com.qwqaq.classschedule.ScheduleFragment;

/**
 * 懒加载
 * Created by YoKeyword on 16/6/5.
 */
public abstract class BaseMainFragment extends BaseFragment
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
     * 处理回退事件
     *
     * @return
     */
    @Override
    public boolean onBackPressedSupport()
    {
        // 隐藏软键盘
        hideSoftInput();

        DrawerLayout drawer = (DrawerLayout) HomeActivity.getHomeActivity().findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        if (getChildFragmentManager().getBackStackEntryCount() > 1) {
            popChild();
        } else {
            if (this instanceof ScheduleFragment) {
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
}
