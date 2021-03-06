package com.donut.app.http.message;

import java.util.List;

public class PayRequest
{
    private String reasonType;

    private String payMoney;

    private String payType;

    private String subject;

    private String body;

    private String memberDefinitionId;

    private Integer lastTime;

    private String starCommentId;

    private List<String> d01Ids;

    private Integer payEntrance;

    public String getReasonType() {
        return reasonType;
    }

    public void setReasonType(String reasonType) {
        this.reasonType = reasonType;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMemberDefinitionId() {
        return memberDefinitionId;
    }

    public void setMemberDefinitionId(String memberDefinitionId) {
        this.memberDefinitionId = memberDefinitionId;
    }

    public Integer getLastTime() {
        return lastTime;
    }

    public void setLastTime(Integer lastTime) {
        this.lastTime = lastTime;
    }

    public String getStarCommentId() {
        return starCommentId;
    }

    public void setStarCommentId(String starCommentId) {
        this.starCommentId = starCommentId;
    }

    public List<String> getD01Ids() {
        return d01Ids;
    }

    public void setD01Ids(List<String> d01Ids) {
        this.d01Ids = d01Ids;
    }

    public Integer getPayEntrance() {
        return payEntrance;
    }

    public void setPayEntrance(Integer payEntrance) {
        this.payEntrance = payEntrance;
    }
}
