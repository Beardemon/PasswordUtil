package com.yf_licz.passwordutil;

/**
 * Created by yfzx-sh-licz on 2018/1/22.
 */

public class GetStringSalt {


    public static native  String getSaltFromC();


    static {
        System.loadLibrary("safeSaltLib");
    }


}
