package com.donut.app.mvp.shakestar.video.SourceVideoDownload;

import android.util.Log;


import com.donut.app.SysApplication;
import com.donut.app.mvp.shakestar.greendaobase.UserDao;

import java.util.ArrayList;
import java.util.List;


public class DownLoadSqlTool {
    /**
     * 创建下载的具体信息
     */
    public void insertInfos(List<DownLoadInfo> infos) {
        for (DownLoadInfo info : infos) {
            User user = new User(null, info.getThreadId(), info.getStartPos(), info.getEndPos(), info.getCompeleteSize(), info.getUrl());
            SysApplication.userDao.insert(user);
        }
    }

    /**
     * 得到下载具体信息
     */
    public List<DownLoadInfo> getInfos(String urlstr) {
        List<DownLoadInfo> list = new ArrayList<DownLoadInfo>();
        List<User> list1 = SysApplication.userDao.queryBuilder().where(UserDao.Properties.Url.eq(urlstr)).build().list();
        for (User user : list1) {
            DownLoadInfo infoss = new DownLoadInfo(
                    user.getThread_id(), user.getStart_pos(), user.getEnd_pos(),
                    user.getCompelete_size(), user.getUrl());
            Log.d("main-----", infoss.toString());
            list.add(infoss);
        }

        return list;
    }

    /**
     * 更新数据库中的下载信息
     */
    public void updataInfos(int threadId, int compeleteSize, String urlstr) {
        User user = SysApplication.userDao.queryBuilder()
                .where(UserDao.Properties.Thread_id.eq(threadId), UserDao.Properties.Url.eq(urlstr)).build().unique();
        user.setCompelete_size(compeleteSize);
        SysApplication.userDao.update(user);
    }

    /**
     * 下载完成后删除数据库中的数据
     */
    public void delete(String url) {
        SysApplication.userDao.deleteAll();
    }

}