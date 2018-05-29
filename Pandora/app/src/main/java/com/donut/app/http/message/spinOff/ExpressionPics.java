package com.donut.app.http.message.spinOff;

import java.io.Serializable;

/**
 * Created by
 * HIQ on 2017/6/8.
 */

public class ExpressionPics implements Serializable {

    private String b10Id;
    private String picUrl;
    private String gifUrl;

    public String getB10Id() {
        return b10Id;
    }

    public void setB10Id(String b10Id) {
        this.b10Id = b10Id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }
}
