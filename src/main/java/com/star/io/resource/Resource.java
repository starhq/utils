package com.star.io.resource;

import com.star.exception.IORuntimeException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * 资源接口定义
 *
 * @author looly
 */
public interface Resource {

    /**
     * 获得资源的url
     *
     * @return url
     */
    URL getUrl();

    /**
     * 获得资源的输入流
     *
     * @return 输入流
     */
    InputStream getStream();

    /**
     * 按指定编码获取资源的Reader
     *
     * @param charset 编码
     * @return Reader
     */
    BufferedReader getReader(Charset charset);

    /**
     * 按指定编码读取资源文件
     *
     * @param charset 指定编码
     * @return 内容
     */
    String readString(Charset charset);

    /**
     * 按utf8读取资源文件内容
     *
     * @return 内容
     * @throws IORuntimeException io异常
     */
    String readUtf8Str() throws IORuntimeException;

    /**
     * 读取资源文件内容
     *
     * @return 内容
     * @throws IORuntimeException io异常
     */
    byte[] readBytes() throws IORuntimeException;
}
