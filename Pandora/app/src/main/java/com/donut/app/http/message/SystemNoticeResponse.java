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
 * @Title: MyCourseResponse.java 
 * @Package com.bis.sportedu.http.message 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 亓振刚  
 * @date 2015-8-10 下午3:38:20 
 * @version V1.0   
 */
package com.donut.app.http.message;

import java.util.List;

/**
 * @ClassName: SystemNoticeResponse
 * @Description:系统通知
 * @author wujiaojiao
 * @date 2015-9-9 下午3:38:20
 * @version 1.0
 */
public class SystemNoticeResponse extends BaseResponse
{
    List<Notice> noticeList;

    /**
     * @return noticeList
     */
    public List<Notice> getNoticeList()
    {
        return noticeList;
    }

    /**
     * @param noticeList
     *            the noticeList to set
     */
    public void setNoticeList(List<Notice> noticeList)
    {
        this.noticeList = noticeList;
    }

    public class Notice
    {
        String title;

        String content;

        String validTime;

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        /**
         * @return content
         */
        public String getContent()
        {
            return content;
        }

        /**
         * @param content
         *            the content to set
         */
        public void setContent(String content)
        {
            this.content = content;
        }

        /**
         * @return validTime
         */
        public String getValidTime()
        {
            return validTime;
        }

        /**
         * @param validTime
         *            the validTime to set
         */
        public void setValidTime(String validTime)
        {
            this.validTime = validTime;
        }

    }
}
