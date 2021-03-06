package com.donut.app.http.message;

public class PresentAddRequest
{
    private String creditCardType;

    private String creditCardNum;

    private String cardHolder;

    private float donutNum;

    private float amount;

    /**
     * @return creditCardType
     */
    public String getCreditCardType()
    {
        return creditCardType;
    }

    /**
     * @param creditCardType
     *            the creditCardType to set
     */
    public void setCreditCardType(String creditCardType)
    {
        this.creditCardType = creditCardType;
    }

    /**
     * @return creditCardNum
     */
    public String getCreditCardNum()
    {
        return creditCardNum;
    }

    /**
     * @param creditCardNum
     *            the creditCardNum to set
     */
    public void setCreditCardNum(String creditCardNum)
    {
        this.creditCardNum = creditCardNum;
    }

    /**
     * @return cardHolder
     */
    public String getCardHolder()
    {
        return cardHolder;
    }

    /**
     * @param cardHolder
     *            the cardHolder to set
     */
    public void setCardHolder(String cardHolder)
    {
        this.cardHolder = cardHolder;
    }

    public float getDonutNum() {
        return donutNum;
    }

    public void setDonutNum(float donutNum) {
        this.donutNum = donutNum;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
