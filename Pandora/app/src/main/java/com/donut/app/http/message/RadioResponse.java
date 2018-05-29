package com.donut.app.http.message;

import java.util.List;

public class RadioResponse extends BaseResponse
{
	private List<SubjectHistoryPKStarComment>  audioList;

	public List<SubjectHistoryPKStarComment> getAudioList() {
		return audioList;
	}

	public void setAudioList(List<SubjectHistoryPKStarComment> audioList) {
		this.audioList = audioList;
	}
}
