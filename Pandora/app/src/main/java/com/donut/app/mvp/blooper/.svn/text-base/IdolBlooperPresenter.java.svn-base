package com.donut.app.mvp.blooper;

import android.util.Log;

import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.StarListRequest;
import com.donut.app.http.message.StarListResponse;
import com.donut.app.utils.JsonUtils;

/**
 * Created by Qi on 2017/3/16.
 * Description : <br>
 */
public class IdolBlooperPresenter extends BlooperContract.Presenter {

    private static final String TAG = "BlooperPresenter";

    private static final int STAR_BLOOPER = 1;

    public String searchName = "";

    public int page = 0, rows = 30;

    public void loadData(boolean showLoadingView) {
        StarListRequest initRequest = new StarListRequest();
        initRequest.setSearchName(searchName);
        Log.i(TAG, "loadData: searchName");
        initRequest.setPage(page);
        initRequest.setRows(rows);
        if (searchName==null || searchName =="") {
            super.loadData(initRequest,
                    HeaderRequest.STARS_REQUEST,
                    STAR_BLOOPER,
                    showLoadingView);
            return;
        }
        super.loadData(initRequest,
                HeaderRequest.SEACH_MESSAGE_STAR,
                STAR_BLOOPER,
                showLoadingView);
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        Log.i(TAG, "onSuccess: actionId" + actionId);
        switch (actionId) {
            case STAR_BLOOPER:
                StarListResponse response
                        = JsonUtils.fromJson(responseJson, StarListResponse.class);
                if (COMMON_SUCCESS.equals(response.getCode())) {
                    Log.i(TAG, "onSuccess: 获取的消息为:" + response.getStarList());
                    mView.showView(response);
                } else {
                    showToast(response.getMsg());
                }
                break;
        }
    }
}