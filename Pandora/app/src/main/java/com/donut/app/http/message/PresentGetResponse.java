package com.donut.app.http.message;

/**
 * Created by Qi on 2016/8/1.
 */
public class PresentGetResponse extends BaseResponse {

    private double ratio;

    private float commentPrice;

    private float rechargeRatio;

    public float getRechargeRatio()
    {
        return rechargeRatio;
    }

    public void setRechargeRatio(float rechargeRatio)
    {
        this.rechargeRatio = rechargeRatio;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public float getCommentPrice() {
        return commentPrice;
    }

    public void setCommentPrice(float commentPrice) {
        this.commentPrice = commentPrice;
    }
}
