package com.donut.app.http.message.home;

import com.donut.app.http.message.BaseResponse;

import java.util.List;

public class HomePageSearchV2Response extends BaseResponse {
    private List<ContentCategory> categoryList;


    public List<ContentCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<ContentCategory> categoryList) {
        this.categoryList = categoryList;
    }
}
