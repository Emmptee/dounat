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
 * @Title: ChallengeListRequest.java 
 * @Package com.donut.server.rest.message 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 樊沛  
 * @date 2016年5月25日 下午2:20:23 
 * @version V1.0   
 */
package com.donut.app.http.message;

public class ChallengeListRequest
{
    private int page;

    private int rows;

    private Integer type;

    private String searchWords;

    private String subjectId;

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

    /**
     * @return type
     */
    public Integer getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(Integer type)
    {
        this.type = type;
    }

    /**
     * @return searchWords
     */
    public String getSearchWords()
    {
        return searchWords;
    }

    /**
     * @param searchWords
     *            the searchWords to set
     */
    public void setSearchWords(String searchWords)
    {
        this.searchWords = searchWords;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
