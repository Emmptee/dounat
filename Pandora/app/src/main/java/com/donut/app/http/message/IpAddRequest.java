package com.donut.app.http.message;

import java.util.List;

public class IpAddRequest 
{
	private String ipId;

	private String name;
	
	private String description;
	
	private String imgUrl;
	
	private String playUrl;
	
	private Long lastTime;
	
	private List<String> starsList;

	public String getIpId() {
		return ipId;
	}

	public void setIpId(String ipId) {
		this.ipId = ipId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public List<String> getStarsList() {
		return starsList;
	}

	public void setStarsList(List<String> starsList) {
		this.starsList = starsList;
	}

	public Long getLastTime() {
		return lastTime;
	}

	public void setLastTime(Long lastTime) {
		this.lastTime = lastTime;
	}
}
