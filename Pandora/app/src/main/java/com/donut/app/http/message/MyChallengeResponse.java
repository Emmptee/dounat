/**   
 *  Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * @Title: AddressListResponse.java 
 * @Package com.donut.server.rest.message 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 樊沛  
 * @date 2016年5月25日 下午4:54:38 
 * @version V1.0   
 */
package com.donut.app.http.message;

import java.util.List;

/**
 * @ClassName: AddressListResponse
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 樊沛
 * @date 2016年5月25日 下午4:54:38
 * @version 1.0
 */
public class MyChallengeResponse extends BaseResponse
{

    private List<MyChallenge> challengeList;

    public List<MyChallenge> getChallengeList() {
		return challengeList;
	}



	public void setChallengeList(List<MyChallenge> challengeList) {
		this.challengeList = challengeList;
	}



	public class MyChallenge
    {
        private String contentId;

		private String subjectId;

        private String thumbnailUrl;

        private String createTime;

        private String description;

        private String title;
		private long browseTimes;
		private long praiseTimes;
		private Integer period;
		private String	subjectName;
		private long rewardAmount;

		public String getSubjectId()
		{
			return subjectId;
		}

		public void setSubjectId(String subjectId)
		{
			this.subjectId = subjectId;
		}

		public long getBrowseTimes()
		{
			return browseTimes;
		}

		public void setBrowseTimes(long browseTimes)
		{
			this.browseTimes = browseTimes;
		}

		public long getPraiseTimes()
		{
			return praiseTimes;
		}

		public void setPraiseTimes(long praiseTimes)
		{
			this.praiseTimes = praiseTimes;
		}

		public Integer getPeriod()
		{
			return period;
		}

		public void setPeriod(Integer period)
		{
			this.period = period;
		}

		public String getSubjectName()
		{
			return subjectName;
		}

		public void setSubjectName(String subjectName)
		{
			this.subjectName = subjectName;
		}

		public long getRewardAmount()
		{
			return rewardAmount;
		}

		public void setRewardAmount(long rewardAmount)
		{
			this.rewardAmount = rewardAmount;
		}

		public String getContentId() {
			return contentId;
		}

		public void setContentId(String contentId) {
			this.contentId = contentId;
		}

		public String getThumbnailUrl() {
			return thumbnailUrl;
		}

		public void setThumbnailUrl(String thumbnailUrl) {
			this.thumbnailUrl = thumbnailUrl;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

        
    }
}
