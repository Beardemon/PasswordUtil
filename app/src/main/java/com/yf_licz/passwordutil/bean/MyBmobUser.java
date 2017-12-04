package com.yf_licz.passwordutil.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by yfzx-sh-licz on 2017/11/21.
 */

public class MyBmobUser extends BmobUser {


    private String safeKey;

    public String getSafeKey() {
        return safeKey;
    }

    public void setSafeKey(String safeKey) {
        this.safeKey = safeKey;
    }
}
