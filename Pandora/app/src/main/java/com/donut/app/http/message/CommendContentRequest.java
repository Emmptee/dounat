package com.donut.app.http.message;

/**
 * Created by hard on 2018/2/9.
 */

public class CommendContentRequest extends BaseRequest{

    private String contentId;
    private String operationType;
    private String content;

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentId() {
        return contentId;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getContent() {
        return content;
    }
}
