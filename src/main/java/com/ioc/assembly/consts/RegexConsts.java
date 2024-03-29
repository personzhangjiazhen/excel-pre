package com.ioc.assembly.consts;

/**
 * 校验正则表达式静态类
 */
public class RegexConsts {



    /** 校验为数字的正则表达式*/
    public static final String REG_NUMBER = "[0-9]*\\.?[0-9]+";

    /** 校验邮箱的正则表达式*/
    public static final String REG_EMAIL = "[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+";

    /** 校验手机号的正则表达式*/
    public static final String REG_PHONE = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\\\d{8}$";

    /** 校验IP的正则表达式*/
    public static final String REG_IP = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

    /** 校验身份证号表达式*/
    public static final String REG_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

    /** 校验日期正则表达式 仅支持yyyy-MM-dd HH:mm:ss和yyyy-MM-dd 两种格式 */
    public static final String REG_DATE = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1][0-9])|([2][0-4]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
}
