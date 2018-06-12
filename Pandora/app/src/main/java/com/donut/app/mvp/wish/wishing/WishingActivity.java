package com.donut.app.mvp.wish.wishing;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
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
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.databinding.ActivityWishingBinding;
import com.donut.app.entity.UploadInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.wish.AddWishRequest;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.service.UploadService;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.lidroid.xutils.exception.DbException;
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public class WishingActivity extends MVPBaseActivity<ActivityWishingBinding, WishingPresenter>
        implements WishingContract.View, com.donut.app.mvp.wish.wishing.ShowChooseMediaPopupWindow.OnShowViewListener {
    private static final String TAG = "WISHING";
    private CharSequence starNameOldSequence, contentOldSequence;

    private String videoFilePath;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wishing;
    }

    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColor(this, Constant.default_bar_color);
        updateHeadTitle(getString(R.string.launch_ip), true);
        mViewBinding.toolbarHead.headRightTv.setText(R.string.submit);
    }

    @Override
    protected void initEvent() {
        mViewBinding.wishingStarName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                starNameOldSequence = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int editStart = mViewBinding.wishingStarName.getSelectionStart();
                int editEnd = mViewBinding.wishingStarName.getSelectionEnd();
                if (starNameOldSequence.length() > 20) {
                    showToast("心愿明星限制20字以内");
                    s.delete(editStart - 1, editEnd);
                    mViewBinding.wishingStarName.setText(s);
                    if (editStart > 20) {
                        editStart = 20 + 1;
                    }
                    mViewBinding.wishingStarName.setSelection(editStart - 1);
                }
            }
        });

        mViewBinding.wishingContent.addTextChangedListener(new TextWatcher() {
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
                    mViewBinding.wishingContentHint.setVisibility(View.GONE);
                } else {
                    mViewBinding.wishingContentHint.setVisibility(View.VISIBLE);
                }

                int editStart = mViewBinding.wishingContent.getSelectionStart();
                int editEnd = mViewBinding.wishingContent.getSelectionEnd();
                if (contentOldSequence.length() > 200) {
                    showToast("心愿描述限制200字以内");
                    s.delete(editStart - 1, editEnd);
                    mViewBinding.wishingContent.setText(s);
                    if (editStart > 200) {
                        editStart = 200 + 1;
                    }
                    mViewBinding.wishingContent.setSelection(editStart - 1);
                }
            }
        });

        mViewBinding.wishingContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
                if ((view.getId() == R.id.wishing_content && canVerticalScroll(mViewBinding.wishingContent))) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;
            }
        });
        mViewBinding.toolbarHead.menu.setOnClickListener(this);
        mViewBinding.wishingRadioStar.setOnClickListener(this);
        mViewBinding.wishingRadioChannel.setOnClickListener(this);
        mViewBinding.wishingRadioPlatform.setOnClickListener(this);
        mViewBinding.wishingImgAdd.setOnClickListener(this);
        mViewBinding.wishingProvisions.setOnClickListener(this);
    }

    @Override
    public void loadData() {
    }

    @Override
    public void showUploadingProgress(int progress) {
        if (progress > 0) {
            Message message = new Message();
            message.what = 999;
            message.arg1 = progress;
            handler.sendMessage(message);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 999:
                    mViewBinding.wishingUploadingPb.setVisibility(View.VISIBLE);
                    mViewBinding.wishingUploadingPb.setProgress(msg.arg1);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void dismissUploadingProgress() {
        mViewBinding.wishingUploadingPb.setVisibility(View.GONE);
    }

    @Override
    public void finishView() {
        setResult(RESULT_OK);
        finish();
    }

    private void saveData() {

        if (!mViewBinding.wishingCbApprove.isChecked()) {
            showToast("请同意活动条款");
            return;
        }

        int wishType = 0;
        if (mViewBinding.wishingRadioChannel.isChecked()) {
            wishType = 1;
        } else if (mViewBinding.wishingRadioPlatform.isChecked()) {
            wishType = 2;
        }

        String starName = mViewBinding.wishingStarName.getText().toString().trim();
        String des = mViewBinding.wishingContent.getText().toString().trim();

        if (wishType == 0 && TextUtils.isEmpty(starName)) {
            showToast("请填写您的心愿明星");
            return;
        }

        if (TextUtils.isEmpty(des)) {
            showToast(getString(R.string.ip_collection_des));
            return;
        }

        if (mPresenter.mUploadArray.size() > 0) {
            showToast("文件正在上传,请稍后");
            return;
        }
        KLog.e("明星名字" + starName);
        KLog.e("描述" + des);
        KLog.e("wishtype" + wishType);
        KLog.e("图片地址" + mPresenter.imgUrl);
        KLog.e("视频地址" + mPresenter.playUrl);
        KLog.e("持续时间" + mPresenter.lastTime);
        AddWishRequest request = new AddWishRequest();
        request.setStarName(starName);
        request.setDescription(des);
        request.setWishType(wishType);
        request.setMediaType(mPresenter.mediaType);
        request.setImgUrl(mPresenter.imgUrl);
        request.setPlayUrl(mPresenter.playUrl);
        request.setLastTime(mPresenter.lastTime);

        mPresenter.saveBehaviour("02", request, HeaderRequest.WISH_ADD);

//        if (mPresenter.isVideo && !mPresenter.takeVideo) {
            //此时为选择的视频文件!
            //非拍摄视频去处理...
            try {
                KLog.e("从图库选择视频上传");
                UploadService.getUploadManager().addNewUpload(
                        videoFilePath,
                        getUserInfo().getUserId(),
                        getUserInfo().getToken(),
                        UploadInfo.SaveTypeEnum.IP_SEND,
                        "",
                        JsonUtils.toJson(request, request.getClass()));
            } catch (DbException e) {
                e.printStackTrace();
            }
            showToast("视频过大可能导致压缩时间较长哦，请耐心等候！");
    /*        launchActivity(UploadManagerActivity.class);
            finish();
            return;
        }*/
        mPresenter.saveData(request);
    }

    @Override
    public void onBackPressed() {
        String strStarName = mViewBinding.wishingStarName.getText().toString().trim();
        String content = mViewBinding.wishingContent.getText().toString().trim();
        if (TextUtils.isEmpty(strStarName) && TextUtils.isEmpty(content)) {
            super.onBackPressed();
        } else {
            Dialog dialog = new AlertDialog.Builder(this)
                    .setMessage("信息未保存，确定返回吗？")
                    .setNegativeButton(getString(R.string.cancel), null)
                    .setPositiveButton(getString(R.string.sure),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    WishingActivity.super.onBackPressed();
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
            case R.id.wishing_radio_star:
                mViewBinding.wishingRadioStar.setChecked(true);
                mViewBinding.wishingRadioChannel.setChecked(false);
                mViewBinding.wishingRadioPlatform.setChecked(false);
                mViewBinding.wishingStarName.setHint("写下一位您的心愿明星");
                break;
            case R.id.wishing_radio_channel:
                mViewBinding.wishingRadioStar.setChecked(false);
                mViewBinding.wishingRadioChannel.setChecked(true);
                mViewBinding.wishingRadioPlatform.setChecked(false);
                mViewBinding.wishingStarName.setHint("写下一位您的心愿明星(选填)");
                break;
            case R.id.wishing_radio_platform:
                mViewBinding.wishingRadioStar.setChecked(false);
                mViewBinding.wishingRadioChannel.setChecked(false);
                mViewBinding.wishingRadioPlatform.setChecked(true);
                mViewBinding.wishingStarName.setHint("写下一位您的心愿明星(选填)");
                break;
            case R.id.wishing_provisions:
                //活动条款
                startActivity(new Intent(this, WebTermsActivity.class));
                break;
            case R.id.wishing_img_add:
                requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (perms != null && perms.size() == 2) {
            mPopupWindow = new com.donut.app.mvp.wish.wishing.ShowChooseMediaPopupWindow(this, this);
            mPopupWindow.show(mViewBinding.getRoot());
        }
    }

    private com.donut.app.mvp.wish.wishing.ShowChooseMediaPopupWindow mPopupWindow;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPopupWindow != null) {
            KLog.e("wishingActivity",requestCode+"-----" +resultCode);
            mPopupWindow.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void chooseSuccess(int type, boolean isVideo, String path) {
        mViewBinding.wishingImgHint.setVisibility(View.GONE);
        switch (type) {
            case com.donut.app.mvp.wish.wishing.ShowChooseMediaPopupWindow.PHOTO_TAKE_REQUEST_CODE:
                BindingUtils.loadImg(mViewBinding.wishingImg, path);
                mPresenter.uploadImg(path, 4, WishingPresenter.UPLOAD_IMG_REQUEST);
                break;
            case com.donut.app.mvp.wish.wishing.ShowChooseMediaPopupWindow.VIDEO_TAKE_REQUEST_CODE:
                BindingUtils.loadImg(mViewBinding.wishingImg, path);
                mPresenter.uploadVideo(path, true);
                break;
            case com.donut.app.mvp.wish.wishing.ShowChooseMediaPopupWindow.GALLERY_REQUEST_CODE:
                BindingUtils.loadImg(mViewBinding.wishingImg, path);
                if (isVideo) {
                    this.videoFilePath = path;
                    mPresenter.uploadVideo(path, false);
                } else {
                    mPresenter.uploadImg(path, 4, WishingPresenter.UPLOAD_IMG_REQUEST);
                }
                break;
        }
    }
}
