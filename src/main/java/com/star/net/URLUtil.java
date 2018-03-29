package com.star.net;

import com.star.exception.IORuntimeException;
import com.star.io.IoUtil;
import com.star.string.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * url工具类
 *
 * @author http://git.oschina.net/loolly/hutool
 */
public final class URLUtil {

    private URLUtil() {
    }


    /**
     * 创建url
     *
     * @param url url字符串
     * @return url
     */
    public static URL url(final String url) {
        try {
            return new URL(Objects.requireNonNull(url));
        } catch (MalformedURLException e) {
            throw new IORuntimeException(StringUtil.format("create url failure,the reason is :{}", e.getMessage()), e);
        }
    }

    /**
     * 获得文件的url
     *
     * @param file 文件
     * @return url
     */
    public static URL getURL(final File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new IORuntimeException(StringUtil.format("get file's url failue,the reason is: {}", e.getMessage()), e);
        }
    }

    /**
     * 格式化url连接
     *
     * @param url url字符串
     * @return 完整路径
     */
    public static String formatUrl(final String url) {
        return url.startsWith("http://") || url.startsWith("https://") ? url : "http://" + url;
    }

    /**
     * 补全相对路径
     *
     * @param baseUrl      基本路径
     * @param relativePath 相对路径
     * @return 完整路径
     */
    public static String complateUrl(final String baseUrl, final String relativePath) {
        final String fullUrl = formatUrl(baseUrl);

        final URL absoluteUrl = url(fullUrl);
        try {
            final URL parseUrl = new URL(absoluteUrl, relativePath);
            return parseUrl.toString();
        } catch (MalformedURLException e) {
            throw new IORuntimeException(StringUtil.format("complate url failure,the reason is: {}", e.getMessage()), e);
        }
    }

    /**
     * 编码url
     *
     * @param url     要编码的url
     * @param charset 编码
     * @return 编码后的url
     */
    public static String encode(final String url, final String charset) {
        try {
            return URLEncoder.encode(url, charset);
        } catch (UnsupportedEncodingException e) {
            throw new IORuntimeException(StringUtil.format("encode url failue,the system doesn't support {}", charset), e);
        }
    }

    /**
     * 解码url
     *
     * @param url     要解码的url
     * @param charset 编码
     * @return 解码后的url
     */
    public static String decode(final String url, final String charset) {
        try {
            return URLDecoder.decode(url, charset);
        } catch (UnsupportedEncodingException e) {
            throw new IORuntimeException(StringUtil.format("encode url failue,the system doesn't support {}", charset), e);
        }
    }

    /**
     * 获得path部分
     *
     * @param uriStr uri字符串
     * @return uri的path部分
     */
    public static String getPath(final String uriStr) {
        try {
            final URI uri = new URI(uriStr);
            return uri.getPath();
        } catch (URISyntaxException e) {
            throw new IORuntimeException(StringUtil.format("get uri's path failure,the reason is: {}", e.getMessage()), e);
        }
    }

    /**
     * 检测是否https
     *
     * @param url 需要检测的url
     * @return 是否https
     */
    public static boolean isHttps(final String url) {
        return url.toLowerCase().startsWith("https");
    }

    /**
     * 转URL为URI
     *
     * @param url URL
     * @return URI
     */
    public static URI toURI(final URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new IORuntimeException(StringUtil.format("get uri from url failure,the reason is {}", e.getMessage()), e);
        }
    }

    /**
     * 转字符串为URI
     *
     * @param location 字符串路径
     * @return URI
     */
    public static URI toURI(final String location) {
        try {
            return new URI(location.replace(" ", "%20"));
        } catch (URISyntaxException e) {
            throw new IORuntimeException(StringUtil.format("location string to uri failure,the reason is {}", e.getMessage()), e);
        }
    }


    /**
     * 从URL中获取流
     *
     * @param url url
     * @return 输入流
     */
    public static InputStream getStream(final URL url) {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new IORuntimeException(StringUtil.format("get inputstream from url failure,the reason is {}", e.getMessage()), e);
        }
    }

    /**
     * 获得Reader
     *
     * @param url     url
     * @param charset 编码
     * @return BufferedReader
     */
    public static BufferedReader getReader(final URL url, final Charset charset) {
        return IoUtil.getReader(getStream(url), charset);
    }


}
