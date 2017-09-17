package com.qwqaq.classschedule;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.qwqaq.classschedule.Base.BaseFragment;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

/**
 * Created by Zneia on 2017/4/16.
 */

public class MainApplication extends Application
{
    public static BaseFragment[] gFragments = new BaseFragment[2];
    public static final int F_HOME = 0;
    public static final int F_WORK = 1;

    @Override
    public void onCreate()
    {
        super.onCreate();

        gFragments = new BaseFragment[2];

        /*Fragmentation.builder()
                // 设置 栈视图 模式为 悬浮球模式   SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.BUBBLE)
                // ture时，遇到异常："Can not perform this action after onSaveInstanceState!"时，会抛出
                // false时，不会抛出，会捕获，可以在handleException()里监听到
                .debug(BuildConfig.DEBUG)
                // 线上环境时，可能会遇到上述异常，此时debug=false，不会抛出该异常（避免crash），会捕获
                // 建议在回调处上传至我们的Crash检测服务器
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 以Bugtags为例子: 手动把捕获到的 Exception 传到 Bugtags 后台。
                        // Bugtags.sendException(e);
                    }
                })
                .install();*/
    }
}