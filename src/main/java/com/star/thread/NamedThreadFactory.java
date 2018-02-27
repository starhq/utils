package com.star.thread;

import com.star.string.StringUtil;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程创建工厂类
 *
 * @author looly
 * <p>
 * https://github.com/looly/hutool/blob/v4-master/hutool-core/src/main/java/cn/hutool/core/thread/NamedThreadFactory.java
 */
public class NamedThreadFactory implements ThreadFactory {

    /**
     * 线程名前缀
     */
    private final String prefix;

    /**
     * 线程组
     */
    private final ThreadGroup threadGroup;

    /**
     * 线程组编号
     */
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    /**
     * 是否守护线程
     */
    private final boolean isDeamon;

    /**
     * 无法捕获的异常统一处理
     */
    private final Thread.UncaughtExceptionHandler handler;

    /**
     * 构造器
     *
     * @param prefix   前缀
     * @param isDeamon 是否守护进程
     */
    public NamedThreadFactory(String prefix, boolean isDeamon) {
        this(prefix, null, isDeamon);
    }

    /**
     * 构造器
     *
     * @param prefix      前缀
     * @param threadGroup 线程组
     * @param isDeamon    是否守护进程
     */
    public NamedThreadFactory(String prefix, ThreadGroup threadGroup, boolean isDeamon) {
        this(prefix, threadGroup, isDeamon, null);
    }

    /**
     * 构造器
     *
     * @param prefix      前缀
     * @param threadGroup 线程组
     * @param isDeamon    是否守护进程
     * @param handler     为捕获异常处理器
     */
    public NamedThreadFactory(String prefix, ThreadGroup threadGroup, boolean isDeamon, Thread.UncaughtExceptionHandler handler) {//NOPMD
        this.prefix = StringUtil.isBlank(prefix) ? "WISP" : prefix;
        this.threadGroup = Objects.isNull(threadGroup) ? ThreadUtil.currentThreadGroup() : threadGroup;
        this.isDeamon = isDeamon;
        this.handler = handler;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        final Thread thread = new Thread(this.threadGroup, runnable, StringUtil.format("{}-{}", prefix, threadNumber.getAndIncrement()));

        if (thread.isDaemon()) {
            if (!isDeamon) {
                thread.setDaemon(false);
            }
        } else {
            if (isDeamon) {
                thread.setDaemon(true);
            }
        }

        if (handler != null) {
            thread.setUncaughtExceptionHandler(handler);
        }
        if (Thread.NORM_PRIORITY != thread.getPriority()) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }

        return thread;
    }
}
