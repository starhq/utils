package com.star.test;

import com.star.log.Log;
import com.star.log.LogFactory;
import com.star.log.level.Level;
import org.junit.Test;

public class LogTest {

    @Test
    public void test() {
        Log log = LogFactory.get();
        // 自动选择日志实现
        log.trace("This is {} log", Level.TRACE);
        log.debug("This is {} log", Level.DEBUG);
        log.info("This is {} log", Level.INFO);
        log.warn("This is {} log", Level.WARN);
        log.error("This is {} log", Level.ERROR);
    }
}
