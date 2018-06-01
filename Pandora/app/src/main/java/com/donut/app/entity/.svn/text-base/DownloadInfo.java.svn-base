package com.donut.app.entity;

import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.http.HttpHandler;

import java.io.File;

/**
 * Author: wyouflf Date: 13-11-10 Time: 下午8:11
 */
public class DownloadInfo
{

    public DownloadInfo()
    {
    }

    private long id;

    @Transient
    private HttpHandler<File> handler;

    private HttpHandler.State state;

    private String downloadUrl;

    private String fileName;

    private String fileSavePath;

    private long progress;

    private long fileLength;

    private boolean autoResume;

    private boolean autoRename;

    private int forceUpdateFlag;

    private String versionCode;

    private String description;

    private String createTime;

    private String fileUrl;

    private Long fileSize;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public HttpHandler<File> getHandler()
    {
        return handler;
    }

    public void setHandler(HttpHandler<File> handler)
    {
        this.handler = handler;
    }

    public HttpHandler.State getState()
    {
        return state;
    }

    public void setState(HttpHandler.State state)
    {
        this.state = state;
    }

    public String getDownloadUrl()
    {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl)
    {
        this.downloadUrl = downloadUrl;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileSavePath()
    {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath)
    {
        this.fileSavePath = fileSavePath;
    }

    public long getProgress()
    {
        return progress;
    }

    public void setProgress(long progress)
    {
        this.progress = progress;
    }

    public long getFileLength()
    {
        return fileLength;
    }

    public void setFileLength(long fileLength)
    {
        this.fileLength = fileLength;
    }

    public boolean isAutoResume()
    {
        return autoResume;
    }

    public void setAutoResume(boolean autoResume)
    {
        this.autoResume = autoResume;
    }

    public boolean isAutoRename()
    {
        return autoRename;
    }

    public void setAutoRename(boolean autoRename)
    {
        this.autoRename = autoRename;
    }

    public int getForceUpdateFlag()
    {
        return forceUpdateFlag;
    }

    public void setForceUpdateFlag(int forceUpdateFlag)
    {
        this.forceUpdateFlag = forceUpdateFlag;
    }

    public String getVersionCode()
    {
        return versionCode;
    }

    public void setVersionCode(String versionCode)
    {
        this.versionCode = versionCode;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getFileUrl()
    {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }

    public Long getFileSize()
    {
        return fileSize;
    }

    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DownloadInfo)) {
            return false;
        }

        DownloadInfo that = (DownloadInfo) o;

        if (id != that.id) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return (int) (id ^ (id >>> 32));
    }
}
