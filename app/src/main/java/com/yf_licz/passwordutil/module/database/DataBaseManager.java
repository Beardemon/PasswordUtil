package com.yf_licz.passwordutil.module.database;

import android.database.sqlite.SQLiteDatabase;

import com.yf_licz.net.base.ApiService;
import com.yf_licz.net.base.NetApi;
import com.yf_licz.passwordutil.MyApplication;
import com.yf_licz.passwordutil.bean.DaoMaster;
import com.yf_licz.passwordutil.bean.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.greenrobot.greendao.test.DbTest.DB_NAME;

/**
 * Created by yfzx-sh-licz on 2017/11/28.
 */

public class DataBaseManager {
    private static final String TAG = "DataBaseManager";

    private static volatile DataBaseManager instance;
    private static volatile DaoMaster.DevOpenHelper helper;
    private DaoSession daoSession;


    public static DataBaseManager getInstance() {
        if (instance == null) {
            synchronized (NetApi.class) {
                if (instance == null) {
                    instance = new DataBaseManager();
                }
            }
        }
        return instance;
    }

    private DataBaseManager() {
        //创建数据库PasswordUtils.db"
        helper = new DaoMaster.DevOpenHelper(MyApplication.getContext(), "PasswordUtils.db", null);

        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    /**
     * 获取DaoSession
     *
     * @return
     */
    public DaoSession getDaoSession() {
        return daoSession;
    }

    /**
     * 打开输出日志的操作,默认是关闭的
     */
    public void setDebug() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    /**
     * 关闭所有的操作,数据库开启的时候，使用完毕了必须要关闭
     */
    public void closeConnection() {
        instance = null;
        if (helper != null) {
            helper.close();
            helper = null;
        }
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
    }




}
