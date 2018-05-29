package com.donut.app.http.message;

/**
 * Created by hard on 2018/2/7.
 */

public class StarCollectRequest {

    private String contentId;
    private int status;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
