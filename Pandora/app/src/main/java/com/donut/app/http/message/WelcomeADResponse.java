package com.donut.app.http.message;

import java.util.List;

/**
 * Created by Qi on 2017/3/31.
 * Description : <br>
 */
public class WelcomeADResponse extends BaseResponse {

    private int totalSeconds;

    private List<ADItem> startPageList;

    public List<ADItem> getStartPageList() {
        return startPageList;
    }

    public void setStartPageList(List<ADItem> startPageList) {
        this.startPageList = startPageList;
    }

    public int getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(int totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    public class ADItem {
        private String title;

        private String picUrl;

        private String skipAddress;

        private int countdown;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getSkipAddress() {
            return skipAddress;
        }

        public void setSkipAddress(String skipAddress) {
            this.skipAddress = skipAddress;
        }

        public int getCountdown() {
            return countdown;
        }

        public void setCountdown(int countdown) {
            this.countdown = countdown;
        }
    }
}
