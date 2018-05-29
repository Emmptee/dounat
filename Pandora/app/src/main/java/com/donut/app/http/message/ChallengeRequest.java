package com.donut.app.http.message;

public class ChallengeRequest extends BaseRequest
{
    private String subjectId;
    
    private String title;
    
    private String content;
    
    private String playUrl;
    
    private String thumbnailUrl;
    
    private Integer lastTime;

    public String getSubjectId()
    {
        return subjectId;
    }

    public void setSubjectId(String subjectId)
    {
        this.subjectId = subjectId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getPlayUrl()
    {
        return playUrl;
    }

    public void setPlayUrl(String playUrl)
    {
        this.playUrl = playUrl;
    }

    public String getThumbnailUrl()
    {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl)
    {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Integer getLastTime()
    {
        return lastTime;
    }

    public void setLastTime(Integer lastTime)
    {
        this.lastTime = lastTime;
    }
    
}
