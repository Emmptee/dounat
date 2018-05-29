package com.donut.app.http.message.auction;

import com.donut.app.http.message.BaseResponse;

import java.util.List;

/**
 * 
 * @ClassName: MyAuctionResponse
 * @Description: 我的竞拍响应
 * @author 李华洞
 * @date 2016年5月26日 下午4:02:42
 * @version 1.0
 */
public class MyAuctionResponse extends BaseResponse
{
    private List<MyAuctionDetail> myAuctionList;

    /**
     * @return myAuctionList
     */
    public List<MyAuctionDetail> getMyAuctionList()
    {
        return myAuctionList;
    }

    /**
     * @param myAuctionList
     *            the myAuctionList to set
     */
    public void setMyAuctionList(List<MyAuctionDetail> myAuctionList)
    {
        this.myAuctionList = myAuctionList;
    }

}