package com.donut.app.http.message.auction;

/**
 * 
 * @ClassName: MyAuctionRequest
 * @Description: 我的竞拍请求
 * @author lihuadong@bis.com.cn
 * @date 2017年4月13日 下午2:08:37
 * @version 1.0
 */
public class MyAuctionRequest
{

    private Integer type;

    private Integer page;

    private Integer rows;

    /**
     * @return type
     */
    public Integer getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(Integer type)
    {
        this.type = type;
    }

    /**
     * @return page
     */
    public Integer getPage()
    {
        return page;
    }

    /**
     * @param page
     *            the page to set
     */
    public void setPage(Integer page)
    {
        this.page = page;
    }

    /**
     * @return rows
     */
    public Integer getRows()
    {
        return rows;
    }

    /**
     * @param rows
     *            the rows to set
     */
    public void setRows(Integer rows)
    {
        this.rows = rows;
    }

}
