package com.donut.app.config;

import java.io.File;

/**
 * 常量
 * 
 */
public class Constant
{
    public static final String PACKAGE_NAME = "com.donut.app";

    public static final String APP_PATH = File.separator + "donut"
            + File.separator;

    public static final String DEFAULT_DBNAME = "donut.db";

    public static final int DEFAULT_DB_VERSION = 2;

    public static final String SP_INFO = "sp_info";

    public static final String SP_CONFIG = "SP_CONFIG";

    public static final String USER_ACCOUNT = "user_account";

    public static final String THIRD_LOGIN_TAG = "third_login_tag";

    public static final String PUSH_USER_ID = "push_user_id";

    public static final String PUSH_CHANNEL_ID = "push_channel_id";

    public static final String NOTICE_STATE = "notice_state";

    public static final String IS_LOGIN = "is_login";

    public static final String NOW_VERSION_CODE = "NOW_VERSION_CODE";

    public static final String RATIO = "RATIO";

    public static final String RECHARGR_RATIO = "RECHARGE_RATIO";

    public static final String COMMENT_PRICE = "COMMENT_PRICE";

    public static final String VIP_TIME = "vip_time";

    public static final String DEVICE = "device";

    public static final String SUBJECT_FIRST = "subject_first";

    public static final String IP_FIRST = "ip_first";

    public static final String HAS_NEW_MSG = "HAS_NEW_MSG";

    public static final String COLLECT_STATUS = "COLLECT_STATUS";

    /**
     * 明星:1 普通用户:0
     */
    public static final String USER_TYPE = "USER_TYPE";
    public static final int USER_TYPE_ACTOR = 0;
    public static final int USER_TYPE_STAR = 1;

    public static final String ONLY_ID = "only_id";

    public static final String NOTICE_FLAG = "notice_flag";

    /**
     * format 格式
     */
    public static final String FORMAT_TIME = "mm:ss.SS";

    public static final int IMAGE_TYPE_JPEG = 1;

    public static final int IMAGE_TYPE_PNG = 2;

    public static final int default_bar_color = 0xFF81D8D0;
    public static final int white_bar_color = 0xFFFFFF;
    public static final int alpha = 0x26;

    /**
     * 三方key值
     */

    public static final String QQ_APPID = "1105771256";

    public static final String QQ_APPKEY = "SJFisalGEmmbkNUA";

    public static final String WEIXIN_APPID = "wx605a0b076c92450b";

    public static final String WEIXIN_SECRET = "ef28f57ba89a286e7bceddf4ef3aec82";

    public static final String SINA_APPID = "931794203";

    public static final String SINA_SECRET = "fb1a604bae375aac8056dddd684975e1";

    public static final String SMSSDK_APPKEY = "15703b619039b";

    public static final String SMSSDK_APPSECRET = "e7d6690363e7a5104b0ced2055c8da8e";

    public static final int FLING_MIN_DISTANCE = 150;
    public static final int FLING_MIN_VELOCITY = 40;

    public static final int LENGTH_16=16;
    public static final int LENGTH_32=32;
    public static final int LENGTH_64=64;
    public static final int LENGTH_512=512;

    public static String DEFAULT_TIPS_MSG = "请升级最新版本后体验此功能哦";
}
