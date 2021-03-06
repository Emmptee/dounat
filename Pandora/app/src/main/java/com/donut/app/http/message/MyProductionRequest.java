package com.donut.app.http.message;

/**
 * Created by hard on 2018/2/12.
 */

public class MyProductionRequest extends BaseRequest {

    private String selectedUserId;
    private int page;
    private int rows;

    public String getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(String selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
