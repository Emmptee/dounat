package com.donut.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseGridLayoutManager;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.IPTopItemRecyclerViewAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.IPListRequest;
import com.donut.app.http.message.IPListResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class IPTopListActivity extends BaseActivity implements IPTopItemRecyclerViewAdapter.OnItemClickListener
{

    @ViewInject(R.id.ip_list)
    private RecyclerView recyclerView;

    private static final int IP_TOP_LIST_REQUEST = 0;

    private List<IPListResponse.IpListDetail> mList = new ArrayList<>();

    private IPTopItemRecyclerViewAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iptop_list);
        StatusBarUtil.setColor(IPTopListActivity.this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle("上榜心愿",true);
        initView();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.IP_TOP.getCode()+"00",request, HeaderRequest.IP_LIST_REQUEST);
    }

    private void initView()
    {
        mAdapter = new IPTopItemRecyclerViewAdapter(this, mList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(getLayoutManager());
        requestData();
    }
    IPListRequest request;
    private void requestData()
    {
        request = new IPListRequest();
        request.setPage(0);
        request.setRows(20);
        request.setSelectType(3);
        sendNetRequest(request, HeaderRequest.IP_LIST_REQUEST, IP_TOP_LIST_REQUEST);
    }

    @Override
    public void OnClick(String uuid)
    {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.IP_TOP.getCode()+"01");
        Intent intent = new Intent(this, IPDetailActivity.class);
        intent.putExtra(IPDetailActivity.IPID, uuid);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.IP_TOP.getCode()+"02");
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.IP_TOP.getCode() + "xx");
        super.onStop();
    }

    @Override
    public void onSuccess(String response, String url, int actionId)
    {
        super.onSuccess(response, url, actionId);
        IPListResponse res
                = JsonUtils.fromJson(response, IPListResponse.class);
        if (COMMON_SUCCESS.equals(res.getCode()))
        {
            showView(res.getIpList());
        } else
        {
            showToast(res.getMsg());
        }
    }

    private void showView(List<IPListResponse.IpListDetail> dataList)
    {
        mList.clear();
        if (dataList != null && dataList.size() > 0)
        {
            mList.addAll(dataList);
            mAdapter.notifyDataSetChanged();
        }
    }


    public RecyclerView.LayoutManager getLayoutManager()
    {
        ABaseGridLayoutManager layoutManager = new ABaseGridLayoutManager(this, 2);

//        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
//        {
//            @Override
//            public int getSpanSize(int position)
//            {
//                return 1;
//            }
//        });
        return layoutManager;
    }


}
