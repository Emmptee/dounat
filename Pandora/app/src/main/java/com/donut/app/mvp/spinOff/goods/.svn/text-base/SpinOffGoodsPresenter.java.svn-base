package com.donut.app.mvp.spinOff.goods;

import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.spinOff.GoodsListRequest;
import com.donut.app.http.message.spinOff.GoodsListResponse;
import com.donut.app.utils.JsonUtils;


/**
 * Created by Qi on 2017/6/5.
 * Description : <br>
 */
public class SpinOffGoodsPresenter<V extends SpinOffGoodsContract.View> extends SpinOffGoodsContract.Presenter<V> {

    private static final int SPIN_OFF_GOODS = 1;

    String searchStarName;

    protected int page = 0, rows = 20;

    public void loadData(boolean showLoadingView) {

        GoodsListRequest request = new GoodsListRequest();
        request.setType(0);
        request.setPage(page);
        request.setRows(rows);
        request.setRequestType(1);
        request.setSearchInput(1);
        request.setSearchStarName(searchStarName);

        super.loadData(request,
                HeaderRequest.SPIN_OFF_GOODS,
                SPIN_OFF_GOODS,
                showLoadingView);
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case SPIN_OFF_GOODS:
                GoodsListResponse response
                        = JsonUtils.fromJson(responseJson, GoodsListResponse.class);
                if (COMMON_SUCCESS.equals(response.getCode())) {
                    mView.showView(response);
                } else {
                    showToast(response.getMsg());
                }
                break;
        }
    }
}
