package com.donut.app.http.message;

public class RewardIncomeListDetail
{

    private String contentId;

    private String contentName;

    private float amount;

    private String createTime;

    /**
     * @return contentId
     */
    public String getContentId()
    {
        return contentId;
    }

    /**
     * @param contentId
     *            the contentId to set
     */
    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }

    /**
     * @return contentName
     */
    public String getContentName()
    {
        return contentName;
    }

    /**
     * @param contentName
     *            the contentName to set
     */
    public void setContentName(String contentName)
    {
        this.contentName = contentName;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    /**
     * @return createTime
     */
    public String getCreateTime()
    {
        return createTime;
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

}
