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
import com.donut.app.activity.FullImageActivity;
import com.donut.app.activity.LoginActivity;
import com.donut.app.activity.WishStarReplyActivity;
import com.donut.app.adapter.WishListRecyclerViewAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.entity.UserInfo;
import com.donut.app.fragment.base.BaseFragment;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.RequestUrl;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.PraiseRequest;
import com.donut.app.http.message.ShareRequest;
import com.donut.app.http.message.wish.NotAchieveWishModel;
import com.donut.app.http.message.wish.AddPlayNumRequest;
import com.donut.app.http.message.wish.WishListResponse;
import com.donut.app.http.message.wish.WishRequest;
import com.donut.app.model.video.VideoActivity;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

public class WishListFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener,
        WishListRecyclerViewAdapter.OnAdapterInteractionListener {

    private SwipeRefreshLayout mSrl;

    private RecyclerView mRecyclerView;

    private TextView mNoData;

    private int page = 0, rows = 10, sortType = 0;

    private static final int WISH_LIST_REQUEST = 1, LIKE_REQUEST = 2, ADD_PLAY_NUM = 4;

    private final List<NotAchieveWishModel> mWishList = new ArrayList<>();

    private WishListRecyclerViewAdapter mAdapter;

    private final int REQUEST_CODE_LOGIN = 1, COMMENT_REQUEST_CODE = 2, REPLY_REQUEST_CODE = 3;

    public WishListFragment() {
    }

    public static WishListFragment newInstance() {
//        WishFulfillFragment fragment = new WishFulfillFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return new WishListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//        }

        loadData(true);
        saveBehaviour("01", request, HeaderRequest.WISH_LIST);
    }

    private WishRequest request;

    private void loadData(boolean showLoading) {
        request = new WishRequest();
        request.setSortType(sortType);
        request.setPage(page);
        request.setRows(rows);
        sendNetRequest(request, HeaderRequest.WISH_LIST, WISH_LIST_REQUEST, showLoading);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);

        mSrl = (SwipeRefreshLayout) view.findViewById(R.id.wish_list_srl);
        mSrl.setColorSchemeResources(R.color.refresh_tiffany);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.wish_list);
        mNoData = (TextView) view.findViewById(R.id.no_data);
        mSrl.setOnRefreshListener(this);

        boolean login = getContext().getSharedPreferences(Constant.SP_INFO,
                Context.MODE_PRIVATE).getBoolean(Constant.IS_LOGIN, false);

        UserInfo userInfo = SysApplication.getUserInfo();
        if (userInfo.getUserType() == 1) {
            mAdapter = new WishListRecyclerViewAdapter(getContext(), userInfo.getUserId(), mWishList, this);
        } else {
            mAdapter = new WishListRecyclerViewAdapter(getContext(), login, mWishList, this);
        }

        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter(mAdapter);
//        DividerLineItemDecoration dividerLine = new DividerLineItemDecoration(DividerLineItemDecoration.VERTICAL);
//        dividerLine.setSize(15);
//        dividerLine.setColor(0xFFF2F2F2);
//        mRecyclerView.addItemDecoration(dividerLine);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean login = getContext().getSharedPreferences(Constant.SP_INFO,
                Context.MODE_PRIVATE).getBoolean(Constant.IS_LOGIN, false);
        mAdapter.setLogin(login);
    }

    private boolean isTop;

    private RecyclerView.LayoutManager getLayoutManager() {
        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                getContext());
        layoutManager.setOnRecyclerViewScrollLocationListener(mRecyclerView,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                        isTop = true;
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (!isTop) {
                            page++;
                            loadData(false);
                            // footerView.setVisibility(View.VISIBLE);
                        }
                        isTop = false;
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
            case WISH_LIST_REQUEST:
                WishListResponse wishListResponse =
                        JsonUtils.fromJson(response, WishListResponse.class);
                if (COMMON_SUCCESS.equals(wishListResponse.getCode())) {
                    if (page == 0) {
                        mWishList.clear();
                    }
                    List<NotAchieveWishModel> list = wishListResponse.getNotAchieveWishList();
                    if (list != null && list.size() > 0) {
                        mWishList.addAll(list);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    showToast(wishListResponse.getMsg());
                }
                if (mWishList.size() <= 0) {
                    // 无数据
                    mNoData.setText("对不起，还没有发布任何心愿哦！");
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
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRefresh() {
        page = 0;
        if (!this.isAdded()) {
            return;
        }
        loadData(false);
    }

    public void refreshBySortType(int sortType) {
        this.sortType = sortType;
        onRefresh();
    }

    public int getSortType() {
        return sortType;
    }

    @Override
    public void onToLogin() {
        startActivityForResult(new Intent(getContext(), LoginActivity.class), REQUEST_CODE_LOGIN);
    }

    @Override
    public void onLike(NotAchieveWishModel model, boolean like) {
        PraiseRequest request = new PraiseRequest();
        request.setContentId(model.getB02Id());
        request.setPraiseType(like ? 1 : 2);
        sendNetRequest(request, HeaderRequest.SUBJECT_PRAISE, LIKE_REQUEST,
                false);
        saveBehaviour("04", request, HeaderRequest.SUBJECT_PRAISE);
    }

    @Override
    public void onToComment(NotAchieveWishModel model) {
        Intent intent = new Intent(getContext(), CommentActivity.class);
        intent.putExtra(CommentActivity.CONTENTID, model.getB02Id());
        startActivityForResult(intent, COMMENT_REQUEST_CODE);
        saveBehaviour("07");
    }

    @Override
    public void onToShare(final NotAchieveWishModel model) {
        this.model = model;
        requestRuntimePermission("为了给您提供更好的服务,甜麦圈需要获取存储器读写权限",
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        saveBehaviour("08");
    }

    private NotAchieveWishModel model;

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        String url = RequestUrl.WISH_DETAIL_SHARE_URL + "header="
                + HeaderRequest.WISH_DETAIL + "&b02Id=" + model.getB02Id();

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo);
        ShareBuilderCommonUtil.Builder builder = new ShareBuilderCommonUtil.Builder(getActivity());
        builder.setTitle(model.getDescription());
        builder.setContent("我在甜麦圈发起了个心愿，快来用点赞、评论等方式助我达成心愿吧！");
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
    public void onToDetail(NotAchieveWishModel model) {
    }

    @Override
    public void onStarToReply(NotAchieveWishModel model) {
        Intent intent = new Intent(getContext(), WishStarReplyActivity.class);
        intent.putExtra(WishStarReplyActivity.B02_ID, model.getB02Id());
        intent.putExtra(WishStarReplyActivity.G01_ID, model.getG01Id());
        intent.putExtra(WishStarReplyActivity.NICK_NAME, model.getNickName());
        intent.putExtra(WishStarReplyActivity.HEAD_PIC, model.getHeadPic());
        intent.putExtra(WishStarReplyActivity.DESCRIPTION, model.getDescription());
        intent.putExtra(WishStarReplyActivity.CREATE_TIME, model.getCreateTime());

        startActivityForResult(intent, REPLY_REQUEST_CODE);
        saveBehaviour("11");
    }

    @Override
    public void onShowImg(NotAchieveWishModel model) {
        Intent intent = new Intent(getContext(), FullImageActivity.class);
        intent.putExtra(FullImageActivity.IMAGE_URL, model.getImgUrl());
        startActivity(intent);
    }

    @Override
    public void onPlayVideo(NotAchieveWishModel model) {
        Intent intent = new Intent(getContext(), VideoActivity.class);
//        intent.putExtra(VideoActivity.VIDEONAME, model.getName());
        intent.putExtra(VideoActivity.VIDEOURL, model.getPlayUrl());
        startActivity(intent);

        AddPlayNumRequest request = new AddPlayNumRequest();
        request.setMediaId(model.getG01Id());
        request.setIdType(1);
        sendNetRequest(request, HeaderRequest.ADD_PLAY_NUM, ADD_PLAY_NUM,
                false);
        saveBehaviour("06", request, HeaderRequest.ADD_PLAY_NUM);
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
            case REPLY_REQUEST_CODE:
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

    private void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(getContext(),
                BehaviourEnum.IP_LIST.getCode() + functionCode);
    }

    private void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(getContext(),
                BehaviourEnum.IP_LIST.getCode() + functionCode, request, header);
    }
}
