package com.donut.app.utils;

import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatCheckUtil {

    public static boolean isMobileNumber(String mobiles) {
        /** 手机号正则 */
        String mobPhoneNumRE = "^((13[0-9])|(14[5,7])|(15[^4,\\D])|(18[0-9])|(17[6,7,8]))\\d{8}$";
        Pattern p = Pattern.compile(mobPhoneNumRE);
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 是否是座机号
     *
     * @param tel
     * @return
     */
    public static boolean isTelephone(String tel) {
        String REG_EXP = "(\\d{3}-\\d{7})|(\\d{3}-\\d{8})|(\\d{4}-\\d{7})|(\\d{4}-\\d{8})";
        Pattern p = Pattern.compile(REG_EXP);
        Matcher m = p.matcher(tel);
        return m.matches();
    }

    /**
     * 是否是有效的联系电话
     *
     * @param number
     * @return
     */
    public static boolean isEffectiveContactPhone(String number) {
        return isMobileNumber(number) || isTelephone(number);
    }

    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    /**
     * 数字校验
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {

        Pattern pattern = Pattern.compile("[0-9]*");

        return pattern.matcher(str).matches();

    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param bankNo
     */
    public static boolean checkBankCard(String bankNo) {

        String firstNum = bankNo.substring(0, 2);
        if (!"62".equals(firstNum)) {
            //校验,只支持银联卡!
            return false;
        }

        int lastNum = Integer.parseInt(bankNo.substring(bankNo.length() - 1,
                bankNo.length()));// 取出最后一位（与luhm进行比较）

        String first15Num = bankNo.substring(0, bankNo.length() - 1);// 前15或18位
        // System.out.println(first15Num);
        char[] newArr = new char[first15Num.length()]; // 倒叙装入newArr
        char[] tempArr = first15Num.toCharArray();
        for (int i = 0; i < tempArr.length; i++) {
            newArr[tempArr.length - 1 - i] = tempArr[i];
        }

        int[] arrSingleNum = new int[newArr.length]; // 奇数位*2的积 <9
        int[] arrSingleNum2 = new int[newArr.length];// 奇数位*2的积 >9
        int[] arrDoubleNum = new int[newArr.length]; // 偶数位数组

        for (int j = 0; j < newArr.length; j++) {
            if ((j + 1) % 2 == 1) {// 奇数位
                if ((int) (newArr[j] - 48) * 2 < 9) {
                    arrSingleNum[j] = (int) (newArr[j] - 48) * 2;
                } else {
                    arrSingleNum2[j] = (int) (newArr[j] - 48) * 2;
                }
            } else
                // 偶数位
            {
                arrDoubleNum[j] = (int) (newArr[j] - 48);
            }
        }

        int[] arrSingleNumChild = new int[newArr.length]; // 奇数位*2 >9
        // 的分割之后的数组个位数
        int[] arrSingleNum2Child = new int[newArr.length];// 奇数位*2 >9
        // 的分割之后的数组十位数

        for (int h = 0; h < arrSingleNum2.length; h++) {
            arrSingleNumChild[h] = (arrSingleNum2[h]) % 10;
            arrSingleNum2Child[h] = (arrSingleNum2[h]) / 10;
        }

        int sumSingleNum = 0; // 奇数位*2 < 9 的数组之和
        int sumDoubleNum = 0; // 偶数位数组之和
        int sumSingleNumChild = 0; // 奇数位*2 >9 的分割之后的数组个位数之和
        int sumSingleNum2Child = 0; // 奇数位*2 >9 的分割之后的数组十位数之和
        int sumTotal = 0;
        for (int m = 0; m < arrSingleNum.length; m++) {
            sumSingleNum = sumSingleNum + arrSingleNum[m];
        }

        for (int n = 0; n < arrDoubleNum.length; n++) {
            sumDoubleNum = sumDoubleNum + arrDoubleNum[n];
        }

        for (int p = 0; p < arrSingleNumChild.length; p++) {
            sumSingleNumChild = sumSingleNumChild + arrSingleNumChild[p];
            sumSingleNum2Child = sumSingleNum2Child + arrSingleNum2Child[p];
        }

        sumTotal = sumSingleNum + sumDoubleNum + sumSingleNumChild
                + sumSingleNum2Child;

        // 计算Luhm值
        int k = sumTotal % 10 == 0 ? 10 : sumTotal % 10;
        int luhm = 10 - k;

        if (lastNum == luhm) {
            return true;
        } else if (bankNo.length() < 16 || bankNo.length() > 19) {
            return false;
        } else {
            return false;
        }
    }

    public static String getDataSize(long size) {
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024L) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "GB";
        } else {
            return "size: error";
        }
    }
}
