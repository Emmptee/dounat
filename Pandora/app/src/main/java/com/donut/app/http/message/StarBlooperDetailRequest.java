package com.donut.app.http.message;

public class StarBlooperDetailRequest
{
    private Integer page;

    private Integer rows;

    private String starId;

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

    public String getStarId() {
        return starId;
    }

    public void setStarId(String starId) {
        this.starId = starId;
    }
}
