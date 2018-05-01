package com.star.clazz;

import com.star.exception.ToolException;
import com.star.string.StringUtil;

import java.util.Objects;

/**
 * ClassLoader工具类
 */
public final class ClassLoaderUtil {

    private ClassLoaderUtil() {
    }

    /**
     * 获取当前线程的classloader
     *
     * @return 当前classloader
     */
    public static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    /**
     * 获取当前线程的classloader,若不存在获取类的classLoader
     *
     * @return 获得classloader
     */
    public static ClassLoader getClassLoader() {
        final ClassLoader classLoader = getContextClassLoader();
        return Objects.isNull(classLoader) ? ClassLoaderUtil.class.getClassLoader() : classLoader;
    }


    /**
     * 加载类
     *
     * @param className     全路径类名
     * @param isInitialized 是否初始化
     * @param <T>           范型
     * @return 实体对象
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> loadClass(final String className, final boolean isInitialized) {
        try {
            return (Class<T>) Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new ToolException(
                    StringUtil.format(" {} load class failure,the reasone is: {}", className, e.getMessage()), e);
        }
    }

    /**
     * 加载类并初始化
     *
     * @param className 全路径类名
     * @param <T>       范型
     * @return 实体对象
     */
    public static <T> Class<T> loadClass(final String className) {
        return loadClass(className, true);
    }
}
