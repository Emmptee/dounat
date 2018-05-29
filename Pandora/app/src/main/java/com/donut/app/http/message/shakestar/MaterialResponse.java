package com.donut.app.http.message.shakestar;

import android.os.Parcel;
import android.os.Parcelable;

import com.donut.app.http.message.BaseResponse;

import java.util.List;

/**
 * Created by hard on 2018/2/22.
 */

public class MaterialResponse extends BaseResponse implements Parcelable {


    private List<MyMaterialListBean> myMaterialList;

    public List<MyMaterialListBean> getMyMaterialList() {
        return myMaterialList;
    }

    public void setMyMaterialList(List<MyMaterialListBean> myMaterialList) {
        this.myMaterialList = myMaterialList;
    }

    public static class MyMaterialListBean implements Parcelable {

        /**
         * type : 0
         * materialTitle : 刘诗诗素材同屏
         * b02Id : 0303c41cae744c9a907aeac652fa0802
         * lastTime : 0
         * title : null
         * playUrl : null
         * fkB03 : null
         * display : null
         * videoNum : null
         * materialThumbnail : http://10.10.10.253:8000/images/donut/20181/a752fe6bb5f84f45971b1418c6844d73.jpg
         * collectionStatus : null
         * createTime : null
         * praiseStatus : null
         * starHeadPic : null
         * g03Id : 225d9b3fdbab43d497da6885b1650386
         * useTimes : 1
         * starName : null
         */

        private int type;
        private String materialTitle;
        private String b02Id;
        private String materialThumbnail;
        private String g03Id;
        private int useTimes;
        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getMaterialTitle() {
            return materialTitle;
        }

        public void setMaterialTitle(String materialTitle) {
            this.materialTitle = materialTitle;
        }

        public String getB02Id() {
            return b02Id;
        }

        public void setB02Id(String b02Id) {
            this.b02Id = b02Id;
        }


        public String getMaterialThumbnail() {
            return materialThumbnail;
        }

        public void setMaterialThumbnail(String materialThumbnail) {
            this.materialThumbnail = materialThumbnail;
        }



        public String getG03Id() {
            return g03Id;
        }

        public void setG03Id(String g03Id) {
            this.g03Id = g03Id;
        }

        public int getUseTimes() {
            return useTimes;
        }

        public void setUseTimes(int useTimes) {
            this.useTimes = useTimes;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.type);
            dest.writeString(this.materialTitle);
            dest.writeString(this.b02Id);
            dest.writeString(this.materialThumbnail);
            dest.writeString(this.g03Id);
            dest.writeInt(this.useTimes);
        }

        public MyMaterialListBean() {
        }

        protected MyMaterialListBean(Parcel in) {
            this.type = in.readInt();
            this.materialTitle = in.readString();
            this.b02Id = in.readString();
            this.materialThumbnail = in.readString();
            this.g03Id = in.readString();
            this.useTimes = in.readInt();
        }

        public static final Parcelable.Creator<MyMaterialListBean> CREATOR = new Parcelable.Creator<MyMaterialListBean>() {
            @Override
            public MyMaterialListBean createFromParcel(Parcel source) {
                return new MyMaterialListBean(source);
            }

            @Override
            public MyMaterialListBean[] newArray(int size) {
                return new MyMaterialListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.myMaterialList);
    }

    public MaterialResponse() {
    }

    protected MaterialResponse(Parcel in) {
        this.myMaterialList = in.createTypedArrayList(MyMaterialListBean.CREATOR);
    }

    public static final Parcelable.Creator<MaterialResponse> CREATOR = new Parcelable.Creator<MaterialResponse>() {
        @Override
        public MaterialResponse createFromParcel(Parcel source) {
            return new MaterialResponse(source);
        }

        @Override
        public MaterialResponse[] newArray(int size) {
            return new MaterialResponse[size];
        }
    };
}
