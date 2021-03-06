package com.donut.app.http.message;

public class PresentListRequest
{

    private Integer page;

    private Integer rows;

    private String startTime;

    private String endTime;

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
     * @return startTime
     */
    public String getStartTime()
    {
        return startTime;
    }

    /**
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    /**
     * @return endTime
     */
    public String getEndTime()
    {
        return endTime;
    }

    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

}
