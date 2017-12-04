package com.yf_licz.passwordutil;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.yf_licz.passwordutil.utils.SharedPreferencesUtils;

import cn.bmob.v3.Bmob;
import okhttp3.OkHttpClient;

/**
 * @author yfzx-sh-licz
 * @date 2017/11/16
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //默认初始化bmob
        Bmob.initialize(this, "18e20d26e18642f1e79e6d89d020f6cb");
        //SharedPreferencesUtils初始化
        SharedPreferencesUtils.init(context);
        // TODO: 2017/11/28  debug版本可视化

        //运行项目 打开Chrome浏览器在地址栏输入chrome://inspect
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

    }


    public static Context getContext() {
        return context;
    }
}