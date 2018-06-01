package com.donut.app.mvp.auction;

import com.donut.app.R;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.PayRequest;
import com.donut.app.http.message.PayResponse;
import com.donut.app.http.message.auction.MyAuctionHandleRequest;
import com.donut.app.http.message.auction.MyAuctionRequest;
import com.donut.app.http.message.auction.MyAuctionResponse;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.PayUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public class MyAuctionPresenter extends MyAuctionContract.Presenter implements PayUtils.onPayListener {

    private static final int MY_AUCTION = 1, CANCEL_MY_AUCTION = 2,
            DELETE_MY_AUCTION = 3, PAY_ORDER = 4;

    protected int page = 0, rows = 10;

    Object requestObject;

    public void loadData(boolean showLoadingView, int type) {
        MyAuctionRequest request = new MyAuctionRequest();
        request.setType(type);
        request.setPage(page);
        request.setRows(rows);
        this.requestObject = request;
        super.loadData(request, HeaderRequest.MY_AUCTION,
                MY_AUCTION, showLoadingView);
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case MY_AUCTION:
                MyAuctionResponse response
                        = JsonUtils.fromJson(responseJson, MyAuctionResponse.class);
                if (COMMON_SUCCESS.equals(response.getCode())) {
                    mView.showView(response.getMyAuctionList());
                } else {
                    showToast(response.getMsg());
                }
                break;
            case CANCEL_MY_AUCTION:
            case DELETE_MY_AUCTION:
                BaseResponse baseResponse = JsonUtils.fromJson(responseJson, BaseResponse.class);
                if (COMMON_SUCCESS.equals(baseResponse.getCode())) {
                    mView.onRefreshView();
                } else {
                    showToast(baseResponse.getMsg());
                }
                break;
            case PAY_ORDER:
                PayResponse payResponse = JsonUtils.fromJson(responseJson, PayResponse.class);
                if (COMMON_SUCCESS.equals(payResponse.getCode())) {
                    if (payResponse.getLanchPay() != null) {
                        //支付宝支付
                        PayUtils.startAliPay(mView.getContext(), this, payResponse.getLanchPay());
                    } else {
                        //微信支付
                        PayUtils.startWxPay(mView.getContext(), this, payResponse);
                    }
                } else {
                    showToast(payResponse.getMsg());
                    if ("9999".equals(payResponse.getCode())) {
                        mView.onRefreshView();
                    }
                }
                break;
        }
    }

    void payAuction(String orderId) {
        List<String> list = new ArrayList<>();
        list.add(orderId);
        PayRequest request = new PayRequest();
        request.setD01Ids(list);
        request.setSubject(mView.getContext().getString(R.string.app_name) + "-商品购买");
        request.setBody(mView.getContext().getString(R.string.app_name) + "-商品购买");
        request.setPayEntrance(2);
        super.loadData(request,
                HeaderRequest.PAY_ORDER,
                PAY_ORDER,
                false);
    }

    void cancelAuction(String d10Id) {
        MyAuctionHandleRequest request = new MyAuctionHandleRequest(d10Id);
        super.loadData(request,
                HeaderRequest.CANCEL_MY_AUCTION,
                CANCEL_MY_AUCTION,
                false);
    }

    void deleteAuction(String d10Id) {
        MyAuctionHandleRequest request = new MyAuctionHandleRequest(d10Id);
        super.loadData(request,
                HeaderRequest.DELETE_MY_AUCTION,
                DELETE_MY_AUCTION,
                false);
    }

    @Override
    public void onPaySuccess(int paytype) {
        showToast("支付成功!");
        mView.onRefreshView();
    }

    @Override
    public void onPayFail(int paytype, int stateCode) {
        if (paytype == 0) {
            if (stateCode == -1) {
                showToast(mView.getContext().getString(R.string.pay_result_fail));
            }
            if (stateCode == -2) {
                showToast(mView.getContext().getString(R.string.pay_result_cancle));
            }
        }
    }

    void saveBehaviour(String functionCode) {
        SaveBehaviourDataService.startAction(mView.getContext(),
                BehaviourEnum.MY_AUCTION_LIST.getCode() + functionCode);
    }

    void saveBehaviour(String functionCode, Object request, String header) {
        SaveBehaviourDataService.startAction(mView.getContext(),
                BehaviourEnum.MY_AUCTION_LIST.getCode() + functionCode, request, header);
    }
}