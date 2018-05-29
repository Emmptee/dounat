package com.donut.app.mvp.blooper.detail;

import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.StarBlooperDetailRequest;
import com.donut.app.http.message.StarBlooperDetailResponse;
import com.donut.app.http.message.wish.AddPlayNumRequest;
import com.donut.app.utils.JsonUtils;


/**
 * Created by Qi on 2017/3/27.
 * Description : <br>
 */
public class BlooperDetailPresenter extends BlooperDetailContract.Presenter {

    private static final int STAR_BLOOPER_DETAIL = 1, ADD_PLAY_NUM = 2;

    protected int page = 0, rows = 10;

    public void loadData(boolean showLoadingView, String starId) {
        StarBlooperDetailRequest initRequest = new StarBlooperDetailRequest();
        initRequest.setStarId(starId);
        initRequest.setPage(page);
        initRequest.setRows(rows);
        super.loadData(initRequest,
                HeaderRequest.STAR_BLOOPER_DETAIL,
                STAR_BLOOPER_DETAIL,
                showLoadingView);
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case STAR_BLOOPER_DETAIL:
                StarBlooperDetailResponse response
                        = JsonUtils.fromJson(responseJson, StarBlooperDetailResponse.class);
                if (COMMON_SUCCESS.equals(response.getCode())) {
                    mView.showView(response);
                } else {
                    showToast(response.getMsg());
                }
                break;
        }
    }

    void addPlayNum(String contentId){
        AddPlayNumRequest request = new AddPlayNumRequest();
        request.setMediaId(contentId);
        request.setIdType(2);
        super.loadData(request,
                HeaderRequest.ADD_PLAY_NUM,
                ADD_PLAY_NUM,
                false);
    }
}
