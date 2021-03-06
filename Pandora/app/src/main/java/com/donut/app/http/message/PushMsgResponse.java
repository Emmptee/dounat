package com.donut.app.http.message;

/**
 * @ClassName: PushMsgResponse
 * @Description: 消息推送Response
 * @author wujiaojiao
 * @date 2015-10-15 下午3:43:25
 * @version 1.0
 */
public class PushMsgResponse extends BaseResponse
{

    /** 0：开 ；1：关 */
    private String pushStatus;

    /**
     * @return pushStatus
     */
    public String getPushStatus()
    {
        return pushStatus;
    }

    /**
     * @param pushStatus
     *            the pushStatus to set
     */
    public void setPushStatus(String pushStatus)
    {
        this.pushStatus = pushStatus;
    }

}
