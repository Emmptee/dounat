package com.donut.app.http.message.home;

import com.donut.app.http.message.SubjectListDetail;

import java.util.List;

public class ChannelItem {
    private String channelId;

    private String channelName;

    private Integer channelType;

    private List<SubjectListDetail> subjectList;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    public List<SubjectListDetail> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<SubjectListDetail> subjectList) {
        this.subjectList = subjectList;
    }

}
