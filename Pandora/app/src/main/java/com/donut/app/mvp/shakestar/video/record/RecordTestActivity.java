package com.donut.app.mvp.shakestar.video.record;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.donut.app.R;
import com.donut.app.mvp.shakestar.DonutVideoView;
import com.donut.app.mvp.shakestar.video.camera.util.FileUtil;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;

import java.io.File;

public class RecordTestActivity extends Activity {

    private DonutVideoView videoview;
    public static int REQUEST_FOR_PREVIEW_FINISH = 101;
    private Button btnTest;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordtestac);
        videoview = findViewById(R.id.testvideo);
        btnTest = findViewById(R.id.btn_test);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
            }
        });
        videoview.setUp(FileUtil.choseSavePath() + File.separator + "dest.mp4",false,null);
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);

    }


    public void setResultFinish(){
        Intent intent = new Intent();
        setResult(REQUEST_FOR_PREVIEW_FINISH,intent);
        finish();
    }
}
