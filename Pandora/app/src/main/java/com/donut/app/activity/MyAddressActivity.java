package com.donut.app.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.MyAddrAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.AddressListRequest;
import com.donut.app.http.message.AddressListResponse;
import com.donut.app.http.message.AddressRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.DeliveryAddress;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

public class MyAddressActivity extends BaseActivity implements MyAddrAdapter.OnRecyclerViewListener
{
    @ViewInject(R.id.swipe_layout)
    private SwipeRefreshLayout myRefreshView;

    @ViewInject(R.id.recyclerView)
    private RecyclerView mRecycleview;

    @ViewInject(R.id.no_data)
    private View noData;

    private View mFooterView;

    private MyAddrAdapter mAdapter;

    private List<DeliveryAddress> addressesList=new ArrayList<DeliveryAddress>();

    private int page=0;

    private int rows=20;

    private static final int ADDRESS_REQUEST=0,ADDRESS_DEFAULT_SET_REQUEST=1,ADDRESS_DELETE_REQUEST=2;

    private int pos=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        StatusBarUtil.setColor(MyAddressActivity.this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle(getString(R.string.my_address), true);
        initView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        page=0;
        requestData();
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_ADDRESS.getCode()+"00",request,HeaderRequest.MY_ADDRESS);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        SaveBehaviourDataService.startAction(MyAddressActivity.this, BehaviourEnum.MY_ADDRESS.getCode()+"05");
    }

    @Override
    protected void onStop() {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_ADDRESS.getCode() + "xx");
        super.onStop();
    }

    @OnClick(value = { R.id.to_add_address})
    private void btnClick(View v)
    {
        switch (v.getId())
        {
            case R.id.to_add_address:
                SaveBehaviourDataService.startAction(MyAddressActivity.this, BehaviourEnum.MY_ADDRESS.getCode()+"04");
                Bundle bundle=new Bundle();
                bundle.putBoolean(AddOrEditAddrActivity.ISADD,true);
                if(addressesList!=null&&addressesList.size()>0){
                    bundle.putBoolean(AddOrEditAddrActivity.HASADDR,true);
                }
                launchActivity(AddOrEditAddrActivity.class,bundle);
                break;
        }
    }

    private void initView()
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFooterView = inflater.inflate(R.layout.recycleview_footer, null,
                false);
        myRefreshView.setColorSchemeResources(R.color.refresh_tiffany);
        mRecycleview.setHasFixedSize(true);
        mRecycleview.setLayoutManager(getLayoutManager());
        mAdapter = new MyAddrAdapter(this, addressesList, mFooterView);
        mRecycleview.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewListener(this);
        myRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                page = 0;
                requestData();
            }
        });

    }

    AddressListRequest request;
    private void requestData(){
        request=new AddressListRequest();
        request.setPage(page);
        request.setRows(rows);
        sendNetRequest(request, HeaderRequest.MY_ADDRESS, ADDRESS_REQUEST,
                false);
    }

    @Override
    public void onDefaultSet(int position)
    {
        pos=position;
        AddressRequest request=new AddressRequest();
        request.setUuid(addressesList.get(position).getUuid());
        sendNetRequest(request, HeaderRequest.SET_DEFAULT_ADDRESS, ADDRESS_DEFAULT_SET_REQUEST,
                false);
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_ADDRESS.getCode()+"01",request,HeaderRequest.SET_DEFAULT_ADDRESS);
    }

    @Override
    public void onDel(final int position)
    {
        Dialog dialog = new AlertDialog.Builder(MyAddressActivity.this)
                .setMessage(getString(R.string.delete_addr_tip))
                .setNegativeButton(getString(R.string.cancel), null)
                .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        pos=position;
                        AddressRequest request=new AddressRequest();
                        request.setUuid(addressesList.get(position).getUuid());
                        sendNetRequest(request, HeaderRequest.ADDRESS_DEL, ADDRESS_DELETE_REQUEST,
                                true);
                        SaveBehaviourDataService.startAction(MyAddressActivity.this, BehaviourEnum.MY_ADDRESS.getCode()+"03",request,HeaderRequest.ADDRESS_DEL);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();

    }

    @Override
    public void onEdit(int position)
    {
        pos=position;
        Bundle bundle=new Bundle();
        bundle.putParcelable(AddOrEditAddrActivity.ADDRESS,addressesList.get(position));
        bundle.putBoolean(AddOrEditAddrActivity.ISADD,false);
        bundle.putBoolean(AddOrEditAddrActivity.HASADDR,true);
        launchActivity(AddOrEditAddrActivity.class,bundle);
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_ADDRESS.getCode()+"02");
    }

    @Override
    public void onSuccess(String response, String url, int actionId)
    {
        super.onSuccess(response, url, actionId);
        switch (actionId){
            case ADDRESS_REQUEST:
                AddressListResponse addrRes = JsonUtils.fromJson(response,
                        AddressListResponse.class);
                if(COMMON_SUCCESS.equals(addrRes.getCode())){
                   showView(addrRes.getDeliveryAddressList());
                }
                break;
            case ADDRESS_DEFAULT_SET_REQUEST:
                BaseResponse defaultRes=JsonUtils.fromJson(response,
                        BaseResponse.class);
                if(COMMON_SUCCESS.equals(defaultRes.getCode())){
                    setDefaultAddrSuccess();
                }else{
                    ToastUtil.showShort(this,defaultRes.getMsg());
                }
                break;
            case ADDRESS_DELETE_REQUEST:
                BaseResponse delRes=JsonUtils.fromJson(response,
                        BaseResponse.class);
                if(COMMON_SUCCESS.equals(delRes.getCode())){

                    if(addressesList.get(pos).getIsDefault()==1&&addressesList.size()>1){
                        addressesList.remove(pos);
                        onDefaultSet(0);
                    }else{
                        addressesList.remove(pos);
                    }

                    if (addressesList.size() <= 0) {
                        noData.setVisibility(View.VISIBLE);
                    }

                    mAdapter.notifyDataSetChanged();
                }else{
                    ToastUtil.showShort(this,delRes.getMsg());
                }
                break;
        }
    }

    private void showView(List<DeliveryAddress> list){

        noData.setVisibility(View.GONE);
        if (page == 0)
        {
            mFooterView.setVisibility(View.GONE);
            addressesList.clear();
            myRefreshView.setRefreshing(false);

            if (list == null || list.size() <= 0) {
                noData.setVisibility(View.VISIBLE);
            }

        } else if (list == null
                || list.size() < rows)
        {
            mFooterView.setVisibility(View.VISIBLE);
            mAdapter.setNoMoreData(true);
        } else
        {
            mFooterView.setVisibility(View.GONE);
        }
        addressesList.addAll(list);
        mAdapter.notifyDataSetChanged();
        page++;
    }


    private void setDefaultAddrSuccess(){
        for(int i=0;i<addressesList.size();i++){
            if(i==pos){
                addressesList.get(i).setIsDefault(1);
            }else{
                addressesList.get(i).setIsDefault(0);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private boolean isTop;
    private ABaseLinearLayoutManager getLayoutManager()
    {

        final ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(
                this);
        layoutManager.setOnRecyclerViewScrollLocationListener(mRecycleview,
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
                            requestData();
                            mFooterView.setVisibility(View.VISIBLE);
                        }
                        isTop = false;

                    }
                });

        return layoutManager;
    }

}
