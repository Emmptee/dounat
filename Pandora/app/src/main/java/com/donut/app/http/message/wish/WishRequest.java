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
 * @Title: WishRequest.java 
 * @Package com.donut.server.rest.message 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 陈淑晓
 * @date 2017年2月20日 下午5:30:02 
 * @version V1.0   
 */
package com.donut.app.http.message.wish;

/**
 * @ClassName: WishRequest
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 陈淑晓
 * @date 2017年2月20日 下午5:30:02
 * @version 1.0
 */
public class WishRequest
{
    private Integer sortType;

    private Integer page;

    private Integer rows;

    /**
     * @return sortType
     */
    public Integer getSortType()
    {
        return sortType;
    }

    /**
     * @param sortType
     *            the sortType to set
     */
    public void setSortType(Integer sortType)
    {
        this.sortType = sortType;
    }

    /**
     * @return page
     */
    public Integer getPage()
    {
        return page;
    }

    /**
     * @param page
     *            the page to set
     */
    public void setPage(Integer page)
    {
        this.page = page;
    }

    /**
     * @return rows
     */
    public Integer getRows()
    {
        return rows;
    }

    /**
     * @param rows
     *            the rows to set
     */
    public void setRows(Integer rows)
    {
        this.rows = rows;
    }

}
