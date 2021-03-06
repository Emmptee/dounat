package com.donut.app.http.message.spinOff;

import android.databinding.BaseObservable;

public class ExclusivePlan extends BaseObservable {
	
	private String starName;

	private String actorId;

	private String b02Id;
	
    private String name;

    private String headPic;
    
    private String playUrl;
    
    private String b02Thumbnail;

    private Long browseTimes;
    
    private Long praiseTimes;
    
    private Long shareTimes;
      
    private int praiseStatus;
    
    private Long commentTimes;
    
    private String createTime;

	public String getStarName() {
		return starName;
	}

	public void setStarName(String starName) {
		this.starName = starName;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public String getB02Id() {
		return b02Id;
	}

	public void setB02Id(String b02Id) {
		this.b02Id = b02Id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public String getB02Thumbnail() {
		return b02Thumbnail;
	}

	public void setB02Thumbnail(String b02Thumbnail) {
		this.b02Thumbnail = b02Thumbnail;
	}

	public Long getBrowseTimes() {
		return browseTimes;
	}

	public void setBrowseTimes(Long browseTimes) {
		this.browseTimes = browseTimes;
		notifyChange();
	}

	public Long getPraiseTimes() {
		return praiseTimes;
	}

	public void setPraiseTimes(Long praiseTimes) {
		this.praiseTimes = praiseTimes;
		notifyChange();
	}

	public Long getShareTimes() {
		return shareTimes;
	}

	public void setShareTimes(Long shareTimes) {
		this.shareTimes = shareTimes;
		notifyChange();
	}

	public int getPraiseStatus() {
		return praiseStatus;
	}

	public void setPraiseStatus(int praiseStatus) {
		this.praiseStatus = praiseStatus;
		notifyChange();
	}

	public Long getCommentTimes() {
		return commentTimes;
	}

	public void setCommentTimes(Long commentTimes) {
		this.commentTimes = commentTimes;
		notifyChange();
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
		notifyChange();
	}
}
