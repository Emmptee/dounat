package com.donut.app.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.donut.app.R;


/**
 * Created by Qi
 * on 2017/6/15.
 */
public class SpinOffBoonsSelectTypePopupWindow extends PopupWindow implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.type_all:
                if (listener != null) {
                    listener.onItemClick(v, 0);
                }
                setBtnView(0);
                break;
            case R.id.type_audio:
                if (listener != null) {
                    listener.onItemClick(v, 1);
                }
                setBtnView(1);
                break;
            case R.id.type_pic:
                if (listener != null) {
                    listener.onItemClick(v, 2);
                }
                setBtnView(2);
                break;
        }
    }

    public interface OnClickListenerWithPosition {
        void onItemClick(View v, int selectType);
    }

    private OnClickListenerWithPosition listener;
    private View mMenuView;

    private TextView typeAll, typeAudio, typePic;

    public SpinOffBoonsSelectTypePopupWindow(Activity context, int selectType,
                                             OnClickListenerWithPosition itemsOnClick) {
        super(context);
        showView(context, itemsOnClick, selectType);
    }

    private void showView(Activity context, final OnClickListenerWithPosition itemsOnClick,
                          final int selectType) {
        listener = itemsOnClick;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.spin_off_bonns_select_type_poputwindow, null);
        typeAll = (TextView) mMenuView.findViewById(R.id.type_all);
        typeAudio = (TextView) mMenuView.findViewById(R.id.type_audio);
        typePic = (TextView) mMenuView.findViewById(R.id.type_pic);
        //设置按钮监听
        typeAll.setOnClickListener(this);
        typeAudio.setOnClickListener(this);
        typePic.setOnClickListener(this);

        //设置PopupWindow的View
        this.setContentView(mMenuView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x73000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getHeight();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        setBtnView(selectType);
    }

    private void setBtnView(int selectType) {
        typeAll.setSelected(false);
        typeAudio.setSelected(false);
        typePic.setSelected(false);

        switch (selectType) {
            case 1:
                typeAudio.setSelected(true);
                break;
            case 2:
                typePic.setSelected(true);
                break;
            default:
                typeAll.setSelected(true);
                break;
        }
    }
}
