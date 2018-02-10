package com.star.clazz;

import com.star.collection.array.ArrayUtil;
import com.star.collection.map.MapUtil;
import com.star.exception.ToolException;
import com.star.io.ResourceUtil;
import com.star.reflect.ConstructorUtil;
import com.star.string.StringUtil;
import com.sun.xml.internal.ws.util.UtilException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * 类工具类
 * <p>
 * Created by win7 on 2017/6/18.
 */
public final class ClassUtil {

    /**
     * 默认值缓存
     */
    private static final Map<Class<?>, Object> CACHE = MapUtil.newHashMap(8);

    static {
        CACHE.put(long.class, 0L);
        CACHE.put(int.class, 0);
        CACHE.put(short.class, (short) 0);
        CACHE.put(char.class, (char) 0);
        CACHE.put(byte.class, (byte) 0);
        CACHE.put(double.class, 0D);
        CACHE.put(float.class, 0f);
        CACHE.put(boolean.class, false);
    }

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
     * 获得ClassPath
     *
     * @return ClassPath
     */
    public static String getClassPath() {
        return getClassPathURL().getPath();
    }

    /**
     * 获得ClassPath URL
     *
     * @return ClassPath URL
     */
    public static URL getClassPathURL() {
        return ResourceUtil.getResource(StringUtil.EMPTY);
    }

    /**
     * @return 获得Java ClassPath路径，不包括 jre
     */
    public static String[] getJavaClassPaths() {
        return System.getProperty("java.class.path").split(System.getProperty("path.separator"));
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
            throw new ToolException(StringUtil.format("instance class {} failure,the reason is: {}", clazz, e.getMessage()),
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
            return ArrayUtil.isEmpty(params) ? clazz.newInstance() : ConstructorUtil.getConstructor(clazz, getClasses(params)).orElseThrow
                    (NullPointerException::new).newInstance(params);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | SecurityException e) {
            throw new ToolException(
                    StringUtil.format("instance class {} failure,the reason is: {}", clazz.getSimpleName(), e.getMessage()),
                    e);
        }
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
     * 获得给定类所在包的名称
     *
     * @param clazz 类
     * @return 包名
     */
    public static String getPackage(final Class<?> clazz) {
        final String name = getClassName(clazz, false);
        final int packageEndIndex = name.lastIndexOf(StringUtil.C_DOT);
        return packageEndIndex == -1 ? name : StringUtil.sub(name, 0, packageEndIndex);
    }

    /**
     * 获得给定类所在包的路径<
     *
     * @param clazz 类
     * @return 包名
     */
    public static String getPackagePath(final Class<?> clazz) {
        return getPackage(clazz).replace(StringUtil.C_DOT, StringUtil.C_SLASH);
    }


    /**
     * 获取指定类型分的默认值
     *
     * @param clazz 类
     * @return 默认值
     */
    public static Optional<Object> getDefaultValue(final Class<?> clazz) {
        return clazz.isPrimitive() ? Optional.of(CACHE.get(clazz)) : Optional.empty();
    }

    /**
     * 获得默认值列表
     *
     * @param classes 值类型
     * @return 默认值列表
     * @since 3.0.9
     */
    public static Object[] getDefaultValues(final Class<?>... classes) {
        final Object[] values = new Object[classes.length];
        for (int i = 0; i < classes.length; i++) {
            values[i] = getDefaultValue(classes[i]).orElse(null);
        }
        return values;
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
