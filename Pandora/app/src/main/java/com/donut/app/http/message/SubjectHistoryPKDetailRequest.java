package com.donut.app.http.message;

public class SubjectHistoryPKDetailRequest 
{

	private String contentId;

	private int commentRows = 5;

	public int getCommentRows() {
		return commentRows;
	}

	public void setCommentRows(int commentRows) {
		this.commentRows = commentRows;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

}
