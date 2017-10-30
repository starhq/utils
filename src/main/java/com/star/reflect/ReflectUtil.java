package com.star.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 反射工具类
 *
 * @author starhq
 */
public final class ReflectUtil {

    private ReflectUtil() {
    }

    /**
     * 新建代理对象
     *
     * @param interfaceClass    被代理的接口
     * @param invocationHandler 回调
     * @param <T>               泛型
     * @return 代理对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T newProxyInstance(final Class<T> interfaceClass, final InvocationHandler invocationHandler) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass},
                invocationHandler);
    }

    public static Object getFieldValue(Object bean, String fieldName) {
        return null;
    }
}
