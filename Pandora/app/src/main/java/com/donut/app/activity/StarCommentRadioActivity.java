package com.donut.app.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.StarCommentRadioRecyclerViewAdapter;
import com.donut.app.config.Constant;
import com.donut.app.entity.UserInfo;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.RadioRequest;
import com.donut.app.http.message.RadioResponse;
import com.donut.app.http.message.RewardInfoResponse;
import com.donut.app.http.message.SubjectHistoryPKStarComment;
import com.donut.app.model.audio.MediaManager;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class StarCommentRadioActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, StarCommentRadioRecyclerViewAdapter.OnUserPayListener {

    @ViewInject(R.id.star_comment_radio_srl)
    private SwipeRefreshLayout srl;

    @ViewInject(R.id.star_comment_radio_list)
    private RecyclerView rvList;

    public static final String CONTENT_ID = "CONTENT_ID";

    public static final String CHALLENGE_USERID = "CHALLENGE_USERID";

    public static final String AUDIO_UUID = "AUDIO_UUID";

    private int page = 0, rows = 10;

    private StarCommentRadioRecyclerViewAdapter mAdapter;

    private List<SubjectHistoryPKStarComment> commentList = new ArrayList<>();

    private View footerView;

    private static final int STAR_COMMENT = 1, REWARD_INFO_GET = 2;

    private String contentId, challUserid, audioUuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_comment_radio);
        StatusBarUtil.setColor(this, Constant.default_bar_color);
        updateHeadTitle(getString(R.string.star_recoder), true);
        ViewUtils.inject(this);
        contentId = getIntent().getStringExtra(CONTENT_ID);
        challUserid = getIntent().getStringExtra(CHALLENGE_USERID);
        audioUuid = getIntent().getStringExtra(AUDIO_UUID);
        initView();

        if (getLoginStatus()) {
            loadBalance();
        } else {
            loadData(true);
        }
    }

    private void loadBalance() {
        sendNetRequest(new Object(), HeaderRequest.REWARD_INFO_GET, REWARD_INFO_GET);
    }

    private void initView() {
        srl.setOnRefreshListener(this);
        srl.setColorSchemeResources(R.color.refresh_tiffany);
        footerView = LayoutInflater.from(this)
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new StarCommentRadioRecyclerViewAdapter(challUserid,
                getUserInfo().getUserId(), getUserInfo().getUserType(),
                commentList, footerView, this);
        rvList.setLayoutManager(getLayoutManager());
        rvList.setAdapter(mAdapter);
    }

    private void loadData(boolean loading) {
        RadioRequest request = new RadioRequest();
        request.setContentId(contentId);
        request.setPage(page);
        request.setRows(rows);
        sendNetRequest(request, HeaderRequest.STAR_COMMENT, STAR_COMMENT, loading);
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
    public void onRefresh() {
        page = 0;
        loadData(false);
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        srl.setRefreshing(false);
        footerView.setVisibility(View.GONE);
        switch (actionId) {
            case STAR_COMMENT:
                RadioResponse radioResponse = JsonUtils.fromJson(response, RadioResponse.class);
                if (COMMON_SUCCESS.equals(radioResponse.getCode())) {
                    if (page == 0) {
                        commentList.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                    if (radioResponse.getAudioList() != null
                            && radioResponse.getAudioList().size() > 0) {
                        showView(radioResponse.getAudioList());
                    }
                } else {
                    showToast(radioResponse.getMsg());
                }
                break;
            case REWARD_INFO_GET:
                loadData(true);
                RewardInfoResponse infoResponse = JsonUtils.fromJson(response, RewardInfoResponse.class);
                if (COMMON_SUCCESS.equals(infoResponse.getCode())) {
                    UserInfo userInfo = getUserInfo();
                    userInfo.setmBalance(infoResponse.getBalance());
                    setUserInfo(userInfo, true);
                }
                break;
        }
    }

    private void showView(List<SubjectHistoryPKStarComment> audioList) {
        page++;
        commentList.addAll(audioList);
        mAdapter.notifyDataSetChanged();
    }

    private boolean isTop;

    public RecyclerView.LayoutManager getLayoutManager() {
        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                this);
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

    @Override
    public void userPaySucc(String uuid) {
        if (audioUuid == null) {
            audioUuid = "";
        }
        if (audioUuid.equals(uuid)) {
            payShowAudio = true;
        }
    }

    boolean payShowAudio = false;

    @Override
    public void onBackPressed() {
        if (payShowAudio) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }
}
