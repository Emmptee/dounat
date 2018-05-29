package com.donut.app.http.message.shakestar;

import android.os.Parcel;
import android.os.Parcelable;

import com.donut.app.http.message.BaseResponse;

import java.util.List;

/**
 * Created by hard on 2018/2/13.
 */

public class MyLikeResponse extends BaseResponse implements Parcelable {


    private List<MyLikeShakingStarListBean> myLikeShakingStarList;

    public List<MyLikeShakingStarListBean> getMyLikeShakingStarList() {
        return myLikeShakingStarList;
    }

    public void setMyLikeShakingStarList(List<MyLikeShakingStarListBean> myLikeShakingStarList) {
        this.myLikeShakingStarList = myLikeShakingStarList;
    }

    public static class MyLikeShakingStarListBean implements Parcelable {

        /**
         * createTime : 2018-01-24 15:44:16.0
         * materialType : 0
         * nickName : 嗡嗡嗡
         * headPic : http://media.sweetdonut.cn:8088/images/headPic/3ffb694799f943b0b1d4b6e26dcd39e1.jpg
         * browseTimes : 22
         * fkA01 : fc41dd9aa91d4b9ea3592a54e4f1cd95
         * lastTime : 6
         * playUrl : http://10.10.10.253:8000/video/20181/1efdf8570fd942029a13c23970a6e749.mp4
         * shareTimes : 19
         * praiseTimes : 1
         * collectionStatus : 0
         * praiseStatus : 1
         * b02Id : ccef098a63314ef4a14d3748d0c126cf
         * commentTimes : 7
         * g03Id : 9466e6f74bca4105899e52e5dc4fdeb1
         * materialTitle : GAI爷  GAI爷
         * contentDesc : 测试一下自己的
         * videoThumbnail : http://10.10.10.253:8000/images/donut/20181/26551b5f92c2452c9b3a80c37ceb5e69.png
         * materialThumbnail : http://10.10.10.253:8000/images/donut/20181/12253a800d344685a01728cbdae3accd.jpg
         * isMember : 0
         */

        private String createTime;
        private int materialType;
        private String nickName;
        private String headPic;
        private int browseTimes;
        private String fkA01;
        private String playUrl;
        private int shareTimes;
        private int praiseTimes;
        private int collectionStatus;
        private int praiseStatus;
        private String b02Id;
        private int commentTimes;
        private String g03Id;
        private String materialTitle;
        private String contentDesc;
        private String videoThumbnail;
        private String materialThumbnail;
        private int isMember;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getMaterialType() {
            return materialType;
        }

        public void setMaterialType(int materialType) {
            this.materialType = materialType;
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

        public int getBrowseTimes() {
            return browseTimes;
        }

        public void setBrowseTimes(int browseTimes) {
            this.browseTimes = browseTimes;
        }

        public String getFkA01() {
            return fkA01;
        }

        public void setFkA01(String fkA01) {
            this.fkA01 = fkA01;
        }



        public String getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        public int getShareTimes() {
            return shareTimes;
        }

        public void setShareTimes(int shareTimes) {
            this.shareTimes = shareTimes;
        }

        public int getPraiseTimes() {
            return praiseTimes;
        }

        public void setPraiseTimes(int praiseTimes) {
            this.praiseTimes = praiseTimes;
        }

        public int getCollectionStatus() {
            return collectionStatus;
        }

        public void setCollectionStatus(int collectionStatus) {
            this.collectionStatus = collectionStatus;
        }

        public int getPraiseStatus() {
            return praiseStatus;
        }

        public void setPraiseStatus(int praiseStatus) {
            this.praiseStatus = praiseStatus;
        }

        public String getB02Id() {
            return b02Id;
        }

        public void setB02Id(String b02Id) {
            this.b02Id = b02Id;
        }

        public int getCommentTimes() {
            return commentTimes;
        }

        public void setCommentTimes(int commentTimes) {
            this.commentTimes = commentTimes;
        }

        public String getG03Id() {
            return g03Id;
        }

        public void setG03Id(String g03Id) {
            this.g03Id = g03Id;
        }

        public String getMaterialTitle() {
            return materialTitle;
        }

        public void setMaterialTitle(String materialTitle) {
            this.materialTitle = materialTitle;
        }

        public String getContentDesc() {
            return contentDesc;
        }

        public void setContentDesc(String contentDesc) {
            this.contentDesc = contentDesc;
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

        public int getIsMember() {
            return isMember;
        }

        public void setIsMember(int isMember) {
            this.isMember = isMember;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.createTime);
            dest.writeInt(this.materialType);
            dest.writeString(this.nickName);
            dest.writeString(this.headPic);
            dest.writeInt(this.browseTimes);
            dest.writeString(this.fkA01);
            dest.writeString(this.playUrl);
            dest.writeInt(this.shareTimes);
            dest.writeInt(this.praiseTimes);
            dest.writeInt(this.collectionStatus);
            dest.writeInt(this.praiseStatus);
            dest.writeString(this.b02Id);
            dest.writeInt(this.commentTimes);
            dest.writeString(this.g03Id);
            dest.writeString(this.materialTitle);
            dest.writeString(this.contentDesc);
            dest.writeString(this.videoThumbnail);
            dest.writeString(this.materialThumbnail);
            dest.writeInt(this.isMember);
        }

        public MyLikeShakingStarListBean() {
        }

        protected MyLikeShakingStarListBean(Parcel in) {
            this.createTime = in.readString();
            this.materialType = in.readInt();
            this.nickName = in.readString();
            this.headPic = in.readString();
            this.browseTimes = in.readInt();
            this.fkA01 = in.readString();
            this.playUrl = in.readString();
            this.shareTimes = in.readInt();
            this.praiseTimes = in.readInt();
            this.collectionStatus = in.readInt();
            this.praiseStatus = in.readInt();
            this.b02Id = in.readString();
            this.commentTimes = in.readInt();
            this.g03Id = in.readString();
            this.materialTitle = in.readString();
            this.contentDesc = in.readString();
            this.videoThumbnail = in.readString();
            this.materialThumbnail = in.readString();
            this.isMember = in.readInt();
        }

        public static final Parcelable.Creator<MyLikeShakingStarListBean> CREATOR = new Parcelable.Creator<MyLikeShakingStarListBean>() {
            @Override
            public MyLikeShakingStarListBean createFromParcel(Parcel source) {
                return new MyLikeShakingStarListBean(source);
            }

            @Override
            public MyLikeShakingStarListBean[] newArray(int size) {
                return new MyLikeShakingStarListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.myLikeShakingStarList);
    }

    public MyLikeResponse() {
    }

    protected MyLikeResponse(Parcel in) {
        this.myLikeShakingStarList = in.createTypedArrayList(MyLikeShakingStarListBean.CREATOR);
    }

    public static final Parcelable.Creator<MyLikeResponse> CREATOR = new Parcelable.Creator<MyLikeResponse>() {
        @Override
        public MyLikeResponse createFromParcel(Parcel source) {
            return new MyLikeResponse(source);
        }

        @Override
        public MyLikeResponse[] newArray(int size) {
            return new MyLikeResponse[size];
        }
    };
}
