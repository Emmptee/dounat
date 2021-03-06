package com.donut.app.customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.donut.app.R;

/**
 * 自定义loading框
 *
 */
public class LoadingDialog extends Dialog
{

    private ImageView progress_imageview;

    private TextView loging_view;

    private Context context;

    private AnimationDrawable frameAnim;

    public LoadingDialog(Context context)
    {
        super(context, R.style.AppTheme);
        this.context = context;
        setContentView(R.layout.loding_layout);
        loging_view = (TextView) findViewById(R.id.message);
        progress_imageview = (ImageView) findViewById(R.id.progress_imageview);

        frameAnim = (AnimationDrawable) progress_imageview.getDrawable();
    }

    public LoadingDialog(Context context, String message)
    {
        super(context, R.style.Theme_dialog);
        this.context = context;
        setContentView(R.layout.loding_layout);
        loging_view = (TextView) findViewById(R.id.message);
        if (message != null && "".equals(message.trim()))
        {
            loging_view.setVisibility(View.VISIBLE);
            loging_view.setText(message);
        }
        progress_imageview = (ImageView) findViewById(R.id.progress_imageview);

    }

    @Override
    public void show()
    {
        try
        {
            super.show();
//            progress_imageview.startAnimation(AnimationUtils.loadAnimation(
//                    context, R.anim.roate));
            if (frameAnim != null && !frameAnim.isRunning()) {
                frameAnim.start();
            }

        }
        catch(Exception e)
        {
e.printStackTrace();
        }
    }

    @Override
    public void dismiss()
    {
        if (frameAnim != null && frameAnim.isRunning()) {
            frameAnim.stop();
        }
        super.dismiss();
    }
}
