package com.yf_licz.passwordutil.bean;

import cn.bmob.v3.exception.BmobException;

/**
 * @author yfzx-sh-licz
 * @date 2017/11/16
 */

public class WrapperBean<T> {
    public final Status status;
    public final T data;
    public final BmobException error;

    public WrapperBean(Status status, T data, BmobException error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> WrapperBean<T> content(T data) {
        return new WrapperBean<>(Status.Content, data, null);
    }

    public static <T> WrapperBean<T> error(T data, BmobException error) {
        return new WrapperBean<>(Status.Error, data, error);
    }
    public static <T> WrapperBean<T> error(BmobException error) {
        return error(null, error);
    }

    public static <T> WrapperBean<T> empty(T data) {
        return new WrapperBean<>(Status.Empty, data, null);
    }
    public static <T> WrapperBean<T> empty() {
        return empty(null);
    }

    public static <T> WrapperBean<T> loading(T data) {
        return new WrapperBean<>(Status.Loading, data, null);
    }
    public static <T> WrapperBean<T> loading() {
        return loading(null);
    }

}
