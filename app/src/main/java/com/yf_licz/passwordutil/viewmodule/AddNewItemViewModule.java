package com.yf_licz.passwordutil.viewmodule;

import android.arch.lifecycle.ViewModel;

import com.yf_licz.passwordutil.bean.MyBmobUser;
import com.yf_licz.passwordutil.bean.WrapperBean;
import com.yf_licz.passwordutil.bean.UserKeyBean;
import com.yf_licz.passwordutil.module.DataRepository;
import com.yf_licz.passwordutil.module.database.DataBaseManager;
import com.yf_licz.passwordutil.livedata.UserKeyLiveData;
import com.yf_licz.passwordutil.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * @author yfzx-sh-licz
 * @date 2017/11/27
 */

public class AddNewItemViewModule extends ViewModel {
    private DataRepository repository = DataRepository.getInstance();
    private DataBaseManager dataBaseManager = DataBaseManager.getInstance();
    private UserKeyLiveData<WrapperBean<List<UserKeyBean>>> userKeyLiveData = UserKeyLiveData.get();

    public void insertUserKey(String keyName, String key, String keyDes, String safeKey) {
        //数据保存入口（一切密码数据从此处保存）
        UserKeyBean userKeyBean = new UserKeyBean();

        try {
            userKeyBean.setKey(SecurityUtils.AES_Encode(key, safeKey));
            userKeyBean.setKeyName(SecurityUtils.AES_Encode(keyName, safeKey));
            userKeyBean.setKeyDes(SecurityUtils.AES_Encode(keyDes, safeKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataBaseManager.getDaoSession().getUserKeyBeanDao()
                .rx()
                .insert(userKeyBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserKeyBean>() {
                    @Override
                    public void call(UserKeyBean userKeyBean) {
                        List<UserKeyBean> userAllKeyList = userKeyLiveData.getValue().data == null ? new ArrayList<UserKeyBean>() : userKeyLiveData.getValue().data;
                        userAllKeyList.add(userKeyBean);
                        userKeyLiveData.setValue(WrapperBean.content(userAllKeyList));
                    }
                });


    }


    public String getSafeKeyMd5() {
        return BmobUser.getCurrentUser(MyBmobUser.class).getSafeKey();
    }
}
