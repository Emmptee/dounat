package com.donut.app.http.message.shakestar;

import com.donut.app.http.message.BaseResponse;

import java.util.List;

/**
 * Created by hard on 2018/2/9.
 */

public class CommendAllResponse extends BaseResponse {


    /**
     * hotComments : [{"commentatorUrl":"http://172.37.0.206:8088/images/headPic/20174/86d25fcdcd5d42f7a1ce6b07c139ef29.png","commentatorName":"bing狗狗🐶狗狗狗🐶","isReplied":"1","userType":"0","createTime":"2018-01-15 13:41:00","updateTime":"2018-01-15 13:41:00","isPraised":"1","commentNum":0,"title":null,"ipAddress":"10.10.10.29","contentId":"aec1545955694f35bf2f0604fc26fefb","subjectId":null,"imgUrl":null,"associationId":null,"rewardNum":null,"commentId":"5a5c3eec8c3e6a0ffaf74da7","commentatorId":"5af8ab43a6e442ab9f933f18af4eb4a8","subCommentsList":[],"repliedUserIdList":[],"praisedUserIdList":[],"praiseList":[],"isMember":0,"praiseNum":0,"userName":null,"address":"局域网","type":null,"content":"啦啦啦啦啦啦啦啦啦","description":null,"status":null}]
     * commentsList : [{"commentatorUrl":"http://172.37.0.206:8088/images/headPic/20174/86d25fcdcd5d42f7a1ce6b07c139ef29.png","commentatorName":"bing狗狗🐶狗狗狗🐶","isReplied":"1","userType":"0","createTime":"2018-01-15 13:41:00","updateTime":"2018-01-15 13:41:00","isPraised":"1","commentNum":0,"title":null,"ipAddress":"10.10.10.29","contentId":"aec1545955694f35bf2f0604fc26fefb","subjectId":null,"imgUrl":null,"associationId":null,"rewardNum":null,"commentId":"5a5c3eec8c3e6a0ffaf74da7","commentatorId":"5af8ab43a6e442ab9f933f18af4eb4a8","subCommentsList":[],"repliedUserIdList":[],"praisedUserIdList":[],"praiseList":[],"isMember":0,"praiseNum":0,"userName":null,"address":"局域网","type":null,"content":"啦啦啦啦啦啦啦啦啦","description":null,"status":null}]
     * contentCommentsDetail : null
     * myComments : null
     * commentTimes : 1
     */

    private Object contentCommentsDetail;
    private Object myComments;
    private int commentTimes;
    private List<HotCommentsBean> hotComments;
    private List<CommentsListBean> commentsList;

    public Object getContentCommentsDetail() {
        return contentCommentsDetail;
    }

    public void setContentCommentsDetail(Object contentCommentsDetail) {
        this.contentCommentsDetail = contentCommentsDetail;
    }

    public Object getMyComments() {
        return myComments;
    }

    public void setMyComments(Object myComments) {
        this.myComments = myComments;
    }

    public int getCommentTimes() {
        return commentTimes;
    }

    public void setCommentTimes(int commentTimes) {
        this.commentTimes = commentTimes;
    }

    public List<HotCommentsBean> getHotComments() {
        return hotComments;
    }

    public void setHotComments(List<HotCommentsBean> hotComments) {
        this.hotComments = hotComments;
    }

    public List<CommentsListBean> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(List<CommentsListBean> commentsList) {
        this.commentsList = commentsList;
    }

    public static class HotCommentsBean {
        /**
         * commentatorUrl : http://172.37.0.206:8088/images/headPic/20174/86d25fcdcd5d42f7a1ce6b07c139ef29.png
         * commentatorName : bing狗狗🐶狗狗狗🐶
         * isReplied : 1
         * userType : 0
         * createTime : 2018-01-15 13:41:00
         * updateTime : 2018-01-15 13:41:00
         * isPraised : 1
         * commentNum : 0
         * title : null
         * ipAddress : 10.10.10.29
         * contentId : aec1545955694f35bf2f0604fc26fefb
         * subjectId : null
         * imgUrl : null
         * associationId : null
         * rewardNum : null
         * commentId : 5a5c3eec8c3e6a0ffaf74da7
         * commentatorId : 5af8ab43a6e442ab9f933f18af4eb4a8
         * subCommentsList : []
         * repliedUserIdList : []
         * praisedUserIdList : []
         * praiseList : []
         * isMember : 0
         * praiseNum : 0
         * userName : null
         * address : 局域网
         * type : null
         * content : 啦啦啦啦啦啦啦啦啦
         * description : null
         * status : null
         */

        private String commentatorUrl;
        private String commentatorName;
        private String isReplied;
        private String userType;
        private String createTime;
        private String updateTime;
        private String isPraised;
        private int commentNum;
        private Object title;
        private String ipAddress;
        private String contentId;
        private Object subjectId;
        private Object imgUrl;
        private Object associationId;
        private Object rewardNum;
        private String commentId;
        private String commentatorId;
        private int isMember;
        private int praiseNum;
        private Object userName;
        private String address;
        private Object type;
        private String content;
        private Object description;
        private Object status;
        private List<?> subCommentsList;
        private List<?> repliedUserIdList;
        private List<?> praisedUserIdList;
        private List<?> praiseList;

        public String getCommentatorUrl() {
            return commentatorUrl;
        }

        public void setCommentatorUrl(String commentatorUrl) {
            this.commentatorUrl = commentatorUrl;
        }

        public String getCommentatorName() {
            return commentatorName;
        }

        public void setCommentatorName(String commentatorName) {
            this.commentatorName = commentatorName;
        }

        public String getIsReplied() {
            return isReplied;
        }

        public void setIsReplied(String isReplied) {
            this.isReplied = isReplied;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getIsPraised() {
            return isPraised;
        }

        public void setIsPraised(String isPraised) {
            this.isPraised = isPraised;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getContentId() {
            return contentId;
        }

        public void setContentId(String contentId) {
            this.contentId = contentId;
        }

        public Object getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(Object subjectId) {
            this.subjectId = subjectId;
        }

        public Object getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(Object imgUrl) {
            this.imgUrl = imgUrl;
        }

        public Object getAssociationId() {
            return associationId;
        }

        public void setAssociationId(Object associationId) {
            this.associationId = associationId;
        }

        public Object getRewardNum() {
            return rewardNum;
        }

        public void setRewardNum(Object rewardNum) {
            this.rewardNum = rewardNum;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getCommentatorId() {
            return commentatorId;
        }

        public void setCommentatorId(String commentatorId) {
            this.commentatorId = commentatorId;
        }

        public int getIsMember() {
            return isMember;
        }

        public void setIsMember(int isMember) {
            this.isMember = isMember;
        }

        public int getPraiseNum() {
            return praiseNum;
        }

        public void setPraiseNum(int praiseNum) {
            this.praiseNum = praiseNum;
        }

        public Object getUserName() {
            return userName;
        }

        public void setUserName(Object userName) {
            this.userName = userName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getType() {
            return type;
        }

        public void setType(Object type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public List<?> getSubCommentsList() {
            return subCommentsList;
        }

        public void setSubCommentsList(List<?> subCommentsList) {
            this.subCommentsList = subCommentsList;
        }

        public List<?> getRepliedUserIdList() {
            return repliedUserIdList;
        }

        public void setRepliedUserIdList(List<?> repliedUserIdList) {
            this.repliedUserIdList = repliedUserIdList;
        }

        public List<?> getPraisedUserIdList() {
            return praisedUserIdList;
        }

        public void setPraisedUserIdList(List<?> praisedUserIdList) {
            this.praisedUserIdList = praisedUserIdList;
        }

        public List<?> getPraiseList() {
            return praiseList;
        }

        public void setPraiseList(List<?> praiseList) {
            this.praiseList = praiseList;
        }
    }

    public static class CommentsListBean {
        /**
         * commentatorUrl : http://172.37.0.206:8088/images/headPic/20174/86d25fcdcd5d42f7a1ce6b07c139ef29.png
         * commentatorName : bing狗狗🐶狗狗狗🐶
         * isReplied : 1
         * userType : 0
         * createTime : 2018-01-15 13:41:00
         * updateTime : 2018-01-15 13:41:00
         * isPraised : 1
         * commentNum : 0
         * title : null
         * ipAddress : 10.10.10.29
         * contentId : aec1545955694f35bf2f0604fc26fefb
         * subjectId : null
         * imgUrl : null
         * associationId : null
         * rewardNum : null
         * commentId : 5a5c3eec8c3e6a0ffaf74da7
         * commentatorId : 5af8ab43a6e442ab9f933f18af4eb4a8
         * subCommentsList : []
         * repliedUserIdList : []
         * praisedUserIdList : []
         * praiseList : []
         * isMember : 0
         * praiseNum : 0
         * userName : null
         * address : 局域网
         * type : null
         * content : 啦啦啦啦啦啦啦啦啦
         * description : null
         * status : null
         */

        private String commentatorUrl;
        private String commentatorName;
        private String isReplied;
        private String userType;
        private String createTime;
        private String updateTime;
        private String isPraised;
        private int commentNum;
        private Object title;
        private String ipAddress;
        private String contentId;
        private Object subjectId;
        private Object imgUrl;
        private Object associationId;
        private Object rewardNum;
        private String commentId;
        private String commentatorId;
        private int isMember;
        private int praiseNum;
        private Object userName;
        private String address;
        private Object type;
        private String content;
        private Object description;
        private Object status;
        private List<?> subCommentsList;
        private List<?> repliedUserIdList;
        private List<?> praisedUserIdList;
        private List<?> praiseList;

        public String getCommentatorUrl() {
            return commentatorUrl;
        }

        public void setCommentatorUrl(String commentatorUrl) {
            this.commentatorUrl = commentatorUrl;
        }

        public String getCommentatorName() {
            return commentatorName;
        }

        public void setCommentatorName(String commentatorName) {
            this.commentatorName = commentatorName;
        }

        public String getIsReplied() {
            return isReplied;
        }

        public void setIsReplied(String isReplied) {
            this.isReplied = isReplied;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getIsPraised() {
            return isPraised;
        }

        public void setIsPraised(String isPraised) {
            this.isPraised = isPraised;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getContentId() {
            return contentId;
        }

        public void setContentId(String contentId) {
            this.contentId = contentId;
        }

        public Object getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(Object subjectId) {
            this.subjectId = subjectId;
        }

        public Object getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(Object imgUrl) {
            this.imgUrl = imgUrl;
        }

        public Object getAssociationId() {
            return associationId;
        }

        public void setAssociationId(Object associationId) {
            this.associationId = associationId;
        }

        public Object getRewardNum() {
            return rewardNum;
        }

        public void setRewardNum(Object rewardNum) {
            this.rewardNum = rewardNum;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getCommentatorId() {
            return commentatorId;
        }

        public void setCommentatorId(String commentatorId) {
            this.commentatorId = commentatorId;
        }

        public int getIsMember() {
            return isMember;
        }

        public void setIsMember(int isMember) {
            this.isMember = isMember;
        }

        public int getPraiseNum() {
            return praiseNum;
        }

        public void setPraiseNum(int praiseNum) {
            this.praiseNum = praiseNum;
        }

        public Object getUserName() {
            return userName;
        }

        public void setUserName(Object userName) {
            this.userName = userName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getType() {
            return type;
        }

        public void setType(Object type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public List<?> getSubCommentsList() {
            return subCommentsList;
        }

        public void setSubCommentsList(List<?> subCommentsList) {
            this.subCommentsList = subCommentsList;
        }

        public List<?> getRepliedUserIdList() {
            return repliedUserIdList;
        }

        public void setRepliedUserIdList(List<?> repliedUserIdList) {
            this.repliedUserIdList = repliedUserIdList;
        }

        public List<?> getPraisedUserIdList() {
            return praisedUserIdList;
        }

        public void setPraisedUserIdList(List<?> praisedUserIdList) {
            this.praisedUserIdList = praisedUserIdList;
        }

        public List<?> getPraiseList() {
            return praiseList;
        }

        public void setPraiseList(List<?> praiseList) {
            this.praiseList = praiseList;
        }
    }
}
