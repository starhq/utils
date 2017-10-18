package com.star.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Type工具类
 */
public final class TypeUtil {

    private TypeUtil() {
    }

    /**
     * 获得给定类的泛型参数
     *
     * @param clazz 被检查的类，必须是已经确定泛型类型的类
     * @param index 泛型类型的索引号，既第几个泛型类型
     * @return {@link Type}
     */
    public static Type getTypeArgument(Class<?> clazz, int index) {
        Type type = clazz;
        if (!(type instanceof ParameterizedType)) {
            type = clazz.getGenericSuperclass();
        }
        return getTypeArgument(type, index);
    }


    /**
     * 获得给定类的泛型参数
     *
     * @param type  被检查的类型，必须是已经确定泛型类型的类
     * @param index 泛型类型的索引号，既第几个泛型类型
     * @return {@link Type}
     */
    public static Type getTypeArgument(Type type, int index) {
        final Type[] typeArguments = getTypeArguments(type);
        if (null != typeArguments && typeArguments.length > index) {
            return typeArguments[index];
        }
        return null;
    }

    /**
     * 获得指定类型中所有泛型参数类型
     *
     * @param type 指定类型
     * @return 所有泛型参数类型
     */
    public static Type[] getTypeArguments(Type type) {
        if (type instanceof ParameterizedType) {
            final ParameterizedType genericSuperclass = (ParameterizedType) type;
            return genericSuperclass.getActualTypeArguments();
        }
        return null;
    }
}
