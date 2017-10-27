package com.star.reflect;

import com.star.clazz.ClassUtil;
import com.star.collection.list.ListUtil;
import com.star.exception.ToolException;
import com.star.lang.Filter;
import com.star.string.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;

/**
 * 方法工具类
 *
 * @author starhq
 */
public final class MethodUtil {

    /**
     * 获取不到类名用未知来替代
     */
    private static final String UNKNOWN = "unknown";

    private MethodUtil() {
    }

    /**
     * 在类、父类、接口中查找公共方法
     *
     * @param clazz      类
     * @param methodName 方法名
     * @param paramTypes 方法参数
     * @return 方法
     */
    public static Method findMethod(final Class<?> clazz, final String methodName, final Class<?>... paramTypes) {
        Method method;
        try {
            method = clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException ex) {
            method = findDeclaredMethod(clazz, methodName, paramTypes);
        }
        return method;
    }

    /**
     * 在类或者超类自身中的所有方法中查找指定方法
     *
     * @param clazz          类
     * @param methodName     方法名
     * @param parameterTypes 参数
     * @return 方法
     */
    public static Method findDeclaredMethod(final Class<?> clazz, final String methodName,
                                            final Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException ex) {
            if (!Objects.isNull(clazz.getSuperclass())) {
                method = findDeclaredMethod(clazz.getSuperclass(), methodName, parameterTypes);
            }
        }
        return method;
    }

    /**
     * 获取类的公共方法
     *
     * @param clazz  指定类
     * @param filter 过滤器
     * @return 方法集合
     */
    public static List<Method> getMethods(final Class<?> clazz, final Filter<Method> filter) {
        final Method[] methods = ClassUtil.getPublicMethods(clazz);
        List<Method> result;
        if (Objects.isNull(filter)) {
            result = ListUtil.newArrayList(methods);
        } else {
            result = ListUtil.newArrayList(methods.length);
            for (final Method method : methods) {
                if (filter.accept(method)) {
                    result.add(method);
                }
            }
        }
        return result;
    }

    /**
     * 反射调用方法
     *
     * @param obj    对象
     * @param method 方法
     * @param args   参数
     * @param <T>    泛型
     * @return 方法返回值
     */
    @SuppressWarnings("unchecked")
    public static <T> T invoke(final Object obj, final Method method, final Object... args) {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        try {
            return (T) method.invoke(Modifier.isStatic(method.getModifiers()) ? null : obj, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ToolException(StringUtil.format("invoke class {}'s method {} failue,the reason is: {}",
                    ClassUtil.getClassName(obj, true).orElse(UNKNOWN), method.getName(), e.getMessage()), e);
        }
    }
}