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

public class OrderDetailResponse extends BaseResponse
{
    private String orderStatus;

    private String type;

    private String goodsId;
    
    private String expressName;
    
    private String expressNo;
    
    private String orderNo;
    
    private String receiver;
    
    private String address;
    
    private String phone;
    
    private String subjectId;
    
    private String subjectName;
    
    private String thumbnail;
    
    private String description;
    
    private float price;
    
    private int num;
    
    private float freight;
    
    private float realPayAmount;
    
    private String createTime;

    private String payStatus;

    private String staticUrl;

    public String getStaticUrl() {
        return staticUrl;
    }

    public void setStaticUrl(String staticUrl) {
        this.staticUrl = staticUrl;
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }
    public String getExpressName()
    {
        return expressName;
    }
    public void setExpressName(String expressName)
    {
        this.expressName = expressName;
    }
    public String getExpressNo()
    {
        return expressNo;
    }
    public void setExpressNo(String expressNo)
    {
        this.expressNo = expressNo;
    }
    public String getOrderNo()
    {
        return orderNo;
    }
    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }
    public String getReceiver()
    {
        return receiver;
    }
    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }
    public String getAddress()
    {
        return address;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }
    public String getPhone()
    {
        return phone;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    public String getSubjectId()
    {
        return subjectId;
    }
    public void setSubjectId(String subjectId)
    {
        this.subjectId = subjectId;
    }
    public String getSubjectName()
    {
        return subjectName;
    }
    public void setSubjectName(String subjectName)
    {
        this.subjectName = subjectName;
    }
    public String getThumbnail()
    {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail)
    {
        this.thumbnail = thumbnail;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public float getPrice()
    {
        return price;
    }
    public void setPrice(float price)
    {
        this.price = price;
    }
    public int getNum()
    {
        return num;
    }
    public void setNum(int num)
    {
        this.num = num;
    }
    public float getFreight()
    {
        return freight;
    }
    public void setFreight(float freight)
    {
        this.freight = freight;
    }
    public float getRealPayAmount()
    {
        return realPayAmount;
    }
    public void setRealPayAmount(float realPayAmount)
    {
        this.realPayAmount = realPayAmount;
    }
    public String getCreateTime()
    {
        return createTime;
    }
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
}
