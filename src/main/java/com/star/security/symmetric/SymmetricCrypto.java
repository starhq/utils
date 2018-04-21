package com.star.security.symmetric;

import com.star.common.RandomUtil;
import com.star.io.CharsetUtil;
import com.star.security.ALGORITHM;
import com.star.security.SecureUtil;
import com.star.security.base64.Base64Util;
import com.star.string.HexUtil;
import com.star.string.StringUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;
import java.nio.charset.Charset;
import java.security.spec.AlgorithmParameterSpec;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 对称加密算法<br>
 * 在对称加密算法中，数据发信方将明文（原始数据）和加密密钥一起经过特殊加密算法处理后，使其变成复杂的加密密文发送出去。<br>
 * 收信方收到密文后，若想解读原文，则需要使用加密用过的密钥及相同算法的逆算法对密文进行解密，才能使其恢复成可读明文。<br>
 * 在对称加密算法中，使用的密钥只有一个，发收信双方都使用这个密钥对数据进行加密和解密，这就要求解密方事先必须知道加密密钥。<br>
 *
 * @author Looly
 */
public class SymmetricCrypto {

    /**
     * pbe算法
     */
    private static final String PBE = "PBE";
    /**
     * SecretKey 负责保存对称密钥
     */
    private SecretKey secretKey;
    /**
     * Cipher负责完成加密或解密工作
     */
    private Cipher clipher;
    /**
     * 加密解密参数
     */
    private AlgorithmParameterSpec params;
    private Lock lock = new ReentrantLock();

    // ------------------------------------------------------------------ Constructor start

    /**
     * 构造，使用随机密钥
     *
     * @param algorithm {@link ALGORITHM}
     */
    public SymmetricCrypto(ALGORITHM algorithm) {
        this(algorithm, (byte[]) null);
    }

    /**
     * 构造
     *
     * @param algorithm 算法 {@link ALGORITHM}
     * @param key       自定义KEY
     */
    public SymmetricCrypto(ALGORITHM algorithm, byte[] key) {
        this(algorithm.toString(), SecureUtil.generateKey(algorithm.toString(), key), null);
    }

    /**
     * 构造
     *
     * @param algorithm 算法 {@link ALGORITHM}
     * @param key       自定义KEY
     * @since 3.1.2
     */
    public SymmetricCrypto(ALGORITHM algorithm, SecretKey key) {
        this(algorithm.toString(), key, null);
    }


    /**
     * 构造
     *
     * @param algorithm  算法
     * @param key        密钥
     * @param paramsSpec 算法参数，例如加盐等
     * @since 3.3.0
     */
    public SymmetricCrypto(String algorithm, SecretKey key, AlgorithmParameterSpec paramsSpec) {
        init(algorithm, key);
        if (null != paramsSpec) {
            setParams(paramsSpec);
        }
    }

    // ------------------------------------------------------------------ Constructor end

    /**
     * 初始化
     *
     * @param algorithm 算法
     * @param key       密钥，如果为<code>null</code>自动生成一个key
     */
    public void init(final String algorithm, final SecretKey key) {
        this.secretKey = key;
        if (algorithm.startsWith(PBE)) {
            // 对于PBE算法使用随机数加盐
            this.params = new PBEParameterSpec(RandomUtil.randomBytes(8), 100);
        }
        try {
            clipher = Cipher.getInstance(algorithm);
        } catch (Exception e) {
            throw new SecurityException(StringUtil.format("init {} failure,the reason is {}", algorithm, e.getMessage()), e);
        }
    }

    /**
     * 设置 {@link AlgorithmParameterSpec}，通常用于加盐或偏移向量
     *
     * @param params {@link AlgorithmParameterSpec}
     * @return 自身
     */
    public SymmetricCrypto setParams(AlgorithmParameterSpec params) {
        this.params = params;
        return this;
    }

    // --------------------------------------------------------------------------------- Encrypt

    /**
     * 加密
     *
     * @param data 被加密的bytes
     * @return 加密后的bytes
     */
    public byte[] encrypt(byte[] data) {
        lock.lock();
        try {
            if (null == this.params) {
                clipher.init(Cipher.ENCRYPT_MODE, secretKey);
            } else {
                clipher.init(Cipher.ENCRYPT_MODE, secretKey, params);
            }
            return clipher.doFinal(data);
        } catch (Exception e) {
            throw new SecurityException(StringUtil.format("encrypt data failure,the reason is {}", e.getMessage()), e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 加密
     *
     * @param data 数据
     * @return 加密后的Hex
     */
    public String encryptHex(byte[] data) {
        return HexUtil.encodeToString(encrypt(data));
    }

    /**
     * 加密
     *
     * @param data 数据
     * @return 加密后的Base64
     * @since 4.0.1
     */
    public String encryptBase64(byte[] data) {
        return Base64Util.encode(encrypt(data));
    }

    /**
     * 加密
     *
     * @param data    被加密的字符串
     * @param charset 编码
     * @return 加密后的bytes
     */
    public byte[] encrypt(String data, Charset charset) {
        return encrypt(StringUtil.bytes(data, charset));
    }

    /**
     * 加密
     *
     * @param data    被加密的字符串
     * @param charset 编码
     * @return 加密后的Hex
     */
    public String encryptHex(String data, Charset charset) {
        return HexUtil.encodeToString(encrypt(data, charset));
    }

    /**
     * 加密
     *
     * @param data    被加密的字符串
     * @param charset 编码
     * @return 加密后的Base64
     */
    public String encryptBase64(String data, Charset charset) {
        return Base64Util.encode(encrypt(data, charset));
    }

    /**
     * 加密，使用UTF-8编码
     *
     * @param data 被加密的字符串
     * @return 加密后的bytes
     */
    public byte[] encrypt(String data) {
        return encrypt(StringUtil.bytes(data, CharsetUtil.CHARSET_UTF_8));
    }

    /**
     * 加密，使用UTF-8编码
     *
     * @param data 被加密的字符串
     * @return 加密后的Hex
     */
    public String encryptHex(String data) {
        return HexUtil.encodeToString(encrypt(data));
    }

    /**
     * 加密，使用UTF-8编码
     *
     * @param data 被加密的字符串
     * @return 加密后的Base64
     */
    public String encryptBase64(String data) {
        return Base64Util.encode(encrypt(data));
    }


    // --------------------------------------------------------------------------------- Decrypt

    /**
     * 解密
     *
     * @param bytes 被解密的bytes
     * @return 解密后的bytes
     */
    public byte[] decrypt(byte[] bytes) {
        lock.lock();
        try {
            if (null == this.params) {
                clipher.init(Cipher.DECRYPT_MODE, secretKey);
            } else {
                clipher.init(Cipher.DECRYPT_MODE, secretKey, params);
            }
            return clipher.doFinal(bytes);
        } catch (Exception e) {
            throw new SecurityException(StringUtil.format("decrypt data failure,the reason is {}", e.getMessage()), e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 解密为字符串
     *
     * @param bytes   被解密的bytes
     * @param charset 解密后的charset
     * @return 解密后的String
     */
    public String decryptStr(byte[] bytes, Charset charset) {
        return StringUtil.str(decrypt(bytes), charset);
    }

    /**
     * 解密为字符串，默认UTF-8编码
     *
     * @param bytes 被解密的bytes
     * @return 解密后的String
     */
    public String decryptStr(byte[] bytes) {
        return decryptStr(bytes, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 解密Hex表示的字符串
     *
     * @param data 被解密的String，必须为16进制字符串表示形式
     * @return 解密后的bytes
     */
    public byte[] decrypt(String data) {
        return decrypt(HexUtil.decode(data));
    }

    /**
     * 解密Base64表示的字符串
     *
     * @param data 被解密的String，必须为Base64形式
     * @return 解密后的bytes
     * @since 4.0.1
     */
    public byte[] decryptFromBase64(String data) {
        return decrypt(Base64Util.decode(data));
    }

    /**
     * 解密Hex表示的字符串
     *
     * @param data    被解密的String
     * @param charset 解密后的charset
     * @return 解密后的String
     */
    public String decryptStr(String data, Charset charset) {
        return StringUtil.str(decrypt(data), charset);
    }

    /**
     * 解密Base64表示的字符串
     *
     * @param data    被解密的String，必须为Base64形式
     * @param charset 解密后的charset
     * @return 解密后的String
     * @since 4.0.1
     */
    public String decryptStrFromBase64(String data, Charset charset) {
        return StringUtil.str(decrypt(Base64Util.decode(data, charset)), charset);
    }

    /**
     * 解密Hex表示的字符串，默认UTF-8编码
     *
     * @param data 被解密的String
     * @return 解密后的String
     */
    public String decryptStr(String data) {
        return decryptStr(data, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 解密Base64表示的字符串，默认UTF-8编码
     *
     * @param data 被解密的String
     * @return 解密后的String
     * @since 4.0.1
     */
    public String decryptStrFromBase64(String data) {
        return decryptStrFromBase64(data, CharsetUtil.CHARSET_UTF_8);
    }


}