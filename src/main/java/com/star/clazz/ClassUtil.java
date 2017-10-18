package com.star.clazz;

import com.star.collection.ArrayUtil;
import com.star.exception.ToolException;
import com.star.reflect.TypeUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

/**
 * 类工具类
 * <p>
 * Created by win7 on 2017/6/18.
 */
public final class ClassUtil {

    private ClassUtil() {
    }

    /**
     * 获得对象数组的类数组
     *
     * @param objects 实例数组
     * @return 对象数据
     */
    public static Class<?>[] getClasses(final Object... objects) {
        Class<?>[] clazzs = new Class<?>[objects.length];
        for (int i = 0; i < objects.length; i++) {
            clazzs[i] = objects[i].getClass();
        }
        return clazzs;
    }

    /**
     * 实例化对象,一些构造方法的例如int等属性药改成包装类
     *
     * @param clazz  类
     * @param params 构造参数
     * @return 实体对象
     */
    public static <T> T newInstance(final Class<T> clazz, final Object... params) {
        try {
            T instance;
            if (ArrayUtil.isEmpty(params)) {
                instance = (T) clazz.newInstance();
            } else {
                instance = clazz.getDeclaredConstructor(getClasses(params)).newInstance(params);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new ToolException(
                    String.format("instance class {} failure,the reason is: {}", clazz.getSimpleName(), e.getMessage()),
                    e);
        }
    }

    /**
     * 获得给定类的第一个泛型参数
     *
     * @param clazz 被检查的类，必须是已经确定泛型类型的类
     * @return {@link Class}
     */
    public static Class<?> getTypeArgument(Class<?> clazz) {
        return getTypeArgument(clazz, 0);
    }

    /**
     * 获得给定类的泛型参数
     *
     * @param clazz 被检查的类，必须是已经确定泛型类型的类
     * @param index 泛型类型的索引号，既第几个泛型类型
     * @return {@link Class}
     */
    public static Class<?> getTypeArgument(Class<?> clazz, int index) {
        final Type argumentType = TypeUtil.getTypeArgument(clazz, index);
        if (null != argumentType && argumentType instanceof Class) {
            return (Class<?>) argumentType;
        }
        return null;
    }
}
