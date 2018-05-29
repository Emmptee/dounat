package com.donut.app.http.message;

import java.util.List;

public class SubjectListResponse extends BaseResponse
{
	private List<SubjectListDetail> subjectList ;
	
    public SubjectListResponse()
    {

    }

	public List<SubjectListDetail> getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(List<SubjectListDetail> subjectList) {
		this.subjectList = subjectList;
	}

}
