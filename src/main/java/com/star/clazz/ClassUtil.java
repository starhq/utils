package com.star.clazz;

import com.star.collection.array.ArrayUtil;
import com.star.exception.ToolException;
import com.star.reflect.ConstructorUtil;
import com.star.reflect.TypeUtil;
import com.star.string.StringUtil;
import com.sun.xml.internal.ws.util.UtilException;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
     * 获得ClassPath
     *
     * @return ClassPath集合
     */
    public static Set<String> getClassPathResources() {
        return getClassPaths(StringUtil.EMPTY);
    }

    /**
     * 获得ClassPath
     *
     * @param packageName 包名
     * @return ClassPath路径字符串集合
     */
    public static Set<String> getClassPaths(final String packageName) {
        final String packagePath = packageName.replace(StringUtil.DOT, StringUtil.SLASH);
        Enumeration<URL> resources;
        try {
            resources = ClassLoaderUtil.getClassLoader().getResources(packagePath);
        } catch (IOException e) {
            throw new UtilException(StringUtil.format("Loading classPath [{}] error!", packagePath), e);
        }
        final Set<String> paths = new HashSet<>();
        while (resources.hasMoreElements()) {
            paths.add(resources.nextElement().getPath());
        }
        return paths;
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

    /**
     * 比较判断types1和types2两组类，如果types1中所有的类都与types2对应位置的类相同，或者是其父类或接口，则返回<code>true</code>
     *
     * @param types1 类组1
     * @param types2 类组2
     * @return 是否相同、父类或接口
     */
    public static boolean isAllAssignableFrom(final Class<?>[] types1, final Class<?>[] types2) {
        boolean result = false;
        if (ArrayUtil.isEmpty(types1) && ArrayUtil.isEmpty(types2)) {
            result = true;
        } else if (types1.length == types2.length) {
            for (int i = 0; i < types1.length; i++) {
                if (!types1[i].isAssignableFrom(types2[i])) {
                    break;
                }
            }
            result = true;
        }
        return result;
    }

    /**
     * 加载类
     *
     * @param className     全路径类名
     * @param isInitialized 是否初始化
     * @return 实体对象
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> loadClass(final String className, final boolean isInitialized) {
        try {
            return (Class<T>) Class.forName(className, isInitialized, ClassLoaderUtil.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new ToolException(
                    StringUtil.format(" {} load class failure,the reasone is: {}", className, e.getMessage()), e);
        }
    }

    /**
     * 加载类并初始化
     *
     * @param className 全路径类名
     * @return 实体对象
     */
    public static <T> Class<T> loadClass(final String className) {
        return loadClass(className, true);
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
        final Type argumentType = TypeUtil.getTypeArgument(clazz, index).orElseThrow(NullPointerException::new);
        if (null != argumentType && argumentType instanceof Class) {
            return (Class<?>) argumentType;
        }
        return null;
    }

    /**
     * 获得给定类所在包的名称<br>
     * <p>
     * 例如：<br>
     * <p>
     * com.xiaoleilu.hutool.util.ClassUtil =》 com.xiaoleilu.hutool.util
     *
     * @param clazz 类
     * @return 包名
     */
    public static String getPackage(Class<?> clazz) {
        if (clazz == null) {
            return StringUtil.EMPTY;
        }
        final String className = clazz.getName();
        int packageEndIndex = className.lastIndexOf(StringUtil.DOT);
        if (packageEndIndex == -1) {
            return StringUtil.EMPTY;
        }
        return className.substring(0, packageEndIndex);
    }

    /**
     * 获得给定类所在包的路径<br>
     * <p>
     * 例如：<br>
     * <p>
     * com.xiaoleilu.hutool.util.ClassUtil =》 com/xiaoleilu/hutool/util
     *
     * @param clazz 类
     * @return 包名
     */
    public static String getPackagePath(Class<?> clazz) {
        return getPackage(clazz).replace(StringUtil.C_DOT, StringUtil.C_SLASH);
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
}
