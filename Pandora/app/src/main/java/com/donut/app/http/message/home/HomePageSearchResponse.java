package com.donut.app.http.message.home;

import com.donut.app.http.message.BaseResponse;

import java.util.List;

public class HomePageSearchResponse extends BaseResponse {

    private List<ChannelItem> channelItems;

    private List<WishItem> wishItems;

    public List<ChannelItem> getChannelItems() {
        return channelItems;
    }

    public void setChannelItems(List<ChannelItem> channelItems) {
        this.channelItems = channelItems;
    }

    public List<WishItem> getWishItems() {
        return wishItems;
    }

    public void setWishItems(List<WishItem> wishItems) {
        this.wishItems = wishItems;
    }


}
