package com.star.net.httpcomponent;


import com.star.collection.CollectionUtil;
import com.star.collection.map.MapUtil;
import com.star.exception.HttpException;
import com.star.exception.ToolException;
import com.star.io.CharsetUtil;
import com.star.string.StringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 使用httpcomponent来做网络请求
 * <p>
 * Created by starhq on 2016/10/31.
 */
public final class HttpUtil {


    private HttpUtil() {
    }

    /**
     * get请求
     *
     * @param url 连接
     * @return 响应信息
     */
    public static String get(final String url) {
        return get(url, null, null);
    }

    /**
     * get请求
     *
     * @param url    连接
     * @param params 参数值
     * @return 响应信息
     */
    public static String getWithParams(final String url, final Map<String, Object> params) {
        return get(url, null, params);
    }

    /**
     * get请求
     *
     * @param url     连接
     * @param headers 头信息
     * @return 响应信息
     */
    public static String getWithHeaders(final String url, final Map<String, Object> headers) {
        return get(url, headers, null);
    }

    /**
     * get请求
     *
     * @param url     连接
     * @param headers 头信息
     * @param params  参数值
     * @return 响应信息
     */
    public static String get(final String url, final Map<String, Object> headers, final Map<String, Object> params) {
        final HttpGet httpGet = new HttpGet(createParamUrl(url, params));
        if (!MapUtil.isEmpty(headers)) {
            for (final Map.Entry<String, Object> entry : headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), StringUtil.str(entry.getValue()));
            }
        }
        return execute(httpGet);
    }

    /**
     * delete请求
     *
     * @param url 连接
     * @return 响应信息
     */
    public static String delete(final String url) {
        return delete(url, null, null);
    }

    /**
     * delete请求
     *
     * @param url    连接
     * @param params 参数值
     * @return 响应信息
     */
    public static String deleteWithParams(final String url, final Map<String, Object> params) {
        return delete(url, null, params);
    }

    /**
     * delete请求
     *
     * @param url     连接
     * @param headers 头信息
     * @return 响应信息
     */
    public static String deleteWithHeaders(final String url, final Map<String, Object> headers) {
        return delete(url, headers, null);
    }

    /**
     * delete请求
     *
     * @param url     连接
     * @param headers 头信息
     * @param params  参数值
     * @return 响应信息
     */
    public static String delete(final String url, final Map<String, Object> headers, final Map<String, Object> params) {
        final HttpDelete httpDelete = new HttpDelete(createParamUrl(url, params));
        if (!MapUtil.isEmpty(headers)) {
            for (final Map.Entry<String, Object> entry : headers.entrySet()) {
                httpDelete.addHeader(entry.getKey(), StringUtil.str(entry.getValue()));
            }
        }
        return execute(httpDelete);
    }

    /**
     * post请求
     *
     * @param url 连接
     * @return 响应信息
     */
    public static String post(final String url) {
        return post(url, null, null);
    }

    /**
     * post请求
     *
     * @param url    连接
     * @param params 参数值
     * @return 响应信息
     */
    public static String postWithParams(final String url, final Map<String, Object> params) {
        return post(url, null, params);
    }

    /**
     * post请求
     *
     * @param url     连接
     * @param headers 头信息
     * @return 响应信息
     */
    public static String postWithHeaders(final String url, final Map<String, Object> headers) {
        return post(url, headers, null);
    }

    /**
     * post请求
     *
     * @param url     连接
     * @param headers 头信息
     * @param params  参数值
     * @return 响应信息
     */
    public static String post(final String url, final Map<String, Object> headers, final Map<String, Object> params) {
        final HttpPost httpPost = new HttpPost(url);
        if (!MapUtil.isEmpty(headers)) {
            for (final Map.Entry<String, Object> entry : headers.entrySet()) {
                httpPost.addHeader(entry.getKey(), StringUtil.str(entry.getValue()));
            }
        }
        final List<NameValuePair> nameValuePairs = getNameValuePairs(params);
        if (!CollectionUtil.isEmpty(nameValuePairs)) {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, CharsetUtil.CHARSET_UTF_8));
        }
        return execute(httpPost);
    }

    /**
     * put请求
     *
     * @param url 连接
     * @return 响应信息
     */
    public static String put(final String url) {
        return put(url, null, null);
    }

    /**
     * put请求
     *
     * @param url    连接
     * @param params 参数值
     * @return 响应信息
     */
    public static String putWithParams(final String url, final Map<String, Object> params) {
        return put(url, null, params);
    }

    /**
     * post请求
     *
     * @param url     连接
     * @param headers 头信息
     * @return 响应信息
     */
    public static String putWithHeaders(final String url, final Map<String, Object> headers) {
        return put(url, headers, null);
    }

    /**
     * put请求
     *
     * @param url     连接
     * @param headers 头信息
     * @param params  参数值
     * @return 响应信息
     */
    public static String put(final String url, final Map<String, Object> headers, final Map<String, Object> params) {
        final HttpPut httpPut = new HttpPut(url);
        if (!MapUtil.isEmpty(headers)) {
            for (final Map.Entry<String, Object> entry : headers.entrySet()) {
                httpPut.addHeader(entry.getKey(), StringUtil.str(entry.getValue()));
            }
        }
        final List<NameValuePair> nameValuePairs = getNameValuePairs(params);
        if (!CollectionUtil.isEmpty(nameValuePairs)) {
            httpPut.setEntity(new UrlEncodedFormEntity(nameValuePairs, CharsetUtil.CHARSET_UTF_8));
        }
        return execute(httpPut);
    }

    /**
     * post请求
     *
     * @param url     连接
     * @param headers 头信息
     * @param json    参数值
     * @return 响应信息
     */
    public static String postWithJson(final String url, final Map<String, Object> headers, final String json) {
        final HttpPost httpPost = new HttpPost(url);
        if (!MapUtil.isEmpty(headers)) {
            for (final Map.Entry<String, Object> entry : headers.entrySet()) {
                httpPost.addHeader(entry.getKey(), StringUtil.str(entry.getValue()));
            }
        }
        httpPost.setEntity(new StringEntity(json, CharsetUtil.CHARSET_UTF_8));
        return execute(httpPost);
    }

    /**
     * post请求
     *
     * @param url     连接
     * @param headers 头信息
     * @param json    参数值
     * @return 响应信息
     */
    public static String putWithJson(final String url, final Map<String, Object> headers, final String json) {
        final HttpPut httpPut = new HttpPut(url);
        if (!MapUtil.isEmpty(headers)) {
            for (final Map.Entry<String, Object> entry : headers.entrySet()) {
                httpPut.addHeader(entry.getKey(), StringUtil.str(entry.getValue()));
            }
        }
        httpPut.setEntity(new StringEntity(json, CharsetUtil.CHARSET_UTF_8));
        return execute(httpPut);
    }


    /**
     * 创建带参数的 URL
     *
     * @param url    无参URL
     * @param params 参数
     * @return String 带参数URL
     */
    private static String createParamUrl(final String url, final Map<String, Object> params) {
        final StringBuilder builder = StringUtil.builder(url);
        if (!MapUtil.isEmpty(params)) {
            final List<NameValuePair> pairs = getNameValuePairs(params);
            try {
                builder.append('?').append(EntityUtils.toString(new UrlEncodedFormEntity(pairs, CharsetUtil
                        .CHARSET_UTF_8)));
            } catch (IOException e) {
                throw new ToolException(StringUtil.format("set params on get or delete method failure: {}", e
                        .getMessage()), e);
            }
        }
        return builder.toString();
    }

    /**
     * 键值对封装成namevaluepair集合
     *
     * @param params 键值对
     * @return http参数
     */
    private static List<NameValuePair> getNameValuePairs(final Map<String, Object> params) {
        List<NameValuePair> pairs;
        if (MapUtil.isEmpty(params)) {
            pairs = Collections.emptyList();
        } else {
            pairs = new ArrayList<>(params.size());
            pairs.addAll(params.entrySet().stream().map(entry -> new BasicNameValuePair(entry.getKey(), entry
                    .getValue().toString())).collect(Collectors.toList()));
        }
        return pairs;

    }

    /**
     * 执行 HTTP 请求
     *
     * @return String
     */
    private static String execute(final HttpRequestBase request) {
        final CloseableHttpClient httpClient = HttpConnectionManagerSingleton.INSTANCE.getHttpClient();
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            final HttpEntity entity = response.getEntity();
            return Objects.isNull(entity) ? StringUtil.EMPTY : EntityUtils.toString(entity, CharsetUtil.CHARSET_UTF_8);
        } catch (IOException e) {
            throw new HttpException(StringUtil.format("execute http request failure,the reason is {}", e.getMessage()), e);
        }
    }
}
