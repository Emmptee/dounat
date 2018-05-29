package com.donut.app.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.manager.RequestManager;
import com.bis.android.plug.refresh_recycler.layoutmanager.FullyLinearLayoutManager;
import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.DetailCommentRecyclerViewAdapter;
import com.donut.app.config.Constant;
import com.donut.app.databinding.ActivityWishDetailBinding;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.CollectRequest;
import com.donut.app.http.message.CommentSubmitRequest;
import com.donut.app.http.message.ContentComments;
import com.donut.app.http.message.PraiseRequest;
import com.donut.app.http.message.ShareRequest;
import com.donut.app.http.message.wish.AchieveWish;
import com.donut.app.http.message.wish.AchieveWishVoice;
import com.donut.app.http.message.wish.AddPlayNumRequest;
import com.donut.app.http.message.wish.WishDetailsRequest;
import com.donut.app.http.message.wish.WishDetailsResponse;
import com.donut.app.model.audio.MediaManager;
import com.donut.app.model.audio.StorageManager;
import com.donut.app.model.video.VideoActivity;
import com.donut.app.utils.GlideCircleTransform;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;
import java.util.Map;

public class WishDetailActivity extends BaseActivity
        implements DetailCommentRecyclerViewAdapter.OnItemClickListener,
        TextView.OnEditorActionListener {

    public static final String CONTENT_ID = "CONTENT_ID";

    private static final int WISH_DETAIL_REQUEST = 1, LIKE_REQUEST = 2,
            COLLECT_REQUEST = 3, ADD_PLAY_NUM = 4, COMMENT_REQUEST = 5;

    private final int REQUEST_CODE_LOGIN = 1, COMMENT_REQUEST_CODE = 2;

    private ActivityWishDetailBinding wishDetailBinding;

    private WishDetailsResponse detailsResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wishDetailBinding
                = DataBindingUtil.setContentView(this, R.layout.activity_wish_detail);
        wishDetailBinding.setHandler(this);
        wishDetailBinding.setUserInfo(getUserInfo());
//        setContentView(R.layout.activity_wish_detail);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        updateHeadTitle("心愿详情", true);

        loadData();
        initEvent();
        initData();
    }

    private void initData() {
        Glide.with(this)
                .load(getUserInfo().getImgUrl())
                .centerCrop()
                .error(R.drawable.default_header)
                .transform(new GlideCircleTransform(this))
                .into(wishDetailBinding.wishBottomUserHeader);
    }

    private void loadData() {
        String uuid = getIntent().getStringExtra(CONTENT_ID);
        WishDetailsRequest request = new WishDetailsRequest();
        request.setB02Id(uuid);
        sendNetRequest(request, HeaderRequest.WISH_DETAIL, WISH_DETAIL_REQUEST);
    }

    private CharSequence temp;
    private int editStart;
    private int editEnd;

    private void initEvent() {
        wishDetailBinding.commentEt.setOnEditorActionListener(this);
        wishDetailBinding.commentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = wishDetailBinding.commentEt.getSelectionStart();
                editEnd = wishDetailBinding.commentEt.getSelectionEnd();
                if (temp.length() > 512) {
                    ToastUtil.showShort(WishDetailActivity.this, getString(R.string.comment_length_tips));
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    wishDetailBinding.commentEt.setText(s);
                    wishDetailBinding.commentEt.setSelection(tempSelection);
                }
            }
        });
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case WISH_DETAIL_REQUEST:
                detailsResponse
                        = JsonUtils.fromJson(response, WishDetailsResponse.class);
                if (COMMON_SUCCESS.equals(detailsResponse.getCode())) {
                    wishDetailBinding.setWish(detailsResponse);

                    if (detailsResponse.getWishType() == 0) {
                        Glide.with(this)
                                .load(detailsResponse.getStarHeadPic())
                                .centerCrop()
                                .error(R.drawable.default_header)
                                .transform(new GlideCircleTransform(this))
                                .into(wishDetailBinding.wishStarHeadImg);

                        String showImgUrl = detailsResponse.getStarHeadPic();
                        if (detailsResponse.getAchievePicUrl() != null
                                && detailsResponse.getAchievePicUrl().length() > 0) {
                            showImgUrl = detailsResponse.getAchievePicUrl();
                        }
                        Glide.with(this)
                                .load(showImgUrl)
                                .centerCrop()
                                .into(wishDetailBinding.wishShowImg);

                        Glide.with(this)
                                .load(detailsResponse.getStarHeadPic())
                                .centerCrop()
                                .error(R.drawable.default_header)
                                .transform(new GlideCircleTransform(this))
                                .into(wishDetailBinding.wishStarHeadImg2);

                    } else {
                        Glide.with(this)
                                .load(R.drawable.icon_logo)
                                .centerCrop()
                                .transform(new GlideCircleTransform(this))
                                .into(wishDetailBinding.wishStarHeadImg);

                        Glide.with(this)
                                .load(detailsResponse.getAchievePicUrl())
                                .error(R.drawable.wish_fulfill_top_bg)
                                .centerCrop()
                                .into(wishDetailBinding.wishShowImg);

                        Glide.with(this)
                                .load(R.drawable.icon_logo)
                                .centerCrop()
                                .transform(new GlideCircleTransform(this))
                                .into(wishDetailBinding.wishStarHeadImg2);
                    }


                    Glide.with(this)
                            .load(detailsResponse.getHeadPic())
                            .centerCrop()
                            .error(R.drawable.default_header)
                            .transform(new GlideCircleTransform(this))
                            .into(wishDetailBinding.wishUserHeadImg);
                    showCommentView(detailsResponse.getCurrentComments());
                } else {
                    showToast(detailsResponse.getMsg());
                }
                break;

            case LIKE_REQUEST:
                BaseResponse praiseRes = JsonUtils.fromJson(response,
                        BaseResponse.class);
                if (!COMMON_SUCCESS.equals(praiseRes.getCode())) {
                    showToast(praiseRes.getMsg());
                }
                break;
            case COLLECT_REQUEST:
                BaseResponse collectRes = JsonUtils.fromJson(response,
                        BaseResponse.class);
                if (!COMMON_SUCCESS.equals(collectRes.getCode())) {
                    showToast(collectRes.getMsg());
                }
                break;
            case COMMENT_REQUEST:
                BaseResponse commentRes = JsonUtils.fromJson(response,
                        BaseResponse.class);
                if (COMMON_SUCCESS.equals(commentRes.getCode())) {
                    wishDetailBinding.commentEt.setText("");
                    loadData();
                } else {
                    showToast(commentRes.getMsg());
                }
                break;
        }
    }

    private void showCommentView(List<ContentComments> comments) {

        DetailCommentRecyclerViewAdapter adapter
                = new DetailCommentRecyclerViewAdapter(this, comments, this);

        RecyclerView view = wishDetailBinding.wishCommentList;
        view.setAdapter(adapter);
        view.setLayoutManager(new FullyLinearLayoutManager(this));

    }

    public void onPlayClick(AchieveWish model) {
        Intent intent = new Intent(this, VideoActivity.class);
//        intent.putExtra(VideoActivity.VIDEONAME, model.getName());
        intent.putExtra(VideoActivity.VIDEOURL, model.getAchieveVideoUrl());
        startActivity(intent);
        model.setVideoPlayTimes(model.getVideoPlayTimes() + 1);

        AddPlayNumRequest request = new AddPlayNumRequest();
        request.setMediaId(model.getG01Id());
        request.setIdType(1);
        sendNetRequest(request, HeaderRequest.ADD_PLAY_NUM, ADD_PLAY_NUM,
                false);
    }

    public void onCollectClick(AchieveWish model) {
        if (!getLoginStatus()) {
            toLogin();
        } else {
            boolean collect;
            if (model.getCollectionStatus() == 0) {
                model.setCollectionStatus(1);
                model.setCollectTimes(model.getCollectTimes() + 1);
                collect = true;
            } else {
                model.setCollectionStatus(0);
                model.setCollectTimes(model.getCollectTimes() - 1);
                collect = false;
            }

            CollectRequest request = new CollectRequest();
            request.setContentId(model.getB02Id());
            request.setType(5);
            request.setStatus(collect ? 1 : 0);
            sendNetRequest(request, HeaderRequest.SUBJECT_COLLECT, COLLECT_REQUEST,
                    false);
        }
    }

    public void onLikeClick(AchieveWish model) {
        if (!getLoginStatus()) {
            toLogin();
            return;
        }

        boolean like;
        if (model.getPraiseStatus() == 0) {
            model.setPraiseStatus(1);
            model.setPraiseTimes(model.getPraiseTimes() + 1);
            like = true;
        } else {
            model.setPraiseStatus(0);
            model.setPraiseTimes(model.getPraiseTimes() - 1);
            like = false;
        }

        PraiseRequest request = new PraiseRequest();
        request.setContentId(model.getB02Id());
        request.setPraiseType(like ? 1 : 2);
        sendNetRequest(request, HeaderRequest.SUBJECT_PRAISE, LIKE_REQUEST,
                false);
    }

    public void onShareClick(final AchieveWish model) {
        requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取存储器读写权限", Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);

        final AchieveWish model = wishDetailBinding.getWish();
        String url = RequestUrl.WISH_DETAIL_SHARE_URL + "header="
                + HeaderRequest.WISH_DETAIL + "&b02Id=" + model.getB02Id();
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
        ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(this);

        String strStarName = model.getStarName();
        if(model.getWishType()!=0){
            strStarName = getString(R.string.app_name);
        }
        String strPlayTimes = "";
        if (model.getVideoPlayTimes() > 0) {
            strPlayTimes = "|小伙伴已围观了" + model.getVideoPlayTimes() + "次";
        }

        builder.setTitle(strStarName + strPlayTimes);
        builder.setContent("我在甜麦圈发起的心愿已被" + strStarName + "达成，快来围观吧！");
        builder.setLinkUrl(url);
//        builder.setImgUrl(model.get);
        builder.setBitmap(bmp);
        builder.setShareMedia(new SHARE_MEDIA[]{SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA});
//        builder.setListener(new ShareBuilderCommonUtil.OnResponseListener() {
//            @Override
//            public void onSuccessRes() {
//            }
//        });
        builder.create();
        ShareRequest request = new ShareRequest();
        request.setContentId(model.getB02Id());
        sendNetRequest(request, HeaderRequest.SHARE);

        model.setShareTimes(model.getShareTimes() + 1);
    }

    public void onCommentClick(AchieveWish model) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(CommentActivity.CONTENTID, model.getB02Id());
        startActivityForResult(intent, COMMENT_REQUEST_CODE);
    }

    public void onAddCommentClick(AchieveWish model) {
        if (!getLoginStatus()) {
            toLogin();
            return;
        }

        wishDetailBinding.commentEt.requestFocus();
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(wishDetailBinding.commentEt, 0);
    }

    public void onPlayAudioClick(View v, AchieveWish model) {

        AddPlayNumRequest request = new AddPlayNumRequest();
        request.setMediaId(model.getAchieveVoiceList().get(0).getG02Id());
        request.setIdType(0);
        sendNetRequest(request, HeaderRequest.ADD_PLAY_NUM, ADD_PLAY_NUM,
                false);

        final View playAnim = wishDetailBinding.wishFulfillAudioPlayAnim;

        AchieveWishVoice voice = model.getAchieveVoiceList().get(0);
        new StorageManager(this).getInfo(voice.getAchieveVoiceUrl(), voice.getLastTime(),
                new RequestManager.RequestListener() {
                    @Override
                    public void onRequest() {
                    }

                    @Override
                    public void onLoading(long l, long l1, String s) {
                    }

                    @Override
                    public void onSuccess(String response, Map<String, String> headers,
                                          String url, int actionId) {
                        playAnim.setBackgroundResource(R.drawable.play_anim);
                        final AnimationDrawable animationDrawable
                                = (AnimationDrawable) playAnim.getBackground();
                        animationDrawable.start();
                        MediaManager.playSound(response, new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                animationDrawable.stop();
                                playAnim.setBackgroundResource(R.drawable.voice_gray);
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMsg, String url, int actionId) {
                    }
                });

        voice.setListenTimes(voice.getListenTimes() + 1);
    }

    private void toLogin() {
        startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_CODE_LOGIN);
    }

    public void onSendComment(AchieveWish model){
        if (TextUtils.isEmpty(wishDetailBinding.commentEt.getText().toString().trim()))
        {
            showToast(getString(R.string.comment_empty_tips));
            return;
        }
        CommentSubmitRequest request = new CommentSubmitRequest();
        request.setContentId(model.getB02Id());
        request.setOperationType("0");
        request.setContent(wishDetailBinding.commentEt.getText().toString());
        sendNetRequest(request, HeaderRequest.SUBJECT_COMMENT_SUBMIT, COMMENT_REQUEST,
                true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    //
                    wishDetailBinding.setUserInfo(getUserInfo());
                }
                break;
            case COMMENT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    loadData();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }

    @Override
    public void OnCommentItemClick() {
        onCommentClick(detailsResponse);
    }

    @Override
    protected boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            View bottomView = wishDetailBinding.bottomComment;
            int[] l = {0, 0};
            bottomView.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + bottomView.getHeight(), right = left
                    + bottomView.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
          /*判断是否是“send”键*/
//        if (actionId == EditorInfo.IME_ACTION_SEND)
//        {
        if (actionId == 0 && event.getAction() == 0) {
            /*隐藏软键盘*/
            InputMethodManager imm = (InputMethodManager) v
                    .getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(
                        v.getApplicationWindowToken(), 0);
            }
            // 提交评论
            onSendComment(wishDetailBinding.getWish());
            return true;
        }
        return false;
    }
}
