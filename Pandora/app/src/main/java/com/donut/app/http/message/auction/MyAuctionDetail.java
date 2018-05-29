package com.donut.app.http.message.auction;

import android.databinding.BaseObservable;

public class MyAuctionDetail extends BaseObservable {

    private String d10Id;

    private String description;

    private String title;

    private int auctionLogsStatus;

    private String auctionLogsStatusText;

    private long payCountdown;

    private String thumbnail;

    private int postageStatus;

    private float freight;

    private float memberFreight;

    private float auctionResultPrice;

    private int needMember;

    private float totalPrice;

    private int auctionAddTimes;

    private String createTime;

    /**
     * 订单是否正常，0:有效订单；1打开支付详情h5
     */
    private int d01ValidStatus;

    private String d01Id;

    /**
     * 商品Id
     */
    private String d02Id;

    public String getD02Id() {
        return d02Id;
    }

    public void setD02Id(String d02Id) {
        this.d02Id = d02Id;
    }

    public String getD10Id() {
        return d10Id;
    }

    public void setD10Id(String d10Id) {
        this.d10Id = d10Id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuctionLogsStatus() {
        return auctionLogsStatus;
    }

    public void setAuctionLogsStatus(int auctionLogsStatus) {
        this.auctionLogsStatus = auctionLogsStatus;
        notifyChange();
    }

    public String getAuctionLogsStatusText() {
        return auctionLogsStatusText;
    }

    public void setAuctionLogsStatusText(String auctionLogsStatusText) {
        this.auctionLogsStatusText = auctionLogsStatusText;
    }

    public long getPayCountdown() {
        return payCountdown;
    }

    public void setPayCountdown(long payCountdown) {
        this.payCountdown = payCountdown;
        notifyChange();
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getPostageStatus() {
        return postageStatus;
    }

    public void setPostageStatus(int postageStatus) {
        this.postageStatus = postageStatus;
    }

    public float getFreight() {
        return freight;
    }

    public void setFreight(float freight) {
        this.freight = freight;
    }

    public float getMemberFreight() {
        return memberFreight;
    }

    public void setMemberFreight(float memberFreight) {
        this.memberFreight = memberFreight;
    }

    public float getAuctionResultPrice() {
        return auctionResultPrice;
    }

    public void setAuctionResultPrice(float auctionResultPrice) {
        this.auctionResultPrice = auctionResultPrice;
    }

    public int getNeedMember() {
        return needMember;
    }

    public void setNeedMember(int needMember) {
        this.needMember = needMember;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getAuctionAddTimes() {
        return auctionAddTimes;
    }

    public void setAuctionAddTimes(int auctionAddTimes) {
        this.auctionAddTimes = auctionAddTimes;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getD01ValidStatus() {
        return d01ValidStatus;
    }

    public void setD01ValidStatus(int d01ValidStatus) {
        this.d01ValidStatus = d01ValidStatus;
    }

    public String getD01Id() {
        return d01Id;
    }

    public void setD01Id(String d01Id) {
        this.d01Id = d01Id;
    }
}
