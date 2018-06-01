package com.donut.app.mvp.spinOff.boons;

import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.PraiseRequest;
import com.donut.app.http.message.ShareRequest;
import com.donut.app.http.message.spinOff.WelfareZoneRequest;
import com.donut.app.http.message.spinOff.WelfareZoneResponse;
import com.donut.app.http.message.wish.AddPlayNumRequest;
import com.donut.app.utils.JsonUtils;


/**
 * Created by Qi on 2017/6/5.
 * Description : <br>
 */
public class SpinOffBoonsPresenter<V extends SpinOffBoonsContract.View> extends SpinOffBoonsContract.Presenter<V> {

    private static final int SPIN_OFF_BOONS = 1, LIKE_REQUEST = 2, SHARE_REQUEST = 3, ADD_PLAY_NUM = 4;

    String searchStarName;

    protected int page = 0, rows = 20;

    public void loadData(boolean showLoadingView, int type) {

        WelfareZoneRequest request = new WelfareZoneRequest();
        request.setSearchStarName(searchStarName);
        request.setType(type);
        request.setPage(page);
        request.setRows(rows);

        super.loadData(request, HeaderRequest.SPIN_OFF_BOONS,
                SPIN_OFF_BOONS, showLoadingView);
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case SPIN_OFF_BOONS:
                WelfareZoneResponse response
                        = JsonUtils.fromJson(responseJson, WelfareZoneResponse.class);
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

    void onPlayAudio(String contentId) {
        AddPlayNumRequest request = new AddPlayNumRequest();
        request.setMediaId(contentId);
        request.setIdType(2);
        super.loadData(request, HeaderRequest.ADD_PLAY_NUM,
                ADD_PLAY_NUM, false);
    }
}
