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
public class MyFollowListResponse extends BaseResponse
{

    private List<FollowDetail> followList;
   

    public class FollowDetail
    {
        private String followId;
        
        private String starId;

        private String headPic;

        private String name;

        private String lastStarMoment;

		private int status;

		private String memberStatus;

		public String getMemberStatus()
		{
			return memberStatus;
		}

		public void setMemberStatus(String memberStatus)
		{
			this.memberStatus = memberStatus;
		}

		public int getStatus()
		{
			return status;
		}

		public void setStatus(int status)
		{
			this.status = status;
		}

		public String getFollowId() {
			return followId;
		}

		public void setFollowId(String followId) {
			this.followId = followId;
		}

		public String getHeadPic() {
			return headPic;
		}

		public void setHeadPic(String headPic) {
			this.headPic = headPic;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getLastStarMoment() {
			return lastStarMoment;
		}

		public void setLastStarMoment(String lastStarMoment) {
			this.lastStarMoment = lastStarMoment;
		}

		public String getStarId() {
			return starId;
		}

		public void setStarId(String starId) {
			this.starId = starId;
		}
		
		
    }


	public List<FollowDetail> getFollowList() {
		return followList;
	}


	public void setFollowList(List<FollowDetail> followList) {
		this.followList = followList;
	}
    
    
}
