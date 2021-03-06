package com.donut.app.http.message;

public class RewardInfoResponse extends BaseResponse
{
    private String headPic;

    private String nickName;

    private float balance;

    /**
     * @return headPic
     */
    public String getHeadPic()
    {
        return headPic;
    }

    /**
     * @param headPic
     *            the headPic to set
     */
    public void setHeadPic(String headPic)
    {
        this.headPic = headPic;
    }

    /**
     * @return nickName
     */
    public String getNickName()
    {
        return nickName;
    }

    /**
     * @param nickName
     *            the nickName to set
     */
    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    /**
     * @return remainder
     */
    public float getBalance()
    {
        return balance;
    }

    /**
     *    the remainder to set
     */
    public void setBalance(float balance)
    {
        this.balance = balance;
    }

}
