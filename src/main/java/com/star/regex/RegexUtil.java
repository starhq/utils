package com.star.regex;

import com.star.collection.array.ArrayUtil;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 *
 * @author starhq
 */

public final class RegexUtil {

    /**
     * 正则中需要被转义的关键字
     */
    public final static Character[] CHARS = {'$', '(', ')', '*', '+', '.', '[', ']', '?', '\\', '^', '{', '}', '|'};


    private RegexUtil() {

    }

    /**
     * 获得匹配的字符串
     */
    public static String get(final Pattern pattern, final String content, final int groupIndex) {
        final Matcher matcher = pattern.matcher(content);
        return matcher.find() ? matcher.group(groupIndex) : "";
    }

    /**
     * 删除正则匹配到的内容之前的字符 如果没有找到，则返回原文
     */
    public static String delPre(final Pattern pattern, final String content) {
        final Matcher matcher = pattern.matcher(content);
        return matcher.find() ? content.substring(matcher.end(), content.length()) : content;
    }

    /**
     * 给定内容是否匹配正则
     */
    public static boolean isMatch(final Pattern pattern, final String content) {
        return pattern.matcher(content).matches();
    }

    /**
     * 表达式中提取信息到集合中
     */
    public static <T extends Collection<String>> T findAll(final Pattern pattern, final String content, final int group,
                                                           final T collections) {
        final Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            collections.add(matcher.group(group));
        }
        return collections;
    }

    /**
     * 转义字符串，将正则的关键字转义
     */
    public static String escape(final String content) {
        final StringBuilder builder = new StringBuilder();
        char current;
        for (int i = 0; i < content.length(); i++) {
            current = content.charAt(i);
            if (ArrayUtil.contains(CHARS, current)) {
                builder.append('\\');
            }
            builder.append(current);
        }
        return builder.toString();
    }


}
