package com.yf_licz.passwordutil.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by yfzx-sh-licz on 2017/11/29.
 */

public class MyBmobUserKey extends BmobObject {
    private String userName;
    private List<UserKeyBean> userKeyBeanList ;

    public List<UserKeyBean> getUserKeyBeanList() {
        return userKeyBeanList;
    }

    public void setUserKeyBeanList(List<UserKeyBean> userKeyBeanList) {
        this.userKeyBeanList = userKeyBeanList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
