package com.donut.app.http.message.spinOff;

import com.donut.app.http.message.BaseResponse;

import java.util.List;

public class WelfareZoneResponse extends BaseResponse {
    private List<WelfareZone> welfareZone;

    public List<WelfareZone> getWelfareZone() {
        return welfareZone;
    }

    public void setWelfareZone(List<WelfareZone> welfareZone) {
        this.welfareZone = welfareZone;
    }


}
