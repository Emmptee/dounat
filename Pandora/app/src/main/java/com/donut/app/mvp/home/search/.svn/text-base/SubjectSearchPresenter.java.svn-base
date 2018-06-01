package com.donut.app.mvp.home.search;

import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.SubjectListDetail;
import com.donut.app.http.message.SubjectListResponse;
import com.donut.app.http.message.home.ChannelItem;
import com.donut.app.http.message.home.ContentCategory;
import com.donut.app.http.message.home.ContentItem;
import com.donut.app.http.message.home.HomePageSearchRequest;
import com.donut.app.http.message.home.HomePageSearchResponse;
import com.donut.app.http.message.home.HomePageSearchV2Response;
import com.donut.app.http.message.home.WishItem;
import com.donut.app.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Qi on 2017/3/21.
 * Description : <br>
 */
public class SubjectSearchPresenter extends SubjectSearchContract.Presenter {

    private static final int HOME_REQUEST_CODE = 1;

    public String searchContent;

    public void loadData(boolean showLoadingView) {
        HomePageSearchRequest request = new HomePageSearchRequest();
        request.setSearchStr(searchContent);
        super.loadData(request,
                HeaderRequest.SEACH_MESSAGE_STAR,
                HOME_REQUEST_CODE,
                showLoadingView);
    }

    @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case HOME_REQUEST_CODE:
                 HomePageSearchV2Response response = JsonUtils.fromJson(responseJson,HomePageSearchV2Response.class);
                //HomePageSearchResponse response = JsonUtils.fromJson(responseJson, HomePageSearchResponse.class);
                if (COMMON_SUCCESS.equals(response.getCode())) {
                  /*  mView.showView(response);*/
                    mView.showSearchView(response);
                } else {
                    showToast(response.getMsg());
                }
                break;
        }
    }

/*     @Override
    public void onSuccess(String responseJson, String url, int actionId) {
        switch (actionId) {
            case HOME_REQUEST_CODE:
               // HomePageSearchV2Response homePageSearchV2Response = JsonUtils.toJson(responseJson,HomePageSearchV2Response.class);
                HomePageSearchResponse response = JsonUtils.fromJson(responseJson, HomePageSearchResponse.class);
                if (COMMON_SUCCESS.equals(response.getCode())) {
                    mView.showView(response);
                } else {
                    showToast(response.getMsg());
                }
                break;
        }
    }*/
}
