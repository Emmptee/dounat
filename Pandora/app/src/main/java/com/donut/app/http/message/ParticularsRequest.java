package com.donut.app.http.message;

/**
 * Created by hard on 2018/2/6.
 */

public class ParticularsRequest {

    private String g03Id;
    private String b02Id;
    private int page;
    private int rows;
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
