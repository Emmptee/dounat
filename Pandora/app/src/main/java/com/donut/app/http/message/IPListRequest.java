package com.donut.app.http.message;

/**
 * Created by wujiaojiao on 2016/5/31.
 */
public class IPListRequest
{
    Integer selectType;//1：最新idea 2：最强人气 3：最多投票
    Integer page;
    Integer rows;

    public Integer getSelectType()
    {
        return selectType;
    }

    public void setSelectType(Integer selectType)
    {
        this.selectType = selectType;
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
}
