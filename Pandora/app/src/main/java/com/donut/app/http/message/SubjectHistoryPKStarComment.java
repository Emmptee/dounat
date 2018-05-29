package com.donut.app.http.message;

/**
 * Created by wujiaojiao on 2016/8/15.
 */
public class SubjectHistoryPKStarComment
{
    String uuid;
    String actorUuid;
    String name;
    String headPic;
    String commentTime;
    String audioUrl;
    long lastTime;
    String status;
    String fkA01;
    Integer listenTimes;
    Integer memberStatus;

    public Integer getMemberStatus()
    {
        return memberStatus;
    }

    public void setMemberStatus(Integer memberStatus)
    {
        this.memberStatus = memberStatus;
    }

    public Integer getListenTimes()
    {
        return listenTimes;
    }

    public void setListenTimes(Integer listenTimes)
    {
        this.listenTimes = listenTimes;
    }

    public String getFkA01()
    {
        return fkA01;
    }

    public void setFkA01(String fkA01)
    {
        this.fkA01 = fkA01;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getActorUuid()
    {
        return actorUuid;
    }

    public void setActorUuid(String actorUuid)
    {
        this.actorUuid = actorUuid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getHeadPic()
    {
        return headPic;
    }

    public void setHeadPic(String headPic)
    {
        this.headPic = headPic;
    }

    public String getCommentTime()
    {
        return commentTime;
    }

    public void setCommentTime(String commentTime)
    {
        this.commentTime = commentTime;
    }

    public String getAudioUrl()
    {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl)
    {
        this.audioUrl = audioUrl;
    }

    public long getLastTime()
    {
        return lastTime;
    }

    public void setLastTime(long lastTime)
    {
        this.lastTime = lastTime;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
