package com.star.exception;

import com.star.string.StringUtil;

/**
 * jdbc层异常
 *
 * @author xiaoleilu
 */
public class DbException extends RuntimeException {

    public DbException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public DbException(String message) {
        super(message);
    }

    public DbException(String messageTemplate, Object... params) {
        super(StringUtil.format(messageTemplate, params));
    }

    public DbException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public DbException(Throwable throwable, String messageTemplate, Object... params) {
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
