package com.star.string;

import com.star.collection.ArrayUtil;
import com.star.lang.Assert;

/**
 * 16进制转换
 * <p>
 * Created by win7 on 2017/5/28.
 */
public final class HexUtil {

    /**
     * 用于建立十六进制字符的输出的小写字符数组
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 用于建立十六进制字符的输出的大写字符数组
     */
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private HexUtil() {
    }

    /**
     * 是否是16进制数
     *
     * @param value 需要判断的字符串
     * @return 是否16进制
     */
    public static boolean isHexNumber(final String value) {
        final boolean isBlank = StringUtil.isBlank(value);
        final int index = !isBlank && value.startsWith(StringUtil.DASH) ? 1 : 0;
        return !isBlank && (value.startsWith("0x", index) || value.startsWith("0X", index) || value.charAt(0) == '#');
    }

    /**
     * 字节数组转16进制字符串
     *
     * @param data        字节数组
     * @param toLowerCase 是否大小写
     * @return 16进制字符串
     */
    public static String encode(final byte[] data, final boolean toLowerCase) {
        final int len = ArrayUtil.isEmpty(data) ? 0 : data.length;
        char[] out;
        if (len > 0) {
            out = new char[len << 1];
            final char[] toDigits = toLowerCase ? DIGITS_LOWER : DIGITS_UPPER;
            int flag = 0;
            for (int i = 0; i < len; i++) {
                out[flag++] = toDigits[(0xF0 & data[i]) >>> 4];
                out[flag++] = toDigits[0x0F & data[i]];
            }
        } else {
            out = new char[0];
        }
        return new String(out);
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

    private static int toDigit(final char cha, final int index) {
        final int digit = Character.digit(cha, 16);
        Assert.isTrue(digit != -1, StringUtil.format("Illegal hexadecimal character {} at index {}", cha, index));
        return digit;
    }
}
