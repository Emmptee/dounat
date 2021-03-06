package com.donut.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.SysApplication;
import com.donut.app.activity.IPDetailActivity;
import com.donut.app.adapter.CollectItemRecyclerViewAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.GotoChannelUtils;
import com.donut.app.fragment.base.BaseFragment;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.MyCollectionRequest;
import com.donut.app.http.message.MyCollectionResponse;
import com.donut.app.mvp.subject.challenge.SubjectChallengeActivity;
import com.donut.app.mvp.subject.finalpk.SubjectFinalPkActivity;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p>
 */
public class CollectItemFragment extends BaseFragment implements CollectItemRecyclerViewAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    @ViewInject(R.id.collect_srl)
    private SwipeRefreshLayout swipeRefreshLayout;

    @ViewInject(R.id.collect_list)
    private RecyclerView recyclerView;

    @ViewInject(R.id.no_data)
    private TextView mNoData;

    private View footerView;

    private int orderType = 0, page = 0, rows = 20;

    private static final String ORDER_TYPE = "ORDER_TYPE";

    private static final int COLLECT_REQUEST = 1;

    private static final int DETAIL_REQUEST_CODE = 1;

    private List<MyCollectionResponse.MyCollection> collectList = new ArrayList<>();

    private CollectItemRecyclerViewAdapter mAdapter;

    public CollectItemFragment() {
    }

    public static CollectItemFragment newInstance(int type) {
        CollectItemFragment fragment = new CollectItemFragment();
        Bundle args = new Bundle();
        args.putInt(ORDER_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderType = getArguments().getInt(ORDER_TYPE);
            if (orderType != 0) {
                orderType += 1;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collectitem_list, container, false);
        ViewUtils.inject(this, view);
        initView();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mHandler.sendEmptyMessage(3);
            onRefresh();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    swipeRefreshLayout.setRefreshing(true);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void loadData() {
        String functionCode = "";
        switch (orderType) {
            case 0:
                functionCode = "01";
                break;
            case 2:
                functionCode = "02";
                break;
            case 3:
                functionCode = "03";
                break;
        }
        MyCollectionRequest request = new MyCollectionRequest();
        request.setPage(page);
        request.setRows(rows);
        request.setSelectType(orderType);
        sendNetRequest(request, HeaderRequest.MY_COLLECT_REQUEST, COLLECT_REQUEST, false);
        SaveBehaviourDataService.startAction(SysApplication.getInstance(),
                BehaviourEnum.MY_COLLECT.getCode() + functionCode, request, HeaderRequest.MY_COLLECT_REQUEST);
    }

    private void initView() {

        footerView = LayoutInflater.from(getContext())
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new CollectItemRecyclerViewAdapter(getActivity(), collectList, orderType, this,
                footerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(getLayoutManager());
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_tiffany);
    }

    private boolean isTop;

    public RecyclerView.LayoutManager getLayoutManager() {
        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                getActivity());
        layoutManager.setOnRecyclerViewScrollLocationListener(recyclerView,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                        isTop = true;
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (!isTop) {
                            loadData();
                            footerView.setVisibility(View.VISIBLE);
                        }
                        isTop = false;
                    }
                });
        return layoutManager;
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {

        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case COLLECT_REQUEST:
                MyCollectionResponse collectResponse
                        = JsonUtils.fromJson(response, MyCollectionResponse.class);
                if (COMMON_SUCCESS.equals(collectResponse.getCode())) {
                    showView(collectResponse.getCollectionList());
                } else {
                    showToast(collectResponse.getMsg());
                }
                break;
        }
    }

    private void showView(List<MyCollectionResponse.MyCollection> dataList) {

        if (dataList != null && dataList.size() > 0) {
            mNoData.setVisibility(View.GONE);
            if (page == 0) {
                swipeRefreshLayout.setRefreshing(false);
                footerView.setVisibility(View.GONE);
                collectList.clear();
            } else {
                if (dataList.size() >= rows) {
                    footerView.setVisibility(View.GONE);
                } else {
                    footerView.setVisibility(View.VISIBLE);
                    mAdapter.setNoMoreData(true);
                }
            }

            page++;
            collectList.addAll(dataList);
        } else {
            if (page == 0) {
                String noDataStr = "";
                switch (orderType) {
                    case 0:
                        noDataStr = "对不起，您还没有收藏任何专题哦！";
                        break;
                    case 2:
                        noDataStr = "对不起，您还没有收藏任何挑战哦！";
                        break;
                    case 3:
                        noDataStr = "对不起，您还没有收藏任何心愿哦！";
                        break;
                }
                collectList.clear();
                mNoData.setText(noDataStr);
                mNoData.setVisibility(View.VISIBLE);
                footerView.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            } else {
                mNoData.setVisibility(View.GONE);
                footerView.setVisibility(View.VISIBLE);
                mAdapter.setNoMoreData(true);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnChallengeClick(String contentType, String subjectId, String contentId) {
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.MY_COLLECT.getCode() + "05");
        if ("2".equals(contentType)) {
            Intent intent2 = new Intent(getContext(), SubjectChallengeActivity.class);
            intent2.putExtra(SubjectChallengeActivity.CONTENT_ID, contentId);
            intent2.putExtra(SubjectChallengeActivity.SUBJECT_ID, subjectId);
            startActivityForResult(intent2, DETAIL_REQUEST_CODE);
        } else {
            Intent intent2 = new Intent(getContext(), SubjectFinalPkActivity.class);
            intent2.putExtra(SubjectFinalPkActivity.CONTENT_ID, contentId);
            intent2.putExtra(SubjectFinalPkActivity.SUBJECT_ID, subjectId);
            startActivityForResult(intent2, DETAIL_REQUEST_CODE);
        }
    }

    @Override
    public void OnWishClick(String contentId) {
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.MY_COLLECT.getCode() + "06");
        Intent intent = new Intent(getActivity(), IPDetailActivity.class);
        intent.putExtra(IPDetailActivity.IPID, contentId);
        startActivityForResult(intent, DETAIL_REQUEST_CODE);
    }

    @Override
    public void OnSubjectItemClick(String subjectId, String contentType) {
        SaveBehaviourDataService.startAction(getActivity(), BehaviourEnum.MY_COLLECT.getCode() + "04");
        int channelType = -1;
        if ("0".equals(contentType) || "1".equals(contentType)) {
            channelType = GotoChannelUtils.CHANNEL_TYPE_SUBJECT;
        } else if ("5".equals(contentType)) {
            channelType = GotoChannelUtils.CHANNEL_TYPE_SNAP;
        } else if ("7".equals(contentType)) {
            channelType = GotoChannelUtils.CHANNEL_TYPE_NOTICE;
        }
        GotoChannelUtils.GotoSubjectDetail(getActivity(), channelType, subjectId, DETAIL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DETAIL_REQUEST_CODE:
                onRefresh();
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 0;
        loadData();
    }
}
