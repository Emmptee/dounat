package com.donut.app.http.message;

public class StarCommentAddRequest
{
    private String audioUrl;
    
    private String fkB02;
    
    private Long lastTime;

    public String getAudioUrl()
    {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl)
    {
        this.audioUrl = audioUrl;
    }

    public String getFkB02()
    {
        return fkB02;
    }

    public void setFkB02(String fkB02)
    {
        this.fkB02 = fkB02;
    }

    public Long getLastTime()
    {
        return lastTime;
    }

    public void setLastTime(Long lastTime)
    {
        this.lastTime = lastTime;
    }
    
}
