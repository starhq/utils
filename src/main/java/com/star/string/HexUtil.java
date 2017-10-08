package com.star.string;

import com.star.collection.ArrayUtil;
import com.star.exception.ToolException;
import com.star.lang.Assert;

import java.util.Objects;

/**
 * 16进制转换
 * <p>
 * Created by win7 on 2017/5/28.
 */
public final class HexUtil {

    /**
     * 用于建立十六进制字符的输出的小写字符数组
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    /**
     * 用于建立十六进制字符的输出的大写字符数组
     */
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F'};

    private HexUtil() {
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data        byte[]
     * @param toLowerCase 是否转换大小写
     * @return 十六进制char[]
     */
    public static char[] encode(final byte[] data, final boolean toLowerCase) {
        return encode(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data        byte[]
     * @param toLowerCase 是否转换大小写
     * @return 十六进制char[]
     */
    public static String encodeToString(final byte[] data, final boolean toLowerCase) {
        return encodeHexStr(data, toLowerCase ? DIGITS_UPPER : DIGITS_LOWER);
    }

    /**
     * 将16进制字符串转为字节数组
     *
     * @param hex 16进制字符串
     * @return 字节数组
     */
    public static byte[] decode(final String hex) {
        return StringUtil.isBlank(hex) ? new byte[0] : decode(hex.toCharArray());
    }

    /**
     * 16进制字符数组还原成字节数组
     *
     * @param data 字符数组
     * @return 字节数组
     */
    public static byte[] decode(final char... data) {
        final int len = ArrayUtil.isEmpty(data) ? 0 : data.length;
        Assert.isTrue((len & 0x01) == 0, "Odd number of characters.");
        byte[] out;
        if (len > 0) {
            out = new byte[len >> 1];
            for (int i = 0,
                 j = 0; j < len; i++) {
                int flag = toDigit(data[j], j) << 4;
                j++;
                flag = flag | toDigit(data[j], j);
                j++;
                out[i] = (byte) (flag & 0xFF);
            }
        } else {
            out = new byte[0];
        }
        return out;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data     byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制String
     */
    private static String encodeHexStr(final byte[] data, final char... toDigits) {
        return new String(encode(data, toDigits));
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data     byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制char[]
     */
    private static char[] encode(final byte[] data, final char... toDigits) {
        final int len = Objects.isNull(data) ? 0 : data.length;
        char[] out;
        if (len == 0 || ArrayUtil.isEmpty(toDigits)) {
            out = new char[0];
        } else {
            out = new char[len << 1];
            for (int i = 0,
                 j = 0; i < len; i++) {
                out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
                out[j++] = toDigits[0x0F & data[i]];
            }
        }

        return out;
    }

    /**
     * 将十六进制字符转换成一个整数
     *
     * @param cha   十六进制char
     * @param index 十六进制字符在字符数组中的位置
     * @return 一个整数
     */
    private static int toDigit(final char cha, final int index) {
        final int digit = Character.digit(cha, 16);
        if (digit == -1) {
            throw new ToolException(StringUtil.format("Illegal hexadecimal character {} at index {}", cha, index));
        }
        return digit;
    }
}
