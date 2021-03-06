package com.donut.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.activity.LoginActivity;
import com.donut.app.activity.StarDetailActivity;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.StarMomentsRecyclerViewAdapter;
import com.donut.app.config.Constant;
import com.donut.app.entity.UserInfo;
import com.donut.app.fragment.base.BaseFragment;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.RewardInfoResponse;
import com.donut.app.http.message.StarMomentsRequest;
import com.donut.app.http.message.StarMomentsResponse;
import com.donut.app.model.audio.MediaManager;
import com.donut.app.utils.JsonUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StarMomentsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @ViewInject(R.id.star_moments_srl)
    private SwipeRefreshLayout srl;

    @ViewInject(R.id.star_moments_list)
    private RecyclerView rvList;

    @ViewInject(R.id.star_moments_tv_msg)
    private View noMsg;

    private static final String NAME_AND_IMG = "NAME_AND_IMG", STAR_ID = "STAR_ID", USER_TYPE = "USER_TYPE";

    private String starId, nameAndImg;

    private int page = 0, rows = 10;

    private static final int REWARD_INFO_GET = 2;

    private StarMomentsRecyclerViewAdapter mAdapter;

    List<StarMomentsResponse.StarMoments> momentsList = new ArrayList<>();

    private View footerView;

    private static final int LOGIN_SUCCESS = 1;

    public StarMomentsFragment() {
    }

    public static StarMomentsFragment newInstance(String starId, String nameAndImg) {
        StarMomentsFragment fragment = new StarMomentsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(STAR_ID, starId);
        bundle.putString(NAME_AND_IMG, nameAndImg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            starId = getArguments().getString(STAR_ID);
            nameAndImg = getArguments().getString(NAME_AND_IMG);
        }
        super.onCreate(savedInstanceState);
    }

    private void loadBalance() {
        sendNetRequest(new Object(), HeaderRequest.REWARD_INFO_GET, REWARD_INFO_GET);
    }

    private void loadData(boolean loading) {
        StarMomentsRequest request = new StarMomentsRequest();
        request.setStarId(starId);
        request.setPage(page);
        request.setRows(rows);
        request.setInterfaceType(1);
        sendNetRequest(request, HeaderRequest.STAR_MOMENTS, loading);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_star_moments_layout, container, false);
        ViewUtils.inject(this, view);
        initView();
        return view;
    }

    private void initView() {
        srl.setOnRefreshListener(this);
        srl.setColorSchemeResources(R.color.refresh_tiffany);
        footerView = LayoutInflater.from(getContext())
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new StarMomentsRecyclerViewAdapter(momentsList, nameAndImg,
                footerView, new LoginLinster());
        rvList.setLayoutManager(getLayoutManager());
        rvList.setAdapter(mAdapter);
        mAdapter.setUserType(SysApplication.getUserInfo().getUserType());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getLoginStatus()) {
            loadBalance();
        } else {
            page = 0;
//            momentsList.clear();
            loadData(true);
        }
    }

    @Override
    public void onStop() {
        MediaManager.release();
        mAdapter.clearStyle();
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRefresh() {
        page = 0;
//        momentsList.clear();
        loadData(false);
    }

    @Override
    public void onSuccess(String response, Map<String, String> headers,
                          String url, int actionId) {
        dissDialog();
        if (actionId == REWARD_INFO_GET) {
            page = 0;
//            momentsList.clear();
            loadData(true);
            RewardInfoResponse infoResponse = JsonUtils.fromJson(response, RewardInfoResponse.class);
            if (COMMON_SUCCESS.equals(infoResponse.getCode())) {
                StarDetailActivity act = (StarDetailActivity) getActivity();
                UserInfo userInfo = SysApplication.getUserInfo();
                userInfo.setmBalance(infoResponse.getBalance());
                act.setUserInfo(userInfo, true);
            }
            return;
        }
        srl.setRefreshing(false);
        footerView.setVisibility(View.GONE);
        noMsg.setVisibility(View.GONE);
        StarMomentsResponse momentsResponse = JsonUtils.fromJson(response, StarMomentsResponse.class);
        if (COMMON_SUCCESS.equals(momentsResponse.getCode())) {
            if (page == 0) {
                momentsList.clear();
            }
            if (momentsResponse.getStarMoments() != null
                    && momentsResponse.getStarMoments().size() > 0) {
                showView(momentsResponse.getStarMoments());
            } else {
                if (momentsList.size() <= 0) {
                    noMsg.setVisibility(View.VISIBLE);
                }
            }
            mAdapter.notifyDataSetChanged();
        } else {
            showToast(momentsResponse.getMsg());
        }
    }

    private void showView(List<StarMomentsResponse.StarMoments> starMoments) {
        page++;
        momentsList.addAll(starMoments);
    }

    private boolean isTop;

    public RecyclerView.LayoutManager getLayoutManager() {
        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                getActivity());
        layoutManager.setOnRecyclerViewScrollLocationListener(rvList,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                        isTop = true;
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (!isTop) {
                            loadData(false);
                            footerView.setVisibility(View.VISIBLE);
                        }
                        isTop = false;
                    }
                });
        return layoutManager;
    }

    private class LoginLinster implements StarMomentsRecyclerViewAdapter.onLoginListener {
        @Override
        public void onLogin() {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LOGIN_SUCCESS);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_SUCCESS && resultCode == BaseActivity.RESULT_OK) {
            mAdapter.setUserType(SysApplication.getUserInfo().getUserType());
        }
    }
}
