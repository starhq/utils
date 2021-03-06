package com.star.regex;

import com.star.string.StringUtil;
import com.star.time.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 验证器
 *
 * @author starhq
 */
public final class Validator {

    /**
     * 单词数字下划线
     */
    public final static String GENERAL = "^\\w+$";
    /**
     * 数字
     */
    public final static String NUMBERS = "\\d+";
    /**
     * 分组
     */
    public final static String GROUP_VAR = "\\$(\\d+)";
    /**
     * IP v4
     */
    public final static String IPV4 = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)" +
            "\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
    /**
     * IP v6
     */
    public final static String IPV6 = "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1," +
            "4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1," +
            "2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff" +
            "(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|" +
            "(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))";
    /**
     * 货币
     */
    public final static String MONEY = "^(\\d+(?:\\.\\d+)?)$";
    /**
     * 邮件
     */
    public final static String EMAIL = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
    /**
     * 移动电话
     */
    public final static String MOBILE = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
    /**
     * 身份证号码
     */
    public final static String ID18 = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$";
    /**
     * 身份证号码
     */
    public final static String ID15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    /**
     * 邮编
     */
    public final static String ZIP_CODE = "[1-9]\\d{5}(?!\\d)";
    /**
     * UUID
     */
    public final static String UUID = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$";
    /**
     * 不带横线的UUID
     */
    public final static String UUID_SIMPLE = "^[0-9a-z]{32}$";
    /**
     * URL
     */
    public final static String URL = "^((f|ht){1}(tp|tps):\\/\\/)?([\\w-]+\\.)+[\\w-]+(\\/[\\w- ./?%&=]*)?";
    /**
     * 中文
     */
    public final static String CHINESE = "[\u4e00-\u9fa5]";
    /**
     * QQ
     */
    public final static String QQ = "[1-9][0-9]{4,}";
    /**
     * 座机
     */
    public final static String PHONE = "\\d{3}-\\d{8}|\\d{4}-\\{7,8}";
    /**
     * 年月日
     */
    public final static String YMD = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|(" +
            "(0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))";
    /**
     * 时分|秒,和年月日结合中间需要价格/s
     */
    public final static String HMS = "(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d)";
    /**
     * 密码健壮
     */
    public final static String PASSWORDSTRONG = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$";
    /**
     * 月
     */
    public final static String MONTH = "^(0?[1-9]|1[0-2])$";
    /**
     * 天
     */
    public final static String DAY = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
    /**
     * 整数
     */
    public final static String INTEGER = "^[-\\+]?[\\d]+$";
    /**
     * 浮点数
     */
    public final static String DOBULE = "^[-\\+]?\\d+\\.\\d+$";

    private Validator() {
    }


    /**
     * 字符串和正则是否匹配
     *
     * @param regex 正则
     * @param value 内容
     * @return 是否匹配
     */
    public static boolean isMactchRegex(final String regex, final String value) {
        return StringUtil.isBlank(regex) || (!StringUtil.isBlank(value) && value.matches(regex));
    }

    /**
     * 验证是否为生日<br>
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 是否为生日
     */
    public static boolean isBirthday(int year, int month, int day) {
        // 验证年
        int thisYear = DateTimeUtil.getYear(LocalDateTime.now());
        if (year < 1930 || year > thisYear) {
            return false;
        }

        // 验证月
        if (month < 1 || month > 12) {
            return false;
        }

        // 验证日
        if (day < 1 || day > 31) {
            return false;
        }
        if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
            return false;
        }
        if (month == 2) {
            return day <= 29 && (day != 29 || LocalDate.of(year, month, day).isLeapYear());
        }
        return true;
    }

}
