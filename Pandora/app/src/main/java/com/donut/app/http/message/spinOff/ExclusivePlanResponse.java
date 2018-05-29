package com.donut.app.http.message.spinOff;

import com.donut.app.http.message.BaseResponse;

import java.util.List;


public class ExclusivePlanResponse extends BaseResponse
{
	private List<ExclusivePlan> exclusivePlan;

	public List<ExclusivePlan> getExclusivePlan() {
		return exclusivePlan;
	}

	public void setExclusivePlan(List<ExclusivePlan> exclusivePlan) {
		this.exclusivePlan = exclusivePlan;
	}
}