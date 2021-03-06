package com.donut.app.http.message;

public class SubjectHistoryPKCommentDetail
{
	
	private String commentId ;
	
	private String content ;
	
	private String nickName;
	
	public SubjectHistoryPKCommentDetail(){};

	public SubjectHistoryPKCommentDetail(String commentId, String content, String nickName) {
		super();
		this.commentId = commentId;
		this.content = content;
		this.nickName = nickName;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
