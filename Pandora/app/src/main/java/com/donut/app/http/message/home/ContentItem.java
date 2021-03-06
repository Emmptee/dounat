package com.donut.app.http.message.home;

/**
 * @author Qi
 * @date 2018/04/04
 */
public class ContentItem {
    private String subjectId;
    private String b02Id;
    private String starId;
    private String g03Id;
    private String contentName;
    private String userName;
    private Integer period;
    private String starName;
    private String starHeadPic;
    private Integer materialType;
    private Integer welfareZoneType;
    private Integer categoryType;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getB02Id() {
        return b02Id;
    }

    public void setB02Id(String b02Id) {
        this.b02Id = b02Id;
    }

    public String getStarId() {
        return starId;
    }

    public void setStarId(String starId) {
        this.starId = starId;
    }

    public String getG03Id() {
        return g03Id;
    }

    public void setG03Id(String g03Id) {
        this.g03Id = g03Id;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
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

    public Integer getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }

    public Integer getWelfareZoneType() {
        return welfareZoneType;
    }

    public void setWelfareZoneType(Integer welfareZoneType) {
        this.welfareZoneType = welfareZoneType;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }
}
