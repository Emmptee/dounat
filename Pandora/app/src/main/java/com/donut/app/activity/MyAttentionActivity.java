package com.donut.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.MyAttentionRecyclerViewAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.customview.ConcernCanclePopupWindow;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.ConcernedOnStarRequest;
import com.donut.app.http.message.MyFollowListRequest;
import com.donut.app.http.message.MyFollowListResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

public class MyAttentionActivity extends BaseActivity implements MyAttentionRecyclerViewAdapter.OnItemClickListener
        , SwipeRefreshLayout.OnRefreshListener
{
    @ViewInject(R.id.my_attention_srl)
    private SwipeRefreshLayout swipeRefreshLayout;

    @ViewInject(R.id.my_attention_list)
    private RecyclerView recyclerView;

    @ViewInject(R.id.no_data)
    private TextView mNoData;

    @ViewInject(R.id.head_right_iv)
    private ImageView mRight;

    private List<MyFollowListResponse.FollowDetail> mList;

    private View footerView;

    private MyAttentionRecyclerViewAdapter mAdapter;

    private int page = 0, rows = 20;

    private static final int ATTENTION_REQUEST = 0;

    private static final int ATTENTION_ON_STAR_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention);
        StatusBarUtil.setColor(MyAttentionActivity.this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle(getString(R.string.my_attention), true);
        mRight.setVisibility(View.VISIBLE);
        initView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        page = 0;
        requestData(true);
        saveBehaviour("00",request,HeaderRequest.MY_ATTENTION);
    }

    @OnClick(value = { R.id.menu})
    private void btnClick(View v)
    {
        switch (v.getId())
        {
            case R.id.menu:
                saveBehaviour("01");
                launchActivity(StarAttentionSearchActivity.class);
                break;
        }
    }


    private void initView()
    {
        mList = new ArrayList<MyFollowListResponse.FollowDetail>();
        footerView = LayoutInflater.from(this)
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new MyAttentionRecyclerViewAdapter(this,mList, this,
                footerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(getLayoutManager());
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_tiffany);
    }

    MyFollowListRequest request;
    private void requestData(boolean isShowDialog)
    {
        request = new MyFollowListRequest();
        request.setPage(page);
        request.setRows(rows);
        sendNetRequest(request, HeaderRequest.MY_ATTENTION, ATTENTION_REQUEST, isShowDialog);
    }


    @Override
    public void onRefresh()
    {
        page = 0;
        requestData(false);
    }

    @Override
    public void OnItemClick(int position)
    {
        saveBehaviour("04");
        Intent intent=new Intent(this,StarDetailActivity.class);
        intent.putExtra(StarDetailActivity.STAR_ID,mList.get(position).getStarId());
        startActivity(intent);
    }

    private int operaPos;
    ConcernCanclePopupWindow showWindow;
    @Override
    public void onCancle(int position)
    {
        operaPos=position;
        if(mList.get(position).getStatus()==0){
            showWindow = new ConcernCanclePopupWindow(this, itemsOnClick);
            //显示窗口
            showWindow.showAtLocation(findViewById(R.id.attention_main),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }else{
            ConcernedOnStarRequest request = new ConcernedOnStarRequest();
            request.setStarId(mList.get(position).getStarId());
            request.setOperation("0");
            sendNetRequest(request, HeaderRequest.CONCERNED_ON_STAR, ATTENTION_ON_STAR_REQUEST);
            saveBehaviour("03",request,HeaderRequest.CONCERNED_ON_STAR);
        }
    }

    //为弹出窗口实现监听类
    private ConcernCanclePopupWindow.OnClickListenerWithPosition itemsOnClick = new ConcernCanclePopupWindow.OnClickListenerWithPosition()
    {
        @Override
        public void onClick(View v, int actionId)
        {
            if (showWindow != null && showWindow.isShowing())
            {
                showWindow.dismiss();
                switch (v.getId())
                {
                    case R.id.btn_click_three:
                        ConcernedOnStarRequest request = new ConcernedOnStarRequest();
                        request.setStarId(mList.get(operaPos).getStarId());
                        request.setOperation("1");
                        sendNetRequest(request, HeaderRequest.CONCERNED_ON_STAR, ATTENTION_ON_STAR_REQUEST);
                        saveBehaviour("02",request,HeaderRequest.CONCERNED_ON_STAR);
                        break;

                    default:
                        break;
                }
            }
        }
    };

    private void showView(List<MyFollowListResponse.FollowDetail> dataList)
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
                mList.clear();
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
            case ATTENTION_REQUEST:
                swipeRefreshLayout.setRefreshing(false);
                footerView.setVisibility(View.GONE);
                MyFollowListResponse followResponse
                        = JsonUtils.fromJson(response, MyFollowListResponse.class);
                if (COMMON_SUCCESS.equals(followResponse.getCode()))
                {
                    showView(followResponse.getFollowList());
                } else
                {
                    showToast(followResponse.getMsg());
                }
                break;
            case ATTENTION_ON_STAR_REQUEST:
                BaseResponse basResponse
                        = JsonUtils.fromJson(response, BaseResponse.class);
                if(COMMON_SUCCESS.equals(basResponse.getCode())){
                    concernOperSuccess();
                }else{
                    ToastUtil.showShort(MyAttentionActivity.this,basResponse.getMsg());
                }
                break;
        }
    }

    private void concernOperSuccess(){
        if(mList.get(operaPos).getStatus()==0){
            mList.get(operaPos).setStatus(1);
        }else{
            mList.get(operaPos).setStatus(0);
        }
        mAdapter.notifyDataSetChanged();
    }


    private boolean isTop;

    private ABaseLinearLayoutManager getLayoutManager()
    {
        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                MyAttentionActivity.this);
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

    @Override
    public void onBackPressed() {
        saveBehaviour("05");
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        saveBehaviour("xx");
        super.onStop();
    }

    private void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_ATTENTION.getCode() + functionCode);
    }

    private void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_ATTENTION.getCode() + functionCode, request, header);
    }
}
