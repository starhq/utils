package com.star.net.http;

import com.star.collection.array.ArrayUtil;
import com.star.collection.iter.IterUtil;
import com.star.collection.map.MapUtil;
import com.star.exception.HttpException;
import com.star.id.NessUUID;
import com.star.io.CharsetUtil;
import com.star.io.IoUtil;
import com.star.string.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;

/**
 * http请求类
 *
 * @author http://git.oschina.net/loolly/hutool
 */
public class HttpRequest extends AbstractHttpBase<HttpRequest> {

    /**
     * 分隔文本的开始 上传用
     */
    private static final String BOUNDARY = "--------------------starimba_"
            + NessUUID.toString(UUID.randomUUID()).replace("-", "");
    /**
     * 分隔文本的结束 上传用
     */
    private static final byte[] BOUNDARY_END = StringUtil.format("--{}--\r\n", BOUNDARY).getBytes(CharsetUtil.DEFAULT);
    /**
     * 不知道派啥用处，好像是文件上传下载描述用的
     */
    private static final String CD_TEMPLATE = "Content-Disposition: form-data; name=\"{}\"\r\n\r\n";
    /**
     * 不知道派啥用处，好像是文件上传下载描述用的
     */
    private static final String CD_FILE_TEMPLATE = "Content-Disposition: form-data; name=\"{}\"; filename=\"{}\"\r\n";
    /**
     * 不知道派啥用处，好像是文件上传下载描述用的
     */
    private static final String CT_MULTIPART_PREFIX = "multipart/form-data; boundary=";
    /**
     * 不知道派啥用处，好像是文件上传下载描述用的
     */
    private static final String CT_FILE_TEMPLATE = "Content-Type: {}\r\n\r\n";

    /**
     * 地址
     */
    private transient String url;
    /**
     * 请求方法
     */
    private transient HttpMethod method = HttpMethod.GET;
    /**
     * 默认超时
     */
    private transient int timeout = -1;
    /**
     * 存储表单数据
     */
    protected transient Map<String, Object> form;
    /**
     * 文件表单对象，用于文件上传
     */
    protected transient Map<String, File> fileForm;

    /**
     * 连接对象
     */
    private transient HttpConnection httpConnection;

    /**
     * 构造方法
     *
     * @param url 链接
     */
    public HttpRequest(final String url) {
        super();
        this.url = url;
    }

    /**
     * new 请求，同时设置方法
     *
     * @param url    链接
     * @param method http方法
     * @return 本身
     */
    public static HttpRequest getRequest(final String url, final HttpMethod method) {
        return new HttpRequest(url).setMethod(method);
    }

    /**
     * 设置请求方法
     *
     * @param method http方法
     * @return 本身
     */
    public HttpRequest setMethod(final HttpMethod method) {
        this.method = method;
        return this;
    }

    /**
     * 设置contenttype
     *
     * @param contentType 类型
     * @return 本身
     */
    public HttpRequest contentType(final String contentType) {
        setHeader(HttpHeader.CONTENT_TYPE, contentType, true);
        return this;
    }

    /**
     * 设置是否常连接
     *
     * @param isKeepAlive 是否保持存活
     * @return 本身
     */
    public HttpRequest keepAlive(final boolean isKeepAlive) {
        setHeader(HttpHeader.CONNECTION, isKeepAlive ? "Keep-Alive" : "Close", true);
        return this;
    }

    /**
     * 是否为长连接
     *
     * @return 是否长链接
     */
    public boolean isKeepAlive() {
        final String connection = getHeader(HttpHeader.CONNECTION);

        return Objects.isNull(connection) ? !HttpVersion.HTTP_1_0.toString().equalsIgnoreCase(httpVersion)
                : !"close".equalsIgnoreCase(connection);
    }

    /**
     * 获取内容长度
     *
     * @return 内容长度
     */
    public String contentLength() {
        return getHeader(HttpHeader.CONTENT_LENGTH);
    }

    /**
     * 设置内容长度
     *
     * @param value 长度
     * @return 本身
     */
    public HttpRequest contentLength(final int value) {
        setHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(value), true);
        return this;
    }

    /**
     * 设置表单数据
     *
     * @param name  键
     * @param value 值
     * @return 本身
     */
    public HttpRequest setForm(final String name, final Object value) {
        this.body = "";

        if (value instanceof File) {
            return this.setForm(name, (File) value);
        } else if (this.form == null) {
            form = new HashMap<>();
        }

        String strValue;
        if (value instanceof List) {
            strValue = IterUtil.join((List<?>) value, ",");
        } else if (ArrayUtil.isArray(value)) {
            strValue = StringUtil.join(",", (Object[]) value);
        } else if (value instanceof String) {
            strValue = (String) value;
        } else {
            strValue = value == null ? "" : value.toString();
        }

        form.put(name, strValue);
        return this;
    }

    /**
     * 文件表单项
     *
     * @param name 键
     * @param file 文件
     * @return 本身
     */
    public HttpRequest setForm(final String name, final File file) {
        if (!Objects.isNull(file)) {
            if (!isKeepAlive()) {
                keepAlive(true);
            }

            if (MapUtil.isEmpty(fileForm)) {
                fileForm = new HashMap<String, File>();
            }
            // 文件对象
            this.fileForm.put(name, file);
        }
        return this;
    }

    /**
     * 获取表单数据
     *
     * @return map
     */
    public Map<String, Object> getForm() {
        return form;
    }

    /**
     * 设置map类型表单数据
     *
     * @param formMap map
     * @return 本身
     */
    public HttpRequest setForm(final Map<String, Object> formMap) {
        for (final Map.Entry<String, Object> entry : formMap.entrySet()) {
            setForm(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * 设置内容主体
     *
     * @param body q请求体
     * @return 本身
     */
    public HttpRequest body(final String body) {
        this.body = body;
        this.form = Collections.emptyMap(); // 当使用body时，废弃form的使用
        contentLength(body.length());
        return this;
    }

    /**
     * 设置超时
     *
     * @param milliseconds 超时时间
     * @return 本身
     */
    public HttpRequest setTimeout(final int milliseconds) {
        this.timeout = milliseconds;
        return this;
    }

    /**
     * 执行请求
     *
     * @return httpresponse
     */
    public HttpResponse execute() {
        if (HttpMethod.GET.equals(method)) {
            this.url = StringUtil.isBlank(body) ? HttpUtil.urlWithForm(url, this.form)
                    : HttpUtil.urlWithForm(url, this.body);
        }

        this.httpConnection = new HttpConnection(url, method, timeout).header(headers, true).initconn();

        try {
            if (HttpMethod.PUT.equals(method) || HttpMethod.POST.equals(method)) {
                send();
            } else {
                this.httpConnection.connect();
            }
        } catch (IOException e) {
            throw new HttpException(StringUtil.format("send request failue,the reason is: {}", e.getMessage()), e);
        }

        final HttpResponse httpResponse = HttpResponse.readResponse(httpConnection);

        this.httpConnection.disconnect();
        return httpResponse;
    }

    /**
     * basic验证
     *
     * @param username 用户名
     * @param password 密码
     * @return 本身
     */
    public HttpRequest basicAuth(final String username, final String password) {
        final String data = username.concat(":").concat(password);
        String base64;
        try {
            base64 = new String(Base64.getDecoder().decode(data), charset);
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(StringUtil.format("basic verify failue,system not supports {} charset", charset),
                    e);
        }
        setHeader("Authorization", "Basic " + base64, true);
        return this;
    }

    /**
     * 发送数据流
     */
    private void send() {
        if (MapUtil.isEmpty(fileForm)) {
            // Write的时候会优先使用body中的内容，write时自动关闭OutputStream
            final String content = StringUtil.isBlank(this.body) ? HttpUtil.toParams(this.form) : this.body;
            final OutputStream output = this.httpConnection.getOutputStream();
            try {
                IoUtil.write(output, this.charset, false, content);
            } finally {
                IoUtil.close(output);
            }
        } else {
            sendMltipart();
        }
    }

    /**
     * 发送多组件请求,上传文件用
     */
    private void sendMltipart() {
        setMultipart();
        this.httpConnection.disableCache();
        final OutputStream out = this.httpConnection.getOutputStream();
        try {
            writeFileForm(out);
            writeForm(out);
            formEnd(out);
        } finally {
            IoUtil.close(out);
        }
    }

    /**
     * 普通字符串数据
     *
     * @param output 输出流
     */
    private void writeForm(final OutputStream output) {
        if (!MapUtil.isEmpty(form)) {
            final StringBuilder builder = new StringBuilder();
            for (final Entry<String, Object> entry : this.form.entrySet()) {
                builder.append("--").append(BOUNDARY).append(StringUtil.CRLF)
                        .append(StringUtil.format(CD_TEMPLATE, entry.getKey())).append(entry.getValue())
                        .append(StringUtil.CRLF);
            }
            IoUtil.write(output, this.charset, true, builder.toString());
        }
    }

    /**
     * 发送文件对象表单
     *
     * @param output 输出流
     */
    private void writeFileForm(final OutputStream output) {
        for (final Entry<String, File> entry : this.fileForm.entrySet()) {
            final File file = entry.getValue();
            final StringBuilder builder = new StringBuilder();
            builder.append("--").append(BOUNDARY).append(StringUtil.CRLF)
                    .append(StringUtil.format(CD_FILE_TEMPLATE, entry.getKey(), file.getName()))
                    .append(StringUtil.format(CT_FILE_TEMPLATE, HttpUtil.getMimeType(file.getName())));
            IoUtil.write(output, this.charset, true, builder.toString());
            try {
                IoUtil.copy(new FileInputStream(file), output, 0);
            } catch (FileNotFoundException e) {
                throw new HttpException(
                        StringUtil.format("send form object failure,the file {} not exists", file.getName()), e);
            }
            IoUtil.write(output, this.charset, true, StringUtil.CRLF);

        }
    }

    /**
     * 添加结尾数据
     *
     * @param output 输出流
     */
    private void formEnd(final OutputStream output) {
        try {
            output.write(BOUNDARY_END);
            output.flush();
        } catch (IOException e) {
            throw new HttpException(StringUtil.format("upload file failure,the reason is: {}", e.getMessage()), e);
        }
    }

    /**
     * 设置表单类型为Multipart（文件上传）
     */
    private void setMultipart() {
        this.httpConnection.header(HttpHeader.CONTENT_TYPE, CT_MULTIPART_PREFIX + BOUNDARY, true);
    }
}
