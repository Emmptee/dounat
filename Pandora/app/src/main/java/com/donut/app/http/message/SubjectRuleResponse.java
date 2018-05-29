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
 * @Title: SubjectRuleResponse.java 
 * @Package com.donut.server.rest.message 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 樊沛  
 * @date 2016年5月26日 下午3:18:42 
 * @version V1.0   
 */
package com.donut.app.http.message;


public class SubjectRuleResponse extends BaseResponse
{
    private String subjectName;

    private String description;

    private String publicPic;

    private String name;

    private String headPic;

    private String endTime;

    /**
     * @return subjectName
     */
    public String getSubjectName()
    {
        return subjectName;
    }

    /**
     * @param subjectName
     *            the subjectName to set
     */
    public void setSubjectName(String subjectName)
    {
        this.subjectName = subjectName;
    }

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
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
