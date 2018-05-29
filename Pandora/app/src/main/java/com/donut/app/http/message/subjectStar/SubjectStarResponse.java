package com.donut.app.http.message.subjectStar;

import android.os.Parcel;
import android.os.Parcelable;

import com.donut.app.http.message.BaseResponse;

import java.util.ArrayList;
import java.util.List;

public class SubjectStarResponse extends BaseResponse implements Parcelable {

    private String starId;

    private String headPic;
    
    private String name;
    
    private String birth;
    
    private Integer blood;
    
    private float height;
    
    private float weight;
    
    private String description;

    private long followNum;

    private int isConcerned;

    private String role;

    private int isMember;
    
    private List<StarPlan> starPlans = new ArrayList<StarPlan>();
    
    private List<StarWorks> starWorks = new ArrayList<StarWorks>();

    public String getStarId() {
        return starId;
    }

    public void setStarId(String starId) {
        this.starId = starId;
    }

    public String getHeadPic()
    {
        return headPic;
    }

    public void setHeadPic(String headPic)
    {
        this.headPic = headPic;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBirth()
    {
        return birth;
    }

    public void setBirth(String birth)
    {
        this.birth = birth;
    }

    public Integer getBlood()
    {
        return blood;
    }

    public void setBlood(Integer blood)
    {
        this.blood = blood;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    public float getWeight()
    {
        return weight;
    }

    public void setWeight(float weight)
    {
        this.weight = weight;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public long getFollowNum() {
        return followNum;
    }

    public void setFollowNum(long followNum) {
        this.followNum = followNum;
    }

    public int getIsConcerned() {
        return isConcerned;
    }

    public void setIsConcerned(int isConcerned) {
        this.isConcerned = isConcerned;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getIsMember() {
        return isMember;
    }

    public void setIsMember(int isMember) {
        this.isMember = isMember;
    }

    public List<StarPlan> getStarPlans() {
        return starPlans;
    }

    public void setStarPlans(List<StarPlan> starPlans) {
        this.starPlans = starPlans;
    }

    public List<StarWorks> getStarWorks()
    {
        return starWorks;
    }

    public void setStarWorks(List<StarWorks> starWorks)
    {
        this.starWorks = starWorks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.starId);
        dest.writeString(this.headPic);
        dest.writeString(this.name);
        dest.writeString(this.birth);
        dest.writeValue(this.blood);
        dest.writeFloat(this.height);
        dest.writeFloat(this.weight);
        dest.writeString(this.description);
        dest.writeValue(this.followNum);
        dest.writeInt(this.isConcerned);
        dest.writeString(this.role);
        dest.writeInt(this.isMember);
        dest.writeList(this.starPlans);
        dest.writeList(this.starWorks);
    }

    public SubjectStarResponse() {
    }

    protected SubjectStarResponse(Parcel in) {
        this.starId = in.readString();
        this.headPic = in.readString();
        this.name = in.readString();
        this.birth = in.readString();
        this.blood = (Integer) in.readValue(Integer.class.getClassLoader());
        this.height = in.readFloat();
        this.weight = in.readFloat();
        this.description = in.readString();
        this.followNum = in.readLong();
        this.isConcerned = in.readInt();
        this.role = in.readString();
        this.isMember = in.readInt();
        this.starPlans = new ArrayList<StarPlan>();
        in.readList(this.starPlans, StarPlan.class.getClassLoader());
        this.starWorks = new ArrayList<StarWorks>();
        in.readList(this.starWorks, StarWorks.class.getClassLoader());
    }

    public static final Parcelable.Creator<SubjectStarResponse> CREATOR = new Parcelable.Creator<SubjectStarResponse>() {
        @Override
        public SubjectStarResponse createFromParcel(Parcel source) {
            return new SubjectStarResponse(source);
        }

        @Override
        public SubjectStarResponse[] newArray(int size) {
            return new SubjectStarResponse[size];
        }
    };
}
