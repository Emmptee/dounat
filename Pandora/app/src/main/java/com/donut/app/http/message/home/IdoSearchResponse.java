package com.donut.app.http.message.home;

import com.donut.app.http.message.BaseResponse;

import java.util.List;

/**
 * @author Qi
 * @date 2018/04/04
 */
public class IdoSearchResponse extends BaseResponse {

    private List<ContentCategory> categoryList;

    public List<ContentCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<ContentCategory> categoryList) {
        this.categoryList = categoryList;
    }
}
