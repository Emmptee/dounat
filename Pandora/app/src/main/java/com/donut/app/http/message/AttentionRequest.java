package com.donut.app.http.message;

/**
 *
 * Created by hard on 2018/2/22.
 */

public class AttentionRequest extends BaseRequest {

    private String starId;
    private String operation;

    public String getStarId() {
        return starId;
    }

    public void setStarId(String starId) {
        this.starId = starId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
