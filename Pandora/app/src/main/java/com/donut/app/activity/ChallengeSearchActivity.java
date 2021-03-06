package com.donut.app.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseGridLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.ChallengeItemRecyclerViewAdapter;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.ChallengeListRequest;
import com.donut.app.http.message.ChallengeListResponse;
import com.donut.app.mvp.subject.challenge.SubjectChallengeActivity;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

public class ChallengeSearchActivity extends BaseActivity implements ChallengeItemRecyclerViewAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @ViewInject(R.id.challenge_search_srl)
    private SwipeRefreshLayout swipeRefreshLayout;

    @ViewInject(R.id.challenge_search_list)
    private RecyclerView recyclerView;

    @ViewInject(R.id.challenge_search_tv_msg)
    private TextView tvMsg;

    @ViewInject(R.id.challenge_search_et)
    private EditText etSearch;

    @ViewInject(R.id.challenge_search_iv_etClean)
    private View ivEtClean;

    private View footerView;

    private int orderType = 0, page = 0, rows = 20;

    public static final String ORDER_TYPE = "ORDER_TYPE", SUBJECT_ID = "SUBJECT_ID";

    private static final int CHALLENGE_REQUEST = 1;

    private List<ChallengeListResponse.Challenge> challengeList = new ArrayList<>();

    private ChallengeItemRecyclerViewAdapter mAdapter;

    private String subjectId, searchWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_search);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        initData();
        initView();
    }

    private void initData() {
        orderType = getIntent().getIntExtra(ORDER_TYPE, 0);
        subjectId = getIntent().getStringExtra(SUBJECT_ID);
    }

    private void initView() {

        footerView = LayoutInflater.from(this)
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new ChallengeItemRecyclerViewAdapter(challengeList, orderType, this,
                footerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(getLayoutManager());

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_tiffany);

        footerView.setVisibility(View.GONE);
        tvMsg.setVisibility(View.VISIBLE);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    ivEtClean.setVisibility(View.GONE);
                }
                else {
                    ivEtClean.setVisibility(View.VISIBLE);
                }
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    page = 0;
                    searchWords = v.getText().toString();
                    loadData();
                    etSearch.setEnabled(false);
                }
                return false;
            }
        });
    }

    private void loadData() {
        ChallengeListRequest request = new ChallengeListRequest();
        request.setSubjectId(subjectId);
        request.setPage(page);
        request.setRows(rows);
        request.setType(orderType);
        request.setSearchWords(searchWords);
        sendNetRequest(request, HeaderRequest.CHALLENGE_REQUEST, CHALLENGE_REQUEST);
    }

    @OnClick({R.id.challenge_search_tv_cancel,
            R.id.challenge_search_iv_etClean})
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.challenge_search_tv_cancel:
                onBackPressed();
                break;
            case R.id.challenge_search_iv_etClean:
                etSearch.setText("");
                searchWords = "";
                break;
        }
    }

    private boolean isTop;

    public RecyclerView.LayoutManager getLayoutManager() {
        ABaseGridLayoutManager layoutManager = new ABaseGridLayoutManager(this, 2);
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
        etSearch.setEnabled(true);
        switch (actionId) {
            case CHALLENGE_REQUEST:
                tvMsg.setVisibility(View.GONE);
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

    @Override
    public void onError(String errorMsg, String url, int actionId) {
        super.onError(errorMsg, url, actionId);
        etSearch.setEnabled(true);
    }

    private void showView(List<ChallengeListResponse.Challenge> dataList) {

        if (page == 0) {
            challengeList.clear();
            if (dataList == null || dataList.size() <= 0) {
                tvMsg.setVisibility(View.VISIBLE);
                Drawable drawable = getResources().getDrawable(R.drawable.no_msg_icon);
                assert drawable != null;
                drawable.setBounds(0, 0, 50, 32);
                tvMsg.setCompoundDrawables(null, drawable, null, null);
                tvMsg.setText(R.string.subject_search_no_mag);
            }
        }
        if (dataList != null && dataList.size() > 0) {
            challengeList.addAll(dataList);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnClick(String uuid) {
        Intent intent = new Intent(this, SubjectChallengeActivity.class);
        intent.putExtra(SubjectChallengeActivity.CONTENT_ID, uuid);
        intent.putExtra(SubjectChallengeActivity.SUBJECT_ID, subjectId);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        page = 0;
        loadData();
    }
}
