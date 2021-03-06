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
 * @Title: AddressListRequest.java 
 * @Package com.donut.server.rest.message 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 樊沛  
 * @date 2016年5月25日 下午4:53:38 
 * @version V1.0   
 */
package com.donut.app.http.message;

/**
 * @ClassName: AddressListRequest
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 樊沛
 * @date 2016年5月25日 下午4:53:38
 * @version 1.0
 */
public class MyIpRequest
{
    private Integer page;

    private Integer rows;

	private Integer selectStatus;//0:未审核；1：不通过；2通过';3：查询所有

	public Integer getSelectStatus()
	{
		return selectStatus;
	}

	public void setSelectStatus(Integer selectStatus)
	{
		this.selectStatus = selectStatus;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

    
}
