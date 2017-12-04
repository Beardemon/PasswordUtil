package com.yf_licz.passwordutil.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yf_licz.passwordutil.R;
import com.yf_licz.passwordutil.databinding.AboutActivityBinding;

/**
 *
 * @author yfzx-sh-licz
 * @date 2017/12/3
 */

public class AboutActivity extends AppCompatActivity {
    private AboutActivityBinding aboutActivityBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutActivityBinding = DataBindingUtil.setContentView(this, R.layout.application_activity_about);

    }
}
