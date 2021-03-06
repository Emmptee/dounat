package com.donut.app.http.message;

import java.util.List;

/**
 * Created by Qi on 2017/3/22.
 * Description : <br>
 */
public class ConfigItemResponse extends BaseResponse {

    private List<ConfigItem> configList;

    public List<ConfigItem> getConfigList() {
        return configList;
    }

    public void setConfigList(List<ConfigItem> configList) {
        this.configList = configList;
    }

    public class ConfigItem {
        private String name;

        private String value;

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
}


