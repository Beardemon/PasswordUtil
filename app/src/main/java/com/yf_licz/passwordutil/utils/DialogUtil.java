package com.yf_licz.passwordutil.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.yf_licz.passwordutil.MyApplication;
import com.yf_licz.passwordutil.R;
import com.yf_licz.passwordutil.SetSafeKeyPopupWindowBinding;

/**
 * Created by yfzx-sh-licz on 2017/12/20.
 */

public class DialogUtil {
    private static final String TAG = "DialogUtil";


    public static void showDialog(Context context,String titleText, String message, String leftButtonText, String rightButtonText, DialogInterface.OnClickListener leftClick, DialogInterface.OnClickListener rightClick) {
        AlertDialog.Builder   builder = new AlertDialog.Builder(context);
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


    public static void showCommonDialog(Context context,String message, DialogInterface.OnClickListener leftClick) {
        AlertDialog.Builder   builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.app_name));
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
