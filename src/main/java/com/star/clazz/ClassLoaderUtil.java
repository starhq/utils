package com.star.clazz;

import java.util.Objects;

public class ClassLoaderUtil {

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
}
