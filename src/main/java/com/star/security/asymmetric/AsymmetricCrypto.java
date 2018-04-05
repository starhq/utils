package com.star.security.asymmetric;

import com.star.exception.IORuntimeException;
import com.star.io.CharsetUtil;
import com.star.io.IoUtil;
import com.star.security.ALGORITHM;
import com.star.security.base64.Base64Util;
import com.star.string.HexUtil;
import com.star.string.StringUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 非对称加密算法<br>
 * 1、签名：使用私钥加密，公钥解密。用于让所有公钥所有者验证私钥所有者的身份并且用来防止私钥所有者发布的内容被篡改，但是不用来保证内容不被他人获得。<br>
 * 2、加密：用公钥加密，私钥解密。用于向公钥所有者发布信息,这个信息可能被他人篡改,但是无法被他人获得。
 *
 * @author Looly
 */
public class AsymmetricCrypto extends BaseAsymmetric<AsymmetricCrypto> {

    /**
     * Cipher负责完成加密或解密工作
     */
    protected Cipher clipher;

    // ------------------------------------------------------------------ Constructor start

    /**
     * 构造，创建新的私钥公钥对
     *
     * @param algorithm {@link ALGORITHM}
     */
    public AsymmetricCrypto(ALGORITHM algorithm) {
        this(algorithm, null, null);
    }

    /**
     * 构造，创建新的私钥公钥对
     *
     * @param algorithm 算法
     */
    public AsymmetricCrypto(String algorithm) {
        this(algorithm, null, null);
    }

    /**
     * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param algorithm        {@link ALGORITHM}
     * @param privateKeyBase64 私钥Base64
     * @param publicKeyBase64  公钥Base64
     */
    public AsymmetricCrypto(ALGORITHM algorithm, String privateKeyBase64, String publicKeyBase64) {
        this(algorithm.toString(), Base64Util.decode(privateKeyBase64), Base64Util.decode(publicKeyBase64));
    }

    /**
     * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param algorithm  {@link ALGORITHM}
     * @param privateKey 私钥
     * @param publicKey  公钥
     */
    public AsymmetricCrypto(ALGORITHM algorithm, byte[] privateKey, byte[] publicKey) {
        this(algorithm.toString(), privateKey, publicKey);
    }

    /**
     * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param algorithm  {@link ALGORITHM}
     * @param privateKey 私钥
     * @param publicKey  公钥
     * @since 3.1.1
     */
    public AsymmetricCrypto(ALGORITHM algorithm, PrivateKey privateKey, PublicKey publicKey) {
        this(algorithm.toString(), privateKey, publicKey);
    }

    /**
     * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param algorithm        非对称加密算法
     * @param privateKeyBase64 私钥Base64
     * @param publicKeyBase64  公钥Base64
     */
    public AsymmetricCrypto(String algorithm, String privateKeyBase64, String publicKeyBase64) {
        super(algorithm, privateKeyBase64, publicKeyBase64);
    }

    /**
     * 构造
     * <p>
     * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param algorithm  算法
     * @param privateKey 私钥
     * @param publicKey  公钥
     */
    public AsymmetricCrypto(String algorithm, byte[] privateKey, byte[] publicKey) {
        super(algorithm, privateKey, publicKey);
    }

    /**
     * 构造
     * <p>
     * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param algorithm  算法
     * @param privateKey 私钥
     * @param publicKey  公钥
     * @since 3.1.1
     */
    public AsymmetricCrypto(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
        super(algorithm, privateKey, publicKey);
    }
    // ------------------------------------------------------------------ Constructor end

    /**
     * 初始化<br>
     * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param algorithm  算法
     * @param privateKey 私钥
     * @param publicKey  公钥
     * @return {@link AsymmetricCrypto}
     */
    @Override
    public AsymmetricCrypto init(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
        try {
            this.clipher = Cipher.getInstance(algorithm);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new SecurityException(StringUtil.format("init AsymmetricCrypto failure,the reason is {}", e.getMessage()), e);
        }

        super.init(algorithm, privateKey, publicKey);
        return this;
    }

    // --------------------------------------------------------------------------------- Encrypt

    /**
     * 加密
     *
     * @param data    被加密的bytes
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return 加密后的bytes
     */
    public byte[] encrypt(byte[] data, KeyType keyType) {
        lock.lock();
        try {
            clipher.init(Cipher.ENCRYPT_MODE, getKeyByType(keyType));
            return clipher.doFinal(data);

        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            throw new SecurityException(StringUtil.format("encrypt data failure,the reason is {}", e.getMessage()), e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 编码为Hex字符串
     *
     * @param data    被加密的bytes
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return Hex字符串
     */
    public String encryptHex(byte[] data, KeyType keyType) {
        return HexUtil.encodeToString(encrypt(data, keyType));
    }

    /**
     * 编码为Base64字符串
     *
     * @param data    被加密的bytes
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return Base64字符串
     * @since 4.0.1
     */
    public String encryptBase64(byte[] data, KeyType keyType) {
        return Base64Util.encode(encrypt(data, keyType));
    }


    /**
     * 加密
     *
     * @param data    被加密的字符串
     * @param charset 编码
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return 加密后的bytes
     */
    public byte[] encrypt(String data, Charset charset, KeyType keyType) {
        return encrypt(StringUtil.bytes(data, charset), keyType);
    }

    /**
     * 加密，使用UTF-8编码
     *
     * @param data    被加密的字符串
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return 加密后的bytes
     */
    public byte[] encrypt(String data, KeyType keyType) {
        return encrypt(StringUtil.bytes(data, CharsetUtil.CHARSET_UTF_8), keyType);
    }

    /**
     * 编码为Hex字符串
     *
     * @param data    被加密的字符串
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return Hex字符串
     * @since 4.0.1
     */
    public String encryptHex(String data, KeyType keyType) {
        return HexUtil.encodeToString(encrypt(data, keyType));
    }

    /**
     * 编码为Hex字符串
     *
     * @param data    被加密的bytes
     * @param charset 编码
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return Hex字符串
     * @since 4.0.1
     */
    public String encryptHex(String data, Charset charset, KeyType keyType) {
        return HexUtil.encodeToString(encrypt(data, charset, keyType));
    }

    /**
     * 编码为Base64字符串
     *
     * @param data    被加密的字符串
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return Base64字符串
     * @since 4.0.1
     */
    public String encryptBase64(String data, KeyType keyType) {
        return Base64Util.encode(encrypt(data, keyType));
    }

    /**
     * 编码为Base64字符串
     *
     * @param data    被加密的字符串
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return Base64字符串
     * @since 4.0.1
     */
    public String encryptBase64(String data, Charset charset, KeyType keyType) {
        return Base64Util.encode(encrypt(data, charset, keyType));
    }

    /**
     * 加密
     *
     * @param data    被加密的数据流
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return 加密后的bytes
     * @throws IORuntimeException IO异常
     */
    public byte[] encrypt(InputStream data, KeyType keyType) throws IORuntimeException {
        return encrypt(IoUtil.readBytes(data), keyType);
    }

    /**
     * 编码为Hex字符串
     *
     * @param data    被加密的数据流
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return Hex字符串
     * @since 4.0.1
     */
    public String encryptHex(InputStream data, KeyType keyType) {
        return HexUtil.encodeToString(encrypt(data, keyType));
    }

    /**
     * 编码为Base64字符串
     *
     * @param data    被加密的数据流
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return Base64字符串
     * @since 4.0.1
     */
    public String encryptBase64(InputStream data, KeyType keyType) {
        return Base64Util.encode(encrypt(data, keyType));
    }

    // --------------------------------------------------------------------------------- Decrypt

    /**
     * 解密
     *
     * @param bytes   被解密的bytes
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return 解密后的bytes
     */
    public byte[] decrypt(byte[] bytes, KeyType keyType) {
        lock.lock();
        try {
            clipher.init(Cipher.DECRYPT_MODE, getKeyByType(keyType));
            return clipher.doFinal(bytes);
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            throw new SecurityException(StringUtil.format("encrypt data failure,the reason is {}", e.getMessage()), e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 解密
     *
     * @param data    被解密的bytes
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return 解密后的bytes
     * @throws IORuntimeException IO异常
     */
    public byte[] decrypt(InputStream data, KeyType keyType) throws IORuntimeException {
        return decrypt(IoUtil.readBytes(data), keyType);
    }

    /**
     * 从Hex字符串解密
     *
     * @param hexStr  Hex字符串
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return 解密后的bytes
     * @since 4.0.1
     */
    public byte[] decryptFromHex(String hexStr, KeyType keyType) {
        return decrypt(HexUtil.decode(hexStr), keyType);
    }

    /**
     * 从Base64字符串解密
     *
     * @param base64Str Base64字符串
     * @param keyType   私钥或公钥 {@link KeyType}
     * @return 解密后的bytes
     * @since 4.0.1
     */
    public byte[] decryptFromBase64(String base64Str, KeyType keyType) {
        return decrypt(Base64Util.decode(base64Str, CharsetUtil.CHARSET_UTF_8), keyType);
    }


}
