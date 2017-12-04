package com.yf_licz.passwordutil.utils;

import android.widget.Toast;

import com.yf_licz.passwordutil.MyApplication;


import java.util.Timer;
import java.util.TimerTask;

/**
 * description:showShortToastSafe是可以在自线程直接用的
 */
public class ToastUtils {

    private ToastUtils() {
    }


    public static void showShortToast(CharSequence msg) {
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showShortToast(int resId) {
        Toast.makeText(MyApplication.getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(CharSequence msg) {
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void showLongToast(int resId) {
        Toast.makeText(MyApplication.getContext(), resId, Toast.LENGTH_LONG).show();
    }


    public static void showToast(String msg, int duration) {
        final Timer timer = new Timer();
        final Toast toast = Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_LONG);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3500);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, duration);
    }


}
