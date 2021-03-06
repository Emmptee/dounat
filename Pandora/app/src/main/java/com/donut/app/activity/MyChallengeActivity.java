package com.donut.app.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.MyChallengeRecyclerViewAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.DeleteRequest;
import com.donut.app.http.message.MyChallengeRequest;
import com.donut.app.http.message.MyChallengeResponse;
import com.donut.app.mvp.subject.challenge.SubjectChallengeActivity;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class MyChallengeActivity extends BaseActivity implements MyChallengeRecyclerViewAdapter.OnItemClickListener
        , SwipeRefreshLayout.OnRefreshListener
{
    @ViewInject(R.id.my_challenge_srl)
    private SwipeRefreshLayout swipeRefreshLayout;

    @ViewInject(R.id.my_challenge_list)
    private RecyclerView recyclerView;

    @ViewInject(R.id.no_data)
    private TextView mNoData;

    private List<MyChallengeResponse.MyChallenge> mList;

    private View footerView;

    private MyChallengeRecyclerViewAdapter mAdapter;

    private int page = 0, rows = 10;

    private static final int CHALLENGE_REQUEST = 0;

    private static final int CHALLENGE_DELETE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_challenge);
        StatusBarUtil.setColor(MyChallengeActivity.this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle("我的挑战", true);
        initView();
    }

    @Override
    protected void onResume()
    {
        page=0;
        requestData(true);
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_CHALLENGE.getCode()+"00",request,HeaderRequest.MY_CHALLENGE_REQUEST);
        super.onResume();
    }

    private void initView()
    {
        mList = new ArrayList<MyChallengeResponse.MyChallenge>();
        footerView = LayoutInflater.from(this)
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new MyChallengeRecyclerViewAdapter(this,mList, this,
                footerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(getLayoutManager());
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_tiffany);
    }


    MyChallengeRequest request;
    private void requestData(boolean isShowDialog)
    {
        request = new MyChallengeRequest();
        request.setPage(page);
        request.setRows(rows);
        sendNetRequest(request, HeaderRequest.MY_CHALLENGE_REQUEST, CHALLENGE_REQUEST, isShowDialog);
    }

    private void showView(List<MyChallengeResponse.MyChallenge> dataList)
    {
        if (dataList != null && dataList.size() > 0)
        {
            mNoData.setVisibility(View.GONE);
            if (page == 0)
            {
                footerView.setVisibility(View.GONE);
                mList.clear();
                swipeRefreshLayout.setRefreshing(false);
            } else
            {
                if (dataList.size() >= rows)
                {
                    footerView.setVisibility(View.GONE);
                } else
                {
                    footerView.setVisibility(View.VISIBLE);
                    mAdapter.setNoMoreData(true);
                }
            }
            page++;
            mList.addAll(dataList);

        } else
        {
            if(page==0){
                footerView.setVisibility(View.GONE);
                mNoData.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }else{
                mNoData.setVisibility(View.GONE);
                footerView.setVisibility(View.VISIBLE);
                mAdapter.setNoMoreData(true);
            }

        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onSuccess(String response, String url, int actionId)
    {

        super.onSuccess(response, url, actionId);
        switch (actionId)
        {
            case CHALLENGE_REQUEST:
                swipeRefreshLayout.setRefreshing(false);
                footerView.setVisibility(View.GONE);
                MyChallengeResponse challengeResponse
                        = JsonUtils.fromJson(response, MyChallengeResponse.class);
                if (COMMON_SUCCESS.equals(challengeResponse.getCode()))
                {
                    showView(challengeResponse.getChallengeList());
                } else
                {
                    showToast(challengeResponse.getMsg());
                }
                break;
            case CHALLENGE_DELETE_REQUEST:
                BaseResponse basResponse
                        = JsonUtils.fromJson(response, BaseResponse.class);
                if(COMMON_SUCCESS.equals(basResponse.getCode())){
                    mList.remove(delPos);
                    mAdapter.notifyDataSetChanged();
                    if(mList.size()==0){
                        mNoData.setVisibility(View.VISIBLE);
                    }
                    ToastUtil.showShort(MyChallengeActivity.this,"删除成功");
                }else{
                    ToastUtil.showShort(MyChallengeActivity.this,basResponse.getMsg());
                }
                break;
        }
    }

    @Override
    public void OnClick(int pos)
    {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_CHALLENGE.getCode()+"02");
        Bundle bundle = new Bundle();
        bundle.putString(SubjectChallengeActivity.CONTENT_ID, mList.get(pos).getContentId());
        bundle.putString(SubjectChallengeActivity.SUBJECT_ID, mList.get(pos).getSubjectId());
        launchActivity(SubjectChallengeActivity.class, bundle);
    }

    int delPos;
    @Override
    public void onDelete(final int pos)
    {
        Dialog dialog = new AlertDialog.Builder(MyChallengeActivity.this)
                .setMessage("确定要删除发布的挑战吗？")
                .setNegativeButton(getString(R.string.cancel), null)
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //删除接口
                        delPos=pos;
                        deleteRequest();
                    }
                }).create();
        dialog.show();
    }

    private void deleteRequest(){
        DeleteRequest delRequest=new DeleteRequest();
        delRequest.setContentId(mList.get(delPos).getContentId());
        sendNetRequest(delRequest, HeaderRequest.CHALLENGE_DELETE, CHALLENGE_DELETE_REQUEST);
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_CHALLENGE.getCode()+"01",delRequest,HeaderRequest.CHALLENGE_DELETE);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_CHALLENGE.getCode()+"03");
    }

    @Override
    protected void onStop() {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_CHALLENGE.getCode() + "xx");
        super.onStop();
    }

    @Override
    public void onRefresh()
    {
        page = 0;
        requestData(false);
    }

    private boolean isTop;

    private ABaseLinearLayoutManager getLayoutManager()
    {
        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                MyChallengeActivity.this);
        layoutManager.setOnRecyclerViewScrollLocationListener(recyclerView,
                new OnRecyclerViewScrollLocationListener()
                {
                    @Override
                    public void onTopWhenScrollIdle(RecyclerView recyclerView)
                    {
                        isTop = true;
                    }

                    @Override
                    public void onBottomWhenScrollIdle(RecyclerView recyclerView)
                    {
                        if (!isTop)
                        {
                            requestData(false);
                            footerView.setVisibility(View.VISIBLE);
                        }
                        isTop = false;
                    }
                });
        return layoutManager;
    }

}
