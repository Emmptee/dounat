package com.donut.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseGridLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.adapter.ChallengeItemRecyclerViewAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.fragment.base.BaseFragment;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.ChallengeListRequest;
import com.donut.app.http.message.ChallengeListResponse;
import com.donut.app.mvp.subject.challenge.SubjectChallengeActivity;
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
public class ChallengeItemFragment extends BaseFragment implements ChallengeItemRecyclerViewAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    @ViewInject(R.id.challenge_srl)
    private SwipeRefreshLayout swipeRefreshLayout;

    @ViewInject(R.id.challenge_list)
    private RecyclerView recyclerView;

    @ViewInject(R.id.challenge_tv_msg)
    private TextView viewNoMsg;

    private View footerView;

    private int orderType = 0, page = 0, rows = 20;

    private static final String ORDER_TYPE = "ORDER_TYPE", SUBJECT_ID = "SUBJECT_ID";

    private static final int CHALLENGE_REQUEST = 1;

    private List<ChallengeListResponse.Challenge> challengeList = new ArrayList<>();

    private ChallengeItemRecyclerViewAdapter mAdapter;

    private String subjectId;

    public ChallengeItemFragment() {
    }

    public static ChallengeItemFragment newInstance(int type, String subjectId) {
        ChallengeItemFragment fragment = new ChallengeItemFragment();
        Bundle args = new Bundle();
        args.putInt(ORDER_TYPE, type);
        args.putString(SUBJECT_ID, subjectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            orderType = getArguments().getInt(ORDER_TYPE);
            subjectId = getArguments().getString(SUBJECT_ID);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challengeitem_list, container, false);
        ViewUtils.inject(this, view);

        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 0;
        loadData();
        saveBehaviour();
    }

    private void loadData() {
        ChallengeListRequest request = new ChallengeListRequest();
        request.setSubjectId(subjectId);
        request.setPage(page);
        request.setRows(rows);
        request.setType(orderType);
        sendNetRequest(request, HeaderRequest.CHALLENGE_REQUEST, CHALLENGE_REQUEST);
    }

    private void initView() {

        footerView = LayoutInflater.from(getContext())
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new ChallengeItemRecyclerViewAdapter(challengeList, orderType, this,
                footerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(getLayoutManager());

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_tiffany);
    }

    private boolean isTop;

    public RecyclerView.LayoutManager getLayoutManager() {
        ABaseGridLayoutManager layoutManager = new ABaseGridLayoutManager(getContext(), 2);
        layoutManager.setOnRecyclerViewScrollListener(recyclerView,
                new OnRecyclerViewScrollLocationListener() {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
                        isTop = true;
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
                        if (!isTop) {
                            footerView.setVisibility(View.VISIBLE);
                            page++;
                            loadData();
                        }
                        isTop = false;
                    }
                });
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                if (position == challengeList.size()) {
                    return 2;
                }
                return 1;
            }
        });

        return layoutManager;
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);

        switch (actionId) {
            case CHALLENGE_REQUEST:

                viewNoMsg.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                footerView.setVisibility(View.GONE);

                ChallengeListResponse challengeListResponse
                        = JsonUtils.fromJson(response, ChallengeListResponse.class);
                if (COMMON_SUCCESS.equals(challengeListResponse.getCode())) {
                    showView(challengeListResponse.getChallengeList());
                } else {
                    showToast(challengeListResponse.getMsg());
                }
                break;
        }
    }

    private void showView(List<ChallengeListResponse.Challenge> dataList) {

        if (page == 0) {
            challengeList.clear();
            if(dataList == null || dataList.size() <= 0){
                viewNoMsg.setVisibility(View.VISIBLE);
                if(orderType==0){
                    viewNoMsg.setText(getString(R.string.challenge_no_msg));
                }else{
                    viewNoMsg.setText("对不起，该页面下暂无内容哦！");
                }

            }
        }
        if (dataList != null && dataList.size() > 0) {
            challengeList.addAll(dataList);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnClick(String uuid) {
        Intent intent = new Intent(getContext(), SubjectChallengeActivity.class);
        intent.putExtra(SubjectChallengeActivity.CONTENT_ID, uuid);
        intent.putExtra(SubjectChallengeActivity.SUBJECT_ID, subjectId);
        startActivity(intent);
        SaveBehaviourDataService.startAction(getContext(), BehaviourEnum.CHALLENGE.getCode() + "04");
    }

    @Override
    public void onRefresh() {
        page = 0;
        loadData();
    }

    private void saveBehaviour() {
        String functionCode = "";
        switch (orderType) {
            case 0:
                functionCode = "01";
                break;
            case 1:
                functionCode = "02";
                break;
            case 2:
                functionCode = "03";
                break;
        }
        ChallengeListRequest request = new ChallengeListRequest();
        request.setSubjectId(subjectId);
        request.setPage(0);
        request.setRows(rows);
        request.setType(orderType);
        SaveBehaviourDataService.startAction(getContext(),
                BehaviourEnum.CHALLENGE.getCode() + functionCode,
                request,
                HeaderRequest.CHALLENGE_REQUEST);
    }
}
