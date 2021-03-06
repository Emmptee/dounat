package com.donut.app.http.message;

import java.util.ArrayList;
import java.util.List;

public class MyAppreciatesResponse extends BaseResponse
{

    private List<MyAppreciate> myAppreciates = new ArrayList<MyAppreciate>();
    
    
    public List<MyAppreciate> getMyAppreciates()
    {
        return myAppreciates;
    }


    public void setMyAppreciates(List<MyAppreciate> myAppreciates)
    {
        this.myAppreciates = myAppreciates;
    }


    @Override
    public String toString()
    {
        return "MyAppreciatesResponse [myAppreciates=" + myAppreciates + "]";
    }



    public class MyAppreciate
    {
        private String title;
        
        private String starName;
        
        private String contentId;
        
        private String challengeTitle;
        
        private String userName;
        
        private String userHeadPic;
        
        private String description;
        
        private String thumbnailUrl;
        
        private String createTime;

        private Integer isMember;

        public Integer getIsMember()
        {
            return isMember;
        }

        public void setIsMember(Integer isMember)
        {
            this.isMember = isMember;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getStarName()
        {
            return starName;
        }

        public void setStarName(String starName)
        {
            this.starName = starName;
        }

        public String getContentId()
        {
            return contentId;
        }

        public void setContentId(String contentId)
        {
            this.contentId = contentId;
        }

        public String getChallengeTitle()
        {
            return challengeTitle;
        }

        public void setChallengeTitle(String challengeTitle)
        {
            this.challengeTitle = challengeTitle;
        }

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }

        public String getUserHeadPic()
        {
            return userHeadPic;
        }

        public void setUserHeadPic(String userHeadPic)
        {
            this.userHeadPic = userHeadPic;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }
        
        public String getThumbnailUrl()
        {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl)
        {
            this.thumbnailUrl = thumbnailUrl;
        }

        public String getCreateTime()
        {
            return createTime;
        }

        public void setCreateTime(String createTime)
        {
            this.createTime = createTime;
        }

        @Override
        public String toString()
        {
            return "MyAppreciate [title=" + title + ", starName=" + starName + ", contentId=" + contentId + ", challengeTitle=" + challengeTitle + ", userName=" + userName + ", userHeadPic=" + userHeadPic + ", description=" + description + ", thumbnailUrl=" + thumbnailUrl + ", createTime=" + createTime + "]";
        }
        
    }
}
