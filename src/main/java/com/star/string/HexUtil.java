package com.star.string;

import com.star.collection.array.ArrayUtil;
import com.star.exception.ToolException;
import com.star.io.CharsetUtil;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * 16进制转换
 * <p>
 * Created by starhq on 2017/5/28.
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

    /**
     * 标志位
     */
    private static final int HEX = 0x01;

    private HexUtil() {
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data byte[]
     * @return 十六进制char[]
     */
    public static char[] encode(final byte[] data) {
        return encode(data, true);
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param str     字符串
     * @param charset 编码
     * @return 十六进制char[]
     */
    public static char[] encodeHex(String str, final Charset charset) {
        return encode(StringUtil.bytes(str, charset), true);
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
     * @param data byte[]
     * @return 十六进制char[]
     */
    public static String encodeToString(final byte[] data) {
        return encodeToString(data, true);
    }

    /**
     * 将字节数组转换为十六进制字符串，结果为小写
     *
     * @param data    被编码的字符串
     * @param charset 编码
     * @return 十六进制String
     */
    public static String encodeToString(final String data, final Charset charset) {
        return encodeToString(StringUtil.bytes(data, charset), true);
    }

    /**
     * 将字节数组转换为十六进制字符串，结果为小写，默认编码是UTF-8
     *
     * @param data 被编码的字符串
     * @return 十六进制String
     */
    public static String encodeToString(final String data) {
        return encodeToString(data, CharsetUtil.CHARSET_UTF_8);
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

    //===========encode============

    /**
     * 将十六进制字符数组转换为字符串，默认编码UTF-8
     *
     * @param hexStr 十六进制String
     * @return 字符串
     */
    public static String decodeHexStr(final String hexStr) {
        return decodeHexStr(hexStr, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 将十六进制字符数组转换为字符串
     *
     * @param hexStr  十六进制String
     * @param charset 编码
     * @return 字符串
     */
    public static String decodeHexStr(final String hexStr, final Charset charset) {
        return decodeHexStr(hexStr.toCharArray(), charset);
    }

    /**
     * 将十六进制字符数组转换为字符串
     *
     * @param hexData 十六进制char[]
     * @param charset 编码
     * @return 字符串
     */
    public static String decodeHexStr(final char[] hexData, final Charset charset) {
        return StringUtil.str(decode(hexData), charset);
    }

    /**
     * 将16进制字符串转为字节数组
     *
     * @param hex 16进制字符串
     * @return 字节数组
     */
    public static byte[] decode(final String hex) {
        return Objects.isNull(hex) ? new byte[0] : decode(hex.toCharArray());
    }

    /**
     * 16进制字符数组还原成字节数组
     *
     * @param data 字符数组
     * @return 字节数组
     */
    public static byte[] decode(final char... data) {
        final int len = ArrayUtil.isEmpty(data) ? 0 : data.length;
        if ((len & HEX) != 0) {
            throw new ToolException("Odd number of characters.");
        }
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

    //=============decode===============

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
