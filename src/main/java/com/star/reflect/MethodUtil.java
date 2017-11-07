package com.star.reflect;

import com.star.clazz.ClassUtil;
import com.star.collection.array.ArrayUtil;
import com.star.exception.ToolException;
import com.star.lang.Filter;
import com.star.string.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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

    /**
     * 缓存
     */
    public static final ClassValue<Method[]> DECLAREDMETHOD = new ClassValue<Method[]>() {
        /**
         * 获取类的所有方法
         * @param clazz 类
         * @return 方法数组
         */
        @Override
        protected Method[] computeValue(final Class<?> clazz) {
            Method[] allMethods = null;
            Class<?> searchType = clazz;
            Method[] declaredMethods;
            while (searchType != null) {
                declaredMethods = searchType.getDeclaredMethods();
                if (null == allMethods) {
                    allMethods = declaredMethods;
                } else {
                    allMethods = ArrayUtil.append(allMethods, declaredMethods);
                }
                searchType = searchType.getSuperclass();
            }
            return allMethods;

        }
    };

    private MethodUtil() {
    }

    /**
     * 获得类的方法列表
     *
     * @param beanClass 类
     * @return 方法列表
     */
    public static Method[] getDeclardMethods(final Class<?> beanClass) {
        return DECLAREDMETHOD.get(beanClass);
    }

    /**
     * 获得类的方法列表,按过滤器过滤
     *
     * @param beanClass    类
     * @param methodFilter 类过滤器
     * @return 方法列表
     */
    public static Method[] getDeclardMethods(final Class<?> beanClass, final Filter<Method> methodFilter) {
        Method[] methods = getDeclardMethods(beanClass);
        if (!Objects.isNull(methodFilter)) {
            final List<Method> methodList = new ArrayList<>();
            for (final Method method : methods) {
                if (methodFilter.accept(method)) {
                    methodList.add(method);
                }
            }
            methods = methodList.toArray(new Method[methodList.size()]);
        }
        return methods;
    }

    /**
     * 获得方法名
     *
     * @param clazz 类
     * @return 方法集合
     */
    public static Set<String> getDeclaredMethodNames(final Class<?> clazz) {
        final Method[] methods = getDeclardMethods(clazz);
        final Set<String> methodSet = new HashSet<>();
        for (final Method method : methods) {
            methodSet.add(method.getName());
        }
        return methodSet;
    }

    /**
     * 根据name和参数类型获得方法
     *
     * @param clazz      类
     * @param name       方法名
     * @param paramTypes 参数类型
     * @return 方法
     */
    public static Optional<Method> getDeclaredMethod(final Class<?> clazz, final String name, final Class<?>... paramTypes) {
        final Method[] methods = getDeclardMethods(clazz);
        Optional<Method> result = Optional.empty();
        for (final Method method : methods) {
            if (name.equals(method.getName()) && ArrayUtil.isEmpty(paramTypes) || Arrays.equals(method.getParameterTypes(), paramTypes)) {
                result = Optional.of(method);
                break;
            }
        }
        return result;
    }

    /**
     * 根据name和参数类型获得对象方法
     *
     * @param object 对象
     * @param name   方法名
     * @param args   参数书列表
     * @return 方法
     */
    public static Optional<Method> getDeclaredMethod(final Object object, final String name, final Object... args) {
        return getDeclaredMethod(ClassUtil.getClass(object).orElse(Object.class), name, ClassUtil.getClasses(args));
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
        makeAccessible(method);
        try {
            return (T) method.invoke(Modifier.isStatic(method.getModifiers()) ? null : obj, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ToolException(StringUtil.format("invoke class {}'s method {} failue,the reason is: {}",
                    ClassUtil.getClassName(obj, true), method.getName(), e.getMessage()), e);
        }
    }

    /**
     * 反射调用方法
     *
     * @param obj        对象
     * @param methodName 方法名
     * @param args       参数
     * @param <T>        泛型
     * @return 方法返回值
     */
    @SuppressWarnings("unchecked")
    public static <T> T invoke(final Object obj, final String methodName, final Object... args) {
        final Method method = getDeclaredMethod(obj, methodName, args).orElseThrow(ToolException::new);
        return invoke(obj, method, args);
    }

    /**
     * 强制转换方法可访问.
     *
     * @param method 需要转换的方法
     */
    public static void makeAccessible(final Method method) {
        if (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            method.setAccessible(true);
        }
    }
}
