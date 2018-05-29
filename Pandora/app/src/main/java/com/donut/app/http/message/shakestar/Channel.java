package com.donut.app.http.message.shakestar;

import com.donut.app.http.message.BaseResponse;

import java.util.List;

/**
 * Created by hard on 2018/2/28.
 */

public class Channel extends BaseResponse {


    private List<MenuListBean> menuList;

    public List<MenuListBean> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuListBean> menuList) {
        this.menuList = menuList;
    }

    public static class MenuListBean {
        /**
         * uuid : 2
         * title : 街拍街拍
         * description : 街拍街拍你最帅
         * typeA : 1
         * typeB : 1
         * recommendStatus : 1
         * jumpStatus : 1
         * skipId :
         */

        private String uuid;
        private String title;
        private String description;
        private int typeA;
        private int typeB;
        private int recommendStatus;
        private int jumpStatus;
        private String skipId;
        private String typeName;
        private String logoUrl;

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getTypeA() {
            return typeA;
        }

        public void setTypeA(int typeA) {
            this.typeA = typeA;
        }

        public int getTypeB() {
            return typeB;
        }

        public void setTypeB(int typeB) {
            this.typeB = typeB;
        }

        public int getRecommendStatus() {
            return recommendStatus;
        }

        public void setRecommendStatus(int recommendStatus) {
            this.recommendStatus = recommendStatus;
        }

        public int getJumpStatus() {
            return jumpStatus;
        }

        public void setJumpStatus(int jumpStatus) {
            this.jumpStatus = jumpStatus;
        }

        public String getSkipId() {
            return skipId;
        }

        public void setSkipId(String skipId) {
            this.skipId = skipId;
        }
    }
}
