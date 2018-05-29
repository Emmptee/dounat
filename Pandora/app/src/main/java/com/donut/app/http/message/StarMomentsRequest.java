package com.donut.app.http.message;

public class StarMomentsRequest
{
    private String starId;
    
    private int page;
    
    private int rows;

    private int interfaceType;

    public String getStarId()
    {
        return starId;
    }

    public void setStarId(String starId)
    {
        this.starId = starId;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getRows()
    {
        return rows;
    }

    public void setRows(int rows)
    {
        this.rows = rows;
    }

    public int getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(int interfaceType) {
        this.interfaceType = interfaceType;
    }
}
