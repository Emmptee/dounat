package com.donut.app.http.message;

public class MyCollectionRequest
{
    private Integer page;

    private Integer rows;
    
    private Integer selectType;

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

	public Integer getSelectType() {
		return selectType;
	}

	public void setSelectType(Integer selectType) {
		this.selectType = selectType;
	}

    
    
    
}