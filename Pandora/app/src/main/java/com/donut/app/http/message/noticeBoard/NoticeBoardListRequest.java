package com.donut.app.http.message.noticeBoard;

public class NoticeBoardListRequest
{
    private Integer page;

    private Integer rows;

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

}
