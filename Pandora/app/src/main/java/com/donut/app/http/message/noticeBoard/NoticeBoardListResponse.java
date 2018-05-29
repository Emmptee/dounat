package com.donut.app.http.message.noticeBoard;

import com.donut.app.http.message.BaseResponse;

import java.util.List;

public class NoticeBoardListResponse extends BaseResponse
{
    private List<NoticeBoardListModel> noticeBoardList;

    /**
     * @return noticeBoardList
     */
    public List<NoticeBoardListModel> getNoticeBoardList()
    {
        return noticeBoardList;
    }

    /**
     * @param noticeBoardList
     *            the noticeBoardList to set
     */
    public void setNoticeBoardList(List<NoticeBoardListModel> noticeBoardList)
    {
        this.noticeBoardList = noticeBoardList;
    }

}
