package com.yf_licz.passwordutil.viewmodule;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.yf_licz.passwordutil.bean.MyBmobUser;
import com.yf_licz.passwordutil.bean.WrapperBean;
import com.yf_licz.passwordutil.module.DataRepository;
import com.yf_licz.passwordutil.module.database.DataBaseManager;
import com.yf_licz.passwordutil.utils.SharedPreferencesUtils;

/**
 *
 * @author yfzx-sh-licz
 * @date 2017/11/23
 */

public class LoginViewModule extends ViewModel {
    private DataRepository repository = DataRepository.getInstance();
    private MutableLiveData<WrapperBean<MyBmobUser>> myBmobUserLiveData;

    public void login(String name, String password) {
         repository.login(name, password, myBmobUserLiveData);
    }

    public MutableLiveData<WrapperBean<MyBmobUser>> getMyBmobUserBean() {
        if (null == myBmobUserLiveData) {
            myBmobUserLiveData = new MutableLiveData<>();
        }

        return myBmobUserLiveData;
    }

}
