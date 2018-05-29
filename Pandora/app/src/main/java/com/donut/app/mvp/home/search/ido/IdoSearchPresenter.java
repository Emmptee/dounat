package com.donut.app.mvp.home.search.ido;

import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.home.HomePageSearchRequest;
import com.donut.app.http.message.home.HomePageSearchResponse;
import com.donut.app.http.message.home.IdoSearchResponse;
import com.donut.app.utils.JsonUtils;


/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public class IdoSearchPresenter extends IdoSearchContract.Presenter {

    private static final int HOME_REQUEST_CODE = 1;

    public String searchContent;

    public void loadData(boolean showLoadingView) {
        HomePageSearchRequest request = new HomePageSearchRequest();
        request.setSearchStr(searchContent);
        super.loadData(request,
                HeaderRequest.IDO_SEARCH,
                HOME_REQUEST_CODE,
                showLoadingView);
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case HOME_REQUEST_CODE:
                IdoSearchResponse response
                        = JsonUtils.fromJson(responseJson, IdoSearchResponse.class);
                if (COMMON_SUCCESS.equals(response.getCode())) {
                    mView.showView(response.getCategoryList());
                } else {
                    showToast(response.getMsg());
                }
                break;
        }
    }
}
