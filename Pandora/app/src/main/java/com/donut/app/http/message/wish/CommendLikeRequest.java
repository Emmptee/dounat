package com.donut.app.http.message.wish;

/**
 * Created by hard on 2018/1/30.
 */

public class CommendLikeRequest {

  private String contentId; //内容id
  private int praiseType;//页数

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public int getPraiseType() {
        return praiseType;
    }

    public void setPraiseType(int praiseType) {
        this.praiseType = praiseType;
    }
}
