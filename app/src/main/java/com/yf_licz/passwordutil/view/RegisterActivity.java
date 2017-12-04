package com.yf_licz.passwordutil.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yf_licz.passwordutil.R;
import com.yf_licz.passwordutil.RegisterViewBinding;
import com.yf_licz.passwordutil.bean.MyBmobUser;
import com.yf_licz.passwordutil.bean.WrapperBean;
import com.yf_licz.passwordutil.utils.ToastUtils;
import com.yf_licz.passwordutil.viewmodule.RegisterViewModule;


/**
 * @author yfzx-sh-licz
 * @date 2017/11/15
 */
public class RegisterActivity extends AppCompatActivity {
    private RegisterViewBinding registerViewBinding;

    private RegisterViewModule registerViewModule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();

    }

    private void initView() {

        registerViewBinding = DataBindingUtil.setContentView(this, R.layout.application_activity_register);
        registerViewBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerViewModule.register(registerViewBinding.etName.getText().toString(), registerViewBinding.etLoginPassword.getText().toString(), registerViewBinding.etSafeKey.getText().toString());
            }
        });
        setMaterialUi();
    }

    private void initData() {
        registerViewModule = ViewModelProviders.of(this).get(RegisterViewModule.class);
        registerViewModule.getMyBmobUserBean().observe(this, new Observer<WrapperBean<MyBmobUser>>() {
            @Override
            public void onChanged(@Nullable WrapperBean<MyBmobUser> myBmobUserWrapperBean) {
                updateView(myBmobUserWrapperBean);
            }

        });
    }

    private void updateView(WrapperBean<MyBmobUser> userPersonalBeanWrapperBean) {
        switch (userPersonalBeanWrapperBean.status) {
            case Content: {
                ToastUtils.showShortToast("用户注册成功");
                startActivity(new Intent(RegisterActivity.this, AppMainActivity.class));
                RegisterActivity.this.finish();
                break;
            }
            case Empty: {
                ToastUtils.showShortToast("empty");
                break;
            }
            case Error: {
                if (202 == userPersonalBeanWrapperBean.error.getErrorCode()) {
                    ToastUtils.showShortToast("该用户已经注册，请重新输入用户名");
                    registerViewBinding.etName.setText("");
                } else {
                    ToastUtils.showShortToast(userPersonalBeanWrapperBean.error.toString());
                }
                break;
            }
            case Loading: {
                ToastUtils.showShortToast("loading");
                break;
            }
            default:
        }
    }

    private void setMaterialUi() {

        setSupportActionBar(registerViewBinding.toolBar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerViewBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
