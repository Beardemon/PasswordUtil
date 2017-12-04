package com.yf_licz.passwordutil.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
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
import com.yf_licz.passwordutil.databinding.AddNewItemBinding;
import com.yf_licz.passwordutil.utils.SecurityUtils;
import com.yf_licz.passwordutil.utils.ToastUtils;
import com.yf_licz.passwordutil.viewmodule.AddNewItemViewModule;

/**
 * @author yfzx-sh-licz
 * @date 2017/11/27
 */

public class AddNewItemActivity extends AppCompatActivity {
    private AddNewItemBinding addNewItemBinding;
    private AddNewItemViewModule addNewItemViewModule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        addNewItemViewModule = ViewModelProviders.of(this).get(AddNewItemViewModule.class);
    }

    private void initView() {
        addNewItemBinding = DataBindingUtil.setContentView(this, R.layout.application_activity_add_new_item);
        setMaterialUi();
        addNewItemBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(addNewItemViewModule.getSafeKeyMd5());
            }
        });
    }

    private void setMaterialUi() {

        setSupportActionBar(addNewItemBinding.toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addNewItemBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showPopupWindow(final String safeKeyMd5) {
        View rootView = LayoutInflater.from(this).inflate(R.layout.application_activity_register, null);
        final SetSafeKeyPopupWindowBinding  setSafeKeyPopupWindowBinding = SetSafeKeyPopupWindowBinding.inflate(LayoutInflater.from(this));
        View contentView = setSafeKeyPopupWindowBinding.getRoot();
        final PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setContentView(contentView);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        setSafeKeyPopupWindowBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (safeKeyMd5.equals(SecurityUtils.MD5_Encode(setSafeKeyPopupWindowBinding.etSafeKey.getText().toString()))) {
                    addNewItemViewModule.insertUserKey(addNewItemBinding.etKeyName.getText().toString(), addNewItemBinding.etKey.getText().toString(), addNewItemBinding.etKeyDes.getText().toString(),setSafeKeyPopupWindowBinding.etSafeKey.getText().toString());
                    ToastUtils.showShortToast("safekey核对正确，创建完毕");
                    popupWindow.dismiss();
                    finish();
                } else {
                    popupWindow.dismiss();
                    ToastUtils.showShortToast("safekey核对错误，不予创建");
                }

            }
        });
        setSafeKeyPopupWindowBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortToast("取消输入safekey");
                popupWindow.dismiss();

            }
        });

    }
}
