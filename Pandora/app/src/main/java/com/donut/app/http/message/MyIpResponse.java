package com.donut.app.http.message;

import java.util.List;


public class MyIpResponse extends BaseResponse
{

    private List<MyIp> ipList;

	public List<MyIp> getIpList() {
		return ipList;
	}
	public void setIpList(List<MyIp> ipList) {
		this.ipList = ipList;
	}

}
