package com.donut.app.model.video;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.utils.L;
import com.donut.app.utils.NetUtils;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class VideoActivity extends BaseActivity implements OnClickListener
{

    @ViewInject(R.id.videoview)
    private FullScreenVideoView mVideo;
    @ViewInject(R.id.top_layout)
    private View mTopView;
    @ViewInject(R.id.bottom_layout)
    private View mBottomView;
    @ViewInject(R.id.seekbar)
    private SeekBar mSeekBar;
    @ViewInject(R.id.play_btn)
    private ImageView mPlay;
    @ViewInject(R.id.play_btn_linear)
    private LinearLayout mPlayLinear;
    @ViewInject(R.id.play_time)
    private TextView mPlayTime;
    @ViewInject(R.id.total_time)
    private TextView mDurationTime;
    @ViewInject(R.id.video_layout)
    private RelativeLayout mlayout;
    @ViewInject(R.id.progress_loading)
    private ProgressBar mLoading;
    @ViewInject(R.id.video_title)
    private TextView mTitle;

    // 音频管理器
    private AudioManager mAudioManager;

    // 屏幕宽高
    private float width;

    private float height;
    // 视频播放时间
    private int playTime;
    private String videoUrl, videoName;

    // 自动隐藏顶部和底部View的时间
    private static final int HIDE_TIME = 5000;

    // 声音调节Toast
    private VolumnController volumnController;

    // 原始屏幕亮度
    private int orginalLight;

    public static final String VIDEOURL = "VIDEOURL";

    public static final String VIDEONAME = "VIDEONAME";

    private boolean flag = false;

    int curPos;

    boolean isComplete = false;

    private long duration;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_main);
        ViewUtils.inject(this);
        videoUrl = getIntent().getStringExtra(VIDEOURL);
        videoName = getIntent().getStringExtra(VIDEONAME);
        volumnController = new VolumnController(this);
        if(videoName!=null){
            mTitle.setText("正在播放：" + videoName);
        }
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        width = DensityUtil.getWidthInPx(this);
        height = DensityUtil.getHeightInPx(this);
        threshold = DensityUtil.dip2px(this, 18);
        orginalLight = LightnessController.getLightness(this);
        mPlay.setOnClickListener(this);
        mPlayLinear.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);

        findViewById(R.id.back).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        if (!NetUtils.isNetworkConnected(this))
        {
            ToastUtil.showShort(this, getString(R.string.connect_no));
            return;
        }
        if (!NetUtils.isWifi(this))
        {
            Dialog dialog = new AlertDialog.Builder(VideoActivity.this)
                    .setMessage("您正在使用非wifi网络，播放将产生流量费用。")
                    .setNegativeButton(getString(R.string.cancel), null)
                    .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            playVideo();
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();

        } else
        {
            playVideo();
        }
        flag = true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (!flag)
        {
            mLoading.setVisibility(View.VISIBLE);
            mVideo.seekTo(curPos);
        }
    }

    private void playVideo()
    {
        if(videoUrl==null){
            return;
        }
        mLoading.setVisibility(View.VISIBLE);
        Uri uri = Uri.parse(videoUrl);
        mVideo.setVideoURI(uri);
        mVideo.requestFocus();
        mVideo.setOnPreparedListener(new OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                if(mp.getVideoWidth()>mp.getVideoHeight()){
                    //横屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }else {
                    //竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                }

                mVideo.setVideoWidth(mp.getVideoWidth());
                mVideo.setVideoHeight(mp.getVideoHeight());
                mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener()
                {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent)
                    {
                        // 获得当前播放时间和当前视频的长度
                        if (mVideo.getCurrentPosition() > 0)
                        {
                            mLoading.setVisibility(View.GONE);
                            mPlayTime.setText(formatLongToTimeStr(mVideo
                                    .getCurrentPosition()));
                            curPos = mVideo.getCurrentPosition();
                            int progress = mVideo.getCurrentPosition() * 100
                                    / mVideo.getDuration();
                            mSeekBar.setProgress(progress);
                            if (mVideo.getCurrentPosition() > mVideo
                                    .getDuration() - 100)
                            {
                                mPlayTime.setText("00:00:00");
                                mSeekBar.setProgress(0);
                            }
                            mSeekBar.setSecondaryProgress(percent);
                        } else
                        {
                            mPlayTime.setText("00:00:00");
                            mSeekBar.setProgress(0);
                        }
                    }
                });
                mVideo.start();
                mHandler.removeCallbacks(hideRunnable);
                mHandler.postDelayed(hideRunnable, HIDE_TIME);
                mDurationTime.setText(formatLongToTimeStr(mVideo.getDuration()));

                Timer timer = new Timer();
                    timer.schedule(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            mHandler.sendEmptyMessage(1);
                        }
                    }, 0, 1000);


                if (playTime != 0)
                {
                    mVideo.seekTo(playTime);
                }

            }
        });
        mVideo.setOnCompletionListener(new OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                isComplete = true;
                duration = mp.getCurrentPosition();
                mPlay.setImageResource(R.drawable.video_btn_down);
                mPlayTime.setText("00:00:00");
                mSeekBar.setProgress(0);
            }
        });
        /**
         * 注册一个错误监听
         */
        mVideo.setOnErrorListener(new MediaPlayer.OnErrorListener()
        {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra)
            {
                ToastUtil.showShort(VideoActivity.this, "播放错误" + what);
                switch (what)
                {
                    case -1004:
                        L.i("Streaming Media", "MEDIA_ERROR_IO");
                        break;
                    case -1007:
                        L.i("Streaming Media", "MEDIA_ERROR_MALFORMED");
                        break;
                    case 200:
                        L.i("Streaming Media",
                                "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK");
                        break;
                    case 100:
                        L.i("Streaming Media", "MEDIA_ERROR_SERVER_DIED");
                        break;
                    case -110:
                        L.i("Streaming Media", "MEDIA_ERROR_TIMED_OUT");
                        break;
                    case 1:
                        L.i("Streaming Media", "MEDIA_ERROR_UNKNOWN");
                        break;
                    case -1010:
                        L.i("Streaming Media", "MEDIA_ERROR_UNSUPPORTED");
                        break;
                }
                switch (extra)
                {
                    case 800:
                        L.i("Streaming Media", "MEDIA_INFO_BAD_INTERLEAVING");
                        break;
                    case 702:
                        L.i("Streaming Media", "MEDIA_INFO_BUFFERING_END");
                        break;
                    case 701:
                        L.i("Streaming Media", "MEDIA_INFO_METADATA_UPDATE");
                        break;
                    case 802:
                        L.i("Streaming Media", "MEDIA_INFO_METADATA_UPDATE");
                        break;
                    case 801:
                        L.i("Streaming Media", "MEDIA_INFO_NOT_SEEKABLE");
                        break;
                    case 1:
                        L.i("Streaming Media", "MEDIA_INFO_UNKNOWN");
                        break;
                    case 3:
                        L.i("Streaming Media", "MEDIA_INFO_VIDEO_RENDERING_START");
                        break;
                    case 700:
                        L.i("Streaming Media", "MEDIA_INFO_VIDEO_TRACK_LAGGING");
                        break;
                }

                return false;
            }
        });
        mVideo.setOnTouchListener(mTouchListener);
        mlayout.setOnTouchListener(mTouchListener);
    }

    private static String formatLongToTimeStr(int l)
    {
        int hour = 0;
        int minute = 0;
        int second = 0;
        DecimalFormat decimalFormat = new DecimalFormat("00");
        second = l / 1000;

        if (second > 60)
        {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60)
        {
            hour = minute / 60;
            minute = minute % 60;
        }
        return (decimalFormat.format(hour) + ":" + decimalFormat.format(minute)
                + ":" + decimalFormat.format(second));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            height = DensityUtil.getWidthInPx(this);
            width = DensityUtil.getHeightInPx(this);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            width = DensityUtil.getWidthInPx(this);
            height = DensityUtil.getHeightInPx(this);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        LightnessController.setLightness(this, orginalLight);
        flag = false;

    }

    private OnSeekBarChangeListener mSeekBarChangeListener = new OnSeekBarChangeListener()
    {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar)
        {
            if (mVideo.isPlaying())
            {
                mLoading.setVisibility(View.VISIBLE);
            }

            mHandler.postDelayed(hideRunnable, HIDE_TIME);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar)
        {
            mHandler.removeCallbacks(hideRunnable);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser)
        {
            if (fromUser)
            {
                int time = progress * mVideo.getDuration() / 100;
                playTime = time;
                mVideo.seekTo(time);
                mPlayTime.setText(formatLongToTimeStr(mVideo
                        .getCurrentPosition()));
            }
        }
    };

    private void backward(float delataX)
    {
        int current = mVideo.getCurrentPosition();
        int backwardTime = (int) (delataX / width * mVideo.getDuration());
        int currentTime = current - backwardTime;
        mVideo.seekTo(currentTime);
        mSeekBar.setProgress(currentTime * 100 / mVideo.getDuration());
        mPlayTime.setText(formatLongToTimeStr(currentTime));
    }

    private void forward(float delataX)
    {
        int current = mVideo.getCurrentPosition();
        int forwardTime = (int) (delataX / width * mVideo.getDuration());
        int currentTime = current + forwardTime;
        mVideo.seekTo(currentTime);
        mSeekBar.setProgress(currentTime * 100 / mVideo.getDuration());
        mPlayTime.setText(formatLongToTimeStr(currentTime));
    }

    private void volumeDown(float delatY)
    {
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int down = (int) (delatY / height * max * 3);
        int volume = Math.max(current - down, 0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        int transformatVolume = volume * 100 / max;
        // volumnController.show(transformatVolume);
    }

    private void volumeUp(float delatY)
    {
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int up = (int) ((delatY / height) * max * 3);
        int volume = Math.min(current + up, max);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        int transformatVolume = volume * 100 / max;
        // volumnController.show(transformatVolume);
    }

    private void lightDown(float delatY)
    {
        int down = (int) ((delatY / height) * 255 * 3);
        int transformatLight = LightnessController.getLightness(this) - down;
        LightnessController.setLightness(this, transformatLight);
    }

    private void lightUp(float delatY)
    {
        int up = (int) ((delatY / height) * 255 * 3);
        int transformatLight = LightnessController.getLightness(this) + up;
        if (transformatLight <= 255)
        {
            LightnessController.setLightness(this, transformatLight);
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mHandler.removeMessages(0);
        mHandler.removeCallbacksAndMessages(null);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    if (mVideo.getCurrentPosition() > 0)
                    {
                        // dissDialog();
                        mLoading.setVisibility(View.GONE);
                        if (!isComplete)
                        {
                            duration = mVideo.getDuration();
                        }
                        mPlayTime.setText(formatLongToTimeStr(mVideo
                                .getCurrentPosition()));
                        int progress = mVideo.getCurrentPosition() * 100
                                / mVideo.getDuration();
                        playTime = mVideo.getCurrentPosition();
                        mSeekBar.setProgress(progress);
                        if (mVideo.getCurrentPosition() > duration - 100)
                        {
                            mPlayTime.setText("00:00:00");
                            mSeekBar.setProgress(0);
                        }
                        mSeekBar.setSecondaryProgress(mVideo.getBufferPercentage());
                    } else
                    {
                        mPlayTime.setText("00:00:00");
                        mSeekBar.setProgress(0);
                    }

                    break;
                case 2:
                    showOrHide();
                    break;

                default:
                    break;
            }
        }
    };

    private Runnable hideRunnable = new Runnable()
    {

        @Override
        public void run()
        {
            showOrHide();
        }
    };

    @SuppressLint("SimpleDateFormat")
    private String formatLongToTimeStr(long time)
    {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }

    private float mLastMotionX;

    private float mLastMotionY;

    private int startX;

    private int startY;

    private int threshold;

    private boolean isClick = true;

    private OnTouchListener mTouchListener = new OnTouchListener()
    {

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            final float x = event.getX();
            final float y = event.getY();

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    mLastMotionX = x;
                    mLastMotionY = y;
                    startX = (int) x;
                    startY = (int) y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = x - mLastMotionX;
                    float deltaY = y - mLastMotionY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    // 声音调节标识
                    boolean isAdjustAudio = false;
                    if (absDeltaX > threshold && absDeltaY > threshold)
                    {
                        if (absDeltaX < absDeltaY)
                        {
                            isAdjustAudio = true;
                        } else
                        {
                            isAdjustAudio = false;
                        }
                    } else if (absDeltaX < threshold && absDeltaY > threshold)
                    {
                        isAdjustAudio = true;
                    } else if (absDeltaX > threshold && absDeltaY < threshold)
                    {
                        isAdjustAudio = false;
                    } else
                    {
                        return true;
                    }
                    if (isAdjustAudio)
                    {
                        if (x < width / 2)
                        {
                            if (deltaY > 0)
                            {
                                lightDown(absDeltaY);
                            } else if (deltaY < 0)
                            {
                                lightUp(absDeltaY);
                            }
                        } else
                        {
                            if (deltaY > 0)
                            {
                                volumeDown(absDeltaY);
                            } else if (deltaY < 0)
                            {
                                volumeUp(absDeltaY);
                            }
                        }

                    } else
                    {
                        if (deltaX > 0)
                        {
                            forward(absDeltaX);
                        } else if (deltaX < 0)
                        {
                            backward(absDeltaX);
                        }
                    }
                    mLastMotionX = x;
                    mLastMotionY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(x - startX) > threshold
                            || Math.abs(y - startY) > threshold)
                    {
                        isClick = false;
                    }
                    mLastMotionX = 0;
                    mLastMotionY = 0;
                    startX = (int) 0;
                    if (isClick)
                    {
                        showOrHide();
                    }
                    isClick = true;
                    break;

                default:
                    break;
            }
            return true;
        }

    };

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.play_btn:
                if (mVideo.isPlaying())
                {
                    mVideo.pause();
                    mPlay.setImageResource(R.drawable.video_btn_down);
                } else
                {
                    mVideo.start();
                    mPlay.setImageResource(R.drawable.video_btn_on);
                }
                break;
            default:
                break;
        }
    }

    private void showOrHide()
    {
        if (mTopView.getVisibility() == View.VISIBLE)
        {
            mTopView.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.option_leave_from_top);
            animation.setAnimationListener(new AnimationImp()
            {
                @Override
                public void onAnimationEnd(Animation animation)
                {
                    super.onAnimationEnd(animation);
                    mTopView.setVisibility(View.GONE);
                }
            });
            mTopView.startAnimation(animation);

            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this,
                    R.anim.option_leave_from_bottom);
            animation1.setAnimationListener(new AnimationImp()
            {
                @Override
                public void onAnimationEnd(Animation animation)
                {
                    super.onAnimationEnd(animation);
                    mBottomView.setVisibility(View.GONE);
                }
            });
            mBottomView.startAnimation(animation1);
        } else
        {
            mTopView.setVisibility(View.VISIBLE);
            mTopView.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.option_entry_from_top);
            mTopView.startAnimation(animation);

            mBottomView.setVisibility(View.VISIBLE);
            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this,
                    R.anim.option_entry_from_bottom);
            mBottomView.startAnimation(animation1);
            mHandler.removeCallbacks(hideRunnable);
            mHandler.postDelayed(hideRunnable, HIDE_TIME);
        }
    }

    private class AnimationImp implements AnimationListener
    {

        @Override
        public void onAnimationEnd(Animation animation)
        {

        }

        @Override
        public void onAnimationRepeat(Animation animation)
        {
        }

        @Override
        public void onAnimationStart(Animation animation)
        {
        }

    }

}
