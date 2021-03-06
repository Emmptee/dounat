package com.donut.app.http.message.spinOff;

import com.donut.app.http.message.BaseResponse;

import java.util.List;

/**
 * 
 * @ClassName: GoodsListResponse
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 李华洞
 * @date 2016年5月26日 下午4:02:42
 * @version 1.0
 */
public class GoodsListResponse extends BaseResponse
{
    private List<GoodsListDetail> goodsList;

    /**
     * @return goodsList
     */
    public List<GoodsListDetail> getGoodsList()
    {
        return goodsList;
    }

    /**
     * @param goodsList
     *            the goodsList to set
     */
    public void setGoodsList(List<GoodsListDetail> goodsList)
    {
        this.goodsList = goodsList;
    }

}
