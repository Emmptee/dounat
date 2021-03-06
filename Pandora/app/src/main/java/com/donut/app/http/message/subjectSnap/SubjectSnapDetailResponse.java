package com.donut.app.http.message.subjectSnap;

import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.ContentComments;

import java.util.List;


public class SubjectSnapDetailResponse extends BaseResponse
{
    private String contentId;

    private String name;

    private String publicPic;

    private String playUrl;

    private long lastTime;

    private long browseTimes;

    private int period;

    private String actorId;

    private String headPic;

    private String actorName;

    private int memberStatus;

    private long challengeTimes;

    private long rewardTimes;

    private long praiseTimes;

    private long shareTimes;

    private long collectTimes;

    private int followStatus;

    private int collectionStatus;

    private int praiseStatus;

    private String pubTime;

    private int commentTimes;

    private String adTitle;

    /**
     * 专题内容描述
     */
    private String description;

    private List<Advertisement> adList;

    private List<Advertisement> portraitList;

    private List<ContentComments> currentComments;

    public class Advertisement
    {
        private String uuid;

        private String title;

        private String description;

        private String imgUrl;

        public String getUuid()
        {
            return uuid;
        }

        public void setUuid(String uuid)
        {
            this.uuid = uuid;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public String getImgUrl()
        {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl)
        {
            this.imgUrl = imgUrl;
        }

    }


    public String getContentId()
    {
        return contentId;
    }

    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPublicPic()
    {
        return publicPic;
    }

    public void setPublicPic(String publicPic)
    {
        this.publicPic = publicPic;
    }

    public String getPlayUrl()
    {
        return playUrl;
    }

    public void setPlayUrl(String playUrl)
    {
        this.playUrl = playUrl;
    }

    public long getLastTime()
    {
        return lastTime;
    }

    public void setLastTime(long lastTime)
    {
        this.lastTime = lastTime;
    }

    public long getBrowseTimes()
    {
        return browseTimes;
    }

    public void setBrowseTimes(long browseTimes)
    {
        this.browseTimes = browseTimes;
    }

    public int getPeriod()
    {
        return period;
    }

    public void setPeriod(int period)
    {
        this.period = period;
    }

    public String getActorId()
    {
        return actorId;
    }

    public void setActorId(String actorId)
    {
        this.actorId = actorId;
    }

    public String getHeadPic()
    {
        return headPic;
    }

    public void setHeadPic(String headPic)
    {
        this.headPic = headPic;
    }

    public String getActorName()
    {
        return actorName;
    }

    public void setActorName(String actorName)
    {
        this.actorName = actorName;
    }

    public int getMemberStatus()
    {
        return memberStatus;
    }

    public void setMemberStatus(int memberStatus)
    {
        this.memberStatus = memberStatus;
    }

    public long getChallengeTimes()
    {
        return challengeTimes;
    }

    public void setChallengeTimes(long challengeTimes)
    {
        this.challengeTimes = challengeTimes;
    }

    public long getRewardTimes()
    {
        return rewardTimes;
    }

    public void setRewardTimes(long rewardTimes)
    {
        this.rewardTimes = rewardTimes;
        notifyChange();
    }

    public long getPraiseTimes()
    {
        return praiseTimes;
    }

    public void setPraiseTimes(long praiseTimes)
    {
        this.praiseTimes = praiseTimes;
        notifyChange();
    }

    public long getShareTimes()
    {
        return shareTimes;
    }

    public void setShareTimes(long shareTimes)
    {
        this.shareTimes = shareTimes;
        notifyChange();
    }

    public long getCollectTimes()
    {
        return collectTimes;
    }

    public void setCollectTimes(long collectTimes)
    {
        this.collectTimes = collectTimes;
        notifyChange();
    }

    public int getFollowStatus()
    {
        return followStatus;
    }

    public void setFollowStatus(int followStatus)
    {
        this.followStatus = followStatus;
        notifyChange();
    }

    public int getCollectionStatus()
    {
        return collectionStatus;
    }

    public void setCollectionStatus(int collectionStatus)
    {
        this.collectionStatus = collectionStatus;
        notifyChange();
    }

    public int getPraiseStatus()
    {
        return praiseStatus;
    }

    public void setPraiseStatus(int praiseStatus)
    {
        this.praiseStatus = praiseStatus;
        notifyChange();
    }

    public String getPubTime()
    {
        return pubTime;
    }

    public void setPubTime(String pubTime)
    {
        this.pubTime = pubTime;
    }

    public int getCommentTimes()
    {
        return commentTimes;
    }

    public void setCommentTimes(int commentTimes)
    {
        this.commentTimes = commentTimes;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Advertisement> getPortraitList() {
        return portraitList;
    }

    public void setPortraitList(List<Advertisement> portraitList) {
        this.portraitList = portraitList;
    }

    public List<Advertisement> getAdList()
    {
        return adList;
    }

    public void setAdList(List<Advertisement> adList)
    {
        this.adList = adList;
    }

    public List<ContentComments> getCurrentComments()
    {
        return currentComments;
    }

    public void setCurrentComments(List<ContentComments> currentComments)
    {
        this.currentComments = currentComments;
    }

}
