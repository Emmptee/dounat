package com.donut.app.mvp.spinOff.plans;

import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.PraiseRequest;
import com.donut.app.http.message.ShareRequest;
import com.donut.app.http.message.spinOff.ExclusivePlanRequest;
import com.donut.app.http.message.spinOff.ExclusivePlanResponse;
import com.donut.app.http.message.wish.AddPlayNumRequest;
import com.donut.app.utils.JsonUtils;


/**
 * Created by Qi on 2017/6/5.
 * Description : <br>
 */
public class SpinOffPlansPresenter<V extends SpinOffPlansContract.View> extends SpinOffPlansContract.Presenter<V> {

    private static final int SPIN_OFF_PLANS = 1, LIKE_REQUEST = 2, SHARE_REQUEST = 3, ADD_PLAY_NUM = 4;

    String searchStarName;

    protected int page = 0, rows = 10;

    public void loadData(boolean showLoadingView) {

        ExclusivePlanRequest request = new ExclusivePlanRequest();
        request.setSearchStarName(searchStarName);
        request.setPage(page);
        request.setRows(rows);

        super.loadData(request,
                HeaderRequest.SPIN_OFF_PLANS,
                SPIN_OFF_PLANS,
                showLoadingView);
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case SPIN_OFF_PLANS:
                ExclusivePlanResponse response
                        = JsonUtils.fromJson(responseJson, ExclusivePlanResponse.class);
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

    void addPlayNum(String contentId){
        AddPlayNumRequest request = new AddPlayNumRequest();
        request.setMediaId(contentId);
        request.setIdType(2);
        super.loadData(request, HeaderRequest.ADD_PLAY_NUM,
                ADD_PLAY_NUM, false);
    }
}
