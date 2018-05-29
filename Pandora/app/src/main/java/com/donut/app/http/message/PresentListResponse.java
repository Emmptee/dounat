package com.donut.app.http.message;

import java.util.List;

public class PresentListResponse extends BaseResponse
{
    private List<PresentListDetail> presentList;

    /**
     * @return presentList
     */
    public List<PresentListDetail> getPresentList()
    {
        return presentList;
    }

    /**
     * @param presentList
     *            the presentList to set
     */
    public void setPresentList(List<PresentListDetail> presentList)
    {
        this.presentList = presentList;
    }

}
