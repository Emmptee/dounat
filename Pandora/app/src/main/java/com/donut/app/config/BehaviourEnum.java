package com.donut.app.config;

import com.donut.app.utils.AppConfigUtil;

/**
 * Created by Administrator on 2016/8/1.
 */
public enum BehaviourEnum {

    LOGIN("000"),
    REGISTER("001"),
    HOT("005"),
    SUBJECT_DETAIL("007"),
    COMMENT("008"),
    COMMENT_DETAIL("009"),
    CHALLENGE_DETAIL("020"),
    CHALLENGE_RULE("021"),
    FINAL_PK_DETAIL("023"),
    FORGOT_PASS("003"),
    BANDING_PHONE("004"),
    SUBJECT("006"),
    REWARD("010"),
    STAR_DETAIL("017"),
    CHALLENGE_SEND("019"),
    CHALLENGE("022"),
    IP_CONTENT("028"),
    STARS("029"),
    IP_CONTENT_TERM("030"),
    SETTINGS("035"),
    UPDATE_PASS("037"),
    REWARD_INCOME("041"),
    CASH_PRESENT("042"),
    PRESENT_ADD("043"),
    VIP("050"),
    RECHARGE("051"),
    MY_ORDER("053"),
    ORDER_DETAIL("054"),
    STAR_ZONE("024"),
    IP_LIST("025"),
    WISHING("026"),
    WISH_DETAIL("027"),
    IP_DETAIL("027"),
    MINE("031"),
    PERSONAL_INFO("036"),
    MY_ADDRESS("038"),
    ADDRESS_ADD("039"),
    ADDRESS_EDIT("040"),
    MESSAGE("044"),
    ADVICE("046"),
    ABOUT_APP("047"),
    RECOMMAND_FRIENDS("048"),
    MY_CHALLENGE("049"),
    MY_IP("052"),
    MY_COLLECT("055"),
    COMMENT_ABOUT_ME("056"),
    MY_COMMENT_DETAIL("057"),
    STAR_RECOMMAND("062"),
    IP_TOP("063"),
    HEAD_MODIFY("064"),
    NICKNAME_MODIFY("065"),
    SEX_MODIFY("066"),
    CITY_MODIFY("067"),
    AGE_MODIFY("068"),
    SIGN_MODIFY("069"),
    PROFESSIONAL_MODIFY("070"),
    PERSONAL_BING_PHONE("045"),
    MY_ATTENTION("071"),
    SEARCH_ATTENTION("072"),
    MODIFY_BING_PHONE("073"),
    NOTICE_ACTION("028"),
    CHANNEL_LIST("075"),
    SUBJECT_SNAP_LIST("076"),
    SUBJECT_SNAP_DETAIL("077"),
    SUBJECT_NOTICE_LIST("078"),
    SUBJECT_NOTICE_DETAIL("079"),
    STAR_SEND_NOTICE("080"),
    MY_AUCTION_LIST("081"),
    SHAKE_STAR_PREVIEW("082");

    BehaviourEnum(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return AppConfigUtil.getBehaviourHeader() + code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
