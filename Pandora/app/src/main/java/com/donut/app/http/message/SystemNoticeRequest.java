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

/**
 * @ClassName: SystemNoticeRequest
 * @Description:系统通知
 * @author wujiaojiao
 * @date 2015-9-9 下午3:38:20
 * @version 1.0
 */
public class SystemNoticeRequest
{
    private int page;

    private int rows;

    private int type;

    private String userId;

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * @return page
     */
    public int getPage()
    {
        return page;
    }

    /**
     * @param page
     *            the page to set
     */
    public void setPage(int page)
    {
        this.page = page;
    }

    /**
     * @return rows
     */
    public int getRows()
    {
        return rows;
    }

    /**
     * @param rows
     *            the rows to set
     */
    public void setRows(int rows)
    {
        this.rows = rows;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
}
