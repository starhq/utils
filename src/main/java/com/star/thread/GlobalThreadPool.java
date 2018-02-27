package com.star.thread;

import com.star.string.StringUtil;
import com.sun.xml.internal.ws.util.UtilException;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 全局线程池
 */
public final class GlobalThreadPool {

    /**
     * 执行器
     */
    private static ExecutorService executor;

    /**
     * 锁
     */
    private static Object lock = new Object();

    static {
        init();
    }

    private GlobalThreadPool() {
    }

    /**
     * 初始化池
     */
    public static void init() {
        synchronized (lock) {
            if (executor != null) {
                executor.shutdown();
            }
            executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
                    new SynchronousQueue<>(), new NamedThreadFactory(StringUtil.EMPTY, false));
        }
    }

    /**
     * 关闭池
     *
     * @param isNow 现在关闭
     */
    public static void shutdown(final boolean isNow) {
        synchronized (lock) {
            if (executor != null) {
                if (isNow) {
                    executor.shutdownNow();
                } else {
                    executor.shutdown();
                }
            }
        }
    }

    /**
     * 执行线程
     *
     * @param runnable 线程
     */
    public static void execute(final Runnable runnable) {
        try {
            executor.execute(runnable);
        } catch (Exception e) {
            throw new UtilException(StringUtil.format("executing runnable failure: {}", e.getMessage()), e);
        }
    }

    /**
     * 执行线程(有返回值)
     *
     * @param callable 线程
     */
    public static <T> Future<T> execute(final Callable<T> callable) {
        try {
            return executor.submit(callable);
        } catch (Exception e) {
            throw new UtilException(StringUtil.format("executing callable failure: {}", e.getMessage()), e);
        }
    }

    /**
     * 执行线程
     *
     * @param runnable 线程
     */
    public static Future<?> submit(final Runnable runnable) {
        return executor.submit(runnable);
    }

    /**
     * 获得ExecutorService
     *
     * @return ExecutorService
     */
    public static ExecutorService getExecutor() {
        return executor;
    }
}
