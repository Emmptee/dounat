package com.donut.app.http.message;

import java.util.List;

public class SubjectResponse extends BaseResponse {
    private String contentId;

    private String name;

    private String publicPic;

    private String playUrl;

    private String lastTime;

    private long browseTimes;

    private String description;

    private String period;

    private String actorId;

    private String headPic;

    private String actorName;

    private int memberStatus;

    private long challengeTimes;

    private long rewardTimes;

    private long praiseTimes;

    private long shareTimes;

    private long collectTimes;

    private int status;//1：进行中2:   已结束（历史）

    private int collectionStatus;

    private int praiseStatis;//是否点赞0：未点赞；1已点赞

    private int followStatus;

    private String pubTime;

    private String createTime;

    private List<SubjectDetailListDetail> historyList;

    private List<ContentComments> currentComments;

    private int commentTimes;

    private List<SubjectDetailListDetail> challengeList;

    private List<SubjectDetailListDetail> rankingList;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublicPic() {
        return publicPic;
    }

    public void setPublicPic(String publicPic) {
        this.publicPic = publicPic;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public long getBrowseTimes() {
        return browseTimes;
    }

    public void setBrowseTimes(long browseTimes) {
        this.browseTimes = browseTimes;
        notifyChange();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public int getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(int memberStatus) {
        this.memberStatus = memberStatus;
    }

    public long getChallengeTimes() {
        return challengeTimes;
    }

    public void setChallengeTimes(long challengeTimes) {
        this.challengeTimes = challengeTimes;
        notifyChange();
    }

    public long getRewardTimes() {
        return rewardTimes;
    }

    public void setRewardTimes(long rewardTimes) {
        this.rewardTimes = rewardTimes;
        notifyChange();
    }

    public long getPraiseTimes() {
        return praiseTimes;
    }

    public void setPraiseTimes(long praiseTimes) {
        this.praiseTimes = praiseTimes;
        notifyChange();
    }

    public long getShareTimes() {
        return shareTimes;
    }

    public void setShareTimes(long shareTimes) {
        this.shareTimes = shareTimes;
        notifyChange();
    }

    public long getCollectTimes() {
        return collectTimes;
    }

    public void setCollectTimes(long collectTimes) {
        this.collectTimes = collectTimes;
        notifyChange();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCollectionStatus() {
        return collectionStatus;
    }

    public void setCollectionStatus(int collectionStatus) {
        this.collectionStatus = collectionStatus;
        notifyChange();
    }

    public int getPraiseStatis() {
        return praiseStatis;
    }

    public void setPraiseStatis(int praiseStatis) {
        this.praiseStatis = praiseStatis;
        notifyChange();
    }

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
        notifyChange();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public List<ContentComments> getCurrentComments() {
        return currentComments;
    }

    public void setCurrentComments(List<ContentComments> currentComments) {
        this.currentComments = currentComments;
    }

    public List<SubjectDetailListDetail> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<SubjectDetailListDetail> historyList) {
        this.historyList = historyList;
    }

    public int getCommentTimes() {
        return commentTimes;
    }

    public void setCommentTimes(int commentTimes) {
        this.commentTimes = commentTimes;
        notifyChange();
    }

    public List<SubjectDetailListDetail> getChallengeList() {
        return challengeList;
    }

    public void setChallengeList(List<SubjectDetailListDetail> challengeList) {
        this.challengeList = challengeList;
    }

    public List<SubjectDetailListDetail> getRankingList() {
        return rankingList;
    }

    public void setRankingList(List<SubjectDetailListDetail> rankingList) {
        this.rankingList = rankingList;
    }

    public class SubjectDetailListDetail {
        private String imgUrl;
        private String title;
        private String contentId;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContentId() {
            return contentId;
        }

        public void setContentId(String contentId) {
            this.contentId = contentId;
        }
    }

    public class SubjectHistoryPKCommentDetail {
        private String commentId;
        private String content;
        private String nickName;

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }


}
