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
 * @Title: ChallengeListResponse.java 
 * @Package com.donut.server.rest.message 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 樊沛  
 * @date 2016年5月25日 下午2:22:00 
 * @version V1.0   
 */
package com.donut.app.http.message;

import java.util.List;

public class ChallengeListResponse extends BaseResponse
{
    private List<Challenge> challengeList;

    /**
     * @return challengeList
     */
    public List<Challenge> getChallengeList()
    {
        return challengeList;
    }

    /**
     * @param challengeList
     *            the challengeList to set
     */
    public void setChallengeList(List<Challenge> challengeList)
    {
        this.challengeList = challengeList;
    }

    public class Challenge
    {
        private String uuid;

        private String thumbnailUrl;

        private String headPic;

        private String nickName;

        private String title;

        private String createTime;

        private String browseTimes;

        private String praiseTimes;

        private Integer isMember;

        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Integer getIsMember()
        {
            return isMember;
        }

        public void setIsMember(Integer isMember)
        {
            this.isMember = isMember;
        }

        /**
         * @return uuid
         */
        public String getUuid()
        {
            return uuid;
        }

        /**
         * @param uuid
         *            the uuid to set
         */
        public void setUuid(String uuid)
        {
            this.uuid = uuid;
        }

        /**
         * @return thumbnailUrl
         */
        public String getThumbnailUrl()
        {
            return thumbnailUrl;
        }

        /**
         * @param thumbnailUrl
         *            the thumbnailUrl to set
         */
        public void setThumbnailUrl(String thumbnailUrl)
        {
            this.thumbnailUrl = thumbnailUrl;
        }

        /**
         * @return headPic
         */
        public String getHeadPic()
        {
            return headPic;
        }

        /**
         * @param headPic
         *            the headPic to set
         */
        public void setHeadPic(String headPic)
        {
            this.headPic = headPic;
        }

        /**
         * @return nickName
         */
        public String getNickName()
        {
            return nickName;
        }

        /**
         * @param nickName
         *            the nickName to set
         */
        public void setNickName(String nickName)
        {
            this.nickName = nickName;
        }

        /**
         * @return title
         */
        public String getTitle()
        {
            return title;
        }

        /**
         * @param title
         *            the title to set
         */
        public void setTitle(String title)
        {
            this.title = title;
        }

        /**
         * @return createTime
         */
        public String getCreateTime()
        {
            return createTime;
        }

        /**
         * @param createTime
         *            the createTime to set
         */
        public void setCreateTime(String createTime)
        {
            this.createTime = createTime;
        }

        public String getBrowseTimes() {
            return browseTimes;
        }

        public void setBrowseTimes(String browseTimes) {
            this.browseTimes = browseTimes;
        }

        public String getPraiseTimes() {
            return praiseTimes;
        }

        public void setPraiseTimes(String praiseTimes) {
            this.praiseTimes = praiseTimes;
        }
    }
}
