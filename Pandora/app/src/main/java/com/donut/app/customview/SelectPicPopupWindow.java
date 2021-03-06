package com.donut.app.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.donut.app.R;


/**
 * Created by Qi on 2016/2/23.
 */
public class SelectPicPopupWindow extends PopupWindow {

    public interface OnClickListenerWithPosition {
        void onClick(View v, int actionId);
    }

    private OnClickListenerWithPosition listener;
    private int DEFAULT_ACTION_ID = 0;
    private Button btn_click_one, btn_click_two, btn_click_three, btn_cancel;
    private View mMenuView;
    private int actionId = DEFAULT_ACTION_ID;

    public SelectPicPopupWindow(Context context, OnClickListenerWithPosition itemsOnClick) {
        super(context);
        showView(context, itemsOnClick, DEFAULT_ACTION_ID);
    }

    public SelectPicPopupWindow(Context context,
                                OnClickListenerWithPosition itemsOnClick, int actionId) {
        super(context);
        showView(context, itemsOnClick, actionId);
    }

    public SelectPicPopupWindow(Context context, OnClickListenerWithPosition itemsOnClick,
                                String... BtnNames) {
        super(context);
        showView(context, itemsOnClick, DEFAULT_ACTION_ID);
        if (BtnNames.length == 1) {
            btn_click_one.setVisibility(View.GONE);
            btn_click_three.setText(BtnNames[0]);
        } else {
            btn_click_one.setText(BtnNames[0]);
            btn_click_three.setText(BtnNames[1]);
            if (BtnNames.length > 2) {
                btn_click_two.setVisibility(View.VISIBLE);
                btn_click_two.setText(BtnNames[2]);
            }
        }
    }

    public SelectPicPopupWindow(Context context, OnClickListenerWithPosition itemsOnClick,
                                int actionId, String FirBtnName, String SecBtnName) {
        super(context);
        showView(context, itemsOnClick, actionId);
        btn_click_one.setText(FirBtnName);
        btn_click_three.setText(SecBtnName);
    }

    private void showView(Context context, final OnClickListenerWithPosition itemsOnClick,
                          final int actionId) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_pic_poputwindow, null);
        btn_click_one = (Button) mMenuView.findViewById(R.id.btn_click_one);
        btn_click_two = (Button) mMenuView.findViewById(R.id.btn_click_two);
        btn_click_three = (Button) mMenuView.findViewById(R.id.btn_click_three);
        btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        //取消按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
        btn_click_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsOnClick.onClick(v, actionId);
            }
        });
        btn_click_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsOnClick.onClick(v, actionId);
            }
        });
        btn_click_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsOnClick.onClick(v, actionId);
            }
        });
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
