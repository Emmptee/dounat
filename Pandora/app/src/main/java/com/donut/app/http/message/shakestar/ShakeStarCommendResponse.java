package com.donut.app.http.message.shakestar;

import android.databinding.BaseObservable;

import com.donut.app.BR;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.ShakingStarListBean;

import java.util.List;

/**
 * Created by hard on 2018/1/30.
 */

public class ShakeStarCommendResponse extends BaseResponse{


    public List<ShakingStarListBean> shakingStarList;

    public List<ShakingStarListBean> getShakingStarList() {
        return shakingStarList;
    }

    public void setShakingStarList(List<ShakingStarListBean> shakingStarList) {
        this.shakingStarList = shakingStarList;
    }
    public class ShakingStarListBean extends BaseObservable {
        /**
         * b02Id : c53cf10fab8f4bf387ab4dbfff1f8987
         * praiseTimes : 0
         * shareTimes : 0
         * commentTimes : 0
         * praiseStatus : 0
         * createTime : 2017-12-28 18:14:53.0
         * headPic : http://wx.qlogo.cn/mmopen/kqodNCVWpEutS2bHbvCOZtEeV3yRRrfvCibAppDUG8Tq0JEfYjlFwPibbV2fwayMkwhIr5Hf8FzpiaMYeN3pTbcCWPXGfKulrDc/0
         * nickName : 华仔北京
         * contentDesc : 第一次测试
         * lastTime : 14000
         * browseTimes : 0
         * playUrl : http://media.sweetdonut.cn/video/20173/037c84d73bf3411fb75771a80708d18b.mp4
         * fkA01 : a16685d7373549598674f047f3622eb2
         * isMember : 0
         * videoThumbnail : http://media.sweetdonut.cn/other/20173/c89e9cd8b9f449278c72121a15739463.jpg
         * materialThumbnail : null
         * materialTitle : 刘诗诗素材同屏
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
        private int followStatus;
        private int praiseStatus;
        private int collectionStatus;
        private long shareTimes;
        private String videoThumbnail;
        private String materialThumbnail;
        private int materialType;
        private String playUrl;
        private long lastTime;
        private int isMember;
        private long browseTimes;

        public int getFollowStatus() {
            return followStatus;
        }

        public void setFollowStatus(int followStatus) {
            this.followStatus = followStatus;
        }

        public void setB02Id(String b02Id) {
            this.b02Id = b02Id;
        }

        public void setG03Id(String g03Id) {
            this.g03Id = g03Id;
        }

        public void setFkA01(String fkA01) {
            this.fkA01 = fkA01;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public void setContentDesc(String contentDesc) {
            this.contentDesc = contentDesc;
        }

        public void setMaterialTitle(String materialTitle) {
            this.materialTitle = materialTitle;
        }

        public void setPraiseTimes(long praiseTimes) {
            this.praiseTimes = praiseTimes;
            notifyPropertyChanged(BR.comment);
        }

        public void setCommentTimes(long commentTimes) {
            this.commentTimes = commentTimes;
        }

        public void setPraiseStatus(int praiseStatus) {
            this.praiseStatus = praiseStatus;
            notifyChange();
        }

        public void setCollectionStatus(int collectionStatus) {
            this.collectionStatus = collectionStatus;
        }

        public void setShareTimes(long shareTimes) {
            this.shareTimes = shareTimes;
        }


        public void setMaterialThumbnail(String materialThumbnail) {
            this.materialThumbnail = materialThumbnail;
        }

        public void setMaterialType(int materialType) {
            this.materialType = materialType;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        public void setLastTime(long lastTime) {
            this.lastTime = lastTime;
        }

        public void setIsMember(int isMember) {
            this.isMember = isMember;
        }

        public void setBrowseTimes(long browseTimes) {
            this.browseTimes = browseTimes;
        }

        public String getB02Id() {

            return b02Id;
        }

        public String getG03Id() {
            return g03Id;
        }

        public String getFkA01() {
            return fkA01;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getNickName() {
            return nickName;
        }

        public String getHeadPic() {
            return headPic;
        }

        public String getContentDesc() {
            return contentDesc;
        }

        public String getMaterialTitle() {
            return materialTitle;
        }

        public long getPraiseTimes() {
            return praiseTimes;
        }

        public long getCommentTimes() {
            return commentTimes;
        }

        public int getPraiseStatus() {
            return praiseStatus;
        }

        public int getCollectionStatus() {
            return collectionStatus;
        }

        public long getShareTimes() {
            return shareTimes;
        }

        public void setVideoThumbnail(String videoThumbnail) {
            this.videoThumbnail = videoThumbnail;
        }

        public String getVideoThumbnail() {

            return videoThumbnail;
        }

        public String getMaterialThumbnail() {
            return materialThumbnail;
        }

        public int getMaterialType() {
            return materialType;
        }

        public String getPlayUrl() {
            return playUrl;
        }

        public long getLastTime() {
            return lastTime;
        }

        public int getIsMember() {
            return isMember;
        }

        public long getBrowseTimes() {
            return browseTimes;
        }
    }

}
