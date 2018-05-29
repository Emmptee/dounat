package com.donut.app.customview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Html;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatTextView;

/**
 * Created by Administrator on 2016/12/2.
 */

public class EllipsizingTextView extends AppCompatTextView {
    private static final String ELLIPSIS = "...更多";
    private static final String COLOR_MORE = "...<font color='#81D8D0'>更多</font>";

    public interface EllipsizeListener {
        void ellipsizeStateChanged(boolean ellipsized);
    }

    private final List<EllipsizeListener> ellipsizeListeners = new ArrayList<EllipsizeListener>();
    private boolean isEllipsized;
    private boolean isStale;
    private boolean programmaticChange;
    private String fullText;
    private int maxLines = -1;
    private float lineSpacingMultiplier = 1.0f;
    private float lineAdditionalVerticalPadding = 0.0f;

    public EllipsizingTextView(Context context) {
        super(context);
    }

    public EllipsizingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EllipsizingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addEllipsizeListener(EllipsizeListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        ellipsizeListeners.add(listener);
    }

    public void removeEllipsizeListener(EllipsizeListener listener) {
        ellipsizeListeners.remove(listener);
    }

    public boolean isEllipsized() {
        return isEllipsized;
    }

    @Override
    public void setMaxLines(int maxLines) {
        super.setMaxLines(maxLines);
        this.maxLines = maxLines;
        isStale = true;
    }

    @Override
    public int getMaxLines() {
        return maxLines;
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        this.lineAdditionalVerticalPadding = add;
        this.lineSpacingMultiplier = mult;
        super.setLineSpacing(add, mult);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        if (!programmaticChange) {
            fullText = text.toString();
            isStale = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isStale) {
            super.setEllipsize(null);
            resetText();
        }
        super.onDraw(canvas);
    }

    private void resetText() {
        int maxLines = getMaxLines();
        String workingText = fullText;
        boolean ellipsized = false;
        if (maxLines != -1) {
            Layout layout = createWorkingLayout(workingText);
            if (layout.getLineCount() > maxLines) {
                workingText = fullText.substring(0, layout.getLineEnd(maxLines - 1)).trim();
                while (createWorkingLayout(workingText + ELLIPSIS).getLineCount() > maxLines) {
                    //int lastSpace = workingText.lastIndexOf(' ');
                    //if (lastSpace == -1) {
                    //    break;
                    //}
                    //workingText = workingText.substring(0, lastSpace);
                    // 由于我们大多数情况下workingText为中文，所以按照之前的逻辑找空格是不合适的
                    // 这里改成直接替换最后的字符
                    workingText = workingText.substring(0, workingText.length() - 1);
                }
                workingText = workingText + COLOR_MORE;
                ellipsized = true;
            }
        }
        if (!workingText.equals(getText())) {
            programmaticChange = true;
            try {
                workingText = workingText.replace("\n","<br/>");
                setText(Html.fromHtml(workingText));
            } finally {
                programmaticChange = false;
            }
        }
        isStale = false;
        if (ellipsized != isEllipsized) {
            isEllipsized = ellipsized;
            for (EllipsizeListener listener : ellipsizeListeners) {
                listener.ellipsizeStateChanged(ellipsized);
            }
        }
    }

    private Layout createWorkingLayout(String workingText) {
        return new StaticLayout(workingText, getPaint(), getWidth() - getPaddingLeft() - getPaddingRight(),
                Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineAdditionalVerticalPadding, false);
    }

    @Override
    public void setEllipsize(TruncateAt where) {
        // Ellipsize settings are not respected
    }
}