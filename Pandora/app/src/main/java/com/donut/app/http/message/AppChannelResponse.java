package com.donut.app.http.message;

import java.util.List;

/**
 * AppChannelResponse
 * Created by Qi on 2017/3/2.
 */

public class AppChannelResponse extends BaseResponse {
    private List<AppChannel> recommendList;

    private List<AppChannel> generalList;

    public List<AppChannel> getRecommendList() {
        return recommendList;
    }

    public void setRecommendList(List<AppChannel> recommendList) {
        this.recommendList = recommendList;
    }

    public List<AppChannel> getGeneralList() {
        return generalList;
    }

    public void setGeneralList(List<AppChannel> generalList) {
        this.generalList = generalList;
    }
}

