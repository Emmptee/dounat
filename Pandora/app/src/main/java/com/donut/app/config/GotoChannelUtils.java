package com.donut.app.config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.donut.app.http.message.AppChannel;
import com.donut.app.mvp.channel.list.ChannelListActivity;
import com.donut.app.mvp.channel.list2.ChannelList2Activity;
import com.donut.app.mvp.subject.notice.SubjectNoticeActivity;
import com.donut.app.mvp.subject.snap.SubjectSnapActivity;
import com.donut.app.mvp.subject.special.SubjectSpecialActivity;
import com.donut.app.utils.ToastUtil;

/**
 * Created by Qi on 2017/3/24.
 * Description : <br>
 *
 *         0:有本事你来模式
 1:街拍街拍模式
 2:大咖有通告
 4:心愿
 6:花絮
 89:福利区
 10:独家策划
 11:素材
 */
public class GotoChannelUtils {

    public static final int CHANNEL_TYPE_SUBJECT = 0, CHANNEL_TYPE_SNAP = 1, CHANNEL_TYPE_NOTICE = 2;



    public static void GotoSubjectDetail(Activity activity,
                                         int channelType,
                                         String subjectId,
                                         int requestCode) {
        if (channelType == CHANNEL_TYPE_SUBJECT) {
            //有本事你来
            Intent intent = new Intent(activity, SubjectSpecialActivity.class);
            intent.putExtra(SubjectSpecialActivity.SUBJECT_ID, subjectId);
            activity.startActivityForResult(intent, requestCode);
        } else if (channelType == CHANNEL_TYPE_SNAP) {
            //街拍街拍
            Intent intent = new Intent(activity, SubjectSnapActivity.class);
            intent.putExtra(SubjectSnapActivity.SUBJECT_ID, subjectId);
            activity.startActivityForResult(intent, requestCode);
        } else if (channelType == CHANNEL_TYPE_NOTICE) {
            //大咖有通告
            Intent intent = new Intent(activity, SubjectNoticeActivity.class);
            intent.putExtra(SubjectNoticeActivity.SUBJECT_ID, subjectId);
            activity.startActivityForResult(intent, requestCode);
        } else {
            ToastUtil.showShort(activity, Constant.DEFAULT_TIPS_MSG);
        }
    }

    public static void GotoSubjectDetail(Fragment fragment,
                                         int channelType,
                                         String subjectId,
                                         int requestCode) {
        if (channelType == CHANNEL_TYPE_SUBJECT) {
            //有本事你来
            Intent intent = new Intent(fragment.getContext(), SubjectSpecialActivity.class);
            intent.putExtra(SubjectSpecialActivity.SUBJECT_ID, subjectId);
            fragment.startActivityForResult(intent, requestCode);
        } else if (channelType == CHANNEL_TYPE_SNAP) {
            //街拍街拍
            Intent intent = new Intent(fragment.getContext(), SubjectSnapActivity.class);
            intent.putExtra(SubjectSnapActivity.SUBJECT_ID, subjectId);
            fragment.startActivityForResult(intent, requestCode);
        } else if (channelType == CHANNEL_TYPE_NOTICE) {
            //大咖有通告
            Intent intent = new Intent(fragment.getContext(), SubjectNoticeActivity.class);
            intent.putExtra(SubjectNoticeActivity.SUBJECT_ID, subjectId);
            fragment.startActivityForResult(intent, requestCode);
        } else {
            ToastUtil.showShort(fragment.getContext(), Constant.DEFAULT_TIPS_MSG);
        }
    }

    public static void GotoSubjectDetail(Context context,
                                         int channelType,
                                         String subjectId) {
        if (channelType == CHANNEL_TYPE_SUBJECT) {
            //有本事你来
            Intent intent = new Intent(context, SubjectSpecialActivity.class);
            intent.putExtra(SubjectSpecialActivity.SUBJECT_ID, subjectId);
            context.startActivity(intent);
        } else if (channelType == CHANNEL_TYPE_SNAP) {
            //街拍街拍
            Intent intent = new Intent(context, SubjectSnapActivity.class);
            intent.putExtra(SubjectSnapActivity.SUBJECT_ID, subjectId);
            context.startActivity(intent);
        } else if (channelType == CHANNEL_TYPE_NOTICE) {
            //大咖有通告
            Intent intent = new Intent(context, SubjectNoticeActivity.class);
            intent.putExtra(SubjectNoticeActivity.SUBJECT_ID, subjectId);
            context.startActivity(intent);
        } else {
            ToastUtil.showShort(context, Constant.DEFAULT_TIPS_MSG);
        }
    }

    public static void GotoList(Fragment fragment, AppChannel channel, int requestCode) {
        if (channel.getType() == CHANNEL_TYPE_SUBJECT || channel.getType() == CHANNEL_TYPE_SNAP) {
            Intent intent = new Intent(fragment.getContext(), ChannelListActivity.class);
            intent.putExtra(ChannelListActivity.CHANNEL_ID, channel.getUuid());
            intent.putExtra(ChannelListActivity.CHANNEL_TYPE, channel.getType());
            intent.putExtra(ChannelListActivity.CHANNEL_NAME, channel.getName());
            fragment.startActivityForResult(intent, requestCode);
        } else if (channel.getType() == CHANNEL_TYPE_NOTICE) {
            Intent intent = new Intent(fragment.getContext(), ChannelList2Activity.class);
            intent.putExtra(ChannelList2Activity.CHANNEL_ID, channel.getUuid());
            intent.putExtra(ChannelList2Activity.CHANNEL_TYPE, channel.getType());
            intent.putExtra(ChannelList2Activity.CHANNEL_NAME, channel.getName());
            fragment.startActivityForResult(intent, requestCode);
        } else {
            ToastUtil.showShort(fragment.getContext(), channel.getContent());
        }
    }
}
