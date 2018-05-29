package com.donut.app.http.message.spinOff;

public class GoodsListDetail
{
    private String goodsId;

    private String thumbnail;

    private String description;

    private String detailId;

    private Integer type;

    private Float price;

    private Float memberPrice;

    private Integer needMember;

    private Integer surplusNum;

    private Integer promotionType;

    private Integer goodsKind;

    private Float auctionCurrentPrice;

    private Float auctionStartPrice;

    private Integer auctionStatus;

    /**
     * @return auctionStartPrice
     */
    public Float getAuctionStartPrice()
    {
        return auctionStartPrice;
    }

    /**
     * @param auctionStartPrice
     *            the auctionStartPrice to set
     */
    public void setAuctionStartPrice(Float auctionStartPrice)
    {
        this.auctionStartPrice = auctionStartPrice;
    }

    /**
     * @return auctionCurrentPrice
     */
    public Float getAuctionCurrentPrice()
    {
        return auctionCurrentPrice;
    }

    /**
     * @param auctionCurrentPrice
     *            the auctionCurrentPrice to set
     */
    public void setAuctionCurrentPrice(Float auctionCurrentPrice)
    {
        this.auctionCurrentPrice = auctionCurrentPrice;
    }

    /**
     * @return auctionStatus
     */
    public Integer getAuctionStatus()
    {
        return auctionStatus;
    }

    /**
     * @param auctionStatus
     *            the auctionStatus to set
     */
    public void setAuctionStatus(Integer auctionStatus)
    {
        this.auctionStatus = auctionStatus;
    }

    /**
     * @return goodsKind
     */
    public Integer getGoodsKind()
    {
        return goodsKind;
    }

    /**
     * @param goodsKind
     *            the goodsKind to set
     */
    public void setGoodsKind(Integer goodsKind)
    {
        this.goodsKind = goodsKind;
    }

    /**
     * @return surplusNum
     */
    public Integer getSurplusNum()
    {
        return surplusNum;
    }

    /**
     * @param surplusNum
     *            the surplusNum to set
     */
    public void setSurplusNum(Integer surplusNum)
    {
        this.surplusNum = surplusNum;
    }

    /**
     * @return goodsId
     */
    public String getGoodsId()
    {
        return goodsId;
    }

    /**
     * @param goodsId
     *            the goodsId to set
     */
    public void setGoodsId(String goodsId)
    {
        this.goodsId = goodsId;
    }

    /**
     * @return thumbnail
     */
    public String getThumbnail()
    {
        return thumbnail;
    }

    /**
     * @param thumbnail
     *            the thumbnail to set
     */
    public void setThumbnail(String thumbnail)
    {
        this.thumbnail = thumbnail;
    }

    /**
     * @return description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return detailId
     */
    public String getDetailId()
    {
        return detailId;
    }

    /**
     * @param detailId
     *            the detailId to set
     */
    public void setDetailId(String detailId)
    {
        this.detailId = detailId;
    }

    /**
     * @return type
     */
    public Integer getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(Integer type)
    {
        this.type = type;
    }

    /**
     * @return price
     */
    public Float getPrice()
    {
        return price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(Float price)
    {
        this.price = price;
    }

    /**
     * @return memberPrice
     */
    public Float getMemberPrice()
    {
        return memberPrice;
    }

    /**
     * @param memberPrice
     *            the memberPrice to set
     */
    public void setMemberPrice(Float memberPrice)
    {
        this.memberPrice = memberPrice;
    }

    /**
     * @return needMember
     */
    public Integer getNeedMember()
    {
        return needMember;
    }

    /**
     * @param needMember
     *            the needMember to set
     */
    public void setNeedMember(Integer needMember)
    {
        this.needMember = needMember;
    }

    public Integer getPromotionType()
    {
        return promotionType;
    }

    public void setPromotionType(Integer promotionType)
    {
        this.promotionType = promotionType;
    }

}
