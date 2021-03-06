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
 * @Title: AddressRequest.java 
 * @Package com.donut.server.rest.message 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 樊沛  
 * @date 2016年5月25日 下午5:33:03 
 * @version V1.0   
 */
package com.donut.app.http.message;

/**
 * 
 * @ClassName: StarNoticeAddRequest
 * @Description: App,创建大咖有通告,请求model
 * @author lihuadong@bis.com.cn
 * @date 2017年4月17日 上午11:21:40
 * @version 1.0
 */
public class StarNoticeAddRequest
{
    private String description; // 描述

    private String publicPic; // 专题封面图，650*620

    private String thumbnail; // 视频第一帧，缩略图

    private String playUrl; // 视频播放地址

    private Long lastTime; // 视频时长，单位是秒

    /**
     * @return description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return publicPic
     */
    public String getPublicPic()
    {
        return publicPic;
    }

    /**
     * @param publicPic
     *            the publicPic to set
     */
    public void setPublicPic(String publicPic)
    {
        this.publicPic = publicPic;
    }

    /**
     * @return thumbnail
     */
    public String getThumbnail()
    {
        return thumbnail;
    }

    /**
     * @param thumbnail
     *            the thumbnail to set
     */
    public void setThumbnail(String thumbnail)
    {
        this.thumbnail = thumbnail;
    }

    /**
     * @return playUrl
     */
    public String getPlayUrl()
    {
        return playUrl;
    }

    /**
     * @param playUrl
     *            the playUrl to set
     */
    public void setPlayUrl(String playUrl)
    {
        this.playUrl = playUrl;
    }

    /**
     * @return lastTime
     */
    public Long getLastTime()
    {
        return lastTime;
    }

    /**
     * @param lastTime
     *            the lastTime to set
     */
    public void setLastTime(Long lastTime)
    {
        this.lastTime = lastTime;
    }

}
