package com.star.security;

import com.star.collection.array.ArrayUtil;
import com.star.common.RandomUtil;
import com.star.io.CharsetUtil;
import com.star.string.StringUtil;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

/**
 * 安全相关工具类<br>
 * 加密分为三种：<br>
 * 1、对称加密（symmetric），例如：AES、DES等<br>
 * 2、非对称加密（asymmetric），例如：RSA、DSA等<br>
 * 3、摘要加密（digest），例如：MD5、SHA-1、SHA-256、HMAC等<br>
 *
 * @author xiaoleilu
 */
public final class SecureUtil {

    /**
     * 默认密钥字节数
     * <p>
     * <pre>
     * RSA/DSA
     * Default Keysize 1024
     * Keysize must be a multiple of 64, ranging from 512 to 1024 (inclusive).
     * </pre>
     */
    public static final int DEFAULT_KEY_SIZE = 1024;

    /**
     * des算法
     */
    private static final String DES = "DES";
    /**
     * pbe算法
     */
    private static final String PBE = "PBE";
    /**
     * DESede算法
     */
    private static final String DESEDE = "DESede";
    /**
     * ECDSA算法
     */
    private static final String ECDSA = "ECDSA";


    private SecureUtil() {
    }

    /**
     * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
     *
     * @param algorithm 算法，支持PBE算法
     * @return {@link SecretKey}
     */
    public static SecretKey generateKey(final String algorithm) {
        return generateKey(algorithm, -1);
    }

    /**
     * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
     *
     * @param algorithm 算法，支持PBE算法
     * @param keySize   密钥长度
     * @return {@link SecretKey}
     * @since 3.1.2
     */
    public static SecretKey generateKey(final String algorithm, final int keySize) {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(StringUtil.format("init key generator failure,the reason is {}", e.getMessage()), e);
        }

        if (keySize > 0) {
            keyGenerator.init(keySize);
        }
        return keyGenerator.generateKey();
    }

    /**
     * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
     *
     * @param algorithm 算法
     * @param key       密钥，如果为{@code null} 自动生成随机密钥
     * @return {@link SecretKey}
     */
    public static SecretKey generateKey(final String algorithm, final byte[] key) {
        SecretKey secretKey;
        if (algorithm.startsWith(PBE)) {
            // PBE密钥
            secretKey = generatePBEKey(algorithm, ArrayUtil.isEmpty(key) ? null : StringUtil.str(key, CharsetUtil.CHARSET_UTF_8).toCharArray());
        } else if (algorithm.startsWith(DES)) {
            // DES密钥
            secretKey = generateDESKey(algorithm, key);
        } else {
            // 其它算法密钥
            secretKey = ArrayUtil.isEmpty(key) ? generateKey(algorithm) : new SecretKeySpec(key, algorithm);
        }
        return secretKey;
    }

    /**
     * 生成 {@link SecretKey}
     *
     * @param algorithm DES算法，包括DES、DESede等
     * @param key       密钥
     * @return {@link SecretKey}
     */
    public static SecretKey generateDESKey(final String algorithm, final byte[] key) {
        if (StringUtil.isBlank(algorithm) || !algorithm.startsWith(DES)) {
            throw new SecurityException(StringUtil.format("Algorithm [{}] is not a DES algorithm!", algorithm));
        }
        SecretKey secretKey;
        if (ArrayUtil.isEmpty(key)) {
            secretKey = generateKey(algorithm);
        } else {
            KeySpec keySpec;
            try {
                if (algorithm.startsWith(DESEDE)) {
                    //DESede兼容
                    keySpec = new DESedeKeySpec(key);
                } else {
                    keySpec = new DESKeySpec(key);
                }
            } catch (InvalidKeyException e) {
                throw new SecurityException(StringUtil.format("init des keyspec failure,the reason is {}", e.getMessage()), e);
            }
            secretKey = generateKey(algorithm, keySpec);
        }
        return secretKey;
    }

    /**
     * 生成PBE {@link SecretKey}
     *
     * @param algorithm PBE算法，包括：PBEWithMD5AndDES、PBEWithSHA1AndDESede、PBEWithSHA1AndRC2_40等
     * @param key       密钥
     * @return {@link SecretKey}
     */
    public static SecretKey generatePBEKey(final String algorithm, final char... key) {
        if (StringUtil.isBlank(algorithm) || !algorithm.startsWith(PBE)) {
            throw new SecurityException(StringUtil.format("Algorithm [{}] is not a PBE algorithm!", algorithm));
        }

        char[] keyCopy = key.clone();
        if (ArrayUtil.isEmpty(keyCopy)) {
            keyCopy = RandomUtil.randomLetters(32).toCharArray();
        }
        final PBEKeySpec keySpec = new PBEKeySpec(keyCopy);
        return generateKey(algorithm, keySpec);
    }

    /**
     * 生成 {@link SecretKey}，仅用于对称加密和摘要算法
     *
     * @param algorithm 算法
     * @param keySpec   {@link KeySpec}
     * @return {@link SecretKey}
     */
    public static SecretKey generateKey(final String algorithm, final KeySpec keySpec) {
        try {
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
            return keyFactory.generateSecret(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new SecurityException(StringUtil.format("get secret key failure,the reason is {}", e.getMessage()), e);
        }
    }

    /**
     * 生成私钥，仅用于非对称加密<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param algorithm 算法
     * @param key       密钥
     * @return 私钥 {@link PrivateKey}
     */
    public static PrivateKey generatePrivateKey(final String algorithm, final byte[] key) {
        return generatePrivateKey(algorithm, new PKCS8EncodedKeySpec(key));
    }

    /**
     * 生成私钥，仅用于非对称加密<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param algorithm 算法
     * @param keySpec   {@link KeySpec}
     * @return 私钥 {@link PrivateKey}
     * @since 3.1.1
     */
    public static PrivateKey generatePrivateKey(final String algorithm, final KeySpec keySpec) {
        final String algo = getAlgorithmAfterWith(algorithm);
        try {
            return KeyFactory.getInstance(algo).generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new SecurityException(StringUtil.format("get private key failure,the reason is {}", e.getMessage()), e);
        }
    }

    /**
     * 生成私钥，仅用于非对称加密
     *
     * @param keyStore {@link KeyStore}
     * @param alias    别名
     * @param password 密码
     * @return 私钥 {@link PrivateKey}
     */
    public static PrivateKey generatePrivateKey(final KeyStore keyStore, final String alias, final char... password) {
        try {
            return (PrivateKey) keyStore.getKey(alias, password);
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new SecurityException(StringUtil.format("get private key failure,the reason is {}", e.getMessage()), e);
        }
    }

    /**
     * 生成公钥，仅用于非对称加密<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param algorithm 算法
     * @param key       密钥
     * @return 公钥 {@link PublicKey}
     */
    public static PublicKey generatePublicKey(final String algorithm, final byte[] key) {
        return generatePublicKey(algorithm, new X509EncodedKeySpec(key));
    }

    /**
     * 生成公钥，仅用于非对称加密<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param algorithm 算法
     * @param keySpec   {@link KeySpec}
     * @return 公钥 {@link PublicKey}
     * @since 3.1.1
     */
    public static PublicKey generatePublicKey(final String algorithm, final KeySpec keySpec) {
        final String algo = getAlgorithmAfterWith(algorithm);
        try {
            return KeyFactory.getInstance(algo).generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new SecurityException(StringUtil.format("get public key failure,the reason is {}", e.getMessage()), e);
        }
    }

    /**
     * 生成用于非对称加密的公钥和私钥，仅用于非对称加密<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * @param algorithm 非对称加密算法
     * @return {@link KeyPair}
     */
    public static KeyPair generateKeyPair(final String algorithm) {
        return generateKeyPair(algorithm, DEFAULT_KEY_SIZE, null);
    }

    /**
     * 生成用于非对称加密的公钥和私钥<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * @param algorithm 非对称加密算法
     * @param keySize   密钥模（modulus ）长度
     * @return {@link KeyPair}
     */
    public static KeyPair generateKeyPair(final String algorithm, final int keySize) {
        return generateKeyPair(algorithm, keySize, null);
    }

    /**
     * 生成用于非对称加密的公钥和私钥<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * @param algorithm 非对称加密算法
     * @param keySize   密钥模（modulus ）长度
     * @param seed      种子
     * @return {@link KeyPair}
     */
    public static KeyPair generateKeyPair(final String algorithm, final int keySize, final byte[] seed) {
        final String algo = getAlgorithmAfterWith(algorithm);
        int size = "EC".equalsIgnoreCase(algo) && (keySize <= 0 || keySize > 256) ? 256 : keySize;
        KeyPairGenerator keyPairGen;
        try {
            keyPairGen = KeyPairGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(StringUtil.format("get  key pair failure,the reason is {}", e.getMessage()), e);
        }

        if (size <= 0) {
            size = DEFAULT_KEY_SIZE;
        }
        if (ArrayUtil.isEmpty(seed)) {
            keyPairGen.initialize(size);
        } else {
            final SecureRandom random = new SecureRandom(seed);
            keyPairGen.initialize(keySize, random);
        }
        return keyPairGen.generateKeyPair();
    }

    /**
     * 获取用于密钥生成的算法<br>
     * 获取XXXwithXXX算法的后半部分算法，如果为ECDSA，返回算法为EC
     *
     * @param algorithm XXXwithXXX算法
     * @return 算法
     */
    public static String getAlgorithmAfterWith(final String algorithm) {
        final int indexOfWith = algorithm.lastIndexOf("with");
        final String algo = indexOfWith > 0 ? StringUtil.sub(algorithm, indexOfWith + 4) : algorithm;
        return ECDSA.equalsIgnoreCase(algorithm) ? "EC" : algo;
    }

    /**
     * 生成签名对象，仅用于非对称加密
     *
     * @param asymmetricAlgorithm {@link ALGORITHM} 非对称加密算法
     * @param digestAlgorithm     {@link ALGORITHM} 摘要算法
     * @return {@link Signature}
     */
    public static Signature generateSignature(ALGORITHM asymmetricAlgorithm, ALGORITHM digestAlgorithm) {
        final String digestPart = Objects.isNull(asymmetricAlgorithm) ? "NONE" : digestAlgorithm.name();
        final String algorithm = StringUtil.format("{}with{}", digestPart, asymmetricAlgorithm);
        try {
            return Signature.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(StringUtil.format("init signature failure,the reason is {}", e.getMessage()), e);
        }
    }

    /**
     * 读取密钥库(Java Key Store，JKS) KeyStore文件<br>
     * KeyStore文件用于数字证书的密钥对保存<br>
     * see: http://snowolf.iteye.com/blog/391931
     *
     * @param input    {@link InputStream}
     * @param password 密码
     * @return {@link KeyStore}
     */
    public static KeyStore readJKSKeyStore(final InputStream input, final char[] password) {
        return readKeyStore("JKS", input, password);
    }

    /**
     * 读取KeyStore文件<br>
     * KeyStore文件用于数字证书的密钥对保存<br>
     * see: http://snowolf.iteye.com/blog/391931
     *
     * @param type     类型
     * @param input    {@link InputStream} 如果想从文件读取.keystore文件
     * @param password 密码
     * @return {@link KeyStore}
     */
    public static KeyStore readKeyStore(final String type, final InputStream input, final char[] password) {
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance(type);
            keyStore.load(input, password);
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
            throw new SecurityException(StringUtil.format("read keystore failure,the reason is {}", e.getMessage()), e);
        }
        return keyStore;
    }

    /**
     * 读取X.509 Certification文件<br>
     * Certification为证书文件<br>
     * see: http://snowolf.iteye.com/blog/391931
     *
     * @param in       {@link InputStream}
     * @param password 密码
     * @return {@link KeyStore}
     */
    public static Certificate readX509Certificate(InputStream input, char[] password) {
        return readCertificate("X.509", input, password);
    }

    /**
     * 读取Certification文件<br>
     * Certification为证书文件<br>
     * see: http://snowolf.iteye.com/blog/391931
     *
     * @param type     类型
     * @param in       {@link InputStream}
     * @param password 密码
     * @return {@link KeyStore}
     */
    public static Certificate readCertificate(final String type, final InputStream input, final char[] password) {
        Certificate certificate;
        try {
            certificate = CertificateFactory.getInstance(type).generateCertificate(input);
        } catch (Exception e) {
            throw new SecurityException(StringUtil.format("read certificate failure,the reason is {}", e.getMessage()), e);
        }
        return certificate;
    }

    /**
     * 获得 Certification
     *
     * @param keyStore {@link KeyStore}
     * @param alias    别名
     * @return {@link Certificate}
     */
    public static Certificate getCertificate(final KeyStore keyStore, final String alias) {
        try {
            return keyStore.getCertificate(alias);
        } catch (Exception e) {
            throw new SecurityException(StringUtil.format("read certificate from keystore failure,the reason is {}", e.getMessage()), e);
        }
    }
}
