package com.donut.app.entity;

/**
 * Created by Qi on 2017/3/22.
 * Description : <br>
 */
public class ConfigInfo {
    private long id;

    private String name;

    private String value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
