package com.donut.app.http.message;

import java.util.List;

/**
 * Created by wujiaojiao on 2016/5/24.
 */
public class HomeResponse extends BaseResponse
{
    String subjectId;
    String publicPic;
    String starName;
    String title;
    int period;
    String content;
    int lastTime;
    List<PastSubject> pastSubjectList;

    public String getStarName()
    {
        return starName;
    }

    public void setStarName(String starName)
    {
        this.starName = starName;
    }

    public String getSubjectId()
    {
        return subjectId;
    }

    public void setSubjectId(String subjectId)
    {
        this.subjectId = subjectId;
    }

    public String getPublicPic()
    {
        return publicPic;
    }

    public void setPublicPic(String publicPic)
    {
        this.publicPic = publicPic;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getPeriod()
    {
        return period;
    }

    public void setPeriod(int period)
    {
        this.period = period;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public int getLastTime()
    {
        return lastTime;
    }

    public void setLastTime(int lastTime)
    {
        this.lastTime = lastTime;
    }

    public List<PastSubject> getPastSubjectList()
    {
        return pastSubjectList;
    }

    public void setPastSubjectList(List<PastSubject> pastSubjectList)
    {
        this.pastSubjectList = pastSubjectList;
    }

    public class PastSubject
    {
        String uuid;
        String contentId;
        String publicPic;
        String title;
        int period;

        public String getUuid()
        {
            return uuid;
        }

        public void setUuid(String uuid)
        {
            this.uuid = uuid;
        }

        public String getContentId()
        {
            return contentId;
        }

        public void setContentId(String contentId)
        {
            this.contentId = contentId;
        }

        public String getPublicPic()
        {
            return publicPic;
        }

        public void setPublicPic(String publicPic)
        {
            this.publicPic = publicPic;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public int getPeriod()
        {
            return period;
        }

        public void setPeriod(int period)
        {
            this.period = period;
        }
    }

}
