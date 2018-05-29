package com.donut.app.http.message;


public class PayResponse extends BaseResponse
{

    public PayResponse()
    {
    }
    
    //alipay 订单验证信息
    private String lanchPay;
    
    
    /*weipay 订单信息*/
    private String appId;
    
    private String partnerId;
    
    private String prepayId;
    
    private String nonceStr;
    
    private String timeStamp;
    
    private String sign;

    public String getLanchPay()
    {
        return lanchPay;
    }

    public void setLanchPay(String lanchPay)
    {
        this.lanchPay = lanchPay;
    }

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getPartnerId()
    {
        return partnerId;
    }

    public void setPartnerId(String partnerId)
    {
        this.partnerId = partnerId;
    }

    public String getPrepayId()
    {
        return prepayId;
    }

    public void setPrepayId(String prepayId)
    {
        this.prepayId = prepayId;
    }

    public String getNonceStr()
    {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr)
    {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }
}