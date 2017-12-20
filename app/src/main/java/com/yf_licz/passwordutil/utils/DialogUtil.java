package com.yf_licz.passwordutil.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.yf_licz.passwordutil.MyApplication;
import com.yf_licz.passwordutil.R;

/**
 * Created by yfzx-sh-licz on 2017/12/20.
 */

public class DialogUtil {
    private static final String TAG = "DialogUtil";
    private static volatile DialogUtil instance;
    private Context mContext;
    private AlertDialog.Builder builder;

    public static DialogUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (DialogUtil.class) {
                if (instance == null) {
                    instance = new DialogUtil(context);
                }
            }

        }
        return instance;
    }

    private DialogUtil(Context context) {
        mContext = context;
        builder = new AlertDialog.Builder(context);

    }

    public void showDialog(String titleText, String message, String leftButtonText, String rightButtonText, DialogInterface.OnClickListener leftClick, DialogInterface.OnClickListener rightClick) {

        builder.setTitle(titleText == null ? "" : titleText);
        builder.setMessage(message);
        if (leftButtonText != null) {
            builder.setPositiveButton(leftButtonText, leftClick);
        }
        if (rightButtonText != null) {
            builder.setNegativeButton(rightButtonText, rightClick);
        }
        builder.show();

    }

    public void showCommonDialog(String message, DialogInterface.OnClickListener leftClick) {

        builder.setTitle(MyApplication.getContext().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setPositiveButton("确定", leftClick);

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }
}
