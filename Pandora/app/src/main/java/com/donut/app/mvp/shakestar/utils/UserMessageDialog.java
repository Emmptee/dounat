package com.donut.app.mvp.shakestar.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bis.android.plug.autolayout.AutoLinearLayout;
import com.donut.app.R;

/**
 * Created by hard on 2018/3/2.
 */

public class UserMessageDialog {

    //点击确认按钮回调接口
    public interface OnConfirmListener {
        void onConfirmClick();
    }
    /** * @Title: show * @Description: 显示Dialog * @param activity * @param content * 提示内容 * @param confirmListener * void * @throws */
    public static void show(Activity activity, String content, final OnConfirmListener confirmListener) {
        // 加载布局文件
        View view = View.inflate(activity, R.layout.dialog_message, null);
         TextView text = (TextView) view.findViewById(R.id.dialog_content);
        Button confirm =view.findViewById(R.id.dialog_btn);
         if (!UserMessageDialog.isNullOrEmpty(content)) {
             text.setText(content);
         }

        // 创建Dialog
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setCancelable(false);
        // 设置点击dialog以外区域不取消Dialog
        dialog.show();
        dialog.setContentView(view);
        //设置背景透明
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(UserMessageDialog.getWidth(activity)-100,UserMessageDialog.getHidth(activity) / 5 * 3);
        //设置弹出框高度为屏幕高度的三分之二
        // 确定
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
//         // 取消
//         cancel.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 dialog.dismiss();
//             }
//         });
    }
    public static boolean isNullOrEmpty(String string) {
        boolean flag = false;
        if (null == string || string.trim().length() == 0) {
            flag = true;
        }
        return flag;
    }

    public static int getWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        return width;
    }
    public static int getHidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        return height;
    }

}



