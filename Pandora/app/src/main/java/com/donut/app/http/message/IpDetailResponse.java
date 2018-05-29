package com.donut.app.http.message;

import java.util.List;

public class IpDetailResponse extends BaseResponse
{
	private String ipId;

	private String contentId;
	
	private String ipUserId;
	
	private String headPic;
	
	private String ipUserName;
	
	private Long lastTime;
	
	private String playUrl;
	
	private String imgUrl;
	
	private String description;
	
	private String name;
	
	private Integer sum;
	
	private Integer commentTimes;
	
	private Long rewardTimes;
	
	private Long praiseTimes;
	
	private Long shareTimes;
	
	private Long collectTimes;
	
	private Long browseTimes;
	
	private String createTime;
	
	private Integer collectionStatus;
	
	private Integer praiseStatis;

	private String thumbnailUrl;
	
	private List<String> starList;


	private SubjectHistoryPKCommentDetail commentDetail;

	private List<IpDetailMyDetail> myIpList ;

	List<ContentComments> currentComments;

	Integer memberStatus;

	int userType;

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getThumbnailUrl()
	{
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl)
	{
		this.thumbnailUrl = thumbnailUrl;
	}

	public Integer getMemberStatus()
	{
		return memberStatus;
	}

	public void setMemberStatus(Integer memberStatus)
	{
		this.memberStatus = memberStatus;
	}

	public List<ContentComments> getCurrentComments()
	{
		return currentComments;
	}

	public void setCurrentComments(List<ContentComments> currentComments)
	{
		this.currentComments = currentComments;
	}

	public String getContentId()
	{
		return contentId;
	}

	public void setContentId(String contentId)
	{
		this.contentId = contentId;
	}

	public String getIpId() {
		return ipId;
	}

	public void setIpId(String ipId) {
		this.ipId = ipId;
	}

	public String getIpUserId() {
		return ipUserId;
	}

	public void setIpUserId(String ipUserId) {
		ipUserId = ipUserId;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getIpUserName() {
		return ipUserName;
	}

	public void setIpUserName(String ipUserName) {
		ipUserName = ipUserName;
	}

	public Long getLastTime() {
		return lastTime;
	}

	public void setLastTime(Long lastTime) {
		this.lastTime = lastTime;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSum() {
		return sum;
	}

	public void setSum(Integer sum) {
		this.sum = sum;
	}

	public Integer getCommentTimes() {
		return commentTimes;
	}

	public void setCommentTimes(Integer commentTimes) {
		this.commentTimes = commentTimes;
	}

	public Long getRewardTimes() {
		return rewardTimes;
	}

	public void setRewardTimes(Long rewardTimes) {
		this.rewardTimes = rewardTimes;
	}

	public Long getPraiseTimes() {
		return praiseTimes;
	}

	public void setPraiseTimes(Long praiseTimes) {
		this.praiseTimes = praiseTimes;
	}

	public Long getShareTimes() {
		return shareTimes;
	}

	public void setShareTimes(Long shareTimes) {
		this.shareTimes = shareTimes;
	}

	public Long getCollectTimes() {
		return collectTimes;
	}

	public void setCollectTimes(Long collectTimes) {
		this.collectTimes = collectTimes;
	}

	public Long getBrowseTimes() {
		return browseTimes;
	}

	public void setBrowseTimes(Long browseTimes) {
		this.browseTimes = browseTimes;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getCollectionStatus() {
		return collectionStatus;
	}

	public void setCollectionStatus(Integer collectionStatus) {
		this.collectionStatus = collectionStatus;
	}

	public List<String> getStarList() {
		return starList;
	}

	public void setStarList(List<String> starList) {
		this.starList = starList;
	}

	public SubjectHistoryPKCommentDetail getCommentDetail()
	{
		return commentDetail;
	}

	public void setCommentDetail(SubjectHistoryPKCommentDetail commentDetail)
	{
		this.commentDetail = commentDetail;
	}

	public List<IpDetailMyDetail> getMyIpList() {
		return myIpList;
	}

	public void setMyIpList(List<IpDetailMyDetail> myIpList) {
		this.myIpList = myIpList;
	}

	public Integer getPraiseStatis() {
		return praiseStatis;
	}

	public void setPraiseStatis(Integer praiseStatis) {
		this.praiseStatis = praiseStatis;
	}

	

	
    
    

}
