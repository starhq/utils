package com.star.extra;

import com.star.string.StringUtil;

/**
 * 一些电话工具类
 *
 * @author starhq
 */
public class PhoneUtil {

    private PhoneUtil() {
    }

    /**
     * 把手机叫转成139****1234
     *
     * @param str 原手机号
     * @return 处理过的手机号
     */
    public static String getPhoneNumber(final String str) {
        String result = str;
        if (StringUtil.isBlank(result)) {
            result = result.trim();
            result = result.substring(0, 3) + "****" + result.substring(7, str.length());
        }
        return result;
    }
}
