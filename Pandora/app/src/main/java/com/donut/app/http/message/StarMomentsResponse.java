package com.donut.app.http.message;

import java.util.ArrayList;
import java.util.List;

public class StarMomentsResponse extends BaseResponse
{
    private List<StarMoments> starMoments = new ArrayList<StarMoments>();

    public List<StarMoments> getStarMoments()
    {
        return starMoments;
    }

    public void setStarMoments(List<StarMoments> starMoments)
    {
        this.starMoments = starMoments;
    }

    public class StarMoments
    {
        private Integer type;

        private String typeName;
        
        private String contentId;
        
        private String subjectId;
        
        private String contentUrl;
        
        private String userName;
        
        private String title;
        
        private String description;
        
        private Integer opType;
        
        private String content;
        
        private String fkA01;
        
        private String userContent;
        
        private String createTime;
        
        private String createTimeStyle;

        private String beRepliedUserName;

        private long lastTime;

        private int isBuyed;

        private int listenTimes;

        private String starCommentId;

        /**
         * 0 心愿未达成, 1 已达成
         */
        private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Integer getType()
        {
            return type;
        }

        public void setType(Integer type)
        {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getContentId()
        {
            return contentId;
        }

        public void setContentId(String contentId)
        {
            this.contentId = contentId;
        }

        public String getSubjectId()
        {
            return subjectId;
        }

        public void setSubjectId(String subjectId)
        {
            this.subjectId = subjectId;
        }

        public String getContentUrl()
        {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl)
        {
            this.contentUrl = contentUrl;
        }

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public Integer getOpType()
        {
            return opType;
        }

        public void setOpType(Integer opType)
        {
            this.opType = opType;
        }

        public String getContent()
        {
            return content;
        }

        public void setContent(String content)
        {
            this.content = content;
        }

        public String getFkA01()
        {
            return fkA01;
        }

        public void setFkA01(String fkA01)
        {
            this.fkA01 = fkA01;
        }

        public String getUserContent()
        {
            return userContent;
        }

        public void setUserContent(String userContent)
        {
            this.userContent = userContent;
        }

        public String getCreateTime()
        {
            return createTime;
        }

        public void setCreateTime(String createTime)
        {
            this.createTime = createTime;
        }

        public String getCreateTimeStyle()
        {
            return createTimeStyle;
        }

        public void setCreateTimeStyle(String createTimeStyle)
        {
            this.createTimeStyle = createTimeStyle;
        }

        public String getBeRepliedUserName() {
            return beRepliedUserName;
        }

        public void setBeRepliedUserName(String beRepliedUserName) {
            this.beRepliedUserName = beRepliedUserName;
        }

        public long getLastTime() {
            return lastTime;
        }

        public void setLastTime(long lastTime) {
            this.lastTime = lastTime;
        }

        public int getIsBuyed() {
            return isBuyed;
        }

        public void setIsBuyed(int isBuyed) {
            this.isBuyed = isBuyed;
        }

        public String getStarCommentId() {
            return starCommentId;
        }

        public void setStarCommentId(String starCommentId) {
            this.starCommentId = starCommentId;
        }

        public int getListenTimes() {
            return listenTimes;
        }

        public void setListenTimes(int listenTimes) {
            this.listenTimes = listenTimes;
        }
    }
}
