package com.donut.app.http.message;

public class StarListRequest
{
    private Integer page;

    private Integer rows;

    private String searchName;

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

    /**
     * @return searchName
     */
    public String getSearchName()
    {
        return searchName;
    }

    /**
     * @param searchName
     *            the searchName to set
     */
    public void setSearchName(String searchName)
    {
        this.searchName = searchName;
    }

}
