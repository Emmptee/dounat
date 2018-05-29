package com.donut.app.http.message;

import java.util.List;

public class SubjectHistoryPKDetailResponse extends BaseResponse {
    private String name;

    private String playUrl;

    private long lastTime;

    private String description;

    private String actorId;

    private String headPic;

    private String actorName;

    private int commentTimes;

    private long challengeTimes;

    private long rewardTimes;

    private long praiseTimes;

    private long shareTimes;

    private long collectTimes;

    private long browseTimes;

    private int collectionStatus;

    private int praiseStatis;

    private int userType;//0 普通用户 1明星

    private int starRecommend;//0正常 1明星推荐

    private int lookGoodOnHim;//是否看好他：0：未看好他；1：已看好他；2：不是该明星的专题

    private String thumbnailUrl;

    private SubjectHistoryPKCommentDetail commentDetail;

    private List<SubjectHistoryPKDetail> challengeList;

    private List<ContentComments> currentComments;

    private SubjectHistoryPKStarComment audio;

    private float balance;

    private int videoPrice;

    private int memberStatus;//0:非会员1:会员

    private int starCommentAudioSum;

    private String pubTime;

    private int followStatus;

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getStarCommentAudioSum() {
        return starCommentAudioSum;
    }

    public void setStarCommentAudioSum(int starCommentAudioSum) {
        this.starCommentAudioSum = starCommentAudioSum;
    }

    public int getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(int memberStatus) {
        this.memberStatus = memberStatus;
    }

    public SubjectHistoryPKStarComment getAudio() {
        return audio;
    }

    public void setAudio(SubjectHistoryPKStarComment audio) {
        this.audio = audio;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getVideoPrice() {
        return videoPrice;
    }

    public void setVideoPrice(int videoPrice) {
        this.videoPrice = videoPrice;
    }

    public int getLookGoodOnHim() {
        return lookGoodOnHim;
    }

    public void setLookGoodOnHim(int lookGoodOnHim) {
        this.lookGoodOnHim = lookGoodOnHim;
        notifyChange();
    }

    public List<ContentComments> getCurrentComments() {
        return currentComments;
    }

    public int getStarRecommend() {
        return starRecommend;
    }

    public void setStarRecommend(int starRecommend) {
        this.starRecommend = starRecommend;
        notifyChange();
    }

    public void setCurrentComments(List<ContentComments> currentComments) {
        this.currentComments = currentComments;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCommentTimes() {
        return commentTimes;
    }

    public void setCommentTimes(int commentTimes) {
        this.commentTimes = commentTimes;
    }

    public long getChallengeTimes() {
        return challengeTimes;
    }

    public void setChallengeTimes(long challengeTimes) {
        this.challengeTimes = challengeTimes;
    }

    public long getRewardTimes() {
        return rewardTimes;
    }

    public void setRewardTimes(long rewardTimes) {
        this.rewardTimes = rewardTimes;
    }

    public long getPraiseTimes() {
        return praiseTimes;
    }

    public void setPraiseTimes(long praiseTimes) {
        this.praiseTimes = praiseTimes;
    }

    public long getShareTimes() {
        return shareTimes;
    }

    public void setShareTimes(long shareTimes) {
        this.shareTimes = shareTimes;
    }

    public long getCollectTimes() {
        return collectTimes;
    }

    public void setCollectTimes(long collectTimes) {
        this.collectTimes = collectTimes;
    }

    public long getBrowseTimes() {
        return browseTimes;
    }

    public void setBrowseTimes(long browseTimes) {
        this.browseTimes = browseTimes;
    }

    public List<SubjectHistoryPKDetail> getChallengeList() {
        return challengeList;
    }

    public void setChallengeList(List<SubjectHistoryPKDetail> challengeList) {
        this.challengeList = challengeList;
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

    public SubjectHistoryPKCommentDetail getCommentDetail() {
        return commentDetail;
    }

    public void setCommentDetail(SubjectHistoryPKCommentDetail commentDetail) {
        this.commentDetail = commentDetail;
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
}
