package com.star.security;

/**
 * 加密算法
 *
 * @author starhq
 */
public enum ALGORITHM {
    //digest
    MD2("MD2"),
    MD5("MD5"),
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512"),


    //hmac
    HmacMD5("HmacMD5"),
    HmacSHA1("HmacSHA1"),
    HmacSHA256("HmacSHA256"),
    HmacSHA384("HmacSHA384"),
    HmacSHA512("HmacSHA512"),


    //非对称算法类型
    RSA("RSA"),
    DSA("DSA"),
    EC("EC"),


    //对称算法类型
    AES("AES"),
    ARCFOUR("ARCFOUR"),
    Blowfish("Blowfish"),
    /**
     * 默认的DES加密方式：DES/ECB/PKCS5Padding
     */
    DES("DES"),
    /**
     * 3DES算法，默认实现为：DESede/CBC/PKCS5Padding
     */
    DESede("DESede"),
    RC2("RC2"),

    PBEWithMD5AndDES("PBEWithMD5AndDES"),
    PBEWithSHA1AndDESede("PBEWithSHA1AndDESede"),
    PBEWithSHA1AndRC2_40("PBEWithSHA1AndRC2_40"),


    // The RSA signature algorithm
    NONEwithRSA("NONEwithRSA"),

    // The MD2/MD5 with RSA Encryption signature algorithm
    MD2withRSA("MD2withRSA"),
    MD5withRSA("MD5withRSA"),

    // The signature algorithm with SHA-* and the RSA
    SHA1withRSA("SHA1withRSA"),
    SHA256withRSA("SHA256withRSA"),
    SHA384withRSA("SHA384withRSA"),
    SHA512withRSA("SHA512withRSA"),

    // The Digital Signature Algorithm
    NONEwithDSA("NONEwithDSA"),
    // The DSA with SHA-1 signature algorithm
    SHA1withDSA("SHA1withDSA"),

    // The ECDSA signature algorithms
    NONEwithECDSA("NONEwithECDSA"),
    SHA1withECDSA("SHA1withECDSA"),
    SHA256withECDSA("SHA256withECDSA"),
    SHA384withECDSA("SHA384withECDSA"),
    SHA512withECDSA("SHA512withECDSA");


    private String value;

    /**
     * 构造
     *
     * @param value 算法字符串表示
     */
    ALGORITHM(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
