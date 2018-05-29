package com.donut.app.http.message.noticeBoard;

/**
 * 
 * @ClassName: NoticeBoardListModel
 * @Description: App接口使用
 * @author lihuadong@bis.com.cn
 * @date 2017年1月17日 上午10:27:28
 * @version 1.0
 */
public class NoticeBoardListModel
{
    private String title;

    private String mediaText;

    private String typeName;

    private String typeUrl;

    private String startDate;

    private String endDate;

    private String uuid;

    /**
     * @return title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * @return mediaText
     */
    public String getMediaText()
    {
        return mediaText;
    }

    /**
     * @param mediaText
     *            the mediaText to set
     */
    public void setMediaText(String mediaText)
    {
        this.mediaText = mediaText;
    }

    /**
     * @return typeName
     */
    public String getTypeName()
    {
        return typeName;
    }

    /**
     * @param typeName
     *            the typeName to set
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    /**
     * @return typeUrl
     */
    public String getTypeUrl()
    {
        return typeUrl;
    }

    /**
     * @param typeUrl
     *            the typeUrl to set
     */
    public void setTypeUrl(String typeUrl)
    {
        this.typeUrl = typeUrl;
    }

    /**
     * @return startDate
     */
    public String getStartDate()
    {
        return startDate;
    }

    /**
     * @param startDate
     *            the startDate to set
     */
    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    /**
     * @return endDate
     */
    public String getEndDate()
    {
        return endDate;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    /**
     * @return uuid
     */
    public String getUuid()
    {
        return uuid;
    }

    /**
     * @param uuid
     *            the uuid to set
     */
    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

}
