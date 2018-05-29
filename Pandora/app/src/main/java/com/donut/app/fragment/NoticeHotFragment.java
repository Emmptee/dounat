package com.donut.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.activity.H5WebActivity;
import com.donut.app.activity.LoginActivity;
import com.donut.app.adapter.NoticeHotRecyclerViewAdapter;
import com.donut.app.config.Constant;
import com.donut.app.fragment.base.BaseFragment;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.noticeHot.CheckPromotionValidRequest;
import com.donut.app.http.message.noticeHot.CheckPromotionValidResponse;
import com.donut.app.http.message.noticeHot.PromotionResponse;
import com.donut.app.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoticeHotFragment extends BaseFragment {

    private List<PromotionResponse.Promotion> promotionList = new ArrayList<>();

    private OnNoticeHotFragmentListener mListener;

    public static NoticeHotFragment newInstance() {
        return new NoticeHotFragment();
    }

    private static final int NOTICE_REQUEST = 1, NOTICE_CHECK_REQUEST = 2;

    private NoticeHotRecyclerViewAdapter mAdapter;

    private TextView no_data;

    private static final int GIVE_GOODS_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener = new Listener();

        if (promotionList.size() <= 0) {
            loadData();
        }
    }

    private void loadData() {
        sendNetRequest(new Object(), HeaderRequest.NOTICE_HOT_CODE, NOTICE_REQUEST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice_list, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_notice_list);
        mRecyclerView.setLayoutManager(new MyLinearLayoutManager(getContext()));

        mAdapter = new NoticeHotRecyclerViewAdapter(getContext(), promotionList, mListener);
        mRecyclerView.setAdapter(mAdapter);
        SwipeRefreshLayout srl = (SwipeRefreshLayout) view.findViewById(R.id.fragment_notice_srl);
        srl.setEnabled(false);
        no_data = (TextView) view.findViewById(R.id.no_data);
        no_data.setText("暂时没有活动，敬请期待");
        return view;
    }

    @Override
    public void onSuccess(String response, String url, int actionId) {
        super.onSuccess(response, url, actionId);
        switch (actionId) {
            case NOTICE_REQUEST:
                PromotionResponse promotion = JsonUtils.fromJson(response, PromotionResponse.class);
                if (COMMON_SUCCESS.equals(promotion.getCode())) {
                    promotionList.clear();
                    List<PromotionResponse.Promotion> list = promotion.getPromotionList();
                    if (list != null && list.size() > 0) {
                        no_data.setVisibility(View.GONE);
                        promotionList.addAll(list);
                        mAdapter.setCheckItemUuid(list.get(0).getUuid());
                    } else {
                        no_data.setVisibility(View.VISIBLE);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    showToast(promotion.getMsg());
                }
                break;
            case NOTICE_CHECK_REQUEST:
                CheckPromotionValidResponse validResponse
                        = JsonUtils.fromJson(response, CheckPromotionValidResponse.class);
                if (COMMON_SUCCESS.equals(validResponse.getCode())) {
                    List<String> orderIdList = validResponse.getOrderIdList();
                    if (orderIdList != null && orderIdList.size() > 0) {
                        StringBuilder orderIdBuilder = new StringBuilder();
                        for (String str :orderIdList ) {
                            orderIdBuilder.append("\\\"");
                            orderIdBuilder.append(str);
                            orderIdBuilder.append("\\\"");
                            orderIdBuilder.append(",");
                        }
                        String orderIds = orderIdBuilder.toString();
                        SharedPreferences sp_Info = getContext()
                                .getSharedPreferences(Constant.SP_INFO, Context.MODE_PRIVATE);
                        sp_Info.edit().putString("orderIds",
                                orderIds.substring(0, orderIds.length() - 1)).apply();
                        Intent intent = new Intent(getContext(), H5WebActivity.class);
                        intent.putExtra(H5WebActivity.URL, "file:///android_asset/www/promotionOrder.html");
                        startActivityForResult(intent, GIVE_GOODS_CODE);
                    }

                } else {
                    showToast(validResponse.getMsg());
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GIVE_GOODS_CODE:
                loadData();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnNoticeHotFragmentListener {

        void onReceiveGoods(Map<String, Integer> goodsIdMap, String uuid);

        void onGoodsDetail(String goodsId, String goodsType);
    }

    private class Listener implements OnNoticeHotFragmentListener {

        @Override
        public void onReceiveGoods(Map<String, Integer> goodsIdMap, String uuid) {
            SharedPreferences sp_Info = getContext().getSharedPreferences(
                    Constant.SP_INFO, Context.MODE_PRIVATE);
            if (!sp_Info.getBoolean(Constant.IS_LOGIN, false)) {
                startActivityForResult(new Intent(getContext(), LoginActivity.class), GIVE_GOODS_CODE);
                showToast("您还未登录,请先登录");
                return;
            }

            CheckPromotionValidRequest request = new CheckPromotionValidRequest();
            request.setPromotionId(uuid);
            request.setGoodsList(setCreateOrderData(goodsIdMap));
            sendNetRequest(request, HeaderRequest.NOTICE_CHECK_CODE, NOTICE_CHECK_REQUEST);
        }

        @Override
        public void onGoodsDetail(String goodsId, String goodsType) {

            SharedPreferences sp_Info = getContext().getSharedPreferences(Constant.SP_INFO,
                    Context.MODE_PRIVATE);
            sp_Info.edit().putString("goodsId", goodsId).apply();
            Intent intent_goods_detail = new Intent(getContext(), H5WebActivity.class);
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


        private List<CheckPromotionValidRequest.Goods> setCreateOrderData(Map<String, Integer> goodsIdMap) {
            List<CheckPromotionValidRequest.Goods> goodsList = new ArrayList<>();
            for (String key : goodsIdMap.keySet()) {
                CheckPromotionValidRequest.Goods goods = new CheckPromotionValidRequest.Goods();
                goods.setD02Id(key);
                goods.setNum(goodsIdMap.get(key));
                goodsList.add(goods);
            }
            return goodsList;
        }
    }

    private boolean recycleViewCanScroll = true;

    class MyLinearLayoutManager extends LinearLayoutManager {

        MyLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public boolean canScrollVertically() {
            return recycleViewCanScroll;
        }
    }
}
