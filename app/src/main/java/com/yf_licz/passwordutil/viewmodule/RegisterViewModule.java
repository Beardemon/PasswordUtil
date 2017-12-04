package com.yf_licz.passwordutil.viewmodule;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.yf_licz.passwordutil.bean.MyBmobUser;
import com.yf_licz.passwordutil.bean.WrapperBean;
import com.yf_licz.passwordutil.module.DataRepository;
import com.yf_licz.passwordutil.utils.SharedPreferencesUtils;

/**
 * @author yfzx-sh-licz
 * @date 2017/11/16
 */

public class RegisterViewModule extends ViewModel {
    private DataRepository repository = DataRepository.getInstance();
    private MutableLiveData<WrapperBean<MyBmobUser>> myBmobUserLiveData;

    public void register(String name, String password,String safeKey) {
        repository.register(name, password,safeKey, myBmobUserLiveData);
    }

    public MutableLiveData<WrapperBean<MyBmobUser>> getMyBmobUserBean() {
        if (null == myBmobUserLiveData) {
            myBmobUserLiveData = new MutableLiveData<>();
        }

        return myBmobUserLiveData;
    }


}
