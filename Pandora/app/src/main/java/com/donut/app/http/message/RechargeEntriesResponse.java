package com.donut.app.http.message;

import java.util.ArrayList;
import java.util.List;

public class RechargeEntriesResponse extends BaseResponse
{
    private String headPic;
    
    private String nickName;
    
    private float balance;
    
    private double ratio;
    
    private List<RechargeEntries> rechargeEntries = new ArrayList<RechargeEntries>();
    
    public class RechargeEntries
    {
        private double realAmount;
        
        private double mqAmount;
        
        private String itunesId;

        public double getRealAmount()
        {
            return realAmount;
        }

        public void setRealAmount(double realAmount)
        {
            this.realAmount = realAmount;
        }

        public double getMqAmount()
        {
            return mqAmount;
        }

        public void setMqAmount(double mqAmount)
        {
            this.mqAmount = mqAmount;
        }

        public String getItunesId()
        {
            return itunesId;
        }

        public void setItunesId(String itunesId)
        {
            this.itunesId = itunesId;
        }

        @Override
        public String toString()
        {
            return "RechargeEntries [realAmount=" + realAmount + ", mqAmount=" + mqAmount + ", itunesId=" + itunesId + "]";
        }
        
    }

    public List<RechargeEntries> getRechargeEntries()
    {
        return rechargeEntries;
    }

    public void setRechargeEntries(List<RechargeEntries> rechargeEntries)
    {
        this.rechargeEntries = rechargeEntries;
    }
    

    public String getHeadPic()
    {
        return headPic;
    }

    public void setHeadPic(String headPic)
    {
        this.headPic = headPic;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public float getBalance()
    {
        return balance;
    }

    public void setBalance(float balance)
    {
        this.balance = balance;
    }
    
    public double getRatio()
    {
        return ratio;
    }

    public void setRatio(double ratio)
    {
        this.ratio = ratio;
    }

    @Override
    public String toString()
    {
        return "RechargeEntriesResponse [headPic=" + headPic + ", nickName=" + nickName + ", balance=" + balance + ", ratio=" + ratio + ", rechargeEntries=" + rechargeEntries + "]";
    }
    
}
