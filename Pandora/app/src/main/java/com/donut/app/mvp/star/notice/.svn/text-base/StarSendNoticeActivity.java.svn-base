package com.donut.app.mvp.star.notice;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.donut.app.R;
import com.donut.app.activity.UploadManagerActivity;
import com.donut.app.activity.WebTermsActivity;
import com.donut.app.config.Constant;
import com.donut.app.databinding.ActivityStarSendNoticeBinding;
import com.donut.app.entity.UploadInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.StarNoticeAddRequest;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.service.UploadService;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.lidroid.xutils.exception.DbException;

import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public class StarSendNoticeActivity extends MVPBaseActivity<ActivityStarSendNoticeBinding, StarSendNoticePresenter>
        implements StarSendNoticeContract.View, ShowChooseVideoPopupWindow.OnShowViewListener,
        ShowChoosePhotoPopupWindow.OnShowViewListener {

    private CharSequence contentOldSequence;

    private String imgFilePath, videoFilePath;

    private static final int img_permission = 1, video_permission = 2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_star_send_notice;
    }

    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(this, Constant.default_bar_color);
        updateHeadTitle("发起通告", true);
        mViewBinding.toolbarHead.headRightTv.setText(R.string.submit);
    }

    @Override
    protected void initEvent() {

        mViewBinding.starSendNoticeContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                contentOldSequence = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mViewBinding.starSendNoticeContentHint.setVisibility(View.GONE);
                } else {
                    mViewBinding.starSendNoticeContentHint.setVisibility(View.VISIBLE);
                }

                int editStart = mViewBinding.starSendNoticeContent.getSelectionStart();
                int editEnd = mViewBinding.starSendNoticeContent.getSelectionEnd();
                if (contentOldSequence.length() > 1024) {
                    showToast("通告描述限制1024字以内");
                    s.delete(editStart - 1, editEnd);
                    mViewBinding.starSendNoticeContent.setText(s);
                    if (editStart > 1024) {
                        editStart = 1024 + 1;
                    }
                    mViewBinding.starSendNoticeContent.setSelection(editStart - 1);
                }
            }
        });

        mViewBinding.starSendNoticeContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
                if ((view.getId() == R.id.star_send_notice_content
                        && canVerticalScroll(mViewBinding.starSendNoticeContent))) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;
            }
        });
        mViewBinding.toolbarHead.menu.setOnClickListener(this);
        mViewBinding.starSendNoticeImgAdd.setOnClickListener(this);
        mViewBinding.starSendNoticeVideoAdd.setOnClickListener(this);

        mViewBinding.starSendNoticeProvisions.setOnClickListener(this);
    }

    @Override
    public void loadData() {
    }

    @Override
    public void showUploadingProgress(String filePath, int progress) {
        if (filePath.equals(imgFilePath) && progress > 0) {
            Message message = new Message();
            message.what = 999;
            message.arg1 = progress;
            handler.sendMessage(message);
        } else if (filePath.equals(videoFilePath) && progress > 0) {
            Message message = new Message();
            message.what = 888;
            message.arg1 = progress;
            handler.sendMessage(message);
        }
    }

    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 999:
                    mViewBinding.starSendNoticeImgUploadingPb.setVisibility(View.VISIBLE);
                    mViewBinding.starSendNoticeImgUploadingPb.setProgress(msg.arg1);
                    break;
                case 888:
                    mViewBinding.starSendNoticeVideoUploadingPb.setVisibility(View.VISIBLE);
                    mViewBinding.starSendNoticeVideoUploadingPb.setProgress(msg.arg1);
                    break;
                case StarSendNoticePresenter.UPLOAD_IMG_REQUEST:
                    mViewBinding.starSendNoticeImgUploadingPb.setVisibility(View.GONE);
                    break;
                case StarSendNoticePresenter.UPLOAD_VIDEO:
                    mViewBinding.starSendNoticeVideoUploadingPb.setVisibility(View.GONE);
                    break;

            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void dismissUploadingProgress(int actionId) {
        Message message = new Message();
        message.what = actionId;
        handler.sendMessage(message);
    }

    @Override
    public void finishView() {
        setResult(RESULT_OK);
        finish();
    }

    private void saveData() {

        if (!mViewBinding.starSendNoticeCbApprove.isChecked()) {
            showToast("请同意活动条款");
            return;
        }
        String des = mViewBinding.starSendNoticeContent.getText().toString().trim();
        if (TextUtils.isEmpty(des)) {
            showToast("请填写通告描述");
            return;
        }
        if (TextUtils.isEmpty(videoFilePath)) {
            showToast("请上传视频");
            return;
        }
        if (mPresenter.mUploadArray.size() > 0) {
            showToast("文件正在上传,请稍后");
            return;
        }
        if (TextUtils.isEmpty(mPresenter.playUrl)) {
            showToast("抱歉，视频上传失败，请重新上传");
            return;
        }

        StarNoticeAddRequest request = new StarNoticeAddRequest();
        request.setDescription(des);
        request.setPublicPic(mPresenter.imgUrl != null && mPresenter.imgUrl.length() > 0
                ? mPresenter.imgUrl : mPresenter.thumbnail);
        request.setThumbnail(mPresenter.thumbnail);
        request.setPlayUrl(mPresenter.playUrl);
        request.setLastTime(mPresenter.lastTime);
        mPresenter.saveBehaviour("01", request, HeaderRequest.STAR_NOTICE_ADD);

        if (!mPresenter.takeVideo) {
            //此时为选择的视频文件!
            //非拍摄视频去处理...
            try {
                UploadService.getUploadManager().addNewUpload(
                        videoFilePath,
                        getUserInfo().getUserId(),
                        getUserInfo().getToken(),
                        UploadInfo.SaveTypeEnum.STAR_SEND_NOTICE,
                        "",
                        JsonUtils.toJson(request, request.getClass()));
            } catch (DbException e) {
                e.printStackTrace();
            }
            showToast("视频过大可能导致压缩时间较长哦，请耐心等候！");
            launchActivity(UploadManagerActivity.class);
            finish();
            return;
        }
        mPresenter.saveData(request);
    }

    @Override
    public void onBackPressed() {
        mPresenter.saveBehaviour("02");
        String content = mViewBinding.starSendNoticeContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)
                && TextUtils.isEmpty(imgFilePath)
                && TextUtils.isEmpty(videoFilePath)) {
            super.onBackPressed();
        } else {
            Dialog dialog = new AlertDialog.Builder(this)
                    .setMessage("信息未保存，确定返回吗？")
                    .setNegativeButton(getString(R.string.cancel), null)
                    .setPositiveButton(getString(R.string.sure),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    StarSendNoticeActivity.super.onBackPressed();
                                }
                            }).create();
            dialog.show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.saveBehaviour("00");
    }

    @Override
    protected void onStop() {
        mPresenter.saveBehaviour("xx");
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.menu:
                saveData();
                break;
            case R.id.star_send_notice_provisions:
                //活动条款
                Intent intent = new Intent(this, WebTermsActivity.class);
                intent.putExtra(WebTermsActivity.IS_STAR_SEND_NOTICE, true);
                startActivity(intent);
                break;
            case R.id.star_send_notice_img_add:
                requestRuntimePermission(img_permission,
                        "为了给您提供更好的服务,甜麦圈需要获取存储器读写及相机权限",
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA);
                break;
            case R.id.star_send_notice_video_add:
                requestRuntimePermission(video_permission,
                        "为了给您提供更好的服务,甜麦圈需要获取存储器读写及相机权限",
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA);
                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == img_permission && perms != null && perms.size() == 3) {
            mChoosePhotoPopupWindow = new ShowChoosePhotoPopupWindow(this, this);
            mChoosePhotoPopupWindow.show(mViewBinding.getRoot());
        } else if (requestCode == video_permission && perms != null && perms.size() == 3) {
            mChooseVideoPopupWindow = new ShowChooseVideoPopupWindow(this, this);
            mChooseVideoPopupWindow.show(mViewBinding.getRoot());
        }
    }

    private ShowChooseVideoPopupWindow mChooseVideoPopupWindow;

    private ShowChoosePhotoPopupWindow mChoosePhotoPopupWindow;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mChoosePhotoPopupWindow != null) {
            mChoosePhotoPopupWindow.onActivityResult(requestCode, resultCode, data);
        }
        if (mChooseVideoPopupWindow != null) {
            mChooseVideoPopupWindow.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    /**
     * 运行时权限申请
     */
    public void requestRuntimePermission(int requestCode, @NonNull String rationale,
                                         @NonNull String... permission) {
        if (EasyPermissions.hasPermissions(this, permission)) {
            // Have permission, do the thing!
            onPermissionsGranted(requestCode, Arrays.asList(permission));
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, rationale,
                    requestCode, permission);
        }
    }

    @Override
    public void choosePhotoSuccess(Bitmap bm, String path) {
        this.imgFilePath = path;
        mViewBinding.starSendNoticeImgImgHint.setVisibility(View.GONE);
        BindingUtils.loadImg(mViewBinding.starSendNoticeImg, path);
        mPresenter.uploadImg(path, 4, StarSendNoticePresenter.UPLOAD_IMG_REQUEST);
    }

    @Override
    public void chooseVideoSuccess(boolean takeVideo, String path) {
        this.videoFilePath = path;
        mViewBinding.starSendNoticeVideoAddHint.setVisibility(View.GONE);
        BindingUtils.loadImg(mViewBinding.starSendNoticeVideoImg, path);
        BindingUtils.loadImg(mViewBinding.starSendNoticeImg, path);
        mPresenter.uploadVideo(path, takeVideo);
    }
}
