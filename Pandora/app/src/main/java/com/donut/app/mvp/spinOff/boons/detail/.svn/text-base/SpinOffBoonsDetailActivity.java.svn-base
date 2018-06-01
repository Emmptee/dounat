package com.donut.app.mvp.spinOff.boons.detail;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bis.android.plug.refresh_recycler.decoration.DividerGridItemDecoration;
import com.bis.android.plug.refresh_recycler.decoration.Y_Divider;
import com.bis.android.plug.refresh_recycler.decoration.Y_DividerBuilder;
import com.bis.android.plug.refresh_recycler.decoration.Y_DividerItemDecoration;
import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseGridLayoutManager;
import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.donut.app.R;
import com.donut.app.activity.CommentActivity;
import com.donut.app.activity.LoginActivity;
import com.donut.app.config.Constant;
import com.donut.app.customview.ninegrid.ImageInfo;
import com.donut.app.customview.ninegrid.preview.ImagePreviewActivity;
import com.donut.app.databinding.ActivitySpinOffBoonsDetailBinding;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.spinOff.ExpressionPics;
import com.donut.app.http.message.spinOff.ExpressionPicsDetailResponse;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.spinOff.boons.SpinOffBoonsFragment;
import com.donut.app.utils.FileUtils;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Qi on 2017/6/8.
 * Description : 页面<br>
 */
public class SpinOffBoonsDetailActivity extends MVPBaseActivity<ActivitySpinOffBoonsDetailBinding, SpinOffBoonsDetailPresenter>
        implements SpinOffBoonsDetailContract.View,
        SpinOffBoonsDetailAdapter.OnItemClickListener {

    public static final String B02_ID = "B02_ID";

    private List<ExpressionPics> pics = new ArrayList<>();

    private SpinOffBoonsDetailAdapter mAdapter;

    private String b02Id;

    public static final int REQUEST_CODE_LOGIN = 1, COMMENT_REQUEST_CODE = 2;

    public static final int PERMISSION_CODE_DOWNLOAD = 1, PERMISSION_CODE_SHARE = 2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_spin_off_boons_detail;
    }

    @Override
    protected void initView() {
        StatusBarCompat.setStatusBarColorForCollapsingToolbar(this,
                mViewBinding.appbarLayout,
                mViewBinding.collapsingToolbar,
                mViewBinding.toolbar,
                Constant.default_bar_color);
        mViewBinding.setHandler(this);

        mAdapter = new SpinOffBoonsDetailAdapter(pics, this);
        mViewBinding.detailList.setAdapter(mAdapter);
        mViewBinding.detailList.setHasFixedSize(true);
        mViewBinding.detailList.setLayoutManager(getLayoutManager());
        mViewBinding.detailList.addItemDecoration(new DividerItemDecoration(this));
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        return new ABaseGridLayoutManager(this, 3);
    }

    @Override
    protected void initEvent() {
    }

    @Override
    public void loadData() {
        b02Id = getIntent().getStringExtra(B02_ID);
        mPresenter.loadData(true, b02Id);
    }

    @Override
    public void showView(final ExpressionPicsDetailResponse detail) {
        pics.clear();
        mViewBinding.setDetail(detail);
        List list = detail.getExpressionPics();
        if (list != null && list.size() > 0) {
            pics.addAll(detail.getExpressionPics());
            mAdapter.notifyDataSetChanged();
        }
    }

    public void viewBack() {
        super.onBackPressed();
    }

    @Override
    public void OnItemClick(int position) {

        List<ImageInfo> images = new ArrayList<>();

        for (ExpressionPics pic : pics) {
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setBigImageUrl(pic.getGifUrl());
            imageInfo.setThumbnailUrl(pic.getPicUrl());
            images.add(imageInfo);
        }
//        for (int i = 0; i < imageInfo.size(); i++) {
//            ImageInfo info = imageInfo.get(i);
//            View imageView;
//            if (i < nineGridView.getMaxSize()) {
//                imageView = nineGridView.getChildAt(i);
//            } else {
//                //如果图片的数量大于显示的数量，则超过部分的返回动画统一退回到最后一个图片的位置
//                imageView = nineGridView.getChildAt(nineGridView.getMaxSize() - 1);
//            }
//            info.imageViewWidth = imageView.getWidth();
//            info.imageViewHeight = imageView.getHeight();
//            int[] points = new int[2];
//            imageView.getLocationInWindow(points);
//            info.imageViewX = points[0];
//            info.imageViewY = points[1] - statusHeight;
//        }
//

        Bundle bundle = new Bundle();
        bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) images);
        bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, position);

        launchActivity(ImagePreviewActivity.class, bundle);
        this.overridePendingTransition(0, 0);
    }

    public void onDownloadAll() {

        String[] permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this.getContext(), permission)) {
            onPermissionsGranted(PERMISSION_CODE_DOWNLOAD, Arrays.asList(permission));
        } else {
            EasyPermissions.requestPermissions(this, "为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                    PERMISSION_CODE_DOWNLOAD, permission);
        }
    }

    public void onLikeClick(ExpressionPicsDetailResponse model) {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        if (model.getPraiseStatus() == 0) {
            model.setPraiseStatus(1);
            model.setPraiseTimes(model.getPraiseTimes() + 1);
            mPresenter.onLike(b02Id, true);
        } else {
            model.setPraiseStatus(0);
            model.setPraiseTimes(model.getPraiseTimes() - 1);
            mPresenter.onLike(b02Id, false);
        }
    }

    public void onCommentClick(ExpressionPicsDetailResponse detail) {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(CommentActivity.CONTENTID, b02Id);
        launchActivityForResult(CommentActivity.class, bundle, COMMENT_REQUEST_CODE);

    }


    public void onShareClick(ExpressionPicsDetailResponse detail) {

        String[] permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this.getContext(), permission)) {
            onPermissionsGranted(PERMISSION_CODE_SHARE, Arrays.asList(permission));
        } else {
            EasyPermissions.requestPermissions(this, "为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                    PERMISSION_CODE_SHARE, permission);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        if (requestCode == PERMISSION_CODE_DOWNLOAD) {

            ExpressionPics[] picsArray = pics.toArray(new ExpressionPics[pics.size()]);

            DownloadPicTask task = new DownloadPicTask();
            task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, picsArray);
        } else if (requestCode == PERMISSION_CODE_SHARE) {
            ExpressionPicsDetailResponse model = mViewBinding.getDetail();

            String linkUrl = RequestUrl.SPIN_OFF_BOONS_PIC_SHARE_URL
                    + "header=00010323&b02Id="
                    + b02Id;

            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
            ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(getContext());
            builder.setTitle(model.getActorName() + "|" + model.getName());
            builder.setContent("爱豆给你的特别福利，快去甜麦圈围观吧！");
            builder.setLinkUrl(linkUrl);
            builder.setBitmap(bmp);
            builder.setShareMedia(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                    SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA});
            builder.create();

            mPresenter.shareRequest(b02Id);
            model.setShareTimes(model.getShareTimes() + 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
            case COMMENT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    loadData();
                }
                break;
        }
    }

    private class DownloadPicTask extends AsyncTask<ExpressionPics, Integer, Boolean> {

        private String path = FileUtils.getSDDir(getString(R.string.app_name)
                        + File.separator
                        + getString(R.string.image),
                mContext);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showToast(getString(R.string.download_start));
        }

        @Override
        protected Boolean doInBackground(ExpressionPics... params) {

            boolean createStatus = false;

            int picSize = params.length;
            if (picSize > 0) {
                for (ExpressionPics pic : params) {
                    try {
                        File file = Glide
                                .with(SpinOffBoonsDetailActivity.this)
                                .load(pic.getGifUrl())
                                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .get();

                        createStatus = FileUtils.copyFile(file, new File(path, UUID.randomUUID() + ".gif"));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
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

    private class DividerItemDecoration extends Y_DividerItemDecoration {

        private DividerItemDecoration(Context context) {
            super(context);
        }

        @Override
        public Y_Divider getDivider(int itemPosition) {
            Y_Divider divider = null;
            switch (itemPosition % 3) {
                case 0:
                case 1:
                    //每一行第一个和第二个显示rignt和bottom
                    divider = new Y_DividerBuilder()
                            .setRightSideLine(true, 0xffffffff, 7.5f, 0, 0)
                            .setBottomSideLine(true, 0xffffffff, 7.5f, 0, 0)
                            .create();
                    break;
                case 2:
                    //最后一个只显示bottom
                    divider = new Y_DividerBuilder()
                            .setBottomSideLine(true, 0xffffffff, 7.5f, 0, 0)
                            .create();
                    break;
                default:
                    break;
            }
            return divider;
        }
    }
}