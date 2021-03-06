package com.donut.app.model.galleypick.entities;

/**
 * FolderEntity
 * Created by Qi on 2017/2/28.
 */

public class FolderEntity {
    private String folderName;

    private int count;

    private String firstPath;

    public FolderEntity(String folderName, int count, String firstPath){
        this.folderName = folderName;
        this.count = count;
        this.firstPath = firstPath;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFirstPath() {
        return firstPath;
    }

    public void setFirstPath(String firstPath) {
        this.firstPath = firstPath;
    }
}
