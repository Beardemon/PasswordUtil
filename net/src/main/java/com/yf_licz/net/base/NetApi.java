package com.yf_licz.net.base;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yfzx-sh-licz on 2017/11/8.
 */

public class NetApi {
    private static final String TAG = "NetApi";

    private static final String baseUrl = "http://api.douban.com/v2/";
    protected ApiService mService;

    private static volatile NetApi instance;


    public static NetApi getInstance() {
        if (instance == null) {
            synchronized (NetApi.class) {
                if (instance == null) {
                    instance = new NetApi();
                }
            }
        }
        return instance;
    }

    private NetApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(initOkHttpClint().build())
                .baseUrl(baseUrl)
                .build();
        mService = retrofit.create(ApiService.class);

    }

    private OkHttpClient.Builder initOkHttpClint() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d(TAG, message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(logging);

        return builder;
    }

    public ApiService getService() {
        return mService;
    }


}
