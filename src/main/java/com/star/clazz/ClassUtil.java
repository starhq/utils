package com.star.clazz;

import com.star.collection.array.ArrayUtil;
import com.star.exception.ToolException;
import com.star.string.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * 类工具类
 * <p>
 * Created by win7 on 2017/6/18.
 */
public final class ClassUtil {

    private ClassUtil() {
    }

    /**
     * 获取对象类型
     *
     * @param obj 对象
     * @param <T> 泛型
     * @return 类型
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<Class<T>> getClass(final T obj) {
        return Objects.isNull(obj) ? Optional.empty() : Optional.of((Class<T>) obj.getClass());
    }

    /**
     * 获取类名
     *
     * @param clazz    类
     * @param isSimple 是否全路径
     * @return 类名
     */
    public static String getClassName(final Class<?> clazz, final boolean isSimple) {
        return Objects.isNull(clazz) ? StringUtil.EMPTY : isSimple ? clazz.getSimpleName() : clazz.getName();
    }

    /**
     * 获取对象的类名
     *
     * @param obj      对象
     * @param isSimple 是否全路径
     * @return 类名
     */
    public static String getClassName(final Object obj, final boolean isSimple) {
        return Objects.isNull(obj) ? StringUtil.EMPTY : getClassName(obj.getClass(), isSimple);
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
            clazzs[i] = getClass(objects[i]).orElse(Object.class);
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
                instance = clazz.newInstance();
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
//        final Type argumentType = TypeUtil.getTypeArgument(clazz, index);
//        if (null != argumentType && argumentType instanceof Class) {
//            return (Class<?>) argumentType;
//        }
        return null;
    }


    /**
     * 获得类的所有公共方法
     *
     * @param clazz 类
     * @return 方法数组
     */
    public static Method[] getPublicMethods(final Class<?> clazz) {
        return clazz.getMethods();
    }

    /**
     * 获取对象的componentType
     *
     * @param obj 对象
     * @return componentType
     */
    public static Class<?> getComponentType(final Object obj) {
        final Class<?> clazz = obj.getClass();
        return clazz.getComponentType();
    }

    /**
     * 获取指定类型分的默认值<br>
     * <p>
     * 默认值规则为：
     * <p>
     * <pre>
     *
     * 1、如果为原始类型，返回0
     *
     * 2、非原始类型返回{@code null}
     *
     * </pre>
     *
     * @param clazz 类
     * @return 默认值
     * @since 3.0.8
     */
    public static Object getDefaultValue(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            if (long.class == clazz) {
                return 0L;
            } else if (int.class == clazz) {
                return 0;
            } else if (short.class == clazz) {
                return (short) 0;
            } else if (char.class == clazz) {
                return (char) 0;
            } else if (byte.class == clazz) {
                return (byte) 0;
            } else if (double.class == clazz) {
                return 0D;
            } else if (float.class == clazz) {
                return 0f;
            } else if (boolean.class == clazz) {
                return false;
            }
        }

        return null;
    }

    /**
     * 获得默认值列表
     *
     * @param classes 值类型
     * @return 默认值列表
     * @since 3.0.9
     */
    public static Object[] getDefaultValues(Class<?>... classes) {
        final Object[] values = new Object[classes.length];
        for (int i = 0; i < classes.length; i++) {
            values[i] = getDefaultValue(classes[i]);
        }
        return values;
    }

    /**
     * 比较判断types1和types2两组类，如果types1中所有的类都与types2对应位置的类相同，或者是其父类或接口，则返回<code>true</code>
     *
     * @param types1 类组1
     * @param types2 类组2
     * @return 是否相同、父类或接口
     */
    public static boolean isAllAssignableFrom(Class<?>[] types1, Class<?>[] types2) {
        if (ArrayUtil.isEmpty(types1) && ArrayUtil.isEmpty(types2)) {
            return true;
        }
        if (types1.length == types2.length) {
            for (int i = 0; i < types1.length; i++) {
                if (false == types1[i].isAssignableFrom(types2[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
