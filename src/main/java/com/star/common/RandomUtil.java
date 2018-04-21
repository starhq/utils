package com.star.common;

import com.star.exception.ToolException;
import com.star.string.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机串工具类
 * Created by starhq on 2017/10/13
 */
public final class RandomUtil {

    /**
     * 用于随机选的数字
     */
    private static final String NUMBER = "0123456789";
    /**
     * 用于随机选的字符
     */
    private static final String LETTER = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * 用于随机选的字符和数字
     */
    private static final String ALL = NUMBER + LETTER;

    /**
     * 初始化random
     */
    private final static ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private RandomUtil() {
    }

    /**
     * 生成指定范围的随机数
     *
     * @param min 开始
     * @param max 结束
     * @return 随机数
     */
    public static int randomInt(final int min, final int max) {
        return RANDOM.nextInt(max - min) + min;
    }

    /**
     * 获得随机数
     *
     * @return 随机数
     */
    public static int randomInt() {
        return RANDOM.nextInt();
    }

    /**
     * 获得0到limit范围的随机数
     *
     * @param limit 最大值不包括
     * @return 随机数
     */
    public static int randomInt(final int limit) {
        return RANDOM.nextInt(limit);
    }

    /**
     * 获取定长的字节数组
     *
     * @param length 数组长度
     * @return 字节数组
     */
    public static byte[] randomBytes(final int length) {
        final byte[] bytes = new byte[length];
        RANDOM.nextBytes(bytes);
        return bytes;
    }

    /**
     * 获取列表随机的元素
     *
     * @param list  列表
     * @param limit 指定范围
     * @param <T>   泛型
     * @return 随机的元素
     */
    public static <T> T randomEle(final List<T> list, final int limit) {
        final int length = list.size();
        if (limit > length) {
            throw new ToolException("limit is greater than list size");
        }
        return list.get(randomInt(limit));
    }

    /**
     * 随机获得列表中的元素
     *
     * @param <T>  元素类型
     * @param list 列表
     * @return 随机元素
     */
    public static <T> T randomEle(final List<T> list) {
        return randomEle(list, list.size());
    }

    /**
     * 随机获取列表中的count个元素
     *
     * @param list  列表
     * @param count 个数
     * @param <T>   泛型
     * @return 元素列表
     */
    public static <T> List<T> randomEles(final List<T> list, final int count) {
        final List<T> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(randomEle(list));
        }
        return result;
    }

    /**
     * 获得一个随机的字符串（只包含数字和字符）
     *
     * @param length 定长
     * @return 数字和字符串
     */
    public static String randomAll(final int length) {
        return randomString(ALL, length);
    }

    /**
     * 获得一个随机的字符串（只包含数字）
     *
     * @param length 定长
     * @return 数字串
     */
    public static String randomNumbers(final int length) {
        return randomString(NUMBER, length);
    }

    /**
     * 获得定长的随机字母串
     *
     * @param length 定长
     * @return 字母串
     */
    public static String randomLetters(final int length) {
        return randomString(LETTER, length);
    }

    /**
     * 获得一个随机字符串
     *
     * @param baseString 在这个字符串里选择
     * @param length     指定长度
     * @return 随机字符串
     */
    public static String randomString(final String baseString, final int length) {
        final StringBuilder builder = StringUtil.builder(length);
        final int tmp = length < 1 ? 1 : length;
        final int baseLength = baseString.length();
        for (int i = 0; i < tmp; i++) {
            builder.append(baseString.charAt(randomInt(baseLength)));
        }

        return builder.toString();
    }
}
