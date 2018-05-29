package com.donut.app.http.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qi on 2017/3/20.
 * Description : <br>
 */
public class SubjectHomePageResponse extends BaseResponse {
    private String trailerPic;

    private List<HpSubject> hpSubjectList = new ArrayList<HpSubject>();

    private List<MoreContent> moreContentList = new ArrayList<MoreContent>();

    public String getTrailerPic() {
        return trailerPic;
    }

    public void setTrailerPic(String trailerPic) {
        this.trailerPic = trailerPic;
    }

    public List<HpSubject> getHpSubjectList() {
        return hpSubjectList;
    }

    public void setHpSubjectList(List<HpSubject> hpSubjectList) {
        this.hpSubjectList = hpSubjectList;
    }

    public List<MoreContent> getMoreContentList() {
        return moreContentList;
    }

    public void setMoreContentList(List<MoreContent> moreContentList) {
        this.moreContentList = moreContentList;
    }

    public class HpSubject {
        private String subjectId;

        private String starName;

        private String publicPic;

        private String title;

        private int period;

        private String content;

        private int lastTime;

        private String channelName;

        /**
         * 0:有本事你来.1:街拍街拍
         */
        private int channelType;

        /**
         * @return subjectId
         */
        public String getSubjectId() {
            return subjectId;
        }

        /**
         * @param subjectId the subjectId to set
         */
        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        /**
         * @return starName
         */
        public String getStarName() {
            return starName;
        }

        /**
         * @param starName the starName to set
         */
        public void setStarName(String starName) {
            this.starName = starName;
        }

        /**
         * @return publicPic
         */
        public String getPublicPic() {
            return publicPic;
        }

        /**
         * @param publicPic the publicPic to set
         */
        public void setPublicPic(String publicPic) {
            this.publicPic = publicPic;
        }

        /**
         * @return title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title the title to set
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * @return period
         */
        public int getPeriod() {
            return period;
        }

        /**
         * @param period the period to set
         */
        public void setPeriod(int period) {
            this.period = period;
        }

        /**
         * @return content
         */
        public String getContent() {
            return content;
        }

        /**
         * @param content the content to set
         */
        public void setContent(String content) {
            this.content = content;
        }

        /**
         * @return lastTime
         */
        public int getLastTime() {
            return lastTime;
        }

        /**
         * @param lastTime the lastTime to set
         */
        public void setLastTime(int lastTime) {
            this.lastTime = lastTime;
        }

        /**
         * @return channelName
         */
        public String getChannelName() {
            return channelName;
        }

        /**
         * @param channelName the channelName to set
         */
        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        /**
         * @return channelType
         */
        public int getChannelType() {
            return channelType;
        }

        /**
         * @param channelType the channelType to set
         */
        public void setChannelType(int channelType) {
            this.channelType = channelType;
        }

    }

    public class MoreContent{
        private String uuid;

        private String picUrl;

        private String title;

        private String description;

        private Integer type;

        private String typeName;

        private String skipId;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getSkipId() {
            return skipId;
        }

        public void setSkipId(String skipId) {
            this.skipId = skipId;
        }
    }
}
