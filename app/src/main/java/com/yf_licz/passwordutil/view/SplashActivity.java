package com.yf_licz.passwordutil.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.yf_licz.passwordutil.R;
import com.yf_licz.passwordutil.bean.MyBmobUser;

import cn.bmob.v3.BmobUser;

/**
 * @author yfzx-sh-licz
 * @date 2017/11/16
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.application_activity_splash);

        //延迟2S跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MyBmobUser userInfo = BmobUser.getCurrentUser(MyBmobUser.class);
                Intent intent = new Intent();
                if (userInfo != null) {
                    //已经登陆 去appmain页面
                    intent.setClass(SplashActivity.this, AppMainActivity.class);

                } else {
                    //还未登陆 去login页面
                    intent.setClass(SplashActivity.this, LoginActivity.class);

                }
                startActivity(intent);
                finish();
            }
        }, 500);
    }

}
