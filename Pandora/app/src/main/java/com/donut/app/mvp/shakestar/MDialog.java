package com.donut.app.mvp.shakestar;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.bis.android.plug.autolayout.AutoLinearLayout;
import com.donut.app.R;
import com.donut.app.model.video.DensityUtil;

/**
 * Created by hard on 2018/3/2.
 */

public class MDialog {

    //点击确认按钮回调接口
 public interface OnConfirmListener {
      void onConfirmClick();
  }
     /** * @Title: show * @Description: 显示Dialog * @param activity * @param content * 提示内容 * @param confirmListener * void * @throws */
     public static void show(Activity activity, String content, final OnConfirmListener confirmListener) {
         // 加载布局文件
         View view = View.inflate(activity, R.layout.dalog_layout, null);
         TextView text = (TextView) view.findViewById(R.id.text);
         TextView confirm = (TextView) view.findViewById(R.id.confirm);
         TextView cancel = (TextView) view.findViewById(R.id.cancel);
         if (!MDialog.isNullOrEmpty(content)) {
             text.setText(content);
         }

         // 创建Dialog
         final AlertDialog dialog = new AlertDialog.Builder(activity).create();
         dialog.setCancelable(false);
         // 设置点击dialog以外区域不取消Dialog
         dialog.show();
         dialog.setContentView(view);
         dialog.getWindow().setLayout(MDialog.getWidth(activity) / 3 * 2, AutoLinearLayout.LayoutParams.WRAP_CONTENT);
         //设置弹出框宽度为屏幕宽度的三分之二
         // 确定
         confirm.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dialog.dismiss();
                 confirmListener.onConfirmClick();
             }
         });
         // 取消
         cancel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dialog.dismiss();
             }
         });
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

}



