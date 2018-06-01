package com.donut.app.entity;

import com.android.volley.manager.LoadController;
import com.android.volley.manager.RequestManager;
import com.lidroid.xutils.db.annotation.Transient;

/**
 * Author: Qi Date: 16-09-20
 */
public class UploadInfo {

    public UploadInfo() {
    }

    private long id;

    @Transient
//    private HttpHandler<File> handler;

    private LoadController handler;
    /**
     * 0:等待
     * 1:处理中
     * 2:上传中
     * 3.成功
     * 4.失败
     * 5.取消
     */
    private int state;

    private String filePath;

    private String outFilePath;

    private String fileName;

    private long progress;

    private long fileLength;

    private int forceUpdateFlag;

    private String versionCode;

    private String description;

    private String createTime;

    private String userId;

    private String token;

//    /**
//     * 1: 创意征集
//     * 2: 发起挑战
//     * 3: 明星回复心愿
//     * 4: 明星发起通告
//     */
    private int saveType;

    public int getSaveType() {
        return saveType;
    }

    public void setSaveType(int saveType) {
        this.saveType = saveType;
    }

    private SaveTypeEnum mSaveTypeEnum;

    public enum SaveTypeEnum {
        IP_SEND(1),
        CHALLENGE(2),
        STAR_REPLY(3),
        STAR_SEND_NOTICE(4);

        SaveTypeEnum(int type) {
            this.type = type;
        }
        private int type;

        public int getType() {
            return type;
        }
    }

    /**
     * 专题名称或创意名称
     */
    private String saveTitle;

    /**
     * 上传完成后组织的json,调用保存数据接口
     */
    private String saveData;

    @Transient
    private RequestManager.RequestListener baseCallBack;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LoadController getHandler() {
        return handler;
    }

    public void setHandler(LoadController handler) {
        this.handler = handler;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public int getForceUpdateFlag() {
        return forceUpdateFlag;
    }

    public void setForceUpdateFlag(int forceUpdateFlag) {
        this.forceUpdateFlag = forceUpdateFlag;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOutFilePath() {
        return outFilePath;
    }

    public void setOutFilePath(String outFilePath) {
        this.outFilePath = outFilePath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSaveTitle() {
        return saveTitle;
    }

    public void setSaveTitle(String saveTitle) {
        this.saveTitle = saveTitle;
    }

    public SaveTypeEnum getSaveTypeEnum() {
        return mSaveTypeEnum;
    }

    public void setSaveTypeEnum(SaveTypeEnum saveTypeEnum) {
        mSaveTypeEnum = saveTypeEnum;
    }

    public String getSaveData() {
        return saveData;
    }

    public void setSaveData(String saveData) {
        this.saveData = saveData;
    }

    public RequestManager.RequestListener getBaseCallBack() {
        return baseCallBack;
    }

    public void setBaseCallBack(RequestManager.RequestListener baseCallBack) {
        this.baseCallBack = baseCallBack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UploadInfo)) {
            return false;
        }

        UploadInfo that = (UploadInfo) o;

        if (id != that.id) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
