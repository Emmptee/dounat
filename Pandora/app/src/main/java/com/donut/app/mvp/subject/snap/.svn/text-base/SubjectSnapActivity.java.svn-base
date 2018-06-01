package com.donut.app.mvp.subject.snap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bis.android.plug.imagecyclelibrary.ImageCycleView;
import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.CommentActivity;
import com.donut.app.activity.H5WebActivity;
import com.donut.app.activity.LoginActivity;
import com.donut.app.activity.RewardActivity;
import com.donut.app.activity.StarDetailActivity;
import com.donut.app.adapter.DetailCommentRecyclerViewAdapter;
import com.donut.app.config.Constant;
import com.donut.app.customview.DonutJCVideoPlayer;
import com.donut.app.customview.ninegrid.ImageInfo;
import com.donut.app.customview.ninegrid.NineGridView;
import com.donut.app.customview.ninegrid.preview.ImagePreviewActivity;
import com.donut.app.databinding.ActivitySubjectSnapBinding;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.subjectSnap.SubjectSnapDetailResponse;
import com.donut.app.model.video.DensityUtil;
import com.donut.app.mvp.MVPBaseActivity;
import com.donut.app.mvp.blooper.detail.BlooperDetailActivity;
import com.donut.app.utils.BindingUtils;
import com.donut.app.utils.GlideRoundTransform;
import com.donut.app.utils.status_bar.StatusBarCompat;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by Qi on 2017/3/16.
 * Description : 街拍街拍<br>
 */
public class SubjectSnapActivity extends MVPBaseActivity<ActivitySubjectSnapBinding, SubjectSnapPresenter>
        implements SubjectSnapContract.View, DonutJCVideoPlayer.OnBackBtnClickListener {

    public static final String SUBJECT_ID = "SUBJECT_ID";

    private static final String TAG = "SubjectSnapActivity";

    public static final int REQUEST_CODE_LOGIN = 1, COMMENT_REQUEST_CODE = 2,
            REWARD_REQUEST_CODE = 3, STAR_DETAIL_CODE = 4;

    private CharSequence temp;

    private int editStart, editEnd;

    private boolean hasPlayed;

    private ImageCycleView.ImageCycleViewListener mCycleViewListener;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_subject_snap;
    }

    @Override
    protected void initView() {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)) {
            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams) mViewBinding.videoCoverBack.getLayoutParams();
            params.setMargins(0, getStatusBarHeight(this), 0, 0);
        }
        StatusBarCompat.translucentStatusBar(this, false);
        float videoWidth = DensityUtil.getWidthInPx(this);
        float videoHeight = videoWidth * 9 / 16;
        ViewGroup videoLayout = mViewBinding.videoTopLayout;
        android.view.ViewGroup.LayoutParams params = videoLayout.getLayoutParams();
        params.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = (int) videoHeight;
        videoLayout.setLayoutParams(params);

        mViewBinding.setHandler(this);
        mViewBinding.setUserInfo(getUserInfo());
        BindingUtils.loadRoundImg(mViewBinding.commentBottomLayout.includeBottomUserHeader,
                getUserInfo().getImgUrl());
    }

    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }

    @Override
    protected void initEvent() {
        mViewBinding.intoGoodsTv.setOnTouchListener(new View.OnTouchListener() {

            RelativeLayout.LayoutParams params
                    = (RelativeLayout.LayoutParams) mViewBinding.intoGoodsTv.getLayoutParams();
            float scale = getResources().getDisplayMetrics().density;
            int marginLeft = (int) (0 - (40 * scale + 0.5f));

            float x = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int left = (int) (marginLeft - (x - event.getX()) / 5);
                        if (left > 0) {
                            left = 0;
                        }
                        params.setMargins(left, params.topMargin, 0, 0);
                        break;
                    case MotionEvent.ACTION_UP:
                        params.setMargins(marginLeft, params.topMargin, 0, 0);
//                        if (event.getEventTime() - event.getDownTime() < 300
//                                && (0 == (x-event.getX()))) {
//                            //点击事件
//                            showGoodsView();
//                        }
                        showGoodsView();
                        break;
                }
                mViewBinding.intoGoodsTv.setLayoutParams(params);
                return true;
            }
        });
        final TextView mCommentBtnSend = mViewBinding.commentBottomLayout.includeCommentBtnSend;
        mCommentBtnSend.setOnClickListener(this);
        final EditText mCommentEt = mViewBinding.commentBottomLayout.includeCommentEt;
        mCommentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = mCommentEt.getSelectionStart();
                editEnd = mCommentEt.getSelectionEnd();
                if (temp.length() > 512) {
                    showToast(getString(R.string.comment_length_tips));
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    mCommentEt.setText(s);
                    mCommentEt.setSelection(tempSelection);
                }

                if (temp.length() > 0) {
                    mCommentBtnSend.setText(getString(R.string.send_msg));
                } else {
                    if (mViewBinding.getSnap() == null) {
                        return;
                    }
                    mCommentBtnSend.setText(
                            String.format(getString(R.string.comment_times),
                                    mViewBinding.getSnap().getCommentTimes()));
                }
            }
        });
    }

    @Override
    public void loadData() {
        //"aebc588491274d378b9e3ec79200fdeb"
        mPresenter.loadData(true, getIntent().getStringExtra(SUBJECT_ID));
    }

    @Override
    public void showView(final SubjectSnapDetailResponse detail) {
        mViewBinding.setSnap(detail);

        BindingUtils.loadImg(mViewBinding.videoCoverImg, detail.getPublicPic());
        BindingUtils.loadRoundImg(mViewBinding.ivSnapStarHead, detail.getHeadPic());

        if (detail.getAdList() != null) {
            ArrayList<String> periodList = new ArrayList<>();
            ArrayList<String> urlList = new ArrayList<>();
            final List<ImageInfo> images = new ArrayList<>();
            for (SubjectSnapDetailResponse.Advertisement ad :
                    detail.getAdList()) {
                periodList.add(ad.getTitle() + "\n" + ad.getDescription());
                urlList.add(ad.getImgUrl());

                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setBigImageUrl(ad.getImgUrl() == null ? "" : ad.getImgUrl());
                imageInfo.setDescription(ad.getTitle() + "\n" + ad.getDescription());
                images.add(imageInfo);
            }
            if (mCycleViewListener == null) {
                if (NineGridView.getImageLoader() == null) {
                    NineGridView.setImageLoader(new NineGridView.ImageLoader() {
                        @Override
                        public void onDisplayImage(Context context, ImageView imageView, String url) {
                            BindingUtils.loadImg(imageView, url);
                        }

                        @Override
                        public Bitmap getCacheImage(String url) {
                            return null;
                        }
                    });
                }
                mCycleViewListener
                        = new ImageCycleView.ImageCycleViewListener() {
                    @Override
                    public void displayImage(String imageURL, ImageView imageView) {
//                    BindingUtils.loadImg(imageView, imageURL);
                        Glide.with(SubjectSnapActivity.this)
                                .load(imageURL)
                                .transform(new GlideRoundTransform(getContext(), 3))
                                .placeholder(R.drawable.default_bg)
                                .error(R.drawable.default_bg)
                                .into(imageView);
                    }

                    @Override
                    public void onImageClick(int position, View imageView) {
//                        SubjectSnapDetailResponse.Advertisement ad = detail.getAdList().get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) images);
                        bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, position);

                        launchActivity(ImagePreviewActivity.class, bundle);
                        SubjectSnapActivity.this.overridePendingTransition(0, 0);
                    }
                };
            }
            mViewBinding.snapImgAds.setImageResources(periodList, urlList, mCycleViewListener);
        }

        if (detail.getPortraitList() != null) {
            if (NineGridView.getImageLoader() == null) {
                NineGridView.setImageLoader(new NineGridView.ImageLoader() {
                    @Override
                    public void onDisplayImage(Context context, ImageView imageView, String url) {
                        BindingUtils.loadImg(imageView, url);
                    }

                    @Override
                    public Bitmap getCacheImage(String url) {
                        return null;
                    }
                });
            }
            final List<ImageInfo> images = new ArrayList<>();
            for (SubjectSnapDetailResponse.Advertisement ad :
                    detail.getPortraitList()) {
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setBigImageUrl(ad.getImgUrl() == null ? "" : ad.getImgUrl());
                images.add(imageInfo);
            }
            PortraitAdapter adapter
                    = new PortraitAdapter(detail.getPortraitList(), new PortraitAdapter.ItemListener() {
                @Override
                public void onItemClick(int position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) images);
                    bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, position);

                    launchActivity(ImagePreviewActivity.class, bundle);
                    SubjectSnapActivity.this.overridePendingTransition(0, 0);
                }
            });

            RecyclerView view = mViewBinding.snapPortraits;
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            view.setLayoutManager(manager);
            view.setNestedScrollingEnabled(false);
            view.setHasFixedSize(false);
            view.setAdapter(adapter);
        }

        DetailCommentRecyclerViewAdapter.OnItemClickListener listener
                = new DetailCommentRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void OnCommentItemClick() {
                onToCommentClick();
            }
        };
        DetailCommentRecyclerViewAdapter adapter
                = new DetailCommentRecyclerViewAdapter(this, detail.getCurrentComments(), listener);

        RecyclerView view = mViewBinding.snapCommentList;
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setNestedScrollingEnabled(false);
        view.setHasFixedSize(false);
        view.setAdapter(adapter);

        EditText mCommentEt = mViewBinding.commentBottomLayout.includeCommentEt;
        if (mCommentEt.getText().length() > 0) {
            mCommentEt.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mViewBinding.commentBottomLayout.includeCommentBtnSend.setText(getString(R.string.send_msg));
                }
            }, 100);
        }
    }

    @Override
    public void clearCommentEdit() {
        if (mViewBinding.getSnap() == null) {
            return;
        }
        mViewBinding.commentBottomLayout
                .includeCommentEt.setText("");
        mViewBinding.commentBottomLayout
                .includeCommentBtnSend.setText(
                String.format(getString(R.string.comment_times),
                        mViewBinding.getSnap().getCommentTimes()));
        loadData();
    }

    @Override
    protected void onStop() {
        mPresenter.saveBehaviour("xx");
        super.onStop();
        mViewBinding.snapImgAds.pushImageCycle();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.saveBehaviour("00", mPresenter.requestObject, HeaderRequest.SUBJECT_SNAP_DETAIL);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.include_comment_btn_send:
                String msg = mViewBinding.commentBottomLayout
                        .includeCommentEt.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {
                    onToCommentClick();
                    break;
                }
                if (!getLoginStatus()) {
                    launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
                    break;
                }
                mPresenter.sendComment(mViewBinding.getSnap(), msg);
                mPresenter.saveBehaviour("05");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        mPresenter.saveBehaviour("06");
        Intent intent = new Intent();
        if (mViewBinding.getSnap() != null) {
            intent.putExtra(Constant.COLLECT_STATUS,
                    mViewBinding.getSnap().getCollectionStatus());
        }
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void backBtnClick() {
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewBinding.getSnap() != null && hasPlayed) {
            onPlayClick();
        }
    }

    private void showGoodsView() {
        sp_Info.edit().putString("subjectId", getIntent().getStringExtra(SUBJECT_ID)).apply();
        Bundle bundle = new Bundle();
        bundle.putString(H5WebActivity.URL, "file:///android_asset/www/more.html");
        launchActivity(H5WebActivity.class, bundle);
        overridePendingTransition(R.anim.first_left_in, R.anim.first_left_out);
        mPresenter.saveBehaviour("01");
    }

    public void onPlayClick() {
        if (mViewBinding.getSnap() == null) {
            return;
        }
        hasPlayed = true;
        mViewBinding.videoCover.setVisibility(View.GONE);
        mViewBinding.snapVideoPlayer.setVisibility(View.VISIBLE);
        DonutJCVideoPlayer videoPlayer = mViewBinding.snapVideoPlayer;
        videoPlayer.setUp(mViewBinding.getSnap().getPlayUrl(),
                DonutJCVideoPlayer.SCREEN_LAYOUT_NORMAL,
                mViewBinding.getSnap().getName(),
                mViewBinding.getSnap().getBrowseTimes(),
                this);
        videoPlayer.startButton.performClick();
//        mPresenter.addPlayNum(mViewBinding.getSnap().getContentId());
    }

    public void onBackClick() {
        onBackPressed();
    }

    public void onStarDetailClick(SubjectSnapDetailResponse model) {
        Log.i(TAG, "onStarDetailClick: 明星空间执行了跳转");
      /*  Bundle bundle = new Bundle();
        ssbundle.putString(StarDetailActivity.STAR_ID, model.getActorId());
        launchActivityForResult(StarDetailActivity.class, bundle, STAR_DETAIL_CODE);*/
        Bundle bundle = new Bundle();
        bundle.putString(BlooperDetailActivity.STAR_ID, model.getActorId());
        launchActivity(BlooperDetailActivity.class, bundle);
        mPresenter.saveBehaviour("03");
    }

    public void onFollowClick(SubjectSnapDetailResponse model) {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
        } else {
            if (model.getFollowStatus() == 0) {
                model.setFollowStatus(1);
                mPresenter.onToFollow(model, true);
            } else {
                model.setFollowStatus(0);
                mPresenter.onToFollow(model, false);
            }
        }
    }

    public void onCollectClick(SubjectSnapDetailResponse model) {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
        } else {
            if (model.getCollectionStatus() == 0) {
                model.setCollectionStatus(1);
                model.setCollectTimes(model.getCollectTimes() + 1);
                mPresenter.onToCollect(model, true);
            } else {
                model.setCollectionStatus(0);
                model.setCollectTimes(model.getCollectTimes() - 1);
                mPresenter.onToCollect(model, false);
            }
        }
    }

    public void onLikeClick(SubjectSnapDetailResponse model) {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        if (model.getPraiseStatus() == 0) {
            model.setPraiseStatus(1);
            model.setPraiseTimes(model.getPraiseTimes() + 1);
            mPresenter.onLike(model, true);
        } else {
            model.setPraiseStatus(0);
            model.setPraiseTimes(model.getPraiseTimes() - 1);
            mPresenter.onLike(model, false);
        }
    }

    public void onRewardClick() {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        if (mViewBinding.getSnap() == null) {
            return;
        }
        SubjectSnapDetailResponse model = mViewBinding.getSnap();
        Bundle reward_bun = new Bundle();
        reward_bun.putString(RewardActivity.PAYEE_NAME, model.getActorName());
        reward_bun.putString(RewardActivity.PAYEE_HEADER, model.getHeadPic());
        reward_bun.putString(RewardActivity.CONTENT_ID, model.getContentId());
        reward_bun.putBoolean(RewardActivity.IS_STAR, getUserInfo().getUserType() == 1);
        launchActivityForResult(RewardActivity.class, reward_bun, REWARD_REQUEST_CODE);
    }

    public void onShareClick() {
        requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void onToCommentClick() {
        if (!getLoginStatus()) {
            launchActivityForResult(LoginActivity.class, REQUEST_CODE_LOGIN);
            return;
        }
        if (mViewBinding.getSnap() == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(CommentActivity.CONTENTID, mViewBinding.getSnap().getContentId());
        launchActivityForResult(CommentActivity.class, bundle, COMMENT_REQUEST_CODE);
        mPresenter.saveBehaviour("04");
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        if (mViewBinding.getSnap() == null) {
            return;
        }
        String linkurl = RequestUrl.SUBJECT_SNAP_SHARE_URL + "header="
                + HeaderRequest.SUBJECT_SNAP_DETAIL + "&subjectId=" + getIntent().getStringExtra(SUBJECT_ID);

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
        ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(this);

        builder.setTitle(mViewBinding.getSnap().getActorName()
                + ",【小伙伴】已围观了" + mViewBinding.getSnap().getBrowseTimes() + "次");
        builder.setContent(mViewBinding.getSnap().getName());
        builder.setLinkUrl(linkurl);
        builder.setBitmap(bmp);
        builder.setShareMedia(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA});
//        builder.setListener(new ShareBuilderCommonUtil.OnResponseListener() {
//            @Override
//            public void onSuccessRes() {
//
//            }
//        });
        builder.create();
        mPresenter.shareRequest(mViewBinding.getSnap().getContentId());
        mViewBinding.getSnap().setShareTimes(mViewBinding.getSnap().getShareTimes() + 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                if (resultCode == RESULT_OK) {
                    refreshView();
                }
                break;
            case REWARD_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String content = data.getStringExtra(RewardActivity.REWARD_CONTENT);
                        if (!TextUtils.isEmpty(content)) {
                            refreshView();
                            break;
                        }
                        float amount = data.getFloatExtra(RewardActivity.REWARD_AMOUNT, 0f);
                        mViewBinding.getSnap().setRewardTimes(
                                (long) (mViewBinding.getSnap().getRewardTimes() + amount));
                    } else {
                        refreshView();
                    }
                }
                break;
            case STAR_DETAIL_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        int followStatus = data.getIntExtra(StarDetailActivity.FOLLOW_STATUS, 0);
                        mViewBinding.getSnap().setFollowStatus(followStatus);
                    }
                }
                break;
        }
    }

    private void refreshView() {
        JCVideoPlayer.releaseAllVideos();
        mViewBinding.videoCover.setVisibility(View.VISIBLE);
        mViewBinding.snapVideoPlayer.setVisibility(View.GONE);
        loadData();
    }

}