package com.donut.app.http.message.shakestar;

import android.os.Parcel;
import android.os.Parcelable;

import com.donut.app.http.message.BaseResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hard on 2018/2/6.
 */

public class ParticularsResponse extends BaseResponse implements Parcelable {


    private int type;
    private String createTime;
    private String fkB03;
    private String starName;
    private String starHeadPic;
    private String title;
    private int display;
    private int videoNum;
    private int praiseStatus;
    private int useTimes;
    private int collectionStatus;
    private List<MaterialVideoListBean> materialVideoList;
    private List<ShakingStarListBean> shakingStarList;


    public int getPraiseStatus() {
        return praiseStatus;
    }

    public void setPraiseStatus(int praiseStatus) {
        this.praiseStatus = praiseStatus;
    }

    public int getCollectionStatus() {
        return collectionStatus;
    }

    public void setCollectionStatus(int collectionStatus) {
        this.collectionStatus = collectionStatus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFkB03() {
        return fkB03;
    }

    public void setFkB03(String fkB03) {
        this.fkB03 = fkB03;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }

    public String getStarHeadPic() {
        return starHeadPic;
    }

    public void setStarHeadPic(String starHeadPic) {
        this.starHeadPic = starHeadPic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public int getVideoNum() {
        return videoNum;
    }

    public void setVideoNum(int videoNum) {
        this.videoNum = videoNum;
    }

    public int getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(int useTimes) {
        this.useTimes = useTimes;
    }

    public List<MaterialVideoListBean> getMaterialVideoList() {
        return materialVideoList;
    }

    public void setMaterialVideoList(List<MaterialVideoListBean> materialVideoList) {
        this.materialVideoList = materialVideoList;
    }

    public List<ShakingStarListBean> getShakingStarList() {
        return shakingStarList;
    }

    public void setShakingStarList(List<ShakingStarListBean> shakingStarList) {
        this.shakingStarList = shakingStarList;
    }

    public static class MaterialVideoListBean{
        /**
         * name : null
         * description : null
         * uuid : 5b8a70278292491897b14cd58e9f132b
         * createTime : null
         * playUrl : null
         * thumbnailUrl : null
         * lastTime : null
         * fkB02 : null
         */

        private String name;
        private String description;
        private String uuid;
        private String createTime;
        private String playUrl;
        private String thumbnailUrl;
        private long lastTime;
        private String fkB02;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public long getLastTime() {
            return lastTime;
        }

        public void setLastTime(long lastTime) {
            this.lastTime = lastTime;
        }

        public String getFkB02() {
            return fkB02;
        }

        public void setFkB02(String fkB02) {
            this.fkB02 = fkB02;
        }
    }

    public static class ShakingStarListBean implements Parcelable {

        /**
         * createTime : 2017-01-23 09:39:31.0
         * nickName : 刘诗诗
         * headPic : http://172.37.0.206:8088/images/headPic/79b6332c4fdf423d9d8c778c1de3f0d6.png
         * materialTitle : 刘诗诗素材
         * contentDesc : 离家近婆婆集体照外婆给你爱回信肉类来PK你我给你摸T1聊QQ积极咯送DHL卢娟积极你牛佛陀来PK老婆婆哦钱各地您是婆婆情侣阿什利工地
         * videoThumbnail : null
         * materialThumbnail : null
         * playUrl : http://media.sweetdonut.cn:8088/video/8e6758c1c5584ed497736438b7f802ea.mp4
         * fkA01 : 0ef0401802644439820a454bdd5480f5
         * b02Id : 05ca828b56a34f6db721df7493fab807
         * praiseTimes : 1
         * shareTimes : 0
         * commentTimes : 1
         * praiseStatus : 1
         * lastTime : 2
         * browseTimes : 14
         * isMember : 0
         */

        private String createTime;
        private String nickName;
        private String headPic;
        private String materialTitle;
        private String contentDesc;
        private String videoThumbnail;
        private String materialThumbnail;
        private String playUrl;
        private String fkA01;
        private String b02Id;
        private int praiseTimes;
        private int shareTimes;
        private int commentTimes;
        private int praiseStatus;
        private long lastTime;
        private int browseTimes;
        private int isMember;

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

        public String getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        public String getFkA01() {
            return fkA01;
        }

        public void setFkA01(String fkA01) {
            this.fkA01 = fkA01;
        }

        public String getB02Id() {
            return b02Id;
        }

        public void setB02Id(String b02Id) {
            this.b02Id = b02Id;
        }

        public int getPraiseTimes() {
            return praiseTimes;
        }

        public void setPraiseTimes(int praiseTimes) {
            this.praiseTimes = praiseTimes;
        }

        public int getShareTimes() {
            return shareTimes;
        }

        public void setShareTimes(int shareTimes) {
            this.shareTimes = shareTimes;
        }

        public int getCommentTimes() {
            return commentTimes;
        }

        public void setCommentTimes(int commentTimes) {
            this.commentTimes = commentTimes;
        }

        public int getPraiseStatus() {
            return praiseStatus;
        }

        public void setPraiseStatus(int praiseStatus) {
            this.praiseStatus = praiseStatus;
        }

        public long getLastTime() {
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

        public int getIsMember() {
            return isMember;
        }

        public void setIsMember(int isMember) {
            this.isMember = isMember;
        }



        public ShakingStarListBean() {
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.createTime);
            dest.writeString(this.nickName);
            dest.writeString(this.headPic);
            dest.writeString(this.materialTitle);
            dest.writeString(this.contentDesc);
            dest.writeString(this.videoThumbnail);
            dest.writeString(this.materialThumbnail);
            dest.writeString(this.playUrl);
            dest.writeString(this.fkA01);
            dest.writeString(this.b02Id);
            dest.writeInt(this.praiseTimes);
            dest.writeInt(this.shareTimes);
            dest.writeInt(this.commentTimes);
            dest.writeInt(this.praiseStatus);
            dest.writeLong(this.lastTime);
            dest.writeInt(this.browseTimes);
            dest.writeInt(this.isMember);
        }

        protected ShakingStarListBean(Parcel in) {
            this.createTime = in.readString();
            this.nickName = in.readString();
            this.headPic = in.readString();
            this.materialTitle = in.readString();
            this.contentDesc = in.readString();
            this.videoThumbnail = in.readString();
            this.materialThumbnail = in.readString();
            this.playUrl = in.readString();
            this.fkA01 = in.readString();
            this.b02Id = in.readString();
            this.praiseTimes = in.readInt();
            this.shareTimes = in.readInt();
            this.commentTimes = in.readInt();
            this.praiseStatus = in.readInt();
            this.lastTime = in.readLong();
            this.browseTimes = in.readInt();
            this.isMember = in.readInt();
        }

        public static final Creator<ShakingStarListBean> CREATOR = new Creator<ShakingStarListBean>() {
            @Override
            public ShakingStarListBean createFromParcel(Parcel source) {
                return new ShakingStarListBean(source);
            }

            @Override
            public ShakingStarListBean[] newArray(int size) {
                return new ShakingStarListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.createTime);
        dest.writeString(this.fkB03);
        dest.writeString(this.starName);
        dest.writeString(this.starHeadPic);
        dest.writeString(this.title);
        dest.writeInt(this.display);
        dest.writeInt(this.videoNum);
        dest.writeInt(this.praiseStatus);
        dest.writeInt(this.useTimes);
        dest.writeInt(this.collectionStatus);
        dest.writeList(this.materialVideoList);
        dest.writeList(this.shakingStarList);
    }

    public ParticularsResponse() {
    }

    protected ParticularsResponse(Parcel in) {
        this.type = in.readInt();
        this.createTime = in.readString();
        this.fkB03 = in.readString();
        this.starName = in.readString();
        this.starHeadPic = in.readString();
        this.title = in.readString();
        this.display = in.readInt();
        this.videoNum = in.readInt();
        this.praiseStatus = in.readInt();
        this.useTimes = in.readInt();
        this.collectionStatus = in.readInt();
        this.materialVideoList = new ArrayList<MaterialVideoListBean>();
        in.readList(this.materialVideoList, MaterialVideoListBean.class.getClassLoader());
        this.shakingStarList = new ArrayList<ShakingStarListBean>();
        in.readList(this.shakingStarList, ShakingStarListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ParticularsResponse> CREATOR = new Parcelable.Creator<ParticularsResponse>() {
        @Override
        public ParticularsResponse createFromParcel(Parcel source) {
            return new ParticularsResponse(source);
        }

        @Override
        public ParticularsResponse[] newArray(int size) {
            return new ParticularsResponse[size];
        }
    };
}
