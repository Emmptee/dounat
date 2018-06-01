package com.donut.app.mvp.mine;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.activity.CollectActivity;
import com.donut.app.activity.CommentAboutMeActivity;
import com.donut.app.activity.H5WebActivity;
import com.donut.app.activity.HomeActivity;
import com.donut.app.activity.LoginActivity;
import com.donut.app.activity.MyChallengeActivity;
import com.donut.app.activity.MyOrderActivity;
import com.donut.app.activity.SettingsActivity;
import com.donut.app.activity.UploadManagerActivity;
import com.donut.app.activity.VipActivity;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.databinding.FragmentMineLayoutBinding;
import com.donut.app.entity.ConfigInfo;
import com.donut.app.entity.UploadInfo;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.UserInfoResponse;
import com.donut.app.mvp.MVPBaseFragment;
import com.donut.app.mvp.notice.NoticeActivity;
import com.donut.app.mvp.spinOff.SpinOffActivity;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.L;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.io.File;
import java.util.List;

/**
 * Created by Qi on 2017/3/28.
 * Description : <br>
 */
public class MineFragment extends MVPBaseFragment<FragmentMineLayoutBinding, MinePresenter>
        implements MineContract.View, ShowChoosePhotoPopupWindow.OnShowViewListener {

    private static final int LOGIN_REQUEST_CODE = 0, SETTING_REQUEST_CODE = 1,
            VIP_REQUEST_CODE = 2, HTML_REQUEST_CODE = 3,
            SHOPPING_REQUEST_CODE = 4, NOTICE_CODE = 5;

    private BaseActivity mActivity;

    private AnimationDrawable frameAnim;

    private ShowChoosePhotoPopupWindow mPopupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_layout;
    }

    @Override
    protected void initView() {
        mViewBinding.setHandler(this);
        mActivity = (BaseActivity) getActivity();
        frameAnim = (AnimationDrawable) mViewBinding.uploadingViewIcon.getDrawable();
    }

    @Override
    protected void initEvent() {
    }

    @Override
    public void loadData() {
        if (getLoginStatus()) {
            mViewBinding.setUserInfo(mActivity.getUserInfo());
            mPresenter.getShoppingAccountRequest();
            if (mActivity.getUserInfo().getUserType() == 1) {
                //明星，重新请求数据
                mPresenter.getStarUserInfo();
            }
        }
        showView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewBinding.uploadingViewIcon.setVisibility(View.GONE);
        if (getLoginStatus()) {
            try {
                String userId = SysApplication.getUserInfo().getUserId();
                List<UploadInfo> list = SysApplication.getDb()
                        .findAll(Selector.from(UploadInfo.class).where("userId", "=", userId));
                if (list != null && list.size() > 0) {
                    mViewBinding.uploadingViewIcon.setVisibility(View.VISIBLE);
                }
//        BadgeFactory.createDot(getContext()).setTextSize(5).setBadgeCount(2).bind(uploadingIcon);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        if (mViewBinding.uploadingViewIcon.getVisibility() == View.VISIBLE
                && frameAnim != null
                && !frameAnim.isRunning()) {
            frameAnim.start();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.MINE.getCode() + "00",
                null, HeaderRequest.GET_PERSONAL_INFO);
    }

    @Override
    public void onStop() {
        if (frameAnim != null && frameAnim.isRunning()) {
            frameAnim.stop();
        }
        super.onStop();
    }

    public void gotoCollect() {
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.MINE.getCode() + "05");
        launchActivity(CollectActivity.class);
    }

    public void gotoChallenge() {
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.MINE.getCode() + "07");
        launchActivity(MyChallengeActivity.class);
    }

    public void gotoOrder() {
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.MINE.getCode() + "08");
        launchActivity(MyOrderActivity.class);
    }

    public void gotoComment() {
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.MINE.getCode() + "06");
        launchActivity(CommentAboutMeActivity.class);
    }

    public void gotoLogin() {
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.MINE.getCode() + "01");
        launchActivityForResult(LoginActivity.class, LOGIN_REQUEST_CODE);
    }

    public void gotoShopCart() {
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.MINE.getCode() + "02");
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, LOGIN_REQUEST_CODE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(H5WebActivity.URL, "file:///android_asset/www/cart.html");
            launchActivityForResult(H5WebActivity.class, bundle, SHOPPING_REQUEST_CODE);
        }
    }

    public void gotoSetting() {
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.MINE.getCode() + "03");
        launchActivityForResult(SettingsActivity.class, SETTING_REQUEST_CODE);
    }

    public void gotoAction() {
        launchActivityForResult(NoticeActivity.class, NOTICE_CODE);
    }

    public void gotoShop() {
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.MINE.getCode() + "11");
//        Bundle bundle = new Bundle();
//        bundle.putString(H5WebActivity.URL, "file:///android_asset/www/my_goods.html");
//        launchActivityForResult(H5WebActivity.class, bundle, HTML_REQUEST_CODE);
        launchActivity(SpinOffActivity.class);
    }

    public void gotoReport() {
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.MINE.getCode() + "12");
        Bundle bundle = new Bundle();
        bundle.putString(H5WebActivity.URL, "file:///android_asset/www/my_report.html");
        launchActivityForResult(H5WebActivity.class, bundle, HTML_REQUEST_CODE);
    }

    public void gotoVipGoods() {
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.MINE.getCode() + "09");
        getActivity().getSharedPreferences(Constant.SP_INFO,
                Context.MODE_PRIVATE).edit().putString("subjectId", "").apply();
        Bundle bundle = new Bundle();
        bundle.putString(H5WebActivity.URL, "file:///android_asset/www/more.html");
        launchActivityForResult(H5WebActivity.class, bundle, HTML_REQUEST_CODE);
    }

    public void gotoVip() {
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.MINE.getCode() + "04");
        launchActivityForResult(VipActivity.class, VIP_REQUEST_CODE);
    }

    public void gotoUploadManager() {
        launchActivity(UploadManagerActivity.class);
    }

    public void changeHeadShot() {
        if (mPopupWindow == null) {
            mPopupWindow = new ShowChoosePhotoPopupWindow(this, this);
        }
        requestRuntimePermission("调用摄像头拍照，请授予相机权限",
                Manifest.permission.CAMERA);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mPopupWindow.show(mViewBinding.getRoot());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SHOPPING_REQUEST_CODE:
                mPresenter.getShoppingAccountRequest();
                break;
            case HTML_REQUEST_CODE:
            case NOTICE_CODE:
                break;
            case LOGIN_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK
                        && mActivity.getUserInfo().getUserType() == 1) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    RadioGroup radioGroup = homeActivity.getTabRg();
                    radioGroup.check(R.id.star_rb);
                    break;
                }
            case SETTING_REQUEST_CODE:
            case VIP_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    loadData();
                } else {
                    mViewBinding.setUserInfo(mActivity.getUserInfo());
                }
                break;
            default:
                mPopupWindow.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void expiresToken() {
        SysApplication.setUserInfo(null);
        mViewBinding.setUserInfo(new UserInfo());
        SharedPreferences sp_Info = getContext().getSharedPreferences(Constant.SP_INFO,
                Context.MODE_PRIVATE);
        sp_Info.edit()
                .putBoolean(Constant.IS_LOGIN, false).apply();
        launchActivityForResult(LoginActivity.class, LOGIN_REQUEST_CODE);
    }

    @Override
    public void showView() {
        try {
            List<ConfigInfo> itemList
                    = SysApplication.getDb().findAll(ConfigInfo.class);
            for (ConfigInfo item : itemList) {
                ImageView imageView = null;
                int errorId = 0;
                switch (item.getName()) {
                    case "PIC_MINE_HDZQ":
                        imageView = mViewBinding.mineAction;
                        errorId = R.drawable.mine_bg1;
                        break;

                    case "PIC_MINE_MXSP":
                        imageView = mViewBinding.mineObj;
                        errorId = R.drawable.mine_bg2;
                        break;

                    case "PIC_MINE_FXBG":
                        imageView = mViewBinding.mineReport;
                        errorId = R.drawable.mine_bg3;
                        break;
                }
                if (imageView == null) {
                    continue;
                }
                Glide.with(this)
                        .load(item.getValue())
                        .centerCrop()
                        .placeholder(R.drawable.default_bg)
                        .error(errorId)
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showShopCartAmount(float totalAmount) {
        mViewBinding.shoppingAccount.setVisibility(View.VISIBLE);
        mViewBinding.shoppingAccount.setText(String.format(
                getString(R.string.order_detail_money), " " + totalAmount));
    }

    @Override
    public void showStarUserInfo(UserInfoResponse res) {
        UserInfo info = mActivity.getUserInfo();
        info.setImgUrl(res.getHeadPic());
        info.setNickName(res.getNickName());
        info.setMemberStatus(res.getMemberStatus());
        mActivity.setUserInfo(info, true);
        mViewBinding.setUserInfo(info);
    }

    @Override
    public void chooseSuccess(Bitmap bm, String path) {
        File file = new File(path);
        if (!file.exists()) {
            showToast(getString(R.string.select_pic_error));
            return;
        }
        mActivity.getUserInfo().setImgUrl(path);
        mViewBinding.setUserInfo(mActivity.getUserInfo());
        mPresenter.saveUserHeadShot(path);
    }
}
