package com.star.log;

import com.star.log.level.Level;
import com.star.string.StringUtil;

import java.io.Serializable;

/**
 * 抽象日志类<br>
 * 实现了一些通用的接口
 *
 * @author Looly
 */
public abstract class AbstractLog implements Log, Serializable {

    private static final long serialVersionUID = 2672857717476329337L;

    @Override
    public boolean isEnabled(Level level) {
        switch (level) {
            case TRACE:
                return isTraceEnabled();
            case DEBUG:
                return isDebugEnabled();
            case INFO:
                return isInfoEnabled();
            case WARN:
                return isWarnEnabled();
            case ERROR:
                return isErrorEnabled();
            default:
                throw new Error(StringUtil.format("Can not identify level: {}", level));
        }
    }

    @Override
    public void trace(Throwable t) {
        this.trace(t, t.getMessage());
    }

    @Override
    public void debug(Throwable t) {
        this.debug(t, t.getMessage());
    }

    @Override
    public void info(Throwable t) {
        this.info(t, t.getMessage());
    }

    @Override
    public void warn(Throwable t) {
        this.warn(t, t.getMessage());
    }

    @Override
    public void error(Throwable t) {
        this.error(t, t.getMessage());
    }
}
