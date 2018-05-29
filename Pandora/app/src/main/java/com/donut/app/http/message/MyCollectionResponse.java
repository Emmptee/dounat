package com.donut.app.http.message;

import java.util.List;


public class MyCollectionResponse extends BaseResponse
{

    private List<MyCollection> collectionList;

	public List<MyCollection> getCollectionList() {
		return collectionList;
	}
	public void setCollectionList(List<MyCollection> collectionList) {
		this.collectionList = collectionList;
	}

	public class MyCollection
    {
        private String ipId;

		private String channelName;

        private String subjectId;

        private String contentId;

		private String contentType;

        private String starId;

        private String headPic;
        
        private String starName;
        
        private String imgUrl;
        
        private String description;
        
        private String name;
        
        private String createTime;
        
        private Integer status;
        
        private Integer period;
        
        private Long rewardTimes;

        private Long praiseTimes;

        private Long browseTimes;

        private List<String> starList;

		Integer memberStatus;

		public Integer getMemberStatus()
		{
			return memberStatus;
		}

		public void setMemberStatus(Integer memberStatus)
		{
			this.memberStatus = memberStatus;
		}
        
		public String getIpId() {
			return ipId;
		}

		public void setIpId(String ipId) {
			this.ipId = ipId;
		}

		public String getSubjectId() {
			return subjectId;
		}

		public void setSubjectId(String subjectId) {
			this.subjectId = subjectId;
		}

		public String getContentId() {
			return contentId;
		}

		public void setContentId(String contentId) {
			this.contentId = contentId;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public String getStarId() {
			return starId;
		}

		public void setStarId(String starId) {
			this.starId = starId;
		}

		public String getHeadPic() {
			return headPic;
		}

		public void setHeadPic(String headPic) {
			this.headPic = headPic;
		}

		public String getStarName() {
			return starName;
		}

		public void setStarName(String starName) {
			this.starName = starName;
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

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public Integer getPeriod() {
			return period;
		}

		public void setPeriod(Integer period) {
			this.period = period;
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

		public Long getBrowseTimes() {
			return browseTimes;
		}

		public void setBrowseTimes(Long browseTimes) {
			this.browseTimes = browseTimes;
		}

		public List<String> getStarList() {
			return starList;
		}

		public void setStarList(List<String> starList) {
			this.starList = starList;
		}

		public String getChannelName() {
			return channelName;
		}

		public void setChannelName(String channelName) {
			this.channelName = channelName;
		}
	}
}
