package com.donut.app.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.donut.app.R;

/**
 * WishingRadioButton
 * Created by Qi on 2017/2/23.
 */

public class WishingRadioButton extends FrameLayout implements View.OnClickListener {

    private boolean checked;

//    private String mTitleText, mContentText;

    private TextView titleTv, contentTv;

    public WishingRadioButton(Context context) {
        this(context, null);
    }

    public WishingRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WishingRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.wishing_radio_button_layout, this, true);
        titleTv = (TextView) findViewById(R.id.wishing_radio_button_title);
        contentTv = (TextView) findViewById(R.id.wishing_radio_button_content);
        setOnClickListener(this);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.WishingRadioButton, defStyleAttr, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.WishingRadioButton_titleText:
                    String mTitleText = a.getString(attr);
                    titleTv.setText(mTitleText);
                    break;
                case R.styleable.WishingRadioButton_contentText:
                    String mContentText = a.getString(attr);
                    contentTv.setText(mContentText);
                    break;
                case R.styleable.WishingRadioButton_cchecked:
                    setChecked(a.getBoolean(attr, false));
                    break;
            }
        }
        a.recycle();
    }

    @Override
    public void onClick(View v) {
        if (!checked) {
            setChecked(true);
        }
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        this.setSelected(checked);
        titleTv.setSelected(checked);
        contentTv.setSelected(checked);
    }

    public boolean isChecked() {
        return checked;
    }
}
