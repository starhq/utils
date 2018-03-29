package com.star.exception;

import com.star.string.StringUtil;

/**
 * 包装io异常
 *
 * @author xiaoleilu
 */
public class HttpException extends RuntimeException {

    public HttpException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String messageTemplate, Object... params) {
        super(StringUtil.format(messageTemplate, params));
    }

    public HttpException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public HttpException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtil.format(messageTemplate, params), throwable);
    }

    /**
     * 导致这个异常的异常是否是指定类型的异常
     *
     * @param clazz 异常类
     * @return 是否为指定类型异常
     */
    public boolean causeInstanceOf(Class<? extends Throwable> clazz) {
        Throwable cause = this.getCause();
        return null != cause && clazz.isInstance(cause);
    }
}
