package com.qwqaq.classschedule.Activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.ListPreference;
import android.preference.Preference;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qwqaq.classschedule.Fragments.HomeFragment;
import com.qwqaq.classschedule.Kernel;
import com.qwqaq.classschedule.R;

import static com.qwqaq.classschedule.Kernel.*;

public class SettingActivity extends PreferenceActivity {

    private Toolbar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar.setTitle(getTitle());
    }

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.activity_setting, new LinearLayout(this), false);

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

        private SettingActivity mActivity;
        private View mView;



        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            initView();
            initPreference();
            initPreferenceFunc();
        }

        public void initView() {
            mActivity = ((SettingActivity) getActivity());
            mView = mActivity.getListView();

            addPreferencesFromResource(R.xml.preferences);
        }

        Preference scheduleGoToEditMode;
        ListPreference scheduleFistColType;
        Preference currentVersion;
        Preference blogLink;

        public void initPreference() {
            scheduleGoToEditMode = (Preference) findPreference("schedule_go_to_edit_mode");
            scheduleFistColType = (ListPreference) findPreference("schedule_fist_col_type");
            currentVersion = (Preference) findPreference("current_version");
            blogLink = (Preference) findPreference("blog_link");
        }

        public void initPreferenceFunc() {
            if(scheduleFistColType.getValue() == null){
                scheduleFistColType.setValueIndex(1);
            }

            scheduleGoToEditMode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    mActivity.actionScheduleGoToEditMode();
                    return true;
                }
            });

            currentVersion.setSummary(getAppVersion());

            blogLink.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
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

        /**
         * 获取版本号
         * @return 当前应用的版本号
         */
        public String getAppVersion() {
            try {
                PackageManager manager = getActivity().getPackageManager();
                PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
                return info.versionName;
            } catch (Exception e) {
                e.printStackTrace();
                return "未知";
            }
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
        HomeFragment fragment = (HomeFragment) Kernel.gFragments[F_HOME];
        ((MainActivity)fragment.getActivity()).onBackToFirstFragment();
        fragment.getScheduleView().editModeEntry();
        finish();
        // TODO: 需要回到课程表 Fragment
    }
}