package com.donut.app.http.message;

import java.util.ArrayList;

/**
 * Created by wujiaojiao on 2016/5/31.
 */
public class IPListResponse extends BaseResponse
{
    ArrayList<IpListDetail> ipList;

    public ArrayList<IpListDetail> getIpList()
    {
        return ipList;
    }

    public void setIpList(ArrayList<IpListDetail> ipList)
    {
        this.ipList = ipList;
    }

    public class IpListDetail{

        String ipId;
        String starId;
        String starName;
        String playUrl;
        String imgUrl;
        String description;
        String name;
        String createTime;
        String headPic;
        String praiseTimes;
        String browseTimes;
        String IpUserName;
        String IpUserId;
        Integer memberStatus;
        int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Integer getMemberStatus()
        {
            return memberStatus;
        }

        public void setMemberStatus(Integer memberStatus)
        {
            this.memberStatus = memberStatus;
        }

        public String getIpUserName()
        {
            return IpUserName;
        }

        public void setIpUserName(String ipUserName)
        {
            IpUserName = ipUserName;
        }

        public String getIpUserId()
        {
            return IpUserId;
        }

        public void setIpUserId(String ipUserId)
        {
            IpUserId = ipUserId;
        }

        public String getHeadPic()
        {
            return headPic;
        }

        public void setHeadPic(String headPic)
        {
            this.headPic = headPic;
        }

        public String getIpId()
        {
            return ipId;
        }

        public void setIpId(String ipId)
        {
            this.ipId = ipId;
        }

        public String getStarId()
        {
            return starId;
        }

        public void setStarId(String starId)
        {
            this.starId = starId;
        }

        public String getStarName()
        {
            return starName;
        }

        public void setStarName(String starName)
        {
            this.starName = starName;
        }

        public String getPlayUrl()
        {
            return playUrl;
        }

        public void setPlayUrl(String playUrl)
        {
            this.playUrl = playUrl;
        }

        public String getImgUrl()
        {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl)
        {
            this.imgUrl = imgUrl;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getCreateTime()
        {
            return createTime;
        }

        public void setCreateTime(String createTime)
        {
            this.createTime = createTime;
        }

        public String getPraiseTimes()
        {
            return praiseTimes;
        }

        public void setPraiseTimes(String praiseTimes)
        {
            this.praiseTimes = praiseTimes;
        }

        public String getBrowseTimes()
        {
            return browseTimes;
        }

        public void setBrowseTimes(String browseTimes)
        {
            this.browseTimes = browseTimes;
        }
    }
}
