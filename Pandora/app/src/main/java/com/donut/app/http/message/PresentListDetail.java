package com.donut.app.http.message;

public class PresentListDetail
{

    private float amount;

    private Integer status;

    private String dealTime;

    private String applicateTime;

    /**
     * @return amount
     */
    public float getAmount()
    {
        return amount;
    }

    /**
     * @param amount
     *            the amount to set
     */
    public void setAmount(float amount)
    {
        this.amount = amount;
    }

    /**
     * @return status
     */
    public Integer getStatus()
    {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    /**
     * @return dealTime
     */
    public String getDealTime()
    {
        return dealTime;
    }

    /**
     * @param dealTime
     *            the dealTime to set
     */
    public void setDealTime(String dealTime)
    {
        this.dealTime = dealTime;
    }

    /**
     * @return applicateTime
     */
    public String getApplicateTime()
    {
        return applicateTime;
    }

    /**
     * @param applicateTime
     *            the applicateTime to set
     */
    public void setApplicateTime(String applicateTime)
    {
        this.applicateTime = applicateTime;
    }

}
