package com.donut.app.http.message.wish;

import com.donut.app.http.message.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 */

public class WishListResponse extends BaseResponse{

    private List<NotAchieveWishModel> notAchieveWishList;

    private List<NotAchieveWishModel> myWishList;

    public List<NotAchieveWishModel> getNotAchieveWishList() {
        return notAchieveWishList;
    }

    public void setNotAchieveWishList(List<NotAchieveWishModel> notAchieveWishList) {
        this.notAchieveWishList = notAchieveWishList;
    }

    public List<NotAchieveWishModel> getMyWishList() {
        return myWishList;
    }

    public void setMyWishList(List<NotAchieveWishModel> myWishList) {
        this.myWishList = myWishList;
    }
}
