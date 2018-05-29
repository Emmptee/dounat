package com.donut.app.http.message.auction;

/**
 * Created by Qi on 2017/4/17.
 * Description : <br>
 */
public class MyAuctionHandleRequest {

    public MyAuctionHandleRequest(String d10Id) {
        this.d10Id = d10Id;
    }

    private String d10Id;

    public String getD10Id() {
        return d10Id;
    }

    public void setD10Id(String d10Id) {
        this.d10Id = d10Id;
    }
}
