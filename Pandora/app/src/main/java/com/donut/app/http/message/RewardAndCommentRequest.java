package com.donut.app.http.message;

public class RewardAndCommentRequest
{

    private String contentId;

    private String content;

    private Integer amount;

    public String getContentId()
    {
        return contentId;
    }

    /**
     * @return amount
     */
    public Integer getAmount()
    {
        return amount;
    }

    /**
     * @param amount
     *            the amount to set
     */
    public void setAmount(Integer amount)
    {
        this.amount = amount;
    }

    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

}
