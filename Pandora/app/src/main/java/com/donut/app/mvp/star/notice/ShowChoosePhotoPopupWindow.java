package com.donut.app.mvp.star.notice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;

import com.donut.app.R;
import com.donut.app.customview.SelectPicPopupWindow;
import com.donut.app.utils.FetchImageUtilsCrop;

/**
 * Created by Qi on 2017/3/22.
 * Description : <br>
 */
public class ShowChoosePhotoPopupWindow {

    private Activity mActivity;

    private FetchImageUtilsCrop mImageUtil;

    private FetchImageUtilsCrop.OnPickFinishedCallback mFinishedCallback;

    public ShowChoosePhotoPopupWindow(Activity activity, final OnShowViewListener listener) {
        this.mActivity = activity;
        mImageUtil = new FetchImageUtilsCrop(activity);
        mFinishedCallback = new FetchImageUtilsCrop.OnPickFinishedCallback() {
            @Override
            public void onPickSuccessed(Bitmap bm, String filePath) {
                listener.choosePhotoSuccess(bm, filePath);
            }

            @Override
            public void onPickFailed() {
            }
        };
    }

    public void show(View view) {
        showWindow = new SelectPicPopupWindow(mActivity, mListener);
        showWindow.showAtLocation(view,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private SelectPicPopupWindow showWindow;
    private SelectPicPopupWindow.OnClickListenerWithPosition mListener
            = new SelectPicPopupWindow.OnClickListenerWithPosition() {
        @Override
        public void onClick(View v, int actionId) {
            showWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_click_one:
                    mImageUtil.doTakePhoto(mFinishedCallback, 650, 620);
                    break;
                case R.id.btn_click_three:
                    mImageUtil.doPickPhotoFromGallery(mFinishedCallback, 650, 620);
                    break;
            }
        }

        private void empty() {
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mImageUtil.onActivityResult(requestCode, resultCode, data);
    }


    interface OnShowViewListener {
        void choosePhotoSuccess(Bitmap bm, String path);
    }
}
