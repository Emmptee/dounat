package com.donut.app.http.message.wish;

import com.donut.app.http.message.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 */

public class WishFulfillResponse extends BaseResponse {

    private List<AchieveWish> achieveWishList;

    public List<AchieveWish> getAchieveWishList() {
        return achieveWishList;
    }

    public void setAchieveWishList(List<AchieveWish> achieveWishList) {
        this.achieveWishList = achieveWishList;
    }
}
