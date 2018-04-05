package com.star.security.digest;

import com.star.io.CharsetUtil;
import com.star.security.ALGORITHM;

import javax.crypto.SecretKey;
import java.nio.charset.Charset;

/**
 * 摘要算法工具类
 *
 * @author Looly
 */
public final class DigestUtil {

    private DigestUtil() {
    }


    // ------------------------------------------------------------------------------------------- MD5

    /**
     * 计算16位MD5摘要值
     *
     * @param data 被摘要数据
     * @return MD5摘要
     */
    public static byte[] md5(final byte[] data) {
        return new Digester(ALGORITHM.MD5).digest(data);
    }

    /**
     * 计算16位MD5摘要值
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return MD5摘要
     */
    public static byte[] md5(final String data, final Charset charset) {
        return new Digester(ALGORITHM.MD5).digest(data, charset);
    }

    /**
     * 计算16位MD5摘要值，使用UTF-8编码
     *
     * @param data 被摘要数据
     * @return MD5摘要
     */
    public static byte[] md5(final String data) {
        return md5(data, CharsetUtil.CHARSET_UTF_8);
    }


    /**
     * 计算16位MD5摘要值，并转为16进制字符串
     *
     * @param data 被摘要数据
     * @return MD5摘要的16进制表示
     */
    public static String md5Hex(final byte[] data) {
        return new Digester(ALGORITHM.MD5).digestHex(data);
    }

    /**
     * 计算16位MD5摘要值，并转为16进制字符串
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return MD5摘要的16进制表示
     */
    public static String md5Hex(final String data, final Charset charset) {
        return new Digester(ALGORITHM.MD5).digestHex(data, charset);
    }

    /**
     * 计算16位MD5摘要值，并转为16进制字符串
     *
     * @param data 被摘要数据
     * @return MD5摘要的16进制表示
     */
    public static String md5Hex(final String data) {
        return md5Hex(data, CharsetUtil.CHARSET_UTF_8);
    }


    // ------------------------------------------------------------------------------------------- SHA-1

    /**
     * 计算SHA-1摘要值
     *
     * @param data 被摘要数据
     * @return SHA-1摘要
     */
    public static byte[] sha1(final byte[] data) {
        return new Digester(ALGORITHM.SHA1).digest(data);
    }

    /**
     * 计算SHA-1摘要值
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return SHA-1摘要
     */
    public static byte[] sha1(final String data, final Charset charset) {
        return new Digester(ALGORITHM.SHA1).digest(data, charset);
    }

    /**
     * 计算sha1摘要值，使用UTF-8编码
     *
     * @param data 被摘要数据
     * @return MD5摘要
     */
    public static byte[] sha1(final String data) {
        return sha1(data, CharsetUtil.CHARSET_UTF_8);
    }


    /**
     * 计算SHA-1摘要值，并转为16进制字符串
     *
     * @param data 被摘要数据
     * @return SHA-1摘要的16进制表示
     */
    public static String sha1Hex(final byte[] data) {
        return new Digester(ALGORITHM.SHA1).digestHex(data);
    }

    /**
     * 计算SHA-1摘要值，并转为16进制字符串
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return SHA-1摘要的16进制表示
     */
    public static String sha1Hex(final String data, final Charset charset) {
        return new Digester(ALGORITHM.SHA1).digestHex(data, charset);
    }

    /**
     * 计算SHA-1摘要值，并转为16进制字符串
     *
     * @param data 被摘要数据
     * @return SHA-1摘要的16进制表示
     */
    public static String sha1Hex(final String data) {
        return sha1Hex(data, CharsetUtil.CHARSET_UTF_8);
    }


    // ------------------------------------------------------------------------------------------- SHA-256

    /**
     * 计算SHA-256摘要值
     *
     * @param data 被摘要数据
     * @return SHA-256摘要
     * @since 3.0.8
     */
    public static byte[] sha256(final byte[] data) {
        return new Digester(ALGORITHM.SHA256).digest(data);
    }

    /**
     * 计算SHA-256摘要值
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return SHA-256摘要
     * @since 3.0.8
     */
    public static byte[] sha256(final String data, final Charset charset) {
        return new Digester(ALGORITHM.SHA256).digest(data, charset);
    }

    /**
     * 计算sha256摘要值，使用UTF-8编码
     *
     * @param data 被摘要数据
     * @return MD5摘要
     * @since 3.0.8
     */
    public static byte[] sha256(final String data) {
        return sha256(data, CharsetUtil.CHARSET_UTF_8);
    }


    /**
     * 计算SHA-1摘要值，并转为16进制字符串
     *
     * @param data 被摘要数据
     * @return SHA-256摘要的16进制表示
     * @since 3.0.8
     */
    public static String sha256Hex(final byte[] data) {
        return new Digester(ALGORITHM.SHA256).digestHex(data);
    }

    /**
     * 计算SHA-256摘要值，并转为16进制字符串
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return SHA-256摘要的16进制表示
     * @since 3.0.8
     */
    public static String sha256Hex(final String data, final Charset charset) {
        return new Digester(ALGORITHM.SHA256).digestHex(data, charset);
    }

    /**
     * 计算SHA-256摘要值，并转为16进制字符串
     *
     * @param data 被摘要数据
     * @return SHA-256摘要的16进制表示
     * @since 3.0.8
     */
    public static String sha256Hex(final String data) {
        return sha256Hex(data, CharsetUtil.CHARSET_UTF_8);
    }


    // ------------------------------------------------------------------------------------------- Hmac

    /**
     * 创建HMac对象，调用digest方法可获得hmac值
     *
     * @param algorithm {@link ALGORITHM}
     * @param key       密钥，如果为<code>null</code>生成随机密钥
     * @return {@link HMac}
     * @since 3.0.3
     */
    public static HMac hmacfinal(ALGORITHM algorithm, final byte[] key) {
        return new HMac(algorithm, key);
    }

    /**
     * 创建HMac对象，调用digest方法可获得hmac值
     *
     * @param algorithm {@link ALGORITHM}
     * @param key       密钥{@link SecretKey}，如果为<code>null</code>生成随机密钥
     * @return {@link HMac}
     * @since 3.0.3
     */
    public static HMac hmac(final ALGORITHM algorithm, final SecretKey key) {
        return new HMac(algorithm, key);
    }

    /**
     * 新建摘要器
     *
     * @param algorithm 签名算法
     * @return Digester
     * @since 4.0.1
     */
    public static Digester digester(final ALGORITHM algorithm) {
        return new Digester(algorithm);
    }
}
