package com.donut.app.mvp.shakestar.select;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by hard on 2018/2/9.
 */

public class ScrollInterceptScrollView extends ScrollView {
    private boolean isScrolledToTop;
    private boolean isScrolledToBottom;
    private IScrollChangedListener mScrollChangedListener;
    /**
     * 接口回调
     */
    private ScrollViewListener scrollViewListener = null;
    public interface ScrollViewListener {


    }
    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public ScrollInterceptScrollView(Context context) {
        super(context);
    }

    public ScrollInterceptScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollInterceptScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(IScrollChangedListener scrollViewListener) {
        this.mScrollChangedListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        // We take the last son in the scrollview
        View view = getChildAt(0);
        if (view == null)
            return;
        int diff = (view.getBottom() - (getHeight() + getScrollY()));

        isScrolledToBottom = false;
        isScrolledToTop = false;
        if (diff == 0) {
            isScrolledToBottom = true;
            if (mScrollChangedListener != null)
                mScrollChangedListener.onScrolledToBottom();
        } else if (getScrollY() == 0) {
            isScrolledToTop = true;
            if (mScrollChangedListener != null)
                mScrollChangedListener.onScrolledToTop();
        }else{
            if (mScrollChangedListener != null) {
                mScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
            }
        }

    }

    public boolean isTopEnd() {
        return isScrolledToTop;
    }

    public boolean isBottomEnd() {
        return isScrolledToBottom;
    }

    /**
     * 定义监听接口
     */
    public interface IScrollChangedListener {
        void onScrolledToBottom();
        void onScrolledToTop();
        void onScrollChanged(ScrollInterceptScrollView scrollView, int x, int y, int oldx, int oldy);
    }
}
