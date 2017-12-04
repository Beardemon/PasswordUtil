package com.yf_licz.passwordutil.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import org.greenrobot.greendao.annotation.Generated;

/**
 * @author yfzx-sh-licz
 * @date 2017/11/27
 */
@Entity
public class UserKeyBean  {
 
    private String keyName;
    private String key;
    private String keyDes;
    @Generated(hash = 959681965)
    public UserKeyBean(String keyName, String key, String keyDes) {
        this.keyName = keyName;
        this.key = key;
        this.keyDes = keyDes;
    }
    @Generated(hash = 1362667873)
    public UserKeyBean() {
    }
    public String getKeyName() {
        return this.keyName;
    }
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getKeyDes() {
        return this.keyDes;
    }
    public void setKeyDes(String keyDes) {
        this.keyDes = keyDes;
    }

    @Override
    public String toString() {
        return "UserKeyBean{" +
                "keyName='" + keyName + '\'' +
                ", key='" + key + '\'' +
                ", keyDes='" + keyDes + '\'' +
                '}';
    }
}
