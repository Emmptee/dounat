package com.donut.app.mvp.shakestar.video;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.donut.app.R;
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer;


/**
 * Created by hard on 2018/3/6.
 */

public class DonutCameraVideoView extends NormalGSYVideoPlayer {

    public DonutCameraVideoView(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public DonutCameraVideoView(Context context) {
        super(context);
        this.mContext=context;
    }

    public DonutCameraVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void init(Context context) {
        super.init(context);
         mStartButton = (ImageView)findViewById(com.shuyu.gsyvideoplayer.R.id.start);
        updateStartImage();
        setAlphaTo0f(mLockScreen,mTopContainer,mBottomContainer);
        mThumbPlay=true;
    }
    @Override
    protected void updateStartImage() {
        if (mStartButton instanceof ImageView) {
            ImageView imageView = (ImageView) mStartButton;
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                imageView.setImageResource(R.drawable.start);
                 setAlphaTo0f(mStartButton);
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                imageView.setImageResource(R.drawable.start);
                imageView.setAlpha(1.0f);
            }else if(mCurrentState==CURRENT_STATE_PAUSE){
                imageView.setAlpha(1.0f);
            }else if(mCurrentState==CURRENT_STATE_AUTO_COMPLETE){
                imageView.setAlpha(1.0f);
                imageView.setImageResource(R.drawable.start);
            }else{
                imageView.setImageResource(R.drawable.start);
            }
//            if(!mHadPlay){
//                imageView.setAlpha(1.0f);
//            }
        }


    }
    protected void  setAlphaTo0f(View... vs){
        for (View view : vs) {
            view.setAlpha(0.0f);
        }
    }
}

