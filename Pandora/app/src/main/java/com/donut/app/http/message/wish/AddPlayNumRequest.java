package com.donut.app.http.message.wish;

/**
 * WishAddAudioNumRequest
 * Created by Qi on 2017/2/25.
 */

public class AddPlayNumRequest {

    private String mediaId;

    private int idType;

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
