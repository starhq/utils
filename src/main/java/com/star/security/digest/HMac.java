package com.star.security.digest;

import com.star.io.CharsetUtil;
import com.star.security.ALGORITHM;
import com.star.security.SecureUtil;
import com.star.string.HexUtil;
import com.star.string.StringUtil;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * HMAC摘要算法<br>
 * HMAC，全称为“Hash Message Authentication Code”，中文名“散列消息鉴别码”<br>
 * 主要是利用哈希算法，以一个密钥和一个消息为输入，生成一个消息摘要作为输出。<br>
 * 一般的，消息鉴别码用于验证传输于两个共 同享有一个密钥的单位之间的消息。<br>
 * HMAC 可以与任何迭代散列函数捆绑使用。MD5 和 SHA-1 就是这种散列函数。HMAC 还可以使用一个用于计算和确认消息鉴别值的密钥。<br>
 * 注意：此对象实例化后为非线程安全！
 *
 * @author Looly
 */
public class HMac {

    /**
     * {@link Mac}
     */
    private Mac mac;
    /**
     * 密钥
     */
    private SecretKey secretKey;

    // ------------------------------------------------------------------------------------------- Constructor start

    /**
     * 构造，自动生成密钥
     *
     * @param algorithm 算法
     */
    public HMac(ALGORITHM algorithm) {
        this(algorithm, (SecretKey) null);
    }

    /**
     * 构造
     *
     * @param algorithm 算法
     * @param key       密钥
     */
    public HMac(ALGORITHM algorithm, byte[] key) {
        init(algorithm, key);
    }

    /**
     * 构造
     *
     * @param algorithm 算法
     * @param key       密钥
     */
    public HMac(ALGORITHM algorithm, SecretKey key) {
        init(algorithm, key);
    }
    // ------------------------------------------------------------------------------------------- Constructor end

    /**
     * 初始化
     *
     * @param algorithm 算法
     * @param key       密钥
     */
    public void init(ALGORITHM algorithm, byte[] key) {
        init(algorithm, (null == key) ? null : new SecretKeySpec(key, algorithm.toString()));
    }

    /**
     * 初始化
     *
     * @param algorithm 算法
     * @param key       密钥 {@link SecretKey}
     * @return {@link HMac}
     */
    private void init(final ALGORITHM algorithm, final SecretKey key) {
        try {
            this.mac = Mac.getInstance(algorithm.toString());
            this.secretKey = Objects.isNull(key) ? SecureUtil.generateKey(algorithm.toString()) : key;
            mac.init(this.secretKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new SecurityException(StringUtil.format("init  hmac failure,the reason is {}", e.getMessage()), e);
        }
    }

    // ------------------------------------------------------------------------------------------- Digest

    /**
     * 生成文件摘要
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return 摘要
     */
    public byte[] digest(final String data, final Charset charset) {
        return digest(StringUtil.bytes(data, charset));
    }

    /**
     * 生成文件摘要
     *
     * @param data 被摘要数据
     * @return 摘要
     */
    public byte[] digest(final String data) {
        return digest(data, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 生成文件摘要，并转为16进制字符串
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return 摘要
     */
    public String digestHex(final String data, final Charset charset) {
        return HexUtil.encodeToString(digest(data, charset));
    }

    /**
     * 生成文件摘要
     *
     * @param data 被摘要数据
     * @return 摘要
     */
    public String digestHex(final String data) {
        return digestHex(data, CharsetUtil.CHARSET_UTF_8);
    }


    /**
     * 生成摘要
     *
     * @param data 数据bytes
     * @return 摘要bytes
     */
    public byte[] digest(final byte[] data) {
        byte[] result;
        try {
            result = mac.doFinal(data);
        } finally {
            mac.reset();
        }
        return result;
    }

    /**
     * 生成摘要，并转为16进制字符串<br>
     *
     * @param data 被摘要数据
     * @return 摘要
     */
    public String digestHex(final byte[] data) {
        return HexUtil.encodeToString(digest(data));
    }


}
