package com.donut.app.http.message.wish;

import com.donut.app.http.message.ContentComments;

import java.util.List;

/**
 * WishDetailsResponse
 * Created by Qi on 2017/2/25.
 */

public class WishDetailsResponse extends AchieveWish {

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private List<ContentComments> currentComments;

    public List<ContentComments> getCurrentComments() {
        return currentComments;
    }

    public void setCurrentComments(List<ContentComments> currentComments) {
        this.currentComments = currentComments;
    }
}
