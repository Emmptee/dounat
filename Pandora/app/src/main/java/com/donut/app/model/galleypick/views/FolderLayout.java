package com.donut.app.model.galleypick.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class FolderLayout extends FrameLayout {
    private final float scale = Resources.getSystem().getDisplayMetrics().density;
    private final int dividerHeight = (int) (1 * scale + 0.5f);
    private Paint paint;

    public FolderLayout(Context context) {
        this(context, null, 0);
    }

    public FolderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FolderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        paint = new Paint();
        paint.setStrokeWidth(dividerHeight);
        paint.setAntiAlias(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(0x42000000);
        int margin = dividerHeight * 2;
        canvas.drawLine(getWidth() - dividerHeight, margin, getWidth() - dividerHeight, getHeight() - dividerHeight, paint);
        canvas.drawLine(margin, getHeight() - dividerHeight, getWidth() - dividerHeight, getHeight() - dividerHeight, paint);

        margin = dividerHeight * 3;
        paint.setColor(0x8A000000);
        canvas.drawLine(getWidth() - margin, dividerHeight, getWidth() - margin, getHeight() - margin, paint);
        canvas.drawLine(dividerHeight, getHeight() - margin, getWidth() - margin, getHeight() - margin, paint);
    }
}
