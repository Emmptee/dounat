package com.donut.app.http;

import com.donut.app.http.message.BaseRequest;

/**
 * Created by hard on 2018/2/9.
 */

public class CommendAllRequest extends BaseRequest{
    private String contentId;
    private String currentUserId;
    private int  rows;
    private int page;
    private int subPage;
    private int subRows;


    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSubPage() {
        return subPage;
    }

    public void setSubPage(int subPage) {
        this.subPage = subPage;
    }

    public int getSubRows() {
        return subRows;
    }

    public void setSubRows(int subRows) {
        this.subRows = subRows;
    }
}
