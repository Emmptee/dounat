package com.donut.app.http.message;

import java.util.List;

public class IpResponse extends BaseResponse
{

    private List<StarListDetail> starList;

    private String name;

    private String description;

    private String imgUrl;

    private String playUrl;

    public IpResponse()
    {

    }

    /**
     * @return playUrl
     */
    public String getPlayUrl()
    {
        return playUrl;
    }

    /**
     * @param playUrl
     *            the playUrl to set
     */
    public void setPlayUrl(String playUrl)
    {
        this.playUrl = playUrl;
    }

    /**
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
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
     * @return imgUrl
     */
    public String getImgUrl()
    {
        return imgUrl;
    }

    /**
     * @param imgUrl
     *            the imgUrl to set
     */
    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    /**
     * @return starList
     */
    public List<StarListDetail> getStarList()
    {
        return starList;
    }

    /**
     * @param starList
     *            the starList to set
     */
    public void setStarList(List<StarListDetail> starList)
    {
        this.starList = starList;
    }

}
