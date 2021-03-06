package com.donut.app.mvp.mine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

    private Fragment mFragment;

    private FetchImageUtilsCrop mImageUtil;

    private FetchImageUtilsCrop.OnPickFinishedCallback mFinishedCallback;

    public ShowChoosePhotoPopupWindow(Fragment fragment, final OnShowViewListener listener) {
        this.mFragment = fragment;
        mImageUtil = new FetchImageUtilsCrop(fragment);
        mFinishedCallback = new FetchImageUtilsCrop.OnPickFinishedCallback() {
            @Override
            public void onPickSuccessed(Bitmap bm, String filePath) {
                listener.chooseSuccess(bm, filePath);
            }

            @Override
            public void onPickFailed() {
            }
        };
    }

    public void show(View view) {
        showWindow = new SelectPicPopupWindow(mFragment.getContext(), mListener);
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
                    mImageUtil.doTakePhoto(mFinishedCallback);
                    break;
                case R.id.btn_click_three:
                    mImageUtil.doPickPhotoFromGallery(mFinishedCallback);
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
        void chooseSuccess(Bitmap bm, String path);
    }
}
