package com.donut.app.http.message.spinOff;

public class ExclusivePlanRequest {
	
	private String searchStarName;
	
	private Integer page;

    private Integer rows;

	public String getSearchStarName() {
		return searchStarName;
	}

	public void setSearchStarName(String searchStarName) {
		this.searchStarName = searchStarName;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}
    
    
}
