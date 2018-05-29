package com.donut.app.http.message;

/**
 * Created by Administrator on 2016/2/28.
 */
public class UploadResponse extends BaseResponse
{
    private String fileUrl;

    private Long videoTime;

    public String getFileUrl()
    {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }

    public Long getVideoTime()
    {
        return videoTime;
    }

    public void setVideoTime(Long videoTime)
    {
        this.videoTime = videoTime;
    }
}
