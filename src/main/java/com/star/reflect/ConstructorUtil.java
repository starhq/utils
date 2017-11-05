package com.star.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

/**
 * 构造器工具类
 *
 * @author starhq
 */
public final class ConstructorUtil {

    /**
     * 缓存
     */
    private static final ClassValue<Constructor<?>[]> CACHE = new ClassValue<Constructor<?>[]>() {
        /**
         * 获得累的构造函数列表
         * @param clazz 类
         * @return 构造函数数组
         */
        @Override
        protected Constructor<?>[] computeValue(final Class<?> clazz) {
            return clazz.getDeclaredConstructors();
        }
    };

    private ConstructorUtil() {
    }

    /**
     * 查询类的构造函数列表
     *
     * @param beanClass 类
     * @param <T>       泛型
     * @return 构造函数
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T>[] getConstructors(final Class<T> beanClass) {
        return (Constructor<T>[]) CACHE.get(beanClass);
    }

    /**
     * 查询类的构造函数列表
     *
     * @param beanClass      类
     * @param parameterTypes 构造函数的入参类型
     * @param <T>            泛型
     * @return 构造函数
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<Constructor<T>> getConstructor(final Class<T> beanClass, final Class<?>... parameterTypes) {
        final Constructor[] constructors = getConstructors(beanClass);
        Class<?>[] pts;
        Optional<Constructor<T>> result = Optional.empty();
        for (final Constructor constructor : constructors) {
            pts = constructor.getParameterTypes();
            if (Arrays.deepEquals(pts, parameterTypes)) {
                result = Optional.of(constructor);
                break;
            }
        }
        return result;
    }

    /**
     * 强制转换constructor可访问.
     *
     * @param constructor 需要转换的构造函数
     */
    public static void makeAccessible(final Constructor<?> constructor) {
        if (!Modifier.isPublic(constructor.getModifiers()) || !Modifier.isPublic(constructor.getDeclaringClass().getModifiers())) {
            constructor.setAccessible(true);
        }
    }
}
