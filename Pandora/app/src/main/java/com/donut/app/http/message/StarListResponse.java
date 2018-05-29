package com.donut.app.http.message;

import java.util.List;

public class StarListResponse extends BaseResponse
{

    private List<StarListDetail> starList;

    public StarListResponse()
    {

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
