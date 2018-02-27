package com.star.thread;

import com.star.string.StringUtil;

import java.util.Objects;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程工具类
 *
 * @author luxiaolei
 */
public final class ThreadUtil {

    private ThreadUtil() {
    }

    /**
     * 新建线程池
     *
     * @param threadSize 池大小
     * @return 线程池
     */
    public static ExecutorService newExecutor(final int threadSize) {
        return new ThreadPoolExecutor(threadSize, threadSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new NamedThreadFactory(StringUtil.EMPTY, false));
    }

    /**
     * 新建线程池
     *
     * @return 线程池
     */
    public static ExecutorService newExecutor() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(), new NamedThreadFactory(StringUtil.EMPTY, false));
    }

    /**
     * 新建线程池
     *
     * @param corePoolSize    初始线程池大小
     * @param maximumPoolSize 最大线程池大小
     * @return 线程池
     */
    public static ExecutorService newExecutor(final int corePoolSize, final int maximumPoolSize) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new NamedThreadFactory(StringUtil.EMPTY, false));
    }

    /**
     * 获得一个新的线程池<br>
     * 传入阻塞系数，线程池的大小计算公式为：CPU可用核心数 / (1 - 阻塞因子)<br>
     * Blocking Coefficient(阻塞系数) = 阻塞时间／（阻塞时间+使用CPU的时间）<br>
     * 计算密集型任务的阻塞系数为0，而IO密集型任务的阻塞系数则接近于1。
     *
     * @param blockingCoefficient 阻塞系数，阻塞因子介于0~1之间的数，阻塞因子越大，线程池中的线程数越多
     * @return 线程池
     */
    public static ExecutorService newExecutor(final float blockingCoefficient) {
        // 最佳的线程数 = CPU可用核心数 / (1 - 阻塞系数)
        final int poolSize = (int) (Runtime.getRuntime().availableProcessors() / (1 - blockingCoefficient));

        return new ThreadPoolExecutor(poolSize, poolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new NamedThreadFactory(StringUtil.EMPTY, false));
    }


    /**
     * 新建一个CompletionService
     *
     * @param <T> 泛型
     * @return CompletionService
     */
    public static <T> CompletionService<T> newCompletionService() {
        return new ExecutorCompletionService<T>(GlobalThreadPool.getExecutor());
    }

    /**
     * 新建一个CompletionService
     *
     * @param executor 线程池
     * @param <T>      泛型
     * @return CompletionService
     */
    public static <T> CompletionService<T> newCompletionService(final ExecutorService executor) {
        return new ExecutorCompletionService<T>(executor);
    }

    /**
     * 新建一个CountDownLatch
     *
     * @param threadCount 线程数量
     * @return
     */
    public static CountDownLatch newCountDownLatch(final int threadCount) {
        return new CountDownLatch(threadCount);
    }


    /**
     * 挂起当前线程
     *
     * @param timeout  挂起时长
     * @param timeUnit 时长单位
     * @return 挂起是否成功
     */
    public static boolean sleep(final Number timeout, final TimeUnit timeUnit) {
        boolean result;
        try {
            timeUnit.sleep(timeout.longValue());
            result = true;
        } catch (InterruptedException e) {
            result = false;
        }
        return result;
    }

    /**
     * 挂起当前线程
     *
     * @param timeout 挂起时长
     * @return 挂起是否成功
     */
    public static boolean sleep(final Number timeout) {
        boolean result;
        try {
            Thread.sleep(timeout.longValue());
            result = true;
        } catch (InterruptedException e) {
            result = false;
        }
        return result;
    }

    /**
     * 创建ThreadLocal对象
     *
     * @param isInheritable 是否为子线程提供从父线程那里继承的值
     * @param <T>           翻新
     * @return ThreadLocal
     */
    public static <T> ThreadLocal<T> createThreadLocal(final boolean isInheritable) {
        return isInheritable ? new InheritableThreadLocal<>() : new ThreadLocal<>();
    }

    /**
     * 获得当前线程组
     *
     * @return 当前线程组
     */
    public static ThreadGroup currentThreadGroup() {
        final SecurityManager securityManager = System.getSecurityManager();
        return Objects.isNull(securityManager) ? Thread.currentThread().getThreadGroup() : securityManager.getThreadGroup();
    }
}
