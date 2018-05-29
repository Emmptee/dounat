package com.donut.app.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.ABaseLinearLayoutManager;
import com.bis.android.plug.refresh_recycler.listener.OnRecyclerViewScrollLocationListener;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.adapter.MyOrderItemRecyclerViewAdapter;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.customview.MyOrderSelectTypePopupWindow;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.MyOrderRequest;
import com.donut.app.http.message.MyOrderResponse;
import com.donut.app.http.message.OrderOperationRequest;
import com.donut.app.http.message.PayRequest;
import com.donut.app.http.message.PayResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.PayUtils;
import com.donut.app.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        MyOrderItemRecyclerViewAdapter.OnItemClickListener, PayUtils.onPayListener {

    @ViewInject(R.id.my_order_tv_type)
    private TextView tvType;

    @ViewInject(R.id.my_order_srl)
    private SwipeRefreshLayout swl;

    @ViewInject(R.id.my_order_list)
    private RecyclerView mRV;

    @ViewInject(R.id.my_order_tv_msg)
    private TextView tvNoMsg;

    private static final int rows = 10, MY_ORDER_REQUEST = 1, ORDER_OPERATION = 2, PAY_ORDER = 3;

    private int page = 0, selectType = 3;

    private List<MyOrderResponse.MyOrder> orderList = new ArrayList<>();

    private MyOrderItemRecyclerViewAdapter mAdapter;

    private View footerView;

    private static final int ORDER_DETAIL_REQ = 1, GOODS_AREA = 2;

    private String operationType;

    private boolean ALIPAY = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle(getString(R.string.my_order_title), true);

        loadData(true);
        initView();
    }

    private void initView() {
        swl.setOnRefreshListener(this);
        swl.setColorSchemeResources(R.color.refresh_tiffany);

        footerView = LayoutInflater.from(this)
                .inflate(R.layout.recycleview_footer, null, false);
        mAdapter = new MyOrderItemRecyclerViewAdapter(orderList, this, footerView);
        mRV.setHasFixedSize(true);
        mRV.setAdapter(mAdapter);
        mRV.setLayoutManager(getLayoutManager());

        tvNoMsg.setText(Html.fromHtml("暂无订单，快去商品专区看看吧"));
    }

    private MyOrderSelectTypePopupWindow mPopupWindow;

    @OnClick({R.id.my_order_top_layout, R.id.my_order_tv_msg})
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.my_order_top_layout:

                mPopupWindow = new MyOrderSelectTypePopupWindow(MyOrderActivity.this, selectType,
                        new MyOrderSelectTypePopupWindow.OnClickListenerWithPosition() {
                            @Override
                            public void onItemClick(View v, int selectType) {
                                mPopupWindow.dismiss();
                                MyOrderActivity.this.selectType = selectType;
                                switch (selectType) {
//                                    case 1:
//                                        tvType.setText(R.string.order_type_1);
//                                        break;
                                    case 0:
                                        tvType.setText(R.string.order_type_2);
                                        break;
                                    case 2:
                                        tvType.setText(R.string.order_type_3);
                                        break;
                                    default:
                                        tvType.setText(R.string.order_type_0);
                                        break;
                                }
                                page = 0;
                                loadData(true);
                                saveBehaviour("01");
                            }
                        });
                mPopupWindow.showAsDropDown(this.findViewById(R.id.my_order_top_line));
                //, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0)
                break;
            case R.id.my_order_tv_msg:
                //startActivityForResult(new Intent(this, H5WebActivity.class), GOODS_AREA);
                break;
        }
    }

    private MyOrderRequest initRequest;

    private void loadData(boolean isShowDialog) {
        tvNoMsg.setVisibility(View.GONE);
        swl.setVisibility(View.VISIBLE);
        if (isShowDialog) {
            page = 0;
        }

        initRequest = new MyOrderRequest();
        initRequest.setRows(rows);
        initRequest.setPage(page);
        initRequest.setSelectType(selectType);
        sendNetRequest(initRequest, HeaderRequest.MY_ORDER_REQUEST, MY_ORDER_REQUEST, isShowDialog);
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case MY_ORDER_REQUEST:
                swl.setRefreshing(false);
                footerView.setVisibility(View.GONE);
                MyOrderResponse myOrderResponse = JsonUtils.fromJson(response, MyOrderResponse.class);
                if (COMMON_SUCCESS.equals(myOrderResponse.getCode())) {
                    if (page == 0) {
                        orderList.clear();
                    }
                    List<MyOrderResponse.MyOrder> list = myOrderResponse.getOrderList();
                    if (list != null && list.size() > 0) {
                        orderList.addAll(list);
                    } else {
                        if (page == 0) {
//                        showToast("暂无数据");
                            tvNoMsg.setVisibility(View.VISIBLE);
                            swl.setVisibility(View.GONE);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    showToast(myOrderResponse.getMsg());
                }
                break;
            case PAY_ORDER:
                PayResponse payResponse = JsonUtils.fromJson(response, PayResponse.class);
                if (COMMON_SUCCESS.equals(payResponse.getCode())) {
                    if (payResponse.getLanchPay() != null) {
                        //支付宝支付
                        PayUtils.startAliPay(this, this, payResponse.getLanchPay());
                        ALIPAY = true;
                    } else {
                        //微信支付
                        PayUtils.startWxPay(this, this, payResponse);
                        ALIPAY = false;
                    }
                } else {
                    showToast(payResponse.getMsg());
                    if ("9999".equals(payResponse.getCode())) {
                        loadData(true);
                    }
                }
                break;
            case ORDER_OPERATION:
                BaseResponse baseResponse = JsonUtils.fromJson(response, BaseResponse.class);
                if (COMMON_SUCCESS.equals(baseResponse.getCode())) {
                    if ("0".equals(operationType)) {
                        showToast("确认收货成功");
                        loadData(true);
                    } else if ("1".equals(operationType)) {
                        showToast("取消订单成功");
                        loadData(true);
                    } else if ("2".equals(operationType)) {
                        showToast("删除订单成功");
//                        setResult(RESULT_OK);
//                        finish();
                        loadData(true);
                    }
                } else {
                    showToast(baseResponse.getMsg());
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 0;
        loadData(false);
    }

    @Override
    public void OnItemClick(String uuid) {
        startActivityForResult(
                new Intent(this, OrderDetailActivity.class)
                        .putExtra(OrderDetailActivity.ORDER_ID, uuid),
                ORDER_DETAIL_REQ);

    }

    /**
     * state 1:确认收货 2:观看视频 3:数据报告 4:去支付 5:取消订单 6:删除订单
     */
    @Override
    public void OnBtnClick(String uuid, int state) {
        switch (state) {
            case 1:
                showDialog(uuid, "0", "是否确认收货?");
                break;
            case 2:
                h5GoodsDetail(uuid, "1");
                break;
            case 3:
//                h5GoodsDetail(uuid, "2");
//                Intent intent = new Intent(this, H5ReadDocActivity.class);
//                intent.putExtra(H5ReadDocActivity.DOC_URL, uuid);
//                startActivity(intent);
                Intent intent_report = new Intent(this, H5WebActivity.class);
                intent_report.putExtra(H5WebActivity.URL, "file:///android_asset/www/goods_details_doc_display.html");
                sp_Info.edit().putString("docUrl", uuid).apply();
                startActivity(intent_report);
                break;
            case 4:
                goToPay(uuid);
                break;
            case 5:
                showDialog(uuid, "1", "是否取消订单?");
                break;
            case 6:
                showDialog(uuid, "2", "是否删除订单?");
                break;
        }
    }

    private void h5GoodsDetail(String goodsId, String goodsType) {
        sp_Info.edit().putString("goodsId", goodsId).apply();
        Intent intent_goods_detail = new Intent(this, H5WebActivity.class);
        switch (goodsType) {
            case "0":
                intent_goods_detail.putExtra(H5WebActivity.URL, "file:///android_asset/www/goods-details.html");
                break;
            case "1":
                intent_goods_detail.putExtra(H5WebActivity.URL, "file:///android_asset/www/goods_details_videos.html");
                break;
            case "2":
                intent_goods_detail.putExtra(H5WebActivity.URL, "file:///android_asset/www/goods_details_documents.html");
                break;
        }
        startActivity(intent_goods_detail);
    }

    private void goToPay(String orderId) {
        List<String> list = new ArrayList<>();
        list.add(orderId);
        PayRequest request = new PayRequest();
        request.setD01Ids(list);
        request.setSubject(getString(R.string.app_name) + "-商品购买");
        request.setBody(getString(R.string.app_name) + "-商品购买");
        request.setPayEntrance(2);
        sendNetRequest(request, HeaderRequest.PAY_ORDER, PAY_ORDER);
    }

    @Override
    public void onPaySuccess(int paytype) {
        showToast("支付成功!");
        loadData(true);
    }

    @Override
    public void onPayFail(int paytype, int stateCode) {
        if (paytype == 0) {
            if (stateCode == -1) {
                showToast(getString(R.string.pay_result_fail));
            }
            if (stateCode == -2) {
                showToast(getString(R.string.pay_result_cancle));
            }
        }
    }

    private void showDialog(final String uuid, final String orderState, String msg) {
        Dialog dialog = new AlertDialog.Builder(this)
                .setMessage(msg)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveBehaviour("02");
                    }
                })
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendOrderState(uuid, orderState);
                    }
                })
                .create();
        dialog.show();
    }

    private void sendOrderState(String orderId, String operationType) {
        this.operationType = operationType;
        OrderOperationRequest orderOperationRequest = new OrderOperationRequest();
        orderOperationRequest.setOrderId(orderId);
        orderOperationRequest.setOperationType(operationType);
        sendNetRequest(orderOperationRequest, HeaderRequest.ORDER_OPERATION, ORDER_OPERATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ORDER_DETAIL_REQ:
                if (resultCode == RESULT_OK) {
                    page = 0;
                    loadData(false);
                }
                break;
            case GOODS_AREA:
                page = 0;
                loadData(false);
                break;
        }
    }

    private boolean isTop;

    public RecyclerView.LayoutManager getLayoutManager() {
        ABaseLinearLayoutManager layoutManager = new ABaseLinearLayoutManager(this);


        layoutManager.setOnRecyclerViewScrollLocationListener(mRV,
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
                            loadData(false);
                        }
                        isTop = false;
                    }
                });

        return layoutManager;
    }

    @Override
    protected void onStart() {
        super.onStart();
        saveBehaviour("00", initRequest, HeaderRequest.MY_ORDER_REQUEST);
    }

    @Override
    protected void onStop() {
        saveBehaviour("xx");
        super.onStop();
    }

    private void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_ORDER.getCode() + functionCode);
    }

    private void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.MY_ORDER.getCode() + functionCode, request, header);
    }

}
