package com.yf_licz.passwordutil.viewmodule;

import android.arch.lifecycle.ViewModel;

import com.yf_licz.passwordutil.bean.MyBmobUser;
import com.yf_licz.passwordutil.bean.WrapperBean;
import com.yf_licz.passwordutil.livedata.UserKeyLiveData;
import com.yf_licz.passwordutil.module.DataRepository;
import com.yf_licz.passwordutil.module.database.DataBaseManager;
import com.yf_licz.passwordutil.bean.UserKeyBean;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by yfzx-sh-licz on 2017/11/29.
 */

public class AppMainViewModule extends ViewModel {
    private DataRepository repository = DataRepository.getInstance();
    private DataBaseManager dataBaseManager = DataBaseManager.getInstance();
    private UserKeyLiveData<WrapperBean<List<UserKeyBean>>> userAllKeyLiveData = UserKeyLiveData.get();

    public UserKeyLiveData getUserAllKeyLiveData() {
        Query<UserKeyBean> userKeyBeanQuery = dataBaseManager.getDaoSession().getUserKeyBeanDao().queryBuilder().build();
        List<UserKeyBean> userKeyBeenList = userKeyBeanQuery.list();

        if (userKeyBeenList != null && userKeyBeenList.size() != 0) {
            //使用数据库
            userAllKeyLiveData.setValue(WrapperBean.content(userKeyBeenList));
        } else {
            //使用net
            repository.getUserKeyData(userAllKeyLiveData);
        }


        return userAllKeyLiveData;

    }

    public void uploadUserKeyDataLocal2Net() {

        repository.uploadUserKeyData(userAllKeyLiveData.getValue().data);


    }

    public void uploadUserKeyDataNet2Local() {
        repository.getUserKeyData(userAllKeyLiveData);
    }

    public String getSafeKeyMd5() {
        return BmobUser.getCurrentUser(MyBmobUser.class).getSafeKey();
    }

    public void delAllData() {
        //删除本地数据库
        dataBaseManager.getDaoSession().deleteAll(UserKeyBean.class);
        //删除网络数据库
        repository.uploadUserKeyData(new ArrayList<UserKeyBean>());
    }

    public void logOut() {
        //清空本地用户信息缓存数据
        BmobUser.logOut();
        //删除本地数据库
        dataBaseManager.getDaoSession().deleteAll(UserKeyBean.class);
    }
    public void updateSafeKey(String md5Safekey){

        repository.updateSafeKey(md5Safekey);

    }


}
