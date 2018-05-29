package com.donut.app.http.message;



public class FileUploadResponse extends BaseResponse
{



    private String fileUrl;
    
    private long videoTime;

    public String getFileUrl()
    {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }

    public long getVideoTime()
    {
        return videoTime;
    }

    public void setVideoTime(long videoTime)
    {
        this.videoTime = videoTime;
    }
    
}
