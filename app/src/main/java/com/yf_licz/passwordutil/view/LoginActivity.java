package com.yf_licz.passwordutil.view;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.yf_licz.passwordutil.R;
import com.yf_licz.passwordutil.SetSafeKeyPopupWindowBinding;
import com.yf_licz.passwordutil.bean.MyBmobUser;
import com.yf_licz.passwordutil.bean.WrapperBean;
import com.yf_licz.passwordutil.databinding.LoginViewBinding;
import com.yf_licz.passwordutil.utils.SecurityUtils;
import com.yf_licz.passwordutil.utils.ToastUtils;
import com.yf_licz.passwordutil.viewmodule.LoginViewModule;


/**
 * @author yfzx-sh-licz
 * @date 2017/11/23
 */

public class LoginActivity extends AppCompatActivity {
    private LoginViewBinding loginViewBinding;
    private SetSafeKeyPopupWindowBinding setSafeKeyPopupWindowBinding;
    private LoginViewModule loginViewModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();

    }

    private void initView() {
        loginViewBinding = DataBindingUtil.setContentView(this, R.layout.application_activity_login);
        loginViewBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModule.login(loginViewBinding.etLoginName.getText().toString(), loginViewBinding.etLoginPassword.getText().toString());
            }
        });
        loginViewBinding.btnGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        loginViewBinding.test.setText(Build.MODEL.toLowerCase().trim());
    }

    private void initData() {
        loginViewModule = ViewModelProviders.of(this).get(LoginViewModule.class);
        loginViewModule.getMyBmobUserBean().observe(this, new Observer<WrapperBean<MyBmobUser>>() {
            @Override
            public void onChanged(@Nullable WrapperBean<MyBmobUser> myBmobUserWrapperBean) {
                updateView(myBmobUserWrapperBean);
            }
        });
    }

    private void updateView(WrapperBean<MyBmobUser> userPersonalBeanWrapperBean) {
        switch (userPersonalBeanWrapperBean.status) {
            case Content: {
                startActivity(new Intent(LoginActivity.this, AppMainActivity.class));
                LoginActivity.this.finish();
                break;
            }
            case Empty: {
                ToastUtils.showShortToast("empty");
                break;
            }
            case Error: {
                loginViewBinding.etLoginName.setText("");
                loginViewBinding.etLoginPassword.setText("");
                ToastUtils.showShortToast(userPersonalBeanWrapperBean.error.toString());

                break;
            }
            case Loading: {
                ToastUtils.showShortToast("loading");
                break;
            }
            default:
        }
    }


}
