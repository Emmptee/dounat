package com.donut.app.http.message.shakestar;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import com.donut.app.http.message.BaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hard on 2018/2/12.
 */

public class MyProductionResponse extends BaseResponse implements Parcelable {


    /**
     * isMember : 0
     * myShakingStarList : [{"materialType":null,"shareTimes":0,"praiseTimes":2,"g03Id":null,"commentTimes":0,"materialTitle":"GAI爷  GAI爷","isMember":0,"contentDesc":"测试一下自己的","videoThumbnail":"http://10.10.10.253:8000/images/donut/20181/26551b5f92c2452c9b3a80c37ceb5e69.png","materialThumbnail":"http://10.10.10.253:8000/images/donut/20181/12253a800d344685a01728cbdae3accd.jpg","collectionStatus":0,"browseTimes":0,"playUrl":"http://10.10.10.253:8000/video/20181/1efdf8570fd942029a13c23970a6e749.mp4","fkA01":"fc41dd9aa91d4b9ea3592a54e4f1cd95","createTime":"2018-01-24 15:44:16.0","headPic":"http://media.sweetdonut.cn:8088/images/headPic/3ffb694799f943b0b1d4b6e26dcd39e1.jpg","nickName":"嗡嗡嗡","praiseStatus":0,"b02Id":"ccef098a63314ef4a14d3748d0c126cf","lastTime":6},{"materialType":null,"shareTimes":0,"praiseTimes":0,"g03Id":null,"commentTimes":0,"materialTitle":"测试","isMember":0,"contentDesc":"刚才别呵呵","videoThumbnail":"http://10.10.10.253:8000/images/donut/20181/28e35bbf56f04bb097b17b9c7fc43e9b.png","materialThumbnail":"http://10.10.10.253:8000/images/donut/201712/5e0114196a1a4caf9bfb354dac6f1949.jpg","collectionStatus":0,"browseTimes":0,"playUrl":"http://10.10.10.253:8000/video/20181/99c27bc1d9534782a43fc7afd713d4af.mp4","fkA01":"fc41dd9aa91d4b9ea3592a54e4f1cd95","createTime":"2018-01-14 15:11:05.0","headPic":"http://media.sweetdonut.cn:8088/images/headPic/3ffb694799f943b0b1d4b6e26dcd39e1.jpg","nickName":"嗡嗡嗡","praiseStatus":0,"b02Id":"4ed0d41848b243b7a2f64ea4a33b965a","lastTime":10}]
     * followStatus : 0
     * attentionCount : 0
     * userNickName : 嗡嗡嗡
     * userHeadPic : http://media.sweetdonut.cn:8088/images/headPic/3ffb694799f943b0b1d4b6e26dcd39e1.jpg
     * userBGPic : http://media.sweetdonut.cn:8088/images/headPic/3ffb694799f943b0b1d4b6e26dcd39e1.jpg
     * followCount : 1
     */

    private int isMember;
    private int followStatus;
    private int attentionCount;
    private String userNickName;
    private String userHeadPic;
    private String userBGPic;
    private int followCount;
    private List<MyShakingStarListBean> myShakingStarList;

    public int getIsMember() {
        return isMember;
    }

    public void setIsMember(int isMember) {
        this.isMember = isMember;
    }

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }

    public int getAttentionCount() {
        return attentionCount;
    }

    public void setAttentionCount(int attentionCount) {
        this.attentionCount = attentionCount;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserHeadPic() {
        return userHeadPic;
    }

    public void setUserHeadPic(String userHeadPic) {
        this.userHeadPic = userHeadPic;
    }

    public String getUserBGPic() {
        return userBGPic;
    }

    public void setUserBGPic(String userBGPic) {
        this.userBGPic = userBGPic;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public List<MyShakingStarListBean> getMyShakingStarList() {
        return myShakingStarList;
    }

    public void setMyShakingStarList(List<MyShakingStarListBean> myShakingStarList) {
        this.myShakingStarList = myShakingStarList;
    }

    public static class MyShakingStarListBean extends BaseObservable implements Parcelable {
        /**
         * materialType : null
         * shareTimes : 0
         * praiseTimes : 2
         * g03Id : null
         * commentTimes : 0
         * materialTitle : GAI爷  GAI爷
         * isMember : 0
         * contentDesc : 测试一下自己的
         * videoThumbnail : http://10.10.10.253:8000/images/donut/20181/26551b5f92c2452c9b3a80c37ceb5e69.png
         * materialThumbnail : http://10.10.10.253:8000/images/donut/20181/12253a800d344685a01728cbdae3accd.jpg
         * collectionStatus : 0
         * browseTimes : 0
         * playUrl : http://10.10.10.253:8000/video/20181/1efdf8570fd942029a13c23970a6e749.mp4
         * fkA01 : fc41dd9aa91d4b9ea3592a54e4f1cd95
         * createTime : 2018-01-24 15:44:16.0
         * headPic : http://media.sweetdonut.cn:8088/images/headPic/3ffb694799f943b0b1d4b6e26dcd39e1.jpg
         * nickName : 嗡嗡嗡
         * praiseStatus : 0
         * b02Id : ccef098a63314ef4a14d3748d0c126cf
         * lastTime : 6
         */

        private String b02Id;
        private String g03Id;
        private String fkA01;
        private String createTime;
        private String nickName;
        private String headPic;
        private String contentDesc;
        private String materialTitle;
        private long praiseTimes;
        private long commentTimes;
        private int praiseStatus;
        private int collectionStatus;
        private long shareTimes;
        private String videoThumbnail;
        private String materialThumbnail;
        private String playUrl;
        private long lastTime;
        private int isMember;
        private long browseTimes;
        private int materialType;

        public String getB02Id() {
            return b02Id;
        }

        public void setB02Id(String b02Id) {
            this.b02Id = b02Id;
        }

        public String getG03Id() {
            return g03Id;
        }

        public void setG03Id(String g03Id) {
            this.g03Id = g03Id;
        }

        public String getFkA01() {
            return fkA01;
        }

        public void setFkA01(String fkA01) {
            this.fkA01 = fkA01;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public String getContentDesc() {
            return contentDesc;
        }

        public void setContentDesc(String contentDesc) {
            this.contentDesc = contentDesc;
        }

        public String getMaterialTitle() {
            return materialTitle;
        }

        public void setMaterialTitle(String materialTitle) {
            this.materialTitle = materialTitle;
        }

        public long getPraiseTimes() {
            return praiseTimes;
        }

        public void setPraiseTimes(long praiseTimes) {
            this.praiseTimes = praiseTimes;
        }

        public long getCommentTimes() {
            return commentTimes;
        }

        public void setCommentTimes(long commentTimes) {
            this.commentTimes = commentTimes;
        }

        public int getPraiseStatus() {
            return praiseStatus;
        }

        public void setPraiseStatus(int praiseStatus) {
            this.praiseStatus = praiseStatus;
            notifyChange();
        }

        public int getCollectionStatus() {
            return collectionStatus;
        }

        public void setCollectionStatus(int collectionStatus) {
            this.collectionStatus = collectionStatus;
        }

        public long getShareTimes() {
            return shareTimes;
        }

        public void setShareTimes(long shareTimes) {
            this.shareTimes = shareTimes;
        }

        public String getVideoThumbnail() {
            return videoThumbnail;
        }

        public void setVideoThumbnail(String videoThumbnail) {
            this.videoThumbnail = videoThumbnail;
        }

        public String getMaterialThumbnail() {
            return materialThumbnail;
        }

        public void setMaterialThumbnail(String materialThumbnail) {
            this.materialThumbnail = materialThumbnail;
        }

        public String getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        public long getLastTime() {
            return lastTime;
        }

        public void setLastTime(long lastTime) {
            this.lastTime = lastTime;
        }

        public int getIsMember() {
            return isMember;
        }

        public void setIsMember(int isMember) {
            this.isMember = isMember;
        }

        public long getBrowseTimes() {
            return browseTimes;
        }

        public void setBrowseTimes(long browseTimes) {
            this.browseTimes = browseTimes;
        }

        public int getMaterialType() {
            return materialType;
        }

        public void setMaterialType(int materialType) {
            this.materialType = materialType;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.b02Id);
            dest.writeString(this.g03Id);
            dest.writeString(this.fkA01);
            dest.writeString(this.createTime);
            dest.writeString(this.nickName);
            dest.writeString(this.headPic);
            dest.writeString(this.contentDesc);
            dest.writeString(this.materialTitle);
            dest.writeLong(this.praiseTimes);
            dest.writeLong(this.commentTimes);
            dest.writeInt(this.praiseStatus);
            dest.writeInt(this.collectionStatus);
            dest.writeLong(this.shareTimes);
            dest.writeString(this.videoThumbnail);
            dest.writeString(this.materialThumbnail);
            dest.writeString(this.playUrl);
            dest.writeLong(this.lastTime);
            dest.writeInt(this.isMember);
            dest.writeLong(this.browseTimes);
            dest.writeInt(this.materialType);
        }

        public MyShakingStarListBean() {
        }

        protected MyShakingStarListBean(Parcel in) {
            this.b02Id = in.readString();
            this.g03Id = in.readString();
            this.fkA01 = in.readString();
            this.createTime = in.readString();
            this.nickName = in.readString();
            this.headPic = in.readString();
            this.contentDesc = in.readString();
            this.materialTitle = in.readString();
            this.praiseTimes = in.readLong();
            this.commentTimes = in.readLong();
            this.praiseStatus = in.readInt();
            this.collectionStatus = in.readInt();
            this.shareTimes = in.readLong();
            this.videoThumbnail = in.readString();
            this.materialThumbnail = in.readString();
            this.playUrl = in.readString();
            this.lastTime = in.readLong();
            this.isMember = in.readInt();
            this.browseTimes = in.readLong();
            this.materialType = in.readInt();
        }

        public static final Creator<MyShakingStarListBean> CREATOR = new Creator<MyShakingStarListBean>() {
            @Override
            public MyShakingStarListBean createFromParcel(Parcel source) {
                return new MyShakingStarListBean(source);
            }

            @Override
            public MyShakingStarListBean[] newArray(int size) {
                return new MyShakingStarListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.isMember);
        dest.writeInt(this.followStatus);
        dest.writeInt(this.attentionCount);
        dest.writeString(this.userNickName);
        dest.writeString(this.userHeadPic);
        dest.writeString(this.userBGPic);
        dest.writeInt(this.followCount);
        dest.writeList(this.myShakingStarList);
    }

    public MyProductionResponse() {
    }

    protected MyProductionResponse(Parcel in) {
        this.isMember = in.readInt();
        this.followStatus = in.readInt();
        this.attentionCount = in.readInt();
        this.userNickName = in.readString();
        this.userHeadPic = in.readString();
        this.userBGPic = in.readString();
        this.followCount = in.readInt();
        this.myShakingStarList = new ArrayList<MyShakingStarListBean>();
        in.readList(this.myShakingStarList, MyShakingStarListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<MyProductionResponse> CREATOR = new Parcelable.Creator<MyProductionResponse>() {
        @Override
        public MyProductionResponse createFromParcel(Parcel source) {
            return new MyProductionResponse(source);
        }

        @Override
        public MyProductionResponse[] newArray(int size) {
            return new MyProductionResponse[size];
        }
    };
}
