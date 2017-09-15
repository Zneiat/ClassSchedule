package com.qwqaq.classschedule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qwqaq.classschedule.Ui.BaseMainFragment;

/**
 * Created by Zneia on 2017/9/14.
 */

public class WorkFragment extends BaseMainFragment {

    public static WorkFragment newInstance()
    {
        Bundle args = new Bundle();

        WorkFragment fragment = new WorkFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private View mView;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.work_fragment, container, false);
        context = mView.getContext();


        return mView;
    }
}
