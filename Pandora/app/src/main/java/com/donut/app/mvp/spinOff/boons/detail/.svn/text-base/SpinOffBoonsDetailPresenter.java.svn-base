package com.donut.app.mvp.spinOff.boons.detail;

import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.PraiseRequest;
import com.donut.app.http.message.ShareRequest;
import com.donut.app.http.message.StarBlooperDetailRequest;
import com.donut.app.http.message.StarBlooperDetailResponse;
import com.donut.app.http.message.spinOff.ExpressionPicsDetailRequest;
import com.donut.app.http.message.spinOff.ExpressionPicsDetailResponse;
import com.donut.app.http.message.wish.AddPlayNumRequest;
import com.donut.app.utils.JsonUtils;


/**
 * Created by Qi on 2017/3/27.
 * Description : <br>
 */
public class SpinOffBoonsDetailPresenter extends SpinOffBoonsDetailContract.Presenter {

    private static final int BOONS_DETAIL = 1, ADD_PLAY_NUM = 4, LIKE_REQUEST = 2, SHARE_REQUEST = 3;

    public void loadData(boolean showLoadingView, String b02Id) {
        ExpressionPicsDetailRequest request = new ExpressionPicsDetailRequest();
        request.setB02Id(b02Id);
        super.loadData(request, HeaderRequest.SPIN_OFF_BOONS_DETAIL,
                BOONS_DETAIL, showLoadingView);
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case BOONS_DETAIL:
                ExpressionPicsDetailResponse response
                        = JsonUtils.fromJson(responseJson, ExpressionPicsDetailResponse.class);
                if (COMMON_SUCCESS.equals(response.getCode())) {
                    mView.showView(response);
                } else {
                    showToast(response.getMsg());
                }
                break;
        }
    }


    void onLike(String contentId, boolean like) {
        PraiseRequest request = new PraiseRequest();
        request.setContentId(contentId);
        request.setPraiseType(like ? 1 : 2);
        super.loadData(request, HeaderRequest.SUBJECT_PRAISE,
                LIKE_REQUEST, false);
    }

    void shareRequest(String contentId) {
        ShareRequest request = new ShareRequest();
        request.setContentId(contentId);
        super.loadData(request, HeaderRequest.SHARE,
                SHARE_REQUEST, false);
    }
}
