package com.donut.app.http.message.spinOff;

public class GoodsListRequest
{

    private Integer type;

    private Integer page;

    private Integer rows;

    private Integer searchInput;

    private String fkB01;

    // 请求状态 0：旧数据，没有竞拍商品默认值 1：新数据，包含竞拍商品
    private Integer requestType;

    private String searchStarName;

    /**
     * @return requestType
     */
    public Integer getRequestType()
    {
        return requestType;
    }

    /**
     * @param requestType
     *            the requestType to set
     */
    public void setRequestType(Integer requestType)
    {
        this.requestType = requestType;
    }

    /**
     * @return searchInput
     */
    public Integer getSearchInput()
    {
        return searchInput;
    }

    /**
     * @param searchInput
     *            the searchInput to set
     */
    public void setSearchInput(Integer searchInput)
    {
        this.searchInput = searchInput;
    }

    /**
     * @return fkB01
     */
    public String getFkB01()
    {
        return fkB01;
    }

    /**
     * @param fkB01
     *            the fkB01 to set
     */
    public void setFkB01(String fkB01)
    {
        this.fkB01 = fkB01;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getPage()
    {
        return page;
    }

    public void setPage(Integer page)
    {
        this.page = page;
    }

    public Integer getRows()
    {
        return rows;
    }

    public void setRows(Integer rows)
    {
        this.rows = rows;
    }

    public String getSearchStarName() {
        return searchStarName;
    }

    public void setSearchStarName(String searchStarName) {
        this.searchStarName = searchStarName;
    }
}
