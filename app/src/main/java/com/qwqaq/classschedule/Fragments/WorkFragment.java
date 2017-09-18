package com.qwqaq.classschedule.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qwqaq.classschedule.R;

/**
 * Created by Zneia on 2017/9/14.
 */

public class WorkFragment extends Fragment {

    public static WorkFragment newInstance()
    {
        Bundle args = new Bundle();

        WorkFragment fragment = new WorkFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private View mView;
    private Context context;

    @Override
    public String getFragmentTitle() {
        return "作业单";
    }

    /**
     * 顶部工具条操作按钮 选项菜单 初始化
     */
    public void onTopToolbarCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_toolbar_work_fragment, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_work, container, false);
        context = mView.getContext();

        return mView;
    }
}
