package com.donut.app.mvp.shakestar.video.record;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.donut.app.R;
import com.donut.app.mvp.shakestar.DonutVideoView;
import com.donut.app.mvp.shakestar.video.camera.util.FileUtil;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;

import java.io.File;

public class RecordTestActivity extends Activity {

    private DonutVideoView videoview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordtestac);
        videoview = findViewById(R.id.testvideo);
        videoview.setUp(FileUtil.choseSavePath() + File.separator + "dest.mp4",false,null);
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);

    }
}
