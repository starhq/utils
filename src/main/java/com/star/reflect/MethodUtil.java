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
     * 包括父类的所有方法的缓存（public和private）
     */
    private static final ClassValue<Method[]> ALL = new ClassValue<Method[]>() {

        /**
         *获取类和父类的所有方法
         * @param clazz 当前类
         * @return 方法列表
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

    /**
     * 类的所有方法的缓存（public和private）
     */
    private static final ClassValue<Method[]> DECLARED = new ClassValue<Method[]>() {

        /**
         *获取类的所有方法
         * @param clazz 当前类
         * @return 方法列表
         */
        @Override
        protected Method[] computeValue(final Class<?> clazz) {
            return clazz.getDeclaredMethods();
        }
    };

    /**
     * 类和父类的的所有public方法的缓存
     */
    private static final ClassValue<Method[]> PUBLIC = new ClassValue<Method[]>() {

        /**
         * 获取类和弗雷的所有public方法
         *
         * @param clazz 当前类
         * @return 方法列表
         */
        @Override
        protected Method[] computeValue(final Class<?> clazz) {
            return clazz.getMethods();
        }
    };

    private MethodUtil() {
    }

    /**
     * 获得类的方法列表
     *
     * @param beanClass             类
     * @param withSuperClassMethods 是否包含父类
     * @return 方法列表
     */
    public static Method[] getMethods(final Class<?> beanClass, final boolean withSuperClassMethods) {
        return withSuperClassMethods ? ALL.get(beanClass) : DECLARED.get(beanClass);
    }

    /**
     * 获得类的public方法列表
     *
     * @param beanClass 类
     * @return 方法列表
     */
    public static Method[] getPublicMethods(final Class<?> beanClass) {
        return PUBLIC.get(beanClass);
    }

    /**
     * 获得类的方法列表,按过滤器过滤
     *
     * @param beanClass 类
     * @param filter    类过滤器
     * @return 方法列表
     */
    public static Method[] getMethods(final Class<?> beanClass, final Filter<Method> filter) {
        final Method[] methods = getMethods(beanClass, true);
        return getMethods(filter, methods);
    }

    /**
     * 获得类的方法列表,按过滤器过滤
     *
     * @param beanClass 类
     * @param filter    类过滤器
     * @return 方法列表
     */
    public static Method[] getPublicMethods(final Class<?> beanClass, final Filter<Method> filter) {
        final Method[] methods = getPublicMethods(beanClass);
        return getMethods(filter, methods);
    }

    /**
     * 获得方法名
     *
     * @param beanClass 类
     * @return 方法集合
     */
    public static Set<String> getMethodNames(final Class<?> beanClass) {
        final Method[] methods = getMethods(beanClass, true);
        final Set<String> methodSet = new HashSet<>();
        for (final Method method : methods) {
            methodSet.add(method.getName());
        }
        return methodSet;
    }

    /**
     * 获得普遍立车方法名
     *
     * @param beanClass 类
     * @return 方法集合
     */
    public static Set<String> getPublicMethodNames(final Class<?> beanClass) {
        final Method[] methods = getPublicMethods(beanClass);
        final Set<String> methodSet = new HashSet<>();
        for (final Method method : methods) {
            methodSet.add(method.getName());
        }
        return methodSet;
    }

    /**
     * 根据name和参数类型获得方法
     *
     * @param beanClass  类
     * @param name       方法名
     * @param paramTypes 参数类型
     * @return 方法
     */
    public static Optional<Method> getMethod(final Class<?> beanClass, final String name, final Class<?>...
            paramTypes) {
        final Method[] methods = getMethods(beanClass, true);
        return getMethod(name, methods, paramTypes);
    }


    /**
     * 根据name和参数类型获得public方法
     *
     * @param beanClass  类
     * @param name       方法名
     * @param paramTypes 参数类型
     * @return 方法
     */
    public static Optional<Method> getPublicMethod(final Class<?> beanClass, final String name, final Class<?>...
            paramTypes) {
        final Method[] methods = getPublicMethods(beanClass);
        return getMethod(name, methods, paramTypes);
    }

    /**
     * 根据name和参数类型获得对象方法
     *
     * @param object 对象
     * @param name   方法名
     * @param args   参数书列表
     * @return 方法
     */
    public static Optional<Method> getMethod(final Object object, final String name, final Object... args) {
        return getMethod(ClassUtil.getClass(object).orElse(Object.class), name, ClassUtil.getClasses(args));
    }

    /**
     * 根据name和参数类型获得对象公共方法
     *
     * @param object 对象
     * @param name   方法名
     * @param args   参数书列表
     * @return 方法
     */
    public static Optional<Method> getPublicMethod(final Object object, final String name, final Object... args) {
        return getPublicMethod(ClassUtil.getClass(object).orElse(Object.class), name, ClassUtil.getClasses(args));
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
                    ClassUtil.getClassName(obj.getClass(), true), method.getName(), e.getMessage()), e);
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
        final Method method = getMethod(obj, methodName, args).orElseThrow(NullPointerException::new);
        return invoke(obj, method, args);
    }

    /**
     * 强制转换方法可访问.
     *
     * @param method 需要转换的方法
     */
    public static void makeAccessible(final Method method) {
        if (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()
        )) {
            method.setAccessible(true);
        }
    }

    /**
     * 是不是public方法
     *
     * @param method 方法
     * @return 是不是public
     */
    public static boolean isPublic(final Method method) {
        return !Objects.isNull(method) && Modifier.isPublic(method.getModifiers()) && Modifier.isPublic(method
                .getDeclaringClass().getModifiers());
    }

    /**
     * 过滤方法
     *
     * @param filter  过滤器
     * @param methods 要过滤的方法数组
     * @return 过滤后的方法
     */
    private static Method[] getMethods(final Filter<Method> filter, final Method... methods) {
        Method[] result;
        if (Objects.isNull(filter)) {
            result = new Method[0];
        } else {
            final List<Method> methodList = new ArrayList<>();
            for (final Method method : methods) {
                if (filter.accept(method)) {
                    methodList.add(method);
                }
            }
            result = methodList.toArray(new Method[methodList.size()]);
        }
        return result;
    }

    /**
     * 获得指定方法
     *
     * @param name       方法名
     * @param methods    方法集合
     * @param paramTypes 参数集合
     * @return 方法
     */
    private static Optional<Method> getMethod(final String name, final Method[] methods, final Class<?>... paramTypes) {
//        Optional<Method> result = Optional.empty();
//        for (final Method method : methods) {
//            if (name.equals(method.getName()) && (ArrayUtil.isEmpty(paramTypes) || ClassUtil.isAllAssignableFrom(method
//                    .getParameterTypes(), paramTypes))) {
//                result = Optional.of(method);
//                break;
//            }
//        }
//        return result;
        return null;
    }

    public static Method findDeclaredMethod(Class<? extends Cloneable> aClass, String clone) {
    }
}
