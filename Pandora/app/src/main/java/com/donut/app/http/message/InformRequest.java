package com.donut.app.http.message;

import com.tencent.mm.opensdk.modelbase.BaseReq;

/**
 * Created by hard on 2018/3/2.
 */

public class InformRequest extends BaseRequest {

    private String fkB02;

    public String getFkB02() {
        return fkB02;
    }

    public void setFkB02(String fkB02) {
        this.fkB02 = fkB02;
    }
}