package com.star.reflect;

import com.star.clazz.ClassUtil;
import com.star.collection.array.ArrayUtil;
import com.star.exception.ToolException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Optional;

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

    /**
     * 实例化对象
     *
     * @param <T>   对象类型
     * @param clazz 类名
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final String clazz) {
        try {
            return (T) Class.forName(clazz).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new ToolException(String.format("instance class {} failure,the reason is: {}", clazz, e.getMessage()),
                    e);
        }

    }

    /**
     * 实例化对象
     *
     * @param <T>    对象类型
     * @param clazz  类
     * @param params 构造函数参数
     * @return 对象
     */
    public static <T> T newInstance(final Class<T> clazz, final Object... params) {
        try {
            T instance;
            if (ArrayUtil.isEmpty(params)) {
                instance = clazz.newInstance();
            } else {
                instance = ConstructorUtil.getConstructor(clazz, ClassUtil.getClasses(params)).orElseThrow(ToolException::new).newInstance(params);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | SecurityException e) {
            throw new ToolException(
                    String.format("instance class {} failure,the reason is: {}", clazz.getSimpleName(), e.getMessage()),
                    e);
        }
    }


    /**
     * 尝试遍历并调用此类的所有构造方法，直到构造成功并返回
     *
     * @param <T>       对象类型
     * @param beanClass 被构造的类
     * @return 构造后的对象
     */
    public static <T> Optional<T> newInstanceIfPossible(final Class<T> beanClass) {
        final Constructor<T>[] constructors = ConstructorUtil.getConstructors(beanClass);
        Class<?>[] parameterTypes;
        Optional<T> result = Optional.empty();
        for (final Constructor<T> constructor : constructors) {
            parameterTypes = constructor.getParameterTypes();
            try {
                result = Optional.of(constructor.newInstance(ClassUtil.getDefaultValues(parameterTypes)));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new ToolException(
                        String.format("instance class {} failure,the reason is: {}", beanClass.getSimpleName(), e.getMessage()),
                        e);
            }
        }
        return result;
    }


}
