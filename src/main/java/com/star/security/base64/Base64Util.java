package com.star.security.base64;

import com.star.io.CharsetUtil;
import com.star.string.StringUtil;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * base64 加解密
 *
 * @author starhq
 */
public final class Base64Util {

    /**
     * 编码器
     */
    private static final Base64.Encoder ENCODER = Base64.getEncoder();
    /**
     * url编码器
     */
    private static final Base64.Encoder URLENCODER = Base64.getUrlEncoder();
    /**
     * 解码器
     */
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    private Base64Util() {
    }

    /**
     * base64编码
     *
     * @param source 被编码的base64字符串
     * @return 被加密后的字符串
     */
    public static String encode(final String source) {
        return encode(source, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * base64编码，URL安全
     *
     * @param source 被编码的base64字符串
     * @return 被加密后的字符串
     * @since 3.0.6
     */
    public static String encodeUrlSafe(final String source) {
        return encodeUrlSafe(source, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * base64编码
     *
     * @param source  被编码的base64字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     */
    public static String encode(final String source, final Charset charset) {
        return encode(StringUtil.bytes(source, charset), charset);
    }

    /**
     * base64编码，URL安全的
     *
     * @param source  被编码的base64字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     * @since 3.0.6
     */
    public static String encodeUrlSafe(final String source, final Charset charset) {
        return encodeUrlSafe(StringUtil.bytes(source, charset), charset);
    }

    /**
     * base64编码
     *
     * @param source 被编码的base64字符串
     * @return 被加密后的字符串
     */
    public static String encode(final byte[] source) {
        return encode(source, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * base64编码,URL安全的
     *
     * @param source 被编码的base64字符串
     * @return 被加密后的字符串
     * @since 3.0.6
     */
    public static String encodeUrlSafe(final byte[] source) {
        return encodeUrlSafe(source, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * base64编码，URL安全的
     *
     * @param source  被编码的base64字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     * @since 3.0.6
     */
    public static String encodeUrlSafe(final byte[] source, final Charset charset) {
        return StringUtil.str(encode(source, true), charset);
    }

    /**
     * base64编码
     *
     * @param source  被编码的base64字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     */
    public static String encode(final byte[] source, final Charset charset) {
        return StringUtil.str(encode(source, false), charset);
    }

    /**
     * 编码为Base64<br>
     *
     * @param source    被编码的数组
     * @param isUrlSafe 是否使用URL安全字符，一般为<code>false</code>
     * @return 编码后的bytes
     */
    public static byte[] encode(final byte[] source, final boolean isUrlSafe) {
        return isUrlSafe ? URLENCODER.encode(source) : ENCODER.encode(source);
    }

    //===============================encoder===========================================

    /**
     * base64解码
     *
     * @param source 被解码的base64字符串
     * @return 被加密后的字符串
     */
    public static String decodeStr(final String source) {
        return decodeStr(source, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * base64解码
     *
     * @param source  被解码的base64字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     */
    public static String decodeStr(final String source, final Charset charset) {
        return StringUtil.str(decode(source, charset), charset);
    }

    /**
     * base64解码
     *
     * @param source 被解码的base64字符串
     * @return 被加密后的字符串
     */
    public static byte[] decode(final String source) {
        return decode(source, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * base64解码
     *
     * @param source  被解码的base64字符串
     * @param charset 字符集
     * @return 被加密后的字符串
     */
    public static byte[] decode(final String source, final Charset charset) {
        return decode(StringUtil.bytes(source, charset));
    }

    /**
     * 解码Base64
     *
     * @param bytes 输入
     * @return 解码后的bytes
     */
    public static byte[] decode(final byte[] bytes) {
        return DECODER.decode(bytes);
    }
    //===============================encoder===========================================
}
