package com.donut.app.http.message.noticeHot;

import com.donut.app.http.message.BaseResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 */

public class PromotionResponse extends BaseResponse {

    private List<Promotion> promotionList;

    public List<Promotion> getPromotionList() {
        return promotionList;
    }

    public void setPromotionList(List<Promotion> promotionList) {
        this.promotionList = promotionList;
    }

    public static class Promotion{
        private String uuid;

        private String title;

        private String description;

        private String validTime;

        /**
         * 奖励类型 0：任选其一 1:每样一个
         */
        private Integer proType;

        /**
         * 领取状态 0：未领取  1：已领取
         */
        private Integer obtain;

        private List<Goods> goodsList;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getValidTime() {
            return validTime;
        }

        public void setValidTime(String validTime) {
            this.validTime = validTime;
        }

        public Integer getProType() {
            return proType == null ? 0 : proType;
        }

        public void setProType(Integer proType) {
            this.proType = proType;
        }

        public Integer getObtain() {
            return obtain == null ? 0 : obtain;
        }

        public void setObtain(Integer obtain) {
            this.obtain = obtain;
        }

        public List<Goods> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<Goods> goodsList) {
            this.goodsList = goodsList;
        }
    }

    public static class Goods{
        private String uuid;

        private String thumbnail;

        /**
         * 商品类型 0：实物商品；1：视频商品；2：数据报告
         */
        private String type;

        /**
         * 剩余货量
         */
        private int residueNum;

        /**
         * 可领取货量
         */
        private int num;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getResidueNum() {
            return residueNum;
        }

        public void setResidueNum(int residueNum) {
            this.residueNum = residueNum;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
