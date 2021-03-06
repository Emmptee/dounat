package com.donut.app.http.message.shakestar;

import android.os.Parcel;
import android.os.Parcelable;

import com.donut.app.http.message.BaseResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hard on 2018/1/31.
 */

public class ShakeStarSelectResponse extends BaseResponse implements Parcelable {


    private List<MaterialListBean> materialList;

    public List<MaterialListBean> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<MaterialListBean> materialList) {
        this.materialList = materialList;
    }

    public static class MaterialListBean implements Parcelable {

       private String g03Id;
       private String b02Id;
       private String fkB03;
       private String createTime;
       private String starName;
       private String starHeadPic;
       private String materialTitle;
       private String materialThumbnail;
       private int type;
       private long useTimes;

        public String getG03Id() {
            return g03Id;
        }

        public void setG03Id(String g03Id) {
            this.g03Id = g03Id;
        }

        public String getB02Id() {
            return b02Id;
        }

        public void setB02Id(String b02Id) {
            this.b02Id = b02Id;
        }

        public String getFkB03() {
            return fkB03;
        }

        public void setFkB03(String fkB03) {
            this.fkB03 = fkB03;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
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

        public String getMaterialTitle() {
            return materialTitle;
        }

        public void setMaterialTitle(String materialTitle) {
            this.materialTitle = materialTitle;
        }

        public String getMaterialThumbnail() {
            return materialThumbnail;
        }

        public void setMaterialThumbnail(String materialThumbnail) {
            this.materialThumbnail = materialThumbnail;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public long getUseTimes() {
            return useTimes;
        }

        public void setUseTimes(long useTimes) {
            this.useTimes = useTimes;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.g03Id);
            dest.writeString(this.b02Id);
            dest.writeString(this.fkB03);
            dest.writeString(this.createTime);
            dest.writeString(this.starName);
            dest.writeString(this.starHeadPic);
            dest.writeString(this.materialTitle);
            dest.writeString(this.materialThumbnail);
            dest.writeInt(this.type);
            dest.writeLong(this.useTimes);
        }

        public MaterialListBean() {
        }

        protected MaterialListBean(Parcel in) {
            this.g03Id = in.readString();
            this.b02Id = in.readString();
            this.fkB03 = in.readString();
            this.createTime = in.readString();
            this.starName = in.readString();
            this.starHeadPic = in.readString();
            this.materialTitle = in.readString();
            this.materialThumbnail = in.readString();
            this.type = in.readInt();
            this.useTimes = in.readLong();
        }

        public static final Creator<MaterialListBean> CREATOR = new Creator<MaterialListBean>() {
            @Override
            public MaterialListBean createFromParcel(Parcel source) {
                return new MaterialListBean(source);
            }

            @Override
            public MaterialListBean[] newArray(int size) {
                return new MaterialListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.materialList);
    }

    public ShakeStarSelectResponse() {
    }

    protected ShakeStarSelectResponse(Parcel in) {
        this.materialList = new ArrayList<MaterialListBean>();
        in.readList(this.materialList, MaterialListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ShakeStarSelectResponse> CREATOR = new Parcelable.Creator<ShakeStarSelectResponse>() {
        @Override
        public ShakeStarSelectResponse createFromParcel(Parcel source) {
            return new ShakeStarSelectResponse(source);
        }

        @Override
        public ShakeStarSelectResponse[] newArray(int size) {
            return new ShakeStarSelectResponse[size];
        }
    };
}
