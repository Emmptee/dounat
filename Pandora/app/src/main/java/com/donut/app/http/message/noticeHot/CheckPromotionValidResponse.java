package com.donut.app.http.message.noticeHot;

import com.donut.app.http.message.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class CheckPromotionValidResponse extends BaseResponse{

    private List<String> orderIdList;

    public List<String> getOrderIdList() {
        return orderIdList;
    }

    public void setOrderIdList(List<String> orderIdList) {
        this.orderIdList = orderIdList;
    }
}
