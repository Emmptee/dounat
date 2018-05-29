package com.donut.app.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.bis.android.sharelibrary.ShareBuilderCommonUtil;
import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.activity.CommentActivity;
import com.donut.app.activity.LoginActivity;
import com.donut.app.adapter.WishFulfillRecyclerViewAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.entity.UserInfo;
import com.donut.app.fragment.base.BaseFragment;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.CollectRequest;
import com.donut.app.http.message.PraiseRequest;
import com.donut.app.http.message.ShareRequest;
import com.donut.app.http.message.wish.AchieveWish;
import com.donut.app.http.message.wish.AddPlayNumRequest;
import com.donut.app.http.message.wish.WishFulfillResponse;
import com.donut.app.http.message.wish.WishRequest;
import com.donut.app.model.audio.MediaManager;
import com.donut.app.model.video.VideoActivity;
import com.donut.app.mvp.wish.user.WishUserActivity;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.L;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

public class WishFulfillFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener,
        WishFulfillRecyclerViewAdapter.OnAdapterInteractionListener {

    private SwipeRefreshLayout mSrl;

    private RecyclerView mRecyclerView;

    private TextView mNoData;

    private int page = 0, rows = 10;

    private static final int WISH_FULFILL_REQUEST = 1, LIKE_REQUEST = 2,
            COLLECT_REQUEST = 3, ADD_PLAY_NUM = 4;

    private final List<AchieveWish> mWishList = new ArrayList<>();

    private WishFulfillRecyclerViewAdapter mAdapter;

    private final int REQUEST_CODE_LOGIN = 1, COMMENT_REQUEST_CODE = 2;

    public WishFulfillFragment() {
    }

    public static WishFulfillFragment newInstance() {
//        WishFulfillFragment fragment = new WishFulfillFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return new WishFulfillFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//        }

        loadData(true);
        saveBehaviour("02", request, HeaderRequest.WISH_FULFILL);
    }

    private WishRequest request;

    private void loadData(boolean showLoading) {
        request = new WishRequest();
        request.setSortType(0);
        request.setPage(page);
        request.setRows(rows);
        sendNetRequest(request, HeaderRequest.WISH_FULFILL, WISH_FULFILL_REQUEST, showLoading);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wish_fulfill_list, container, false);

        mSrl = (SwipeRefreshLayout) view.findViewById(R.id.wish_fulfill_list_srl);
        mSrl.setColorSchemeResources(R.color.refresh_tiffany);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.wish_fulfill_list);
        mNoData = (TextView) view.findViewById(R.id.no_data);
        mSrl.setOnRefreshListener(this);

        boolean login = getContext().getSharedPreferences(Constant.SP_INFO,
                Context.MODE_PRIVATE).getBoolean(Constant.IS_LOGIN, false);

        mAdapter = new WishFulfillRecyclerViewAdapter(getContext(), login, mWishList, this);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter(mAdapter);
//        DividerLineItemDecoration dividerLine = new DividerLineItemDecoration(DividerLineItemDecoration.VERTICAL);
//        dividerLine.setSize(35);
//        dividerLine.setColor(0xFFF2F2F2);
//        mRecyclerView.addItemDecoration(dividerLine);

        return view;
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                getContext());
        layoutManager.setOnRecyclerViewScrollLocationListener(mRecyclerView,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (mWishList.size() >= rows) {
                            page++;
                            loadData(false);
                            // footerView.setVisibility(View.VISIBLE);
                        }
                    }
                });
        return layoutManager;
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        if (mSrl == null || !this.isAdded()) {
            return;
        }
        mSrl.setRefreshing(false);
//        footerView.setVisibility(View.GONE);
        switch (actionId) {
            case WISH_FULFILL_REQUEST:
                WishFulfillResponse wishFulfillResponse =
                        JsonUtils.fromJson(response, WishFulfillResponse.class);
                if (COMMON_SUCCESS.equals(wishFulfillResponse.getCode())) {
                    if (page == 0) {
                        mWishList.clear();
                    }
                    List<AchieveWish> list = wishFulfillResponse.getAchieveWishList();
                    if (list != null && list.size() > 0) {
                        mWishList.addAll(list);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    showToast(wishFulfillResponse.getMsg());
                }
                if (mWishList.size() <= 0) {
                    // 无数据
                    mNoData.setText("对不起，还没有达成任何心愿哦！");
                    mNoData.setVisibility(View.VISIBLE);
                } else {
                    mNoData.setVisibility(View.GONE);
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
        }
    }

    @Override
    public void onRefresh() {
        page = 0;
        if (!this.isAdded()) {
            return;
        }
        loadData(false);
    }

    @Override
    public void onToLogin() {
        startActivityForResult(new Intent(getContext(), LoginActivity.class), REQUEST_CODE_LOGIN);
    }

    @Override
    public void onLike(AchieveWish model, boolean like) {
        PraiseRequest request = new PraiseRequest();
        request.setContentId(model.getB02Id());
        request.setPraiseType(like ? 1 : 2);
        sendNetRequest(request, HeaderRequest.SUBJECT_PRAISE, LIKE_REQUEST,
                false);
        saveBehaviour("04", request, HeaderRequest.SUBJECT_PRAISE);
    }

    @Override
    public void onToComment(AchieveWish model) {
        Intent intent = new Intent(getContext(), CommentActivity.class);
        intent.putExtra(CommentActivity.CONTENTID, model.getB02Id());
        startActivityForResult(intent, COMMENT_REQUEST_CODE);
        saveBehaviour("07");
    }

    @Override
    public void onToShare(final AchieveWish model) {
        this.model = model;
        requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        saveBehaviour("08");
    }

    private AchieveWish model;

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        String url = RequestUrl.WISH_DETAIL_SHARE_URL + "header="
                + HeaderRequest.WISH_DETAIL + "&b02Id=" + model.getB02Id();

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
        ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(getActivity());

        String strStarName = model.getStarName();
        if (model.getWishType() != 0) {
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

    @Override
    public void onToCollect(AchieveWish model, boolean collect) {

        CollectRequest request = new CollectRequest();
        request.setContentId(model.getB02Id());
        request.setType(5);
        request.setStatus(collect ? 1 : 0);
        sendNetRequest(request, HeaderRequest.SUBJECT_COLLECT, COLLECT_REQUEST,
                false);

        saveBehaviour("10", request, HeaderRequest.SUBJECT_COLLECT);
    }

    @Override
    public void onPlayVideo(AchieveWish model) {
        Intent intent = new Intent(getContext(), VideoActivity.class);
//        intent.putExtra(VideoActivity.VIDEONAME, model.getName());
        intent.putExtra(VideoActivity.VIDEOURL, model.getAchieveVideoUrl());
        startActivity(intent);

        AddPlayNumRequest request = new AddPlayNumRequest();
        request.setMediaId(model.getG01Id());
        request.setIdType(1);
        sendNetRequest(request, HeaderRequest.ADD_PLAY_NUM, ADD_PLAY_NUM,
                false);
        saveBehaviour("06", request, HeaderRequest.ADD_PLAY_NUM);
    }

    @Override
    public void onPlayAudio(AchieveWish model) {
        AddPlayNumRequest request = new AddPlayNumRequest();
        request.setMediaId(model.getAchieveVoiceList().get(0).getG02Id());
        request.setIdType(0);
        sendNetRequest(request, HeaderRequest.ADD_PLAY_NUM, ADD_PLAY_NUM,
                false);
    }

    @Override
    public void onShowWish(AchieveWish model) {
        Intent intent = new Intent(getContext(), WishUserActivity.class);
        intent.putExtra(WishUserActivity.CONTENT_ID, model.getB02Id());
        intent.putExtra(WishUserActivity.FULFILL, true);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    setLoginState(true);
                }
                break;
            case COMMENT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    onRefresh();
                }
                break;
        }
    }

    public void setLoginState(boolean login) {
        if (mAdapter == null) {
            return;
        }
        mAdapter.setLogin(login);
        onRefresh();
    }


    @Override
    public void onResume() {
        super.onResume();
        MediaManager.resume();

        try {
            boolean login = getContext().getSharedPreferences(Constant.SP_INFO,
                    Context.MODE_PRIVATE).getBoolean(Constant.IS_LOGIN, false);
            mAdapter.setLogin(login);

            UserInfo userInfo = SysApplication.getUserInfo();
            if (userInfo.getUserType() == 1) {
                onRefresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }

    private void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(getContext(),
                BehaviourEnum.IP_LIST.getCode() + functionCode);
    }

    private void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(getContext(),
                BehaviourEnum.IP_LIST.getCode() + functionCode, request, header);
    }
}
