package com.yf_licz.passwordutil.module;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.yf_licz.passwordutil.bean.MyBmobUser;
import com.yf_licz.passwordutil.bean.MyBmobUserKey;
import com.yf_licz.passwordutil.bean.UserKeyBean;
import com.yf_licz.passwordutil.bean.WrapperBean;
import com.yf_licz.passwordutil.livedata.UserKeyLiveData;
import com.yf_licz.passwordutil.module.database.DataBaseManager;
import com.yf_licz.passwordutil.utils.SecurityUtils;
import com.yf_licz.passwordutil.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.R.attr.data;
import static android.R.id.list;


/**
 * @author yfzx-sh-licz
 * @date 2017/11/17
 */

public class DataRepository {


    private static volatile DataRepository instance;
    private DataBaseManager dataBaseManager;


    public static DataRepository getInstance() {
        if (instance == null) {
            synchronized (DataRepository.class) {
                if (instance == null) {
                    instance = new DataRepository();
                }
            }
        }
        return instance;
    }

    private DataRepository() {
        //初始化
        dataBaseManager = DataBaseManager.getInstance();

    }

    /**
     * 注册
     *
     * @param name
     * @param password
     * @param safeKey
     * @param data
     */
    public void register(final String name, final String password, final String safeKey, final MutableLiveData<WrapperBean<MyBmobUser>> data) {
        //请求bmob进行用户注册
        data.setValue(WrapperBean.<MyBmobUser>loading());
        MyBmobUser myBmobUser = new MyBmobUser();
        myBmobUser.setUsername(name);
        myBmobUser.setPassword(password);
        myBmobUser.setSafeKey(SecurityUtils.MD5_Encode(safeKey));
        data.setValue(WrapperBean.<MyBmobUser>loading());
        myBmobUser.signUp(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (null == e) {
                    MyBmobUser user = BmobUser.getCurrentUser(MyBmobUser.class);
                    data.setValue(WrapperBean.content(user));
                } else {
                    data.setValue(WrapperBean.<MyBmobUser>error(e));
                }

            }
        });

    }

    /**
     * 登录
     *
     * @param name
     * @param password
     * @param data
     */
    public void login(final String name, final String password, final MutableLiveData<WrapperBean<MyBmobUser>> data) {
        //请求bmob进行用户登陆
        data.setValue(WrapperBean.<MyBmobUser>loading());
        BmobUser.loginByAccount(name, password, new LogInListener<MyBmobUser>() {
            @Override
            public void done(MyBmobUser user, BmobException e) {
                if (user != null) {
                    data.setValue(WrapperBean.content(user));
                } else {
                    data.setValue(WrapperBean.<MyBmobUser>error(e));
                }

            }
        });

    }

    /**
     * 从服务器获得userkey，并且存到本地数据库
     *
     * @param data
     */
    public void getUserKeyData(final UserKeyLiveData<WrapperBean<List<UserKeyBean>>> data) {
        data.setValue(WrapperBean.<List<UserKeyBean>>loading());
        //根据username请求bmob进行数据查询
        BmobQuery<MyBmobUserKey> myBmobUserKeyQuery = new BmobQuery<MyBmobUserKey>();
        myBmobUserKeyQuery.addWhereEqualTo("userName", BmobUser.getCurrentUser(MyBmobUser.class).getUsername());
        myBmobUserKeyQuery.setLimit(1);
        myBmobUserKeyQuery.findObjects(new FindListener<MyBmobUserKey>() {
            @Override
            public void done(List<MyBmobUserKey> list, BmobException e) {
                if (e == null) {
                    //只要没有异常从网络上拉下数据后先删除本地数据库
                    dataBaseManager.getDaoSession().deleteAll(UserKeyBean.class);
                    if (list != null && list.size() != 0 && list.get(0).getUserKeyBeanList() != null && list.get(0).getUserKeyBeanList().size() != 0) {
                        //服务器上不为空
                        data.setValue(WrapperBean.content(list.get(0).getUserKeyBeanList()));
                        //存储到本地数据库
                        for (int i = 0; i < list.get(0).getUserKeyBeanList().size(); i++) {
                            UserKeyBean userKeyBean = new UserKeyBean();
                            userKeyBean.setKey(list.get(0).getUserKeyBeanList().get(i).getKey());
                            userKeyBean.setKeyName(list.get(0).getUserKeyBeanList().get(i).getKeyName());
                            userKeyBean.setKeyDes(list.get(0).getUserKeyBeanList().get(i).getKeyDes());
                            dataBaseManager.getDaoSession().getUserKeyBeanDao()
                                    .rx()
                                    .insert(userKeyBean)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Action1<UserKeyBean>() {
                                        @Override
                                        public void call(UserKeyBean userKeyBean) {

                                        }
                                    });
                        }
                    } else {
                        //服务器上为空
                        data.setValue(WrapperBean.<List<UserKeyBean>>empty());

                    }

                } else {
                    data.setValue(WrapperBean.<List<UserKeyBean>>error(e));
                }
            }
        });


    }

    /**
     * 上传userkey
     *
     * @param data
     */
    public void uploadUserKeyData(final List<UserKeyBean> data) {
        // TODO: 2017/11/29  失败ui处理
        //请求bmob上传用户数据
        BmobQuery<MyBmobUserKey> myBmobUserKeyQuery = new BmobQuery<MyBmobUserKey>();
        myBmobUserKeyQuery.addWhereEqualTo("userName", BmobUser.getCurrentUser(MyBmobUser.class).getUsername());
        myBmobUserKeyQuery.setLimit(1);
        myBmobUserKeyQuery.findObjects(new FindListener<MyBmobUserKey>() {
            @Override
            public void done(List<MyBmobUserKey> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() != 0) {
                        //bmob更新数据
                        MyBmobUserKey myBmobUserKey = new MyBmobUserKey();
                        myBmobUserKey.setUserKeyBeanList(data);
                        myBmobUserKey.update(list.get(0).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    ToastUtils.showShortToast("更新数据成功");
                                } else {
                                    ToastUtils.showShortToast("更新数据失败" + e.toString());
                                    Log.d("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });

                    } else {
                        //bmob新建数据
                        MyBmobUserKey myBmobUserKey = new MyBmobUserKey();
                        myBmobUserKey.setUserName(BmobUser.getCurrentUser(MyBmobUser.class).getUsername());
                        myBmobUserKey.setUserKeyBeanList(data);
                        myBmobUserKey.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    ToastUtils.showShortToast("创建数据成功");
                                } else {
                                    ToastUtils.showShortToast("创建数据失败" + e.toString());
                                    Log.d("bmob", "创建失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });

                    }
                } else {
                    ToastUtils.showShortToast("查询数据失败" + e.toString());
                    Log.d("bmob", "查询失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


    }

    public void updateSafeKey(String md5Safekey) {
        //bmob更新数据
        MyBmobUser myBmobUser = BmobUser.getCurrentUser(MyBmobUser.class);
        myBmobUser.setSafeKey(md5Safekey);
        myBmobUser.update(myBmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.showShortToast("Safekey更新成功");
                } else {
                    ToastUtils.showShortToast("Safekey更新失败" + e.toString());
                    Log.d("bmob", "Safekey更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


}
