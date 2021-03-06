package com.donut.app.activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.OrderDetailRequest;
import com.donut.app.http.message.OrderDetailResponse;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends BaseActivity implements PayUtils.onPayListener {

    @ViewInject(R.id.head_right_tv)
    private TextView tvRight;

    @ViewInject(R.id.order_detail_tv_orderNo)
    private TextView tvOrderNo;

    @ViewInject(R.id.order_detail_tv_date)
    private TextView tvDate;

    @ViewInject(R.id.order_detail_tv_orderStatus)
    private TextView tvOrderStatus;

    @ViewInject(R.id.order_detail_ll_express)
    private LinearLayout llExpress;

    @ViewInject(R.id.order_detail_tv_expressName)
    private TextView tvExpressName;

    @ViewInject(R.id.order_detail_tv_expressNo)
    private TextView tvExpressNo;

    @ViewInject(R.id.order_detail_ll_receive)
    private LinearLayout llReceive;

    @ViewInject(R.id.order_detail_tv_receiver)
    private TextView tvReceiver;

    @ViewInject(R.id.order_detail_tv_phone)
    private TextView tvPhone;

    @ViewInject(R.id.order_detail_tv_address)
    private TextView tvAddress;

    @ViewInject(R.id.order_detail_tv_subjectName)
    private TextView tvSubjectName;

    @ViewInject(R.id.order_detail_iv_thumbnail)
    private ImageView ivThumbnail;

    @ViewInject(R.id.order_detail_tv_description)
    private TextView tvDescription;

    @ViewInject(R.id.order_detail_tv_price)
    private TextView tvPrice;

    @ViewInject(R.id.order_detail_tv_num)
    private TextView tvNum;

    @ViewInject(R.id.order_detail_tv_freight)
    private TextView tvFreight;

    @ViewInject(R.id.order_detail_tv_realPayAmount)
    private TextView tvRealPayAmount;

    @ViewInject(R.id.order_detail_btn_success)
    private Button btnSuccess;

    @ViewInject(R.id.order_detail_btn_cancel)
    private Button btnCancel;

    @ViewInject(R.id.order_detail_btn_pay)
    private Button btnPay;

    @ViewInject(R.id.order_detail_btn_del)
    private Button btnDel;

    @ViewInject(R.id.order_detail_btn_show)
    private Button btnShow;

    @ViewInject(R.id.order_detail_ll_btn)
    private LinearLayout llBtn;


    public static final int ORDER_DETAIL_LOAD = 1, ORDER_OPERATION = 2, PAY_ORDER = 3;
    public static final String ORDER_ID = "ORDER_ID";

    private String orderId, goodsId, operationType, goodsType, staticUrl;

    private boolean ALIPAY = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        StatusBarUtil.setColor(this, Constant.default_bar_color);
        ViewUtils.inject(this);
        updateHeadTitle(getString(R.string.order_detail_title), true);

        initData();
        loadData();
    }

    private void initData() {

        if (getIntent() != null) {
            orderId = getIntent().getStringExtra(ORDER_ID);
        }
    }
    private OrderDetailRequest initRequest;
    private void loadData() {
        initRequest = new OrderDetailRequest();
        initRequest.setOrderId(orderId);
        sendNetRequest(initRequest, HeaderRequest.ORDER_DETAIL, ORDER_DETAIL_LOAD);
    }

    @OnClick({R.id.order_detail_btn_del,
            R.id.order_detail_btn_success,
            R.id.order_detail_btn_cancel,
            R.id.order_detail_btn_pay,
            R.id.order_detail_ll_goods,
            R.id.order_detail_tv_expressNo,
            R.id.order_detail_btn_show})
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.order_detail_btn_del:
//                showToast("删除订单");
                showDialog("2", "是否删除订单?");
                saveBehaviour("05");
                break;
            case R.id.order_detail_btn_success:
//                showToast("确认收货");
                showDialog("0", "是否确认收货?");
                saveBehaviour("01");
                break;
            case R.id.order_detail_btn_cancel:
//                showToast("取消订单");
                showDialog("1", "是否取消订单?");
                saveBehaviour("04");
                break;
            case R.id.order_detail_btn_pay:
//                showToast("去支付");
                goToPay();
                saveBehaviour("03");
                break;
            case R.id.order_detail_ll_goods:
                // 跳转商品详情页!
                h5GoodsDetail();
                // showToast("商品详情");
                break;
            case R.id.order_detail_tv_expressNo:
                CharSequence expressNo = tvExpressNo.getText();
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("expressNo", expressNo.subSequence(5, expressNo.length()));
                cm.setPrimaryClip(clipData);
                showToast("快递单号已复制");
                break;
            case R.id.order_detail_btn_show:
//                showToast("在线观看");
                if ("1".equals(goodsType)) {
                    h5GoodsDetail();
                } else {
//                    Intent intent = new Intent(this, H5ReadDocActivity.class);
//                    intent.putExtra(H5ReadDocActivity.DOC_URL, staticUrl);
//                    startActivity(intent);
                    Intent intent_report = new Intent(this, H5WebActivity.class);
                    intent_report.putExtra(H5WebActivity.URL, "file:///android_asset/www/goods_details_doc_display.html");
                    sp_Info.edit().putString("docUrl",staticUrl).apply();
                    startActivity(intent_report);
                }
                break;
        }
    }

    private void h5GoodsDetail() {
        sp_Info.edit().putString("goodsId", goodsId).apply();
        Intent intent_goods_detail = new Intent(this, H5WebActivity.class);
        switch (goodsType){
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

    private void goToPay() {
        List<String> list = new ArrayList<>();
        list.add(orderId);
        PayRequest request = new PayRequest();
        request.setD01Ids(list);
        request.setSubject(getString(R.string.app_name) + "-商品购买");
        request.setBody(getString(R.string.app_name) + "-商品购买");
        request.setPayEntrance(2);
        sendNetRequest(request, HeaderRequest.PAY_ORDER, PAY_ORDER);
    }

    private void showDialog(final String orderState, String msg) {
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
                        sendOrderState(orderState);
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        saveBehaviour("06");
        if (!TextUtils.isEmpty(operationType)) {
            setResult(RESULT_OK);
            //finish();
        }
        super.onBackPressed();
    }

    private void sendOrderState(String operationType) {
        this.operationType = operationType;
        OrderOperationRequest orderOperationRequest = new OrderOperationRequest();
        orderOperationRequest.setOrderId(orderId);
        orderOperationRequest.setOperationType(operationType);
        sendNetRequest(orderOperationRequest, HeaderRequest.ORDER_OPERATION, ORDER_OPERATION);
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case ORDER_DETAIL_LOAD:
                OrderDetailResponse detailResponse = JsonUtils.fromJson(response, OrderDetailResponse.class);
                if (COMMON_SUCCESS.equals(detailResponse.getCode())) {
                    showView(detailResponse);
                } else {
                    showToast(detailResponse.getMsg());
                }
                break;
            case ORDER_OPERATION:
                BaseResponse baseResponse = JsonUtils.fromJson(response, BaseResponse.class);
                if (COMMON_SUCCESS.equals(baseResponse.getCode())) {
                    if ("0".equals(operationType)) {
                        showToast("确认收货成功");
                        loadData();
                    } else if ("1".equals(operationType)) {
                        showToast("取消订单成功");
                        loadData();
                    } else if ("2".equals(operationType)) {
                        showToast("删除订单成功");
                        setResult(RESULT_OK);
                        finish();
                    }
                } else {
                    showToast(baseResponse.getMsg());
                }
                break;
            case PAY_ORDER:
                PayResponse payResponse = JsonUtils.fromJson(response, PayResponse.class);
                if (COMMON_SUCCESS.equals(payResponse.getCode())){
                    if (payResponse.getLanchPay() != null) {
                        //支付宝支付
                        PayUtils.startAliPay(this, this, payResponse.getLanchPay());
                        ALIPAY = true;
                    } else {
                        //微信支付
                        PayUtils.startWxPay(this, this, payResponse);
                        ALIPAY = false;
                    }
                }else {
                    if ("9999".equals(payResponse.getCode())) {
                        loadData();
                    }
                    showToast(payResponse.getMsg());
                }
                break;
        }
    }

    private void showView(OrderDetailResponse detail) {

        btnSuccess.setVisibility(View.GONE);
        llBtn.setVisibility(View.GONE);
        btnShow.setVisibility(View.GONE);
        btnDel.setVisibility(View.GONE);

        goodsId = detail.getGoodsId();

        tvOrderNo.setText(String.format(getString(R.string.order_detail_orderNo), detail.getOrderNo()));
        tvDate.setText(String.format(getString(R.string.order_detail_date), detail.getCreateTime()));

        staticUrl = detail.getStaticUrl();
        goodsType = detail.getType();
        if (goodsType == null || "".equals(goodsType)) {
            goodsType = "-1";
        }

        String orderStatus = detail.getOrderStatus();
        if(orderStatus == null || "".equals(orderStatus)) {
            orderStatus = "-1";
        }

        String payStatus = detail.getPayStatus();
        if (payStatus == null || "".equals(payStatus)) {
            payStatus = "-1";
        }

        String strOrderStatus = "";

        switch (payStatus) {
            case "0":
                strOrderStatus = "未支付";
                llBtn.setVisibility(View.VISIBLE);
                btnSuccess.setVisibility(View.GONE);
                break;
            case "1":
                switch (orderStatus) {
                    case "0":
                        strOrderStatus = "待发货";
//                 btnSuccess.setVisibility(View.VISIBLE);

                        if (!"0".equals(goodsType)) {
                            strOrderStatus = "交易结束";
                            btnShow.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "1":
                        strOrderStatus = "已发货";
                        btnSuccess.setVisibility(View.VISIBLE);
                        break;
                    case "2":
                        strOrderStatus = "交易结束";
                        btnDel.setVisibility(View.VISIBLE);
                        if (!"0".equals(goodsType)) {
                            btnShow.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                break;
            case "2":
                strOrderStatus = "已退款";
                btnSuccess.setVisibility(View.GONE);
                btnDel.setVisibility(View.VISIBLE);
                if (!"0".equals(detail.getType())) {
                    btnShow.setVisibility(View.GONE);
                }
                break;
            case "3":
                strOrderStatus = "已提现";
                btnSuccess.setVisibility(View.GONE);
                btnDel.setVisibility(View.VISIBLE);
                if (!"0".equals(detail.getType())) {
                    btnShow.setVisibility(View.GONE);
                }
                break;
            case "4":
                strOrderStatus = "已取消";
                btnDel.setVisibility(View.VISIBLE);
                btnSuccess.setVisibility(View.GONE);
                if (!"0".equals(detail.getType())) {
                    btnShow.setVisibility(View.GONE);
                }
                break;
        }

        tvOrderStatus.setText(strOrderStatus);

        //判断是实物订单的话以下内容显示
        if ("0".equals(goodsType)) {
            llReceive.setVisibility(View.VISIBLE);
            tvReceiver.setText(String.format(getString(R.string.order_detail_receiver), detail.getReceiver()));
            tvPhone.setText(detail.getPhone());
            tvAddress.setText(String.format(getString(R.string.order_detail_address), detail.getAddress()));
            if ("1".equals(orderStatus) || "2".equals(orderStatus)) {
                llExpress.setVisibility(View.VISIBLE);
                tvExpressName.setText(String.format(getString(R.string.order_detail_expressName), detail.getExpressName()));
                tvExpressNo.setText(String.format(getString(R.string.order_detail_expressNo), detail.getExpressNo()));
            }
        }

        Glide.with(this)
                .load(detail.getThumbnail())
                .placeholder(R.drawable.default_bg)
                .error(R.drawable.default_bg)
                .centerCrop()
                .into(ivThumbnail);

        String strTitle = detail.getSubjectName();
        if (strTitle == null) {
            strTitle = "商品专区";
        }
        switch (goodsType) {
            case "1":
                strTitle = "视频花絮/"+strTitle;
                break;
            case "0":
                strTitle = "商品实物/"+strTitle;
                break;
            case "2":
                strTitle = "报告文档/"+strTitle;
                break;
        }
        tvSubjectName.setText(strTitle);

        tvDescription.setText(detail.getDescription());
        try {
            tvPrice.setText(String.format(
                    getString(R.string.order_detail_money),
                    new BigDecimal(detail.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP)));
            tvNum.setText("x" + detail.getNum());

            tvRealPayAmount.setText(String.format(
                    getString(R.string.order_detail_money),
                    new BigDecimal(detail.getRealPayAmount()).setScale(2, BigDecimal.ROUND_HALF_UP)));
            tvFreight.setText(String.format(
                    getString(R.string.order_detail_money),
                    new BigDecimal(detail.getFreight()).setScale(2, BigDecimal.ROUND_HALF_UP)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaySuccess(int paytype)
    {
        showToast("支付成功!");
        loadData();
    }

    @Override
    public void onPayFail(int paytype, int stateCode)
    {
        if (paytype == 0)
        {
            if (stateCode == -1)
            {
                showToast(getString(R.string.pay_result_fail));
            }
            if (stateCode == -2)
            {
                showToast(getString(R.string.pay_result_cancle));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        saveBehaviour("00", initRequest, HeaderRequest.ORDER_DETAIL);
    }

    @Override
    protected void onStop() {
        saveBehaviour("xx");
        super.onStop();
    }

    private void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.ORDER_DETAIL.getCode() + functionCode);
    }

    private void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(this, BehaviourEnum.ORDER_DETAIL.getCode() + functionCode, request, header);
    }
}
