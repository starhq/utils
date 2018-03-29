package com.star.net.httpcomponent;

import com.star.exception.HttpException;
import com.star.string.StringUtil;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * 如果 https 使用自签名证书（不是第三方机构颁发的证书），无法通过验证；
 * 因此需要创建一个SSL连接，接受自签名证书的信任策略，使其通过验证。
 *
 * @author pengdh
 * @date 2017/10/28
 */
public enum SslSelfSignedSingleton {
    INSTANCE;
    /**
     * 安全连接工厂
     */
    private SSLConnectionSocketFactory socketFactory;

    SslSelfSignedSingleton() {
        init();
    }

    private void init() {
        SSLContext sslContext;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(TrustSelfSignedStrategy.INSTANCE).build();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new HttpException(StringUtil.format("init sslContent failure,the reason is {}", e.getMessage()), e);
        }
        socketFactory = new SSLConnectionSocketFactory(sslContext,
                NoopHostnameVerifier.INSTANCE);
    }

    /**
     * 获得安全连接工厂
     *
     * @return {@link SSLConnectionSocketFactory}
     */
    public SSLConnectionSocketFactory getSslConnectionSocketFactory() {
        return socketFactory;
    }
}
