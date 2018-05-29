package com.bis.android.plug.cameralibrary.materialcamera.internal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.bis.android.plug.cameralibrary.R;
import com.bis.android.plug.cameralibrary.materialcamera.MaterialCamera;
import com.bis.android.plug.cameralibrary.materialcamera.util.CameraUtil;
import com.bis.android.plug.cameralibrary.materialcamera.util.Degrees;

import java.io.File;

import static android.app.Activity.RESULT_CANCELED;
import static com.bis.android.plug.cameralibrary.materialcamera.internal.BaseCaptureActivity.CAMERA_POSITION_BACK;
import static com.bis.android.plug.cameralibrary.materialcamera.internal.BaseCaptureActivity.FLASH_MODE_ALWAYS_ON;
import static com.bis.android.plug.cameralibrary.materialcamera.internal.BaseCaptureActivity.FLASH_MODE_AUTO;
import static com.bis.android.plug.cameralibrary.materialcamera.internal.BaseCaptureActivity.FLASH_MODE_OFF;

/**
 * @author Aidan Follestad (afollestad)
 */
abstract class BaseCameraFragment extends Fragment implements CameraUriInterface, View.OnClickListener {

    protected ImageButton mButtonVideo;
    protected ImageButton mButtonStillshot;
    protected ImageButton mButtonFacing;
    protected ImageButton mButtonFlash;
    protected TextView mRecordDuration;
    protected TextView mDelayStartCountdown;

    private boolean mIsRecording;
    protected String mOutputUri;
    protected BaseCaptureInterface mInterface;
    protected Handler mPositionHandler;
    protected MediaRecorder mMediaRecorder;
    private int mIconTextColor;

    int faceFrontCameraOrientation;
    int faceBackCameraOrientation;

    private SensorManager sensorManager = null;

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            synchronized (this) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    if (sensorEvent.values[0] < 4 && sensorEvent.values[0] > -4) {
                        if (sensorEvent.values[1] > 0) {
                            // UP
                            mInterface.setSensorPosition(MaterialCamera.SENSOR_POSITION_UP);
                            mInterface.setDegrees(mInterface.getDeviceDefaultOrientation() == MaterialCamera.ORIENTATION_PORTRAIT ? 0 : 90);
                        } else if (sensorEvent.values[1] < 0) {
                            // UP SIDE DOWN
                            mInterface.setSensorPosition(MaterialCamera.SENSOR_POSITION_UP_SIDE_DOWN);
                            mInterface.setDegrees(mInterface.getDeviceDefaultOrientation() == MaterialCamera.ORIENTATION_PORTRAIT ? 180 : 270);
                        }
                    } else if (sensorEvent.values[1] < 4 && sensorEvent.values[1] > -4) {
                        if (sensorEvent.values[0] > 0) {
                            // LEFT
                            mInterface.setSensorPosition(MaterialCamera.SENSOR_POSITION_LEFT);
                            mInterface.setDegrees(mInterface.getDeviceDefaultOrientation() == MaterialCamera.ORIENTATION_PORTRAIT ? 90 : 180);
                        } else if (sensorEvent.values[0] < 0) {
                            // RIGHT
                            mInterface.setSensorPosition(MaterialCamera.SENSOR_POSITION_RIGHT);
                            mInterface.setDegrees(mInterface.getDeviceDefaultOrientation() == MaterialCamera.ORIENTATION_PORTRAIT ? 270 : 0);
                        }
                    }
                    onScreenRotation(mInterface.getDegrees());
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    protected static void LOG(Object context, String message) {
        Log.d(context instanceof Class<?> ? ((Class<?>) context).getSimpleName() :
                context.getClass().getSimpleName(), message);
    }

    private final Runnable mPositionUpdater = new Runnable() {
        @Override
        public void run() {
            if (mInterface == null || mRecordDuration == null) {
                return;
            }
            final long mRecordStart = mInterface.getRecordingStart();
            final long mRecordEnd = mInterface.getRecordingEnd();
            if (mRecordStart == -1 && mRecordEnd == -1) {
                return;
            }
            final long now = System.currentTimeMillis();
            if (mRecordEnd != -1) {
                if (now >= mRecordEnd) {
                    stopRecordingVideo(true);
                } else {
                    final long diff = mRecordEnd - now;
                    mRecordDuration.setText(String.format("-%s", CameraUtil.getDurationString(diff)));
                }
            } else {
                mRecordDuration.setText(CameraUtil.getDurationString(now - mRecordStart));
            }
            if (mPositionHandler != null) {
                mPositionHandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.sensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mcam_fragment_videocapture, container, false);
    }

    protected void setImageRes(ImageView iv, @DrawableRes int res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && iv.getBackground() instanceof RippleDrawable) {
            RippleDrawable rd = (RippleDrawable) iv.getBackground();
            rd.setColor(ColorStateList.valueOf(CameraUtil.adjustAlpha(mIconTextColor, 0.3f)));
        }
        //Drawable d = AppCompatResources.getDrawable(iv.getContext(), res);
        Drawable d =  iv.getContext().getResources().getDrawable(res);
        d = DrawableCompat.wrap(d.mutate());
        DrawableCompat.setTint(d, mIconTextColor);
        iv.setImageDrawable(d);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final int defaultOrientation = Degrees.getDeviceDefaultOrientation(getActivity());
        switch (defaultOrientation) {
            case android.content.res.Configuration.ORIENTATION_LANDSCAPE:
                mInterface.setDeviceDefaultOrientation(MaterialCamera.ORIENTATION_LANDSCAPE);
                break;
            default:
                mInterface.setDeviceDefaultOrientation(MaterialCamera.ORIENTATION_PORTRAIT);
                break;
        }

        mDelayStartCountdown = (TextView) view.findViewById(R.id.delayStartCountdown);
        mButtonVideo = (ImageButton) view.findViewById(R.id.video);
        mButtonStillshot = (ImageButton) view.findViewById(R.id.stillshot);
        mRecordDuration = (TextView) view.findViewById(R.id.recordDuration);
        mButtonFacing = (ImageButton) view.findViewById(R.id.facing);
        if (mInterface.shouldHideCameraFacing() || CameraUtil.isChromium()) {
            mButtonFacing.setVisibility(View.GONE);
        } else {
            setImageRes(mButtonFacing, mInterface.getCurrentCameraPosition() == CAMERA_POSITION_BACK ?
                    mInterface.iconFrontCamera() : mInterface.iconRearCamera());
        }

        mButtonFlash = (ImageButton) view.findViewById(R.id.flash);
        setupFlashMode();

        mButtonVideo.setOnClickListener(this);
        mButtonStillshot.setOnClickListener(this);
        mButtonFacing.setOnClickListener(this);
        mButtonFlash.setOnClickListener(this);

        int primaryColor = getArguments().getInt(CameraIntentKey.PRIMARY_COLOR);
        if (CameraUtil.isColorDark(primaryColor)) {
            mIconTextColor = ContextCompat.getColor(getActivity(), R.color.mcam_color_light);
            primaryColor = CameraUtil.darkenColor(primaryColor);
        } else {
            mIconTextColor = ContextCompat.getColor(getActivity(), R.color.mcam_color_dark);
        }
        view.findViewById(R.id.controlsFrame).setBackgroundColor(primaryColor);
        mRecordDuration.setTextColor(mIconTextColor);

        if (mMediaRecorder != null && mIsRecording) {
            setImageRes(mButtonVideo, mInterface.iconStop());
        } else {
            setImageRes(mButtonVideo, mInterface.iconRecord());
            mInterface.setDidRecord(false);
        }

        if (savedInstanceState != null) {
            mOutputUri = savedInstanceState.getString("output_uri");
        }

        if (mInterface.useStillshot()) {
            mButtonVideo.setVisibility(View.GONE);
            mRecordDuration.setVisibility(View.GONE);
            mButtonStillshot.setVisibility(View.VISIBLE);
            setImageRes(mButtonStillshot, mInterface.iconStillshot());
            mButtonFlash.setVisibility(View.VISIBLE);
        }

        if (mInterface.autoRecordDelay() < 1000) {
            mDelayStartCountdown.setVisibility(View.GONE);
        } else {
            mDelayStartCountdown.setText(Long.toString(mInterface.autoRecordDelay() / 1000));
        }
    }

    protected void onFlashModesLoaded() {
        if (getCurrentCameraPosition() != BaseCaptureActivity.CAMERA_POSITION_FRONT) {
            invalidateFlash(false);
        }
    }

    protected void onScreenRotation(int degrees) {
        ViewCompat.setRotation(mRecordDuration, degrees);
        ViewCompat.setRotation(mButtonFacing, degrees);
    }

    protected abstract int getVideoOrientation(int sensorPosition);

    private boolean mDidAutoRecord = false;
    private Handler mDelayHandler;
    private int mDelayCurrentSecond = -1;

    protected void onCameraOpened() {
        if (mDidAutoRecord || mInterface == null || mInterface.useStillshot() || mInterface.autoRecordDelay() < 0 || getActivity() == null) {
            mDelayStartCountdown.setVisibility(View.GONE);
            mDelayHandler = null;
            return;
        }
        mDidAutoRecord = true;
        mButtonFacing.setVisibility(View.GONE);

        if (mInterface.autoRecordDelay() == 0) {
            mDelayStartCountdown.setVisibility(View.GONE);
            mIsRecording = startRecordingVideo();
            mDelayHandler = null;
            return;
        }

        mDelayHandler = new Handler();
        mButtonVideo.setEnabled(false);

        if (mInterface.autoRecordDelay() < 1000) {
            // Less than a second delay
            mDelayStartCountdown.setVisibility(View.GONE);
            mDelayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isAdded() || getActivity() == null || mIsRecording) {
                        return;
                    }
                    mButtonVideo.setEnabled(true);
                    mIsRecording = startRecordingVideo();
                    mDelayHandler = null;
                }
            }, mInterface.autoRecordDelay());
            return;
        }

        mDelayStartCountdown.setVisibility(View.VISIBLE);
        mDelayCurrentSecond = (int) mInterface.autoRecordDelay() / 1000;
        mDelayHandler.postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if (!isAdded() || getActivity() == null || mIsRecording) {
                    return;
                }
                mDelayCurrentSecond -= 1;
                mDelayStartCountdown.setText(Integer.toString(mDelayCurrentSecond));

                if (mDelayCurrentSecond == 0) {
                    mDelayStartCountdown.setVisibility(View.GONE);
                    mButtonVideo.setEnabled(true);
                    mIsRecording = startRecordingVideo();
                    mDelayHandler = null;
                    return;
                }

                mDelayHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mButtonVideo = null;
        mButtonStillshot = null;
        mButtonFacing = null;
        mButtonFlash = null;
        mRecordDuration = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        if (mInterface != null && mInterface.hasLengthLimit()) {
            if (mInterface.countdownImmediately() || mInterface.getRecordingStart() > -1) {
                if (mInterface.getRecordingStart() == -1) {
                    mInterface.setRecordingStart(System.currentTimeMillis());
                }
                startCounter();
            } else {
                mRecordDuration.setText(String.format("-%s", CameraUtil.getDurationString(mInterface.getLengthLimit())));
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        mInterface = (BaseCaptureInterface) activity;
    }

    @NonNull
    protected final File getOutputMediaFile() {
        return CameraUtil.makeTempFile(getActivity(), getArguments().getString(CameraIntentKey.SAVE_DIR), "VID_", ".mp4");
    }

    @NonNull
    protected final File getOutputPictureFile() {
        return CameraUtil.makeTempFile(getActivity(), getArguments().getString(CameraIntentKey.SAVE_DIR), "IMG_", ".jpg");
    }

    public abstract void openCamera();

    public abstract void closeCamera();

    public void cleanup() {
        closeCamera();
        releaseRecorder();
        stopCounter();
    }

    public abstract void takeStillshot();

    public abstract void onPreferencesUpdated();

    @Override
    public void onPause() {
        super.onPause();

        sensorManager.unregisterListener(sensorEventListener);

        cleanup();
    }

    @Override
    public final void onDetach() {
        super.onDetach();
        mInterface = null;
    }

    public final void startCounter() {
        if (mPositionHandler == null) {
            mPositionHandler = new Handler();
        } else {
            mPositionHandler.removeCallbacks(mPositionUpdater);
        }
        mPositionHandler.post(mPositionUpdater);
    }

    @BaseCaptureActivity.CameraPosition
    public final int getCurrentCameraPosition() {
        if (mInterface == null) {
            return BaseCaptureActivity.CAMERA_POSITION_UNKNOWN;
        }
        return mInterface.getCurrentCameraPosition();
    }

    public final int getCurrentCameraId() {
        if (mInterface.getCurrentCameraPosition() == CAMERA_POSITION_BACK) {
            return (Integer) mInterface.getBackCamera();
        } else {
            return (Integer) mInterface.getFrontCamera();
        }
    }

    public final void stopCounter() {
        if (mPositionHandler != null) {
            mPositionHandler.removeCallbacks(mPositionUpdater);
            mPositionHandler = null;
        }
    }

    public final void releaseRecorder() {
        if (mMediaRecorder != null) {
            if (mIsRecording) {
                try {
                    mMediaRecorder.stop();
                } catch (Throwable t) {
                    //noinspection ResultOfMethodCallIgnored
                    new File(mOutputUri).delete();
                    t.printStackTrace();
                }
                mIsRecording = false;
            }
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public boolean startRecordingVideo() {
        if (mInterface != null && mInterface.hasLengthLimit() && !mInterface.countdownImmediately()) {
            // Countdown wasn't started in onResume, start it now
            if (mInterface.getRecordingStart() == -1) {
                mInterface.setRecordingStart(System.currentTimeMillis());
            }
            startCounter();
        }

        final int orientation = Degrees.getActivityOrientation(getActivity());
        //noinspection ResourceType
        getActivity().setRequestedOrientation(orientation);
        mInterface.setDidRecord(true);
        return true;
    }

    public void stopRecordingVideo(boolean reachedZero) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public final void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("output_uri", mOutputUri);
    }

    @Override
    public final String getOutputUri() {
        return mOutputUri;
    }

    protected final void throwError(Exception e) {
        Activity act = getActivity();
        if (act != null) {
            act.setResult(RESULT_CANCELED, new Intent().putExtra(MaterialCamera.ERROR_EXTRA, e));
            act.finish();
        }
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        if (id == R.id.facing) {
            mInterface.toggleCameraPosition();
            setImageRes(mButtonFacing, mInterface.getCurrentCameraPosition() == CAMERA_POSITION_BACK ?
                    mInterface.iconFrontCamera() : mInterface.iconRearCamera());
            closeCamera();
            openCamera();
            setupFlashMode();
        } else if (id == R.id.video) {
            if (mIsRecording) {
                stopRecordingVideo(false);
                mIsRecording = false;
            } else {
                if (getArguments().getBoolean(CameraIntentKey.SHOW_PORTRAIT_WARNING, true) &&
                        Degrees.isPortrait(getActivity())) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.mcam_portrait)
                            .setMessage(R.string.mcam_portrait_warning)
                            .setPositiveButton(R.string.mcam_yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mIsRecording = startRecordingVideo();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                } else {
                    mIsRecording = startRecordingVideo();
                }
            }
        } else if (id == R.id.stillshot) {
            takeStillshot();
        } else if (id == R.id.flash) {
            invalidateFlash(true);
        }
    }

    private void invalidateFlash(boolean toggle) {
        if (toggle) {
            mInterface.toggleFlashMode();
        }
        setupFlashMode();
        onPreferencesUpdated();
    }

    private void setupFlashMode() {
        if (mInterface.shouldHideFlash()) {
            mButtonFlash.setVisibility(View.GONE);
            return;
        } else {
            mButtonFlash.setVisibility(View.VISIBLE);
        }

        final int res;
        switch (mInterface.getFlashMode()) {
            case FLASH_MODE_AUTO:
                res = mInterface.iconFlashAuto();
                break;
            case FLASH_MODE_ALWAYS_ON:
                res = mInterface.iconFlashOn();
                break;
            case FLASH_MODE_OFF:
            default:
                res = mInterface.iconFlashOff();
        }

        setImageRes(mButtonFlash, res);
    }
}