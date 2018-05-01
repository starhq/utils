package com.star.net.http;

import com.star.collection.CollectionUtil;
import com.star.string.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * http基类
 *
 * @author http://git.oschina.net/loolly/hutool
 */
public abstract class AbstractHttpBase<T> {

    /**
     * 存储头信息
     */
    protected transient Map<String, List<String>> headers = new ConcurrentHashMap<>();

    /**
     * http版本
     */
    protected String httpVersion = HttpVersion.HTTP_1_1.toString();

    /**
     * 字符集
     */
    protected String charset = "UTF-8";

    /**
     * 请求体
     */
    protected transient String body;

    /**
     * http版本常量
     */
    enum HttpVersion {
        HTTP_1_0("HTTP/1.0"), HTTP_1_1("HTTP/1.1");

        /**
         * http版本字符串
         */
        private String value;

        HttpVersion(final String value) {
            this.value = value;
        }

        /**
         * 这都要注释啊，满刚的
         */
        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * 空构造方法
     */
    protected AbstractHttpBase() {
        // 空的，啥都不用做
    }

    /**
     * 根据name获得头信息
     *
     * @param name http头
     * @return 头的字符串
     */
    public String getHeader(final HttpHeader name) {
        final List<String> values = headers.get(name.toString());
        return CollectionUtil.isEmpty(values) ? "" : values.get(0);
    }

    /**
     * 删除头信息
     *
     * @param name 头信息字符串
     */
    public void removeHeader(final String name) {
        if (!StringUtil.isBlank(name)) {
            headers.remove(name);
        }
    }

    /**
     * 设置头信息
     *
     * @param name       头信息
     * @param value      头信息的值
     * @param isOverride 是否覆盖
     * @return 本身
     */
    @SuppressWarnings("unchecked")
    public T setHeader(final String name, final String value, final boolean isOverride) {
        if (null != name && null != value) {
            final List<String> values = headers.get(name);
            if (isOverride || CollectionUtil.isEmpty(values)) {
                final List<String> valueList = new ArrayList<>(1);
                valueList.add(value);
                headers.put(name, valueList);
            } else {
                values.add(value);
            }
        }
        return (T) this;
    }

    /**
     * 设置请求头
     *
     * @param name       头信息
     * @param value      头信息的值
     * @param isOverride 是否覆盖
     * @return 本身
     */
    public T setHeader(final HttpHeader name, final String value, final boolean isOverride) {
        return setHeader(name.toString(), value, isOverride);
    }

    /**
     * 获取头信息
     *
     * @return 头信息集合
     */
    public Map<String, List<String>> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    /**
     * 返回http版本
     *
     * @return http版本
     */
    public String getHttpVersion() {
        return httpVersion;
    }

    /**
     * 设置httpversion
     *
     * @param httpVersion http版本
     * @return 本身
     */
    @SuppressWarnings("unchecked")
    public T setHttpVersion(final HttpVersion httpVersion) {
        this.httpVersion = httpVersion.toString();
        return (T) this;
    }

    /**
     * 返回字符集
     *
     * @return 编码
     */
    public String getCharset() {
        return charset;
    }

    /**
     * 设置字符集
     *
     * @param charset 编码字符串
     * @return 本身
     */
    @SuppressWarnings("unchecked")
    public T setCharset(final String charset) {
        this.charset = charset;
        return (T) this;
    }

    /**
     * 这他喵的都要注释
     */
    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder(39);
        stringBuilder.append("Request Headers: ").append(StringUtil.CRLF);
        for (final Entry<String, List<String>> entry : this.headers.entrySet()) {
            stringBuilder.append("    ").append(entry).append(StringUtil.CRLF);
        }

        stringBuilder.append("Request Body: ").append(StringUtil.CRLF).append("    ").append(this.body)
                .append(StringUtil.CRLF);

        return stringBuilder.toString();
    }

}
