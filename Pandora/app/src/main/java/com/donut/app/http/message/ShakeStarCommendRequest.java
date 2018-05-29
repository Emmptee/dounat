package com.donut.app.http.message;

/**
 * Created by hard on 2018/1/30.
 */

public class ShakeStarCommendRequest {

  private int sortType; //类型
  private int page;//页数
  private int rows;//每页的数量

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getSortType() {
        return sortType;
    }

    public int getPage() {
        return page;
    }

    public int getRows() {
        return rows;
    }
}
