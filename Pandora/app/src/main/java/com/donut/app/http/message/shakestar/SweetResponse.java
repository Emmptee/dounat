package com.donut.app.http.message.shakestar;

import com.donut.app.http.message.BaseResponse;

import java.util.List;

/**
 * Created by hard on 2018/2/27.
 */

public class SweetResponse extends BaseResponse {


    private List<HpContentListBean> hpContentList;

    public List<HpContentListBean> getHpContentList() {
        return hpContentList;
    }

    public void setHpContentList(List<HpContentListBean> hpContentList) {
        this.hpContentList = hpContentList;
    }

    public static class HpContentListBean {
        /**
         * description : null
         * period : 666
         * publicPic : http://10.10.10.253:8000/images/donut/20181/86e26e9d68ac455e8cc9ba4ad09a66af.jpg
         * starName : 刘涛
         * channelType : 1
         * channelName : 星包好礼
         * subjectId : ba7135625e604c009e400c1a87f9b052
         * lastTime : 0
         * browseTimes : 31630
         * title : 刘涛的专题嘻嘻嘻
         */

        private String description;
        private int period;
        private String publicPic;
        private String starName;
        private int channelType;
        private String channelName;
        private String subjectId;
        private int lastTime;
        private int browseTimes;
        private String title;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public String getPublicPic() {
            return publicPic;
        }

        public void setPublicPic(String publicPic) {
            this.publicPic = publicPic;
        }

        public String getStarName() {
            return starName;
        }

        public void setStarName(String starName) {
            this.starName = starName;
        }

        public int getChannelType() {
            return channelType;
        }

        public void setChannelType(int channelType) {
            this.channelType = channelType;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        public int getLastTime() {
            return lastTime;
        }

        public void setLastTime(int lastTime) {
            this.lastTime = lastTime;
        }

        public int getBrowseTimes() {
            return browseTimes;
        }

        public void setBrowseTimes(int browseTimes) {
            this.browseTimes = browseTimes;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
