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
 * @Title: UserInfoResponse.java 
 * @Package com.donut.server.rest.message 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 樊沛  
 * @date 2016年5月25日 下午4:01:00 
 * @version V1.0   
 */
package com.donut.app.http.message;

/**
 * @ClassName: UserInfoResponse
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 樊沛
 * @date 2016年5月25日 下午4:01:00
 * @version 1.0
 */
public class UserInfoResponse extends BaseResponse
{
    private String headPic;

    private String nickName;

    private String sex;

    private String age;

    private String days;

    private Integer star;

    private Integer flg;//0：未赠送  1：已赠送

    private String job;

    private String jobCode;

    private int isBindingPhone;//0：绑定，1：未绑定

    private Integer memberStatus;//0非会员 1会员

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public int getIsBindingPhone()
    {
        return isBindingPhone;
    }

    public void setIsBindingPhone(int isBindingPhone)
    {
        this.isBindingPhone = isBindingPhone;
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

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getAge()
    {
        return age;
    }

    public void setAge(String age)
    {
        this.age = age;
    }

    public String getDays()
    {
        return days;
    }

    public void setDays(String days)
    {
        this.days = days;
    }

    public Integer getStar()
    {
        return star;
    }

    public void setStar(Integer star)
    {
        this.star = star;
    }

    public Integer getMemberStatus()
    {
        return memberStatus;
    }

    public void setMemberStatus(Integer memberStatus)
    {
        this.memberStatus = memberStatus;
    }

    public Integer getFlg()
    {
        return flg;
    }

    public void setFlg(Integer flg)
    {
        this.flg = flg;
    }

    public String getJob()
    {
        return job;
    }

    public void setJob(String job)
    {
        this.job = job;
    }
}
