package com.donut.app.http.message.home;

import java.util.List;

/**
 * @author Qi
 * @date 2018/04/04
 */
public class ContentCategory {

    private String categoryName;

    private Integer categoryType;

    private Integer categoryId;

    private List<ContentItem> itemList;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public List<ContentItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ContentItem> itemList) {
        this.itemList = itemList;
    }


    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
