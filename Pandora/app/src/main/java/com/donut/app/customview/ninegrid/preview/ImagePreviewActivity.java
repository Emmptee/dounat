package com.donut.app.customview.ninegrid.preview;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.customview.ninegrid.ImageInfo;
import com.donut.app.utils.FileUtils;
import com.donut.app.utils.ToastUtil;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pub.devrel.easypermissions.EasyPermissions;

public class ImagePreviewActivity extends BaseActivity
        implements ViewTreeObserver.OnPreDrawListener, View.OnClickListener, ImagePreviewAdapter.OnItemLongClickListener {

    public static final String IMAGE_INFO = "IMAGE_INFO";
    public static final String CURRENT_ITEM = "CURRENT_ITEM";
    public static final int ANIMATE_DURATION = 200;

    private RelativeLayout rootView;

    private ImagePreviewAdapter imagePreviewAdapter;
    private List<ImageInfo> imageInfo;
    private int currentItem;
    private int imageHeight;
    private int imageWidth;
    private int screenWidth;
    private int screenHeight;

    public static final int PERMISSION_CODE_DOWNLOAD = 1, PERMISSION_CODE_SHARE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        StatusBarCompat.translucentStatusBar(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final TextView tv_pager = (TextView) findViewById(R.id.tv_pager);
        rootView = (RelativeLayout) findViewById(R.id.rootView);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;

        Intent intent = getIntent();
        imageInfo = (List<ImageInfo>) intent.getSerializableExtra(IMAGE_INFO);
        currentItem = intent.getIntExtra(CURRENT_ITEM, 0);

        imagePreviewAdapter = new ImagePreviewAdapter(this, imageInfo);
        viewPager.setAdapter(imagePreviewAdapter);
        viewPager.setCurrentItem(currentItem);
        viewPager.getViewTreeObserver().addOnPreDrawListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                tv_pager.setText(String.format(getString(R.string.select), currentItem + 1, imageInfo.size()));
            }
        });
        tv_pager.setText(String.format(getString(R.string.select), currentItem + 1, imageInfo.size()));

        findViewById(R.id.tv_more).setOnClickListener(this);
        imagePreviewAdapter.setOnLongClickListener(this);
    }

    @Override
    public void onBackPressed() {
        finishActivityAnim();
    }

    /**
     * 绘制前开始动画
     */
    @Override
    public boolean onPreDraw() {
        rootView.getViewTreeObserver().removeOnPreDrawListener(this);
        final View view = imagePreviewAdapter.getPrimaryItem();
        final ImageView imageView = imagePreviewAdapter.getPrimaryImageView();
        computeImageWidthAndHeight(imageView);

        final ImageInfo imageData = imageInfo.get(currentItem);
        final float vx = imageData.imageViewWidth * 1.0f / imageWidth;
        final float vy = imageData.imageViewHeight * 1.0f / imageHeight;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                long duration = animation.getDuration();
                long playTime = animation.getCurrentPlayTime();
                float fraction = duration > 0 ? (float) playTime / duration : 1f;
                if (fraction > 1) {
                    fraction = 1;
                }
                view.setTranslationX(evaluateInt(fraction, imageData.imageViewX + imageData.imageViewWidth / 2 - imageView.getWidth() / 2, 0));
                view.setTranslationY(evaluateInt(fraction, imageData.imageViewY + imageData.imageViewHeight / 2 - imageView.getHeight() / 2, 0));
                view.setScaleX(evaluateFloat(fraction, vx, 1));
                view.setScaleY(evaluateFloat(fraction, vy, 1));
                view.setAlpha(fraction);
                rootView.setBackgroundColor(evaluateArgb(fraction, Color.TRANSPARENT, Color.BLACK));
            }
        });
        addIntoListener(valueAnimator);
        valueAnimator.setDuration(ANIMATE_DURATION);
        valueAnimator.start();
        return true;
    }

    /**
     * activity的退场动画
     */
    public void finishActivityAnim() {
        final View view = imagePreviewAdapter.getPrimaryItem();
        final ImageView imageView = imagePreviewAdapter.getPrimaryImageView();
        computeImageWidthAndHeight(imageView);

        final ImageInfo imageData = imageInfo.get(currentItem);
        final float vx = imageData.imageViewWidth * 1.0f / imageWidth;
        final float vy = imageData.imageViewHeight * 1.0f / imageHeight;
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                long duration = animation.getDuration();
                long playTime = animation.getCurrentPlayTime();
                float fraction = duration > 0 ? (float) playTime / duration : 1f;
                if (fraction > 1) {
                    fraction = 1;
                }
                view.setTranslationX(evaluateInt(fraction, 0, imageData.imageViewX + imageData.imageViewWidth / 2 - imageView.getWidth() / 2));
                view.setTranslationY(evaluateInt(fraction, 0, imageData.imageViewY + imageData.imageViewHeight / 2 - imageView.getHeight() / 2));
                view.setScaleX(evaluateFloat(fraction, 1, vx));
                view.setScaleY(evaluateFloat(fraction, 1, vy));
                view.setAlpha(1 - fraction);
                rootView.setBackgroundColor(evaluateArgb(fraction, Color.BLACK, Color.TRANSPARENT));
            }
        });
        addOutListener(valueAnimator);
        valueAnimator.setDuration(ANIMATE_DURATION);
        valueAnimator.start();
    }

    /**
     * 计算图片的宽高
     */
    private void computeImageWidthAndHeight(ImageView imageView) {

        // 获取真实大小
        Drawable drawable = imageView.getDrawable();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        int intrinsicWidth = drawable.getIntrinsicWidth();
        // 计算出与屏幕的比例，用于比较以宽的比例为准还是高的比例为准，因为很多时候不是高度没充满，就是宽度没充满
        float h = screenHeight * 1.0f / intrinsicHeight;
        float w = screenWidth * 1.0f / intrinsicWidth;
        if (h > w) {
            h = w;
        } else {
            w = h;
        }

        // 得出当宽高至少有一个充满的时候图片对应的宽高
        imageHeight = (int) (intrinsicHeight * h);
        imageWidth = (int) (intrinsicWidth * w);
    }

    /**
     * 进场动画过程监听
     */
    private void addIntoListener(ValueAnimator valueAnimator) {
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                rootView.setBackgroundColor(0x0);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * 退场动画过程监听
     */
    private void addOutListener(ValueAnimator valueAnimator) {
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                rootView.setBackgroundColor(0x0);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * Integer 估值器
     */
    public Integer evaluateInt(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int) (startInt + fraction * (endValue - startInt));
    }

    /**
     * Float 估值器
     */
    public Float evaluateFloat(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    /**
     * Argb 估值器
     */
    public int evaluateArgb(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return (startA + (int) (fraction * (endA - startA))) << 24//
                | (startR + (int) (fraction * (endR - startR))) << 16//
                | (startG + (int) (fraction * (endG - startG))) << 8//
                | (startB + (int) (fraction * (endB - startB)));
    }

    private BottomSheetDialog bottomSheetDialog;

    long current;
    @Override
    public void onClick(View v) {
        String[] permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        switch (v.getId()) {
            case R.id.tv_more:
                showBottomDialog();
                break;
            case R.id.btn_click_share:
                if(System.currentTimeMillis() - current <= 1000){
                    break;
                }
                current = System.currentTimeMillis();
                if (EasyPermissions.hasPermissions(this, permission)) {
                    onPermissionsGranted(PERMISSION_CODE_SHARE, Arrays.asList(permission));
                } else {
                    EasyPermissions.requestPermissions(this, "为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                            PERMISSION_CODE_SHARE, permission);
                }
                break;

            case R.id.btn_click_save:
                if (EasyPermissions.hasPermissions(this, permission)) {
                    onPermissionsGranted(PERMISSION_CODE_DOWNLOAD, Arrays.asList(permission));
                } else {
                    EasyPermissions.requestPermissions(this, "为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                            PERMISSION_CODE_DOWNLOAD, permission);
                }

                break;

            case R.id.btn_click_cancel:
                bottomSheetDialog.cancel();
                break;
        }
    }

    @Override
    public void onLongClick(View v) {
        showBottomDialog();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == PERMISSION_CODE_DOWNLOAD) {
            DownloadPicTask task = new DownloadPicTask();
            task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                    imageInfo.get(currentItem).getBigImageUrl());
        } else if (requestCode == PERMISSION_CODE_SHARE) {

            showToast("正在分享...");
            UMShareListener shareListener = new UMShareListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {
                }

                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    ToastUtil.showShort(ImagePreviewActivity.this, "发送成功");
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {

                }
            };
            ImageInfo info = imageInfo.get(currentItem);
            String picUrl = info.getBigImageUrl().toLowerCase();
            if (picUrl.endsWith("gif")) {
                UMEmoji image = new UMEmoji(this, info.getBigImageUrl());
                UMImage thumb = new UMImage(this, info.getThumbnailUrl());
                image.setThumb(thumb);
                new ShareAction(this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(image)
                        .setCallback(shareListener)
                        .share();
            } else {
                UMImage image = new UMImage(this, info.getBigImageUrl());
                new ShareAction(this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(image)
                        .setCallback(shareListener)
                        .share();
            }
        }
    }

    private void showBottomDialog() {
        bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.share_pic_bottom, null);
        view.findViewById(R.id.btn_click_share).setOnClickListener(this);
        view.findViewById(R.id.btn_click_save).setOnClickListener(this);
        view.findViewById(R.id.btn_click_cancel).setOnClickListener(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private class DownloadPicTask extends AsyncTask<String, Integer, Boolean> {

        private String path = FileUtils.getSDDir(getString(R.string.app_name)
                        + File.separator
                        + getString(R.string.image),
                ImagePreviewActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showToast(getString(R.string.download_start));
        }

        @Override
        protected Boolean doInBackground(String... params) {

            boolean createStatus = false;

            try {
                String picUrl = params[0];
                File file = Glide
                        .with(ImagePreviewActivity.this)
                        .load(picUrl)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
                File newFile = new File(path, picUrl.substring(picUrl.lastIndexOf(File.separator)));
                if (!newFile.exists()) {
                    createStatus = FileUtils.copyFile(file, newFile);
                    Uri localUri = Uri.fromFile(newFile);
                    Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                    ImagePreviewActivity.this.sendBroadcast(localIntent);
                } else {
                    createStatus = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return createStatus;
        }

        @Override
        protected void onPostExecute(Boolean createStatus) {
            super.onPostExecute(createStatus);
            if (createStatus) {
                showToast(String.format(getString(R.string.download_success), path));
            } else {
                showToast(getString(R.string.download_fail));
            }

        }
    }
}
