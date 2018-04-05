package com.star.security.digest;

import com.star.io.CharsetUtil;
import com.star.security.ALGORITHM;
import com.star.string.HexUtil;
import com.star.string.StringUtil;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 摘要算法<br>
 * 注意：此对象实例化后为非线程安全！
 *
 * @author Looly
 */
public class Digester {

    /**
     * 消息摘要
     */
    private MessageDigest digest;

    /**
     * 构造函数
     *
     * @param algorithm 算法
     */
    public Digester(ALGORITHM algorithm) {
        init(algorithm);
    }

    /**
     * 初始化
     *
     * @param algorithm 算法
     */
    private void init(final ALGORITHM algorithm) {
        try {
            digest = MessageDigest.getInstance(algorithm.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(StringUtil.format("init message digest failure,the reason is {}", e.getMessage()), e);
        }
    }

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
        return HexUtil.encodeToString(digest(data, charset), true);
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
            result = digest.digest(data);
        } finally {
            digest.reset();
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
