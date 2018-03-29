package com.star.net.httpcomponent;

import com.star.properties.Config;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * httpClient 连接管理器
 *
 * @author pengdh
 * @date 2017/10/28
 */
public enum HttpConnectionManagerSingleton {
    INSTANCE;
    /**
     * 设置最大连接数
     */
    private static final int MAX_TOTAL = 100;
    /**
     * 设置每个路由基础上的最大连接数
     */
    private static final int DEFAULT_MAX_PER_ROUTE = 100;
    /**
     * 超时时间
     */
    private static final int MAX_TIMEOUT = 60000;
    /**
     * 连接活动后验证
     */
    private static final int VALIDATE_AFTER_INACTIVITY = 1000;
    /**
     * 池连接管理器
     */
    private CloseableHttpClient httpClient;

    HttpConnectionManagerSingleton() {
        init();
    }

    private void init() {
        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SslSelfSignedSingleton.INSTANCE.getSslConnectionSocketFactory()).build();
        // 连接池管理器
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        final Config config = new Config("config.properties");
        // 设置最大连接数
        connectionManager.setMaxTotal(config.getInt("MAX_TOTAL", MAX_TOTAL));
        // 设置每个路由基础上的最大连接数
        connectionManager.setDefaultMaxPerRoute(config.getInt("DEFAULT_MAX_PER_ROUTE", DEFAULT_MAX_PER_ROUTE));
        //连接活动后验证
        connectionManager.setValidateAfterInactivity(config.getInt("VALIDATE_AFTER_INACTIVITY", VALIDATE_AFTER_INACTIVITY));

        RequestConfig requestConfig = RequestConfig.custom()
//                建立连接超时时间（毫秒）
                .setConnectTimeout(config.getInt("MAX_TIMEOUT", MAX_TIMEOUT))
//                读取数据超时时间（毫秒）
                .setSocketTimeout(config.getInt("MAX_TIMEOUT", MAX_TIMEOUT))
                // 从连接池获取连接的等待时间（毫秒）
                .setConnectionRequestTimeout(config.getInt("MAX_TIMEOUT", MAX_TIMEOUT))
                .build();
        httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig).build();
    }

    /**
     * 获取http客户端
     *
     * @return {@link CloseableHttpClient}
     */
    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }
}
