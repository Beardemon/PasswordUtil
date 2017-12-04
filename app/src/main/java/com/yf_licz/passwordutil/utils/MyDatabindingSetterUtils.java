package com.yf_licz.passwordutil.utils;

import android.databinding.BindingAdapter;
import android.widget.TextView;

/**
 *
 * @author yfzx-sh-licz
 * @date 2017/11/30
 */

public class MyDatabindingSetterUtils {
    /*bind:safe_show,safe_show为相应属性名，可以不加bind加android，用来修改的是自有属性
    * BindingAdapter内参数和方法的属性位置对应
    * */
    @BindingAdapter("display_type")
    public static void changeDisplayType(TextView textView, String safekey) {

        try {
            //截取冒号前面一段
            String headText = textView.getText().toString().substring(0, textView.getText().toString().indexOf(":") + 1);
            String keyText = textView.getText().toString().replace(headText, "");
            //safekey为""的时候该setter不做处理
            if (safekey == null && "".equals(safekey)) {
                //显示密文
                textView.setText(headText + SecurityUtils.AES_Encode(keyText, safekey));
            } else {
                //显示明文
                textView.setText(headText + SecurityUtils.AES_Decode(keyText, safekey));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
