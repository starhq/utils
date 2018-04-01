package com.star.reflect;

import com.star.collection.array.ArrayUtil;
import com.star.collection.list.ListUtil;
import com.star.exception.ToolException;
import com.star.string.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Type工具类
 *
 * @author Looly
 */
public final class TypeUtil {

    private TypeUtil() {
    }

    /**
     * 获得Type的原始类
     *
     * @param type 要查询的type
     * @return 原始类
     */
    public static Optional<Class<?>> getClass(final Type type) {
        Optional<Class<?>> result;
        if (type instanceof Class) {
            result = Optional.of((Class<?>) type);
        } else if (type instanceof ParameterizedType) {
            result = Optional.of((Class<?>) ((ParameterizedType) type).getRawType());
        } else {
            result = Optional.empty();
        }
        return result;
    }

    /**
     * 获得属性的Type类型
     *
     * @param field 属性
     * @return Type类型
     */
    public static Optional<Type> getType(final Field field) {
        Optional<Type> result;
        if (Objects.isNull(field)) {
            result = Optional.empty();
        } else {
            final Type type = field.getGenericType();
            result = Objects.isNull(type) ? Optional.of(field.getType()) : Optional.of(type);
        }
        return result;
    }


    /**
     * 获得属性的原始类
     *
     * @param field 属性
     * @return 原始类
     */
    public static Optional<Class<?>> getClass(final Field field) {
        return Objects.isNull(field) ? Optional.empty() : Optional.of(field.getType());
    }

    // ----------------------------------------------------------------------------------- Param Type

    /**
     * 获取方法的第一个参数类型
     *
     * @param method 方法
     * @return Type
     */
    public static Optional<Type> getParamType(final Method method) {
        return getParamType(method, 0);
    }

    /**
     * 获取方法的第一个参数类
     *
     * @param method 方法
     * @return Class
     */
    public static Optional<Class<?>> getParamClass(final Method method) {
        return getParamClass(method, 0);
    }

    /**
     * 获取方法的参数类型
     *
     * @param method 方法
     * @param index  第index个参数类型
     * @return Type
     */
    public static Optional<Type> getParamType(final Method method, final int index) {
        final Type[] types = getParamTypes(method);
        return ArrayUtil.isEmpty(types) && types.length <= index ? Optional.empty() : Optional.of(types[index]);
    }

    /**
     * 获取方法的第index个参数类
     *
     * @param method 方法
     * @param index  第index个
     * @return Class
     */
    public static Optional<Class<?>> getParamClass(final Method method, final int index) {
        final Class<?>[] classes = getParamClasses(method);
        return ArrayUtil.isEmpty(classes) && classes.length <= index ? Optional.empty() : Optional.of(classes[index]);
    }


    /**
     * 获取方法的参数类型列表
     *
     * @param method 方法
     * @return Type数组
     */
    public static Type[] getParamTypes(final Method method) {
        return Objects.isNull(method) ? new Type[0] : method.getGenericParameterTypes();
    }

    /**
     * 获得方法的参数类列表
     *
     * @param method 方法
     * @return 参数类列表
     */
    public static Class<?>[] getParamClasses(final Method method) {
        return Objects.isNull(method) ? new Class<?>[0] : method.getParameterTypes();
    }


    // ----------------------------------------------------------------------------------- Return Type

    /**
     * 获取方法的参数类型列表
     *
     * @param method 方法
     * @return Type
     */
    public static Optional<Type> getReturnType(final Method method) {
        return Objects.isNull(method) ? Optional.empty() : Optional.of(method.getGenericReturnType());
    }

    /**
     * 获取方法的参数类列表
     *
     * @param method 方法
     * @return Type
     */
    public static Optional<Class<?>> getReturnClass(final Method method) {
        return Objects.isNull(method) ? Optional.empty() : Optional.of(method.getReturnType());
    }

    // ----------------------------------------------------------------------------------- Type Argument

    /**
     * 获得给定类的第一个泛型参数
     *
     * @param clazz 被检查的类，必须是已经确定泛型类型的类
     * @return Type
     */
    public static Optional<Type> getTypeArgument(final Class<?> clazz) {
        return getTypeArgument(clazz, 0);
    }

    /**
     * 获得给定类的第index个泛型参数
     *
     * @param clazz 被检查的类，必须是已经确定泛型类型的类
     * @param index 第index个
     * @return Type
     */
    public static Optional<Type> getTypeArgument(final Class<?> clazz, final int index) {
        return getTypeArgument(clazz.getGenericSuperclass(), index);
    }

    /**
     * 获得给定类的第一个泛型参数
     *
     * @param type 被检查的类型，必须是已经确定泛型类型的类
     * @return Type
     */
    public static Optional<Type> getTypeArgument(final Type type) {
        return getTypeArgument(type, 0);
    }

    /**
     * 获得给定类的泛型参数
     *
     * @param type  被检查的类型，必须是已经确定泛型类型的类
     * @param index 泛型类型的索引号，既第几个泛型类型
     * @return Type
     */
    public static Optional<Type> getTypeArgument(final Type type, final int index) {
        final Type[] typeArguments = getTypeArguments(type);
        return ArrayUtil.isEmpty(typeArguments) && typeArguments.length <= index ? Optional.empty() : Optional.of
                (typeArguments[index]);
    }

    /**
     * 获得指定类型中所有泛型参数类型
     *
     * @param type 指定类型
     * @return 所有泛型参数类型
     */
    public static Type[] getTypeArguments(final Type type) {
        return type instanceof ParameterizedType ? ((ParameterizedType) type).getActualTypeArguments() : new Type[0];
    }

    // ----------------------------------------------------------------------------------- super genric type

    /**
     * 获得指定类的父类的泛型参数的第一个实际类型
     *
     * @param clazz 要查询的类
     * @return 实际类型
     */
    public static Optional<Type> getSuperClassGenricType(final Class<?> clazz) {
        return getTypeArgument(clazz.getGenericSuperclass(), 0);
    }

    /**
     * 获得指定类的父类的泛型参数的第index个实际类型
     *
     * @param clazz 要查询的类
     * @param index 第index个
     * @return 实际类型
     */
    public static Optional<Type> getSuperClassGenricType(final Class<?> clazz, final int index) {
        return getTypeArgument(clazz.getGenericSuperclass(), index);
    }

    // ----------------------------------------------------------------------------------- field genric type

    /**
     * 获得属性的第index个泛型参数实际类型
     *
     * @param field 属性
     * @param index 第index个
     * @return 实际类型
     */
    public static Optional<Type> getFieldGenericType(final Field field, final int index) {
        return getTypeArgument(field.getGenericType(), index);
    }

    /**
     * 获得属性的第1个泛型参数实际类型
     *
     * @param field 属性
     * @return 实际类型
     */
    public static Optional<Type> getFieldGenericType(final Field field) {
        return getFieldGenericType(field, 0);
    }

    // ----------------------------------------------------------------------------------- method genric type

    /**
     * 获得方法返回值泛型参数的第一个实际类型
     *
     * @param method 方法
     * @return 实际类型
     */
    public static Optional<Type> getMethodGenericReturnType(final Method method) {
        return getMethodGenericReturnType(method, 0);
    }

    /**
     * 获得方法返回值泛型参数的第index个实际类型
     *
     * @param method 方法
     * @param index  第index个
     * @return 实际类型
     */
    public static Optional<Type> getMethodGenericReturnType(final Method method, final int index) {
        return getTypeArgument(method.getGenericReturnType(), index);
    }

    /**
     * 获得方法签名泛型参数的第一个实际类型列表
     *
     * @param method 方法
     * @return 实际类型列表
     */
    public static List<Type> getMethodGenericParameterTypes(final Method method) {
        return getMethodGenericParameterTypes(method, 0);
    }

    /**
     * 获得方法签名泛型参数的第index个实际类型列表
     *
     * @param method 方法
     * @param index  第index个
     * @return 实际类型列表
     */
    public static List<Type> getMethodGenericParameterTypes(final Method method, final int index) {
        final Type[] parameterTypes = method.getGenericParameterTypes();
        if (index >= parameterTypes.length || index < 0) {
            throw new ToolException(StringUtil.format("get method's generic type failure:the index you input {}",
                    index < 0 ? "must not less than zero" : "bigger than field's count"));
        }
        final Type[] parameterArgTypes = getTypeArguments(parameterTypes[index]);
        return ArrayUtil.isEmpty(parameterArgTypes) ? Collections.emptyList() : ListUtil.newArrayList
                (parameterArgTypes);
    }
}
