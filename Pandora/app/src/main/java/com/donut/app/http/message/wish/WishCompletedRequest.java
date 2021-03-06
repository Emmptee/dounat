package com.donut.app.http.message.wish;

import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */

public class WishCompletedRequest {
    private String g01Id;

    private String b02Id;

    private String achievePicUrl;

    private String achieveVideoUrl;

    private Long videoLastTime;

    private List<AchieveWishVoice> achieveVoiceList;

    private String achieveText;

    /**
     * @return g01Id
     */
    public String getG01Id()
    {
        return g01Id;
    }

    /**
     * @return videoLastTime
     */
    public Long getVideoLastTime()
    {
        return videoLastTime;
    }

    /**
     * @param videoLastTime
     *            the videoLastTime to set
     */
    public void setVideoLastTime(Long videoLastTime)
    {
        this.videoLastTime = videoLastTime;
    }

    /**
     * @param g01Id
     *            the g01Id to set
     */
    public void setG01Id(String g01Id)
    {
        this.g01Id = g01Id;
    }

    /**
     * @return b02Id
     */
    public String getB02Id()
    {
        return b02Id;
    }

    /**
     * @param b02Id
     *            the b02Id to set
     */
    public void setB02Id(String b02Id)
    {
        this.b02Id = b02Id;
    }

    /**
     * @return achievePicUrl
     */
    public String getAchievePicUrl()
    {
        return achievePicUrl;
    }

    /**
     * @param achievePicUrl
     *            the achievePicUrl to set
     */
    public void setAchievePicUrl(String achievePicUrl)
    {
        this.achievePicUrl = achievePicUrl;
    }

    /**
     * @return achieveVideoUrl
     */
    public String getAchieveVideoUrl()
    {
        return achieveVideoUrl;
    }

    /**
     * @param achieveVideoUrl
     *            the achieveVideoUrl to set
     */
    public void setAchieveVideoUrl(String achieveVideoUrl)
    {
        this.achieveVideoUrl = achieveVideoUrl;
    }

    /**
     * @return achieveVoiceList
     */
    public List<AchieveWishVoice> getAchieveVoiceList()
    {
        return achieveVoiceList;
    }

    /**
     * @param achieveVoiceList
     *            the achieveVoiceList to set
     */
    public void setAchieveVoiceList(List<AchieveWishVoice> achieveVoiceList)
    {
        this.achieveVoiceList = achieveVoiceList;
    }

    /**
     * @return achieveText
     */
    public String getAchieveText()
    {
        return achieveText;
    }

    /**
     * @param achieveText
     *            the achieveText to set
     */
    public void setAchieveText(String achieveText)
    {
        this.achieveText = achieveText;
    }
}
