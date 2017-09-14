package com.qwqaq.classschedule;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qwqaq.classschedule.Ui.BaseMainFragment;

/**
 * Created by Zneia on 2017/9/14.
 */

public class HomeworkFragment extends BaseMainFragment {

    public static HomeworkFragment newInstance()
    {
        Bundle args = new Bundle();

        HomeworkFragment fragment = new HomeworkFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private View mView;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.homework_fragment, container, false);
        context = mView.getContext();


        return mView;
    }
}
