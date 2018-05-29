package com.donut.app.mvp.shakestar.video.record;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.donut.app.R;
import com.donut.app.databinding.ActivityRecordBinding;
import com.donut.app.http.message.shakestar.CommendAllResponse;
import com.donut.app.http.message.shakestar.ParticularsResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsContract;
import com.donut.app.mvp.shakestar.select.particulars.ParticularsPresenter;
import com.donut.app.mvp.shakestar.video.camera.JCameraView;
import com.donut.app.mvp.shakestar.video.camera.listener.ErrorListener;
import com.donut.app.mvp.shakestar.video.camera.listener.JCameraListener;
import com.donut.app.mvp.shakestar.video.camera.util.FileUtil;
import com.socks.library.KLog;

import java.util.List;


public class RecordActivity extends MVPBaseActivity<ActivityRecordBinding,ParticularsPresenter> implements ParticularsContract.View{
    @Override
    protected int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    protected void initView() {
        //此界面只是引入自定义view，具体的监听由view来处理
    }

    @Override
    protected void loadData() {

    }


    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
//        if (Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        } else {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(option);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        jCameraView.onPause();
    }

    @Override
    public void showView(List<ParticularsResponse.ShakingStarListBean> starCommendResponses, ParticularsResponse particularsResponse) {

    }

    @Override
    public void showCommend(List<CommendAllResponse.CommentsListBean> commendAllResponse, CommendAllResponse Response) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
