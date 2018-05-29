package com.donut.app.http.message.noticeHot;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class CheckPromotionValidRequest {

    private String promotionId;

    private List<Goods> goodsList;

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public static class Goods{

        private String d02Id;
        private int num;

        public String getD02Id() {
            return d02Id;
        }

        public void setD02Id(String d02Id) {
            this.d02Id = d02Id;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
