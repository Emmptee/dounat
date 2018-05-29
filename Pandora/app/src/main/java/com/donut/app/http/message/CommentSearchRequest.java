package com.donut.app.http.message;

public class CommentSearchRequest extends BaseRequest 
{
    private String contentId;
    
    private int page;
    
    private int rows;

    private String currentUserId;
    
    private int subPage;
    
    private int subRows;

    public String getCurrentUserId()
    {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId)
    {
        this.currentUserId = currentUserId;
    }

    public String getContentId()
    {
        return contentId;
    }

    public void setContentId(String contentId)
    {
        this.contentId = contentId;
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
    
}
