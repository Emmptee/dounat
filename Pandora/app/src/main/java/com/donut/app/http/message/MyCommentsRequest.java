package com.donut.app.http.message;

public class MyCommentsRequest extends BaseRequest 
{
    private int page;
    
    private int rows;
    
    private int subPage;
    
    private int subRows;

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

    public int getSubPage()
    {
        return subPage;
    }

    public void setSubPage(int subPage)
    {
        this.subPage = subPage;
    }

    public int getSubRows()
    {
        return subRows;
    }

    public void setSubRows(int subRows)
    {
        this.subRows = subRows;
    }

    @Override
    public String toString()
    {
        return "MyCommentsRequest [page=" + page + ", rows=" + rows + ", subPage=" + subPage + ", subRows=" + subRows + "]";
    }
}
