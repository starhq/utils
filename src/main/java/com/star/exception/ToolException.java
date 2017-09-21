package com.star.exception;

/**
 * 工具类的异常封装
 * <p>
 * Created by win7 on 2017/5/20.
 */
public class ToolException extends RuntimeException {

    public ToolException() {
    }

    public ToolException(String message) {
        super(message);
    }

    public ToolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToolException(Throwable cause) {
        super(cause);
    }

    public ToolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
