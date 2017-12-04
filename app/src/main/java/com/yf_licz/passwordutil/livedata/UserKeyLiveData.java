package com.yf_licz.passwordutil.livedata;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.MainThread;

import com.yf_licz.passwordutil.bean.UserKeyBean;
import com.yf_licz.passwordutil.bean.WrapperBean;

import java.util.ArrayList;

/**
 * @author yfzx-sh-licz
 * @date 2017/11/29
 */

public class UserKeyLiveData<T> extends MutableLiveData<T> {
    private static UserKeyLiveData sInstance;

    @MainThread
    public static UserKeyLiveData get() {
        if (sInstance == null) {
            sInstance = new UserKeyLiveData();
            sInstance.setValue(WrapperBean.content(new ArrayList<UserKeyBean>()));

        }
        return sInstance;
    }

    public UserKeyLiveData() {

    }

}
