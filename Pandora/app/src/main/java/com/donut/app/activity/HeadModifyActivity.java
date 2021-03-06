package com.donut.app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.customview.SelectPicPopupWindow;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.EditUserInfoResponse;
import com.donut.app.http.message.FileUploadResponse;
import com.donut.app.http.message.UserInfoEditRequest;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.FetchImageUtilsCrop;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class HeadModifyActivity extends BaseActivity
{
    @ViewInject(R.id.head)
    private ImageView mHeader;

    @ViewInject(R.id.head_right_tv)
    private TextView mRight;

    private String headurl;

    public static final String HEADURL = "headurl";

    public static final int UPLOAD_IMG_REQUEST = 0, HEAD_MODIFY = 1;

    private FetchImageUtilsCrop mImageUtil;
    SelectPicPopupWindow showWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_modify);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle("个人头像", true);
        mRight.setText(getString(R.string.modify));
        headurl = getIntent().getStringExtra(HEADURL);
        Glide.with(this)
                .load(headurl)
                .placeholder(R.drawable.default_header)
                .error(R.drawable.default_header)
                .centerCrop()
                .into(mHeader);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.HEAD_MODIFY.getCode() + "00");
    }

    @Override
    protected void onStop()
    {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.HEAD_MODIFY.getCode() + "xx");
        super.onStop();
    }

    @OnClick({R.id.menu})
    private void viewOnClick(View v)
    {
        switch (v.getId())
        {
            case R.id.menu:
                if (mImageUtil == null)
                {
                    mImageUtil = new FetchImageUtilsCrop(this);
                }
                showWindow = new SelectPicPopupWindow(this, itemsOnClick);
                //显示窗口
                showWindow.showAtLocation(this.findViewById(R.id.head_modify_linear),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
        }
    }

    public void onBack()
    {
        Intent intent = new Intent();
        intent.putExtra(HEADURL, headurl);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.HEAD_MODIFY.getCode() + "02");
        super.onBackPressed();
    }

    //为弹出窗口实现监听类
    private SelectPicPopupWindow.OnClickListenerWithPosition itemsOnClick = new SelectPicPopupWindow.OnClickListenerWithPosition()
    {
        @Override
        public void onClick(View v, int actionId)
        {
            if (showWindow != null && showWindow.isShowing())
            {
                showWindow.dismiss();
                switch (v.getId())
                {
                    case R.id.btn_click_one:
                        requestRuntimePermission("调用摄像头拍照，请授予相机权限",
                                Manifest.permission.CAMERA);
                        break;
                    case R.id.btn_click_three:
                        mImageUtil.doPickPhotoFromGallery(pickCallback);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mImageUtil.doTakePhoto(pickCallback);
    }

    private FetchImageUtilsCrop.OnPickFinishedCallback pickCallback = new FetchImageUtilsCrop.OnPickFinishedCallback()
    {
        @Override
        public void onPickFailed()
        {
            // Toast.makeText(PersonalInfoActivity.this,
            // R.string.errcode_take_photo, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPickSuccessed(Bitmap bm, String path)
        {
            if (path != null && path.startsWith("file://")) {
                path = path.substring(7);
            }

            File file = new File(path);
            if (!file.exists())
            {
                showToast(getString(R.string.select_pic_error));
                return;
            }
//            ImageLoaderHelper.displayImage("file://" + path,
//                    ImageLoaderHelper.optionsForCommon, mHeader);
            Glide.with(HeadModifyActivity.this)
                    .load(path)
                    .placeholder(R.drawable.default_bg)
                    .error(R.drawable.default_bg)
                    .centerCrop()
                    .into(mHeader);
            UpLoadNetRequest(path, 2, UPLOAD_IMG_REQUEST);
        }
    };

    @Override
    public void onSuccess(String response, String url, int actionId)
    {
        super.onSuccess(response, url, actionId);
        switch (actionId)
        {
            case UPLOAD_IMG_REQUEST:
                FileUploadResponse ret = JsonUtils.fromJson(response,
                        FileUploadResponse.class);
                if (COMMON_SUCCESS.equals(ret.getCode()))
                {
                    headurl = ret.getFileUrl();
                    uploadHead(ret.getFileUrl());
                } else
                {
                    ToastUtil.showShort(this, ret.getMsg());
                }
                break;
            case HEAD_MODIFY:
                EditUserInfoResponse headRes = JsonUtils.fromJson(response, EditUserInfoResponse.class);
                if (COMMON_SUCCESS.equals(headRes.getCode()))
                {
                    UserInfo info = getUserInfo();
                    info.setImgUrl(headurl);
                    setUserInfo(info, true);
                    ToastUtil.showShort(this, getString(R.string.head_upload_success));
                    if (headRes.getIsFirst() == 1)
                    {
                        saveFirstComplete();
                    }
                    onBack();
                } else
                {
                    ToastUtil.showShort(this, headRes.getMsg());
                }
                break;
        }

    }

    private void saveFirstComplete()
    {
        SharedPreferences.Editor editor = sp_Info.edit();
        editor.putLong(Constant.VIP_TIME, System.currentTimeMillis());
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mImageUtil.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadHead(String path)
    {

        UserInfoEditRequest headRequest = new UserInfoEditRequest();
        headRequest.setHeadPic(path);
        sendNetRequest(headRequest, HeaderRequest.MODIFY_PERSONAL_INFO, HEAD_MODIFY, false);
        SaveBehaviourDataService.startAction(this, BehaviourEnum.HEAD_MODIFY.getCode() + "01", headRequest, HeaderRequest.MODIFY_PERSONAL_INFO);
    }
}
