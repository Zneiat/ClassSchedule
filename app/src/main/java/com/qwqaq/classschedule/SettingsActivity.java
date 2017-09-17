package com.qwqaq.classschedule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.Preference;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import static com.qwqaq.classschedule.MainApplication.*;

public class SettingsActivity extends PreferenceActivity {

    private Toolbar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar.setTitle(getTitle());
    }

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.activity_settings, new LinearLayout(this), false);

        // 初始化工具条
        mActionBar = (Toolbar) contentView.findViewById(R.id.action_bar);
        mActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 设置界面
        ViewGroup contentWrapper = (ViewGroup) contentView.findViewById(R.id.content_wrapper);
        LayoutInflater.from(this).inflate(layoutResID, contentWrapper, true);
        getWindow().setContentView(contentView);

        // Fragment
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_wrapper, new SettingsPreferenceFragment())
                .commit();
    }

    /**
     * 设置 > 首页 Fragment
     */
    public static class SettingsPreferenceFragment extends PreferenceFragment {

        private SettingsActivity mActivity;
        private View mView;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            initView();
            initPreferenceListener();
        }

        public void initView() {
            mActivity = ((SettingsActivity) getActivity());
            mView = mActivity.getListView();

            addPreferencesFromResource(R.xml.preferences);
        }

        public void initPreferenceListener() {
            Preference goToScheduleEditMode = findPreference("schedule_go_to_edit_mode");
            goToScheduleEditMode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    mActivity.actionScheduleGoToEditMode();
                    return true;
                }
            });

            Preference blogPref = findPreference("blog");
            blogPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    Uri content_url = Uri.parse("http://www.qwqaq.com");
                    intent.setData(content_url);
                    startActivity(Intent.createChooser(intent, "选择一个浏览器打开链接"));
                    return true;
                }
            });
        }

    }

    /*
    |--------------------------------------------------------------------------
    | Setting Actions
    |--------------------------------------------------------------------------
    |
    | 以下代码全是关于设置操作的
    |
    */

    private void actionScheduleGoToEditMode() {
        HomeFragment fragment = (HomeFragment)MainApplication.gFragments[F_HOME];
        ((MainActivity)fragment.getActivity()).onBackToFirstFragment();
        fragment.scheduleEditModeInto();
        finish();
        // TODO: 需要回到课程表 Fragment
    }
}