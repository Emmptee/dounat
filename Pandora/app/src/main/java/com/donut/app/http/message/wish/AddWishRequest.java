package com.donut.app.http.message.wish;


public class AddWishRequest {

    private String description;

    private Integer wishType;

    private String starName;

    private String imgUrl;

    private int mediaType;

    private String playUrl;

    private String lastTime;

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
     * @return wishType
     */
    public Integer getWishType()
    {
        return wishType;
    }

    /**
     * @param wishType
     *            the wishType to set
     */
    public void setWishType(Integer wishType)
    {
        this.wishType = wishType;
    }

    /**
     * @return starName
     */
    public String getStarName()
    {
        return starName;
    }

    /**
     * @param starName
     *            the starName to set
     */
    public void setStarName(String starName)
    {
        this.starName = starName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
}
