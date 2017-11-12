package com.star.beans;

import com.star.clazz.ClassUtil;
import com.star.collection.array.ArrayUtil;
import com.star.collection.map.MapUtil;
import com.star.exception.ToolException;
import com.star.reflect.MethodUtil;
import com.star.string.StringUtil;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * bean工具类
 *
 * @author starhq
 */
public final class BeanUtil {

    /**
     * 缓存
     */
    private static final ClassValue<PropertyDescriptor[]> CACHE = new ClassValue<PropertyDescriptor[]>() {

        /**
         * 获得bean的PropertyDescriptor
         *
         * @param type 当前类
         * @return PropertyDescriptor数组
         */
        @Override
        protected PropertyDescriptor[] computeValue(final Class<?> type) {
            try {
                return Introspector.getBeanInfo(type).getPropertyDescriptors();
            } catch (IntrospectionException e) {
                throw new ToolException(
                        StringUtil.format("get clazz {}'s PropertyDescriptor array failue,the reasone is: {}",
                                ClassUtil.getClassName(type, false), e.getMessage()),
                        e);
            }
        }
    };

    /**
     * class常量
     */
    private static final String CLASS = "class";


    private BeanUtil() {
    }

    /**
     * 获得bean的PropertyDescriptor
     *
     * @param clazz 当前类
     * @return PropertyDescriptor数组
     */
    public static PropertyDescriptor[] getPropertyDescriptors(final Class<?> clazz) {
        return CACHE.get(clazz);
    }


    /**
     * 获取对象的简单属性
     *
     * @param instance 实例
     * @param name     属性名
     * @return 对应属性的值
     */
    public static <T> Object getSimpleProperty(final T instance, final String name) {
        final PropertyDescriptor descriptor = getDescriptor(instance, name);
        final Method method = descriptor.getReadMethod();
        return MethodUtil.invoke(instance, method);
    }

    /**
     * 为对象设置简单属性
     *
     * @param instance 实例
     * @param name     属性名
     * @param value    值
     */
    public static <T> void setSimpleProperty(final T instance, final String name, final Object value) {
        final PropertyDescriptor descriptor = getDescriptor(instance, name);
        final Method method = descriptor.getWriteMethod();
        MethodUtil.invoke(instance, method, value);
    }

    /**
     * 对象转map
     *
     * @param instance 实例
     * @return 实例的键值对
     */
    public static <T> Map<String, Object> beanToMap(final T instance) {
        final PropertyDescriptor[] descriptors = getPropertyDescriptors(instance.getClass());
        final Map<String, Object> maps = MapUtil.newHashMap(descriptors.length);
        for (final PropertyDescriptor descriptor : descriptors) {
            final String name = descriptor.getName();
            if (!CLASS.equals(name)) {
                final Object value = getSimpleProperty(instance, name);
                if (!Objects.isNull(value)) {
                    maps.put(name, value);
                }
            }
        }
        return maps;
    }

    /**
     * 复制Bean对象属性
     *
     * @param source           源
     * @param target           目标
     * @param ignoreProperties 忽略的属性值
     */
    public static void copyProperties(final Object source, final Object target, final String... ignoreProperties) {
        final PropertyDescriptor[] sourceDescriptors = getPropertyDescriptors(source.getClass());
        final PropertyDescriptor[] targetDescriptors = getPropertyDescriptors(target.getClass());
        final int len = sourceDescriptors.length;
        for (int i = 0; i < len; i++) {
            final PropertyDescriptor sourceDescriptor = sourceDescriptors[i];
            final String sourceName = sourceDescriptor.getName();
            if (CLASS.equals(sourceName) || ArrayUtil.contains(ignoreProperties, sourceName)) {
                continue;
            }
            final PropertyDescriptor targetDescriptor = targetDescriptors[i];
            final String targetName = targetDescriptor.getName();
            if (sourceName.equals(targetName)) {
                final Object value = getSimpleProperty(source, sourceName);
                if (!Objects.isNull(value)) {
                    setSimpleProperty(target, targetName, value);
                }
            }
        }

    }

    /**
     * map转对象
     *
     * @param maps      键值对
     * @param beanClass 要转换成的类
     * @return 设置好参数的对象
     */
    public static <T> T mapToBean(final Map<String, Object> maps, final Class<T> beanClass) {
        final T instance = ClassUtil.newInstance(beanClass);
        final PropertyDescriptor[] descriptors = getPropertyDescriptors(beanClass);
        for (final PropertyDescriptor descriptor : descriptors) {
            final String name = descriptor.getName();
            if (!CLASS.equals(name)) {
                setSimpleProperty(instance, name, maps.get(name));
            }
        }
        return instance;
    }

    /**
     * 获得实例中指定的propertydescriptor
     *
     * @param instance 实例
     * @param name     属性名
     * @param <T>      泛型
     * @return 返回propertydescriptor
     */

    private static <T> PropertyDescriptor getDescriptor(final T instance, final String name) {
        PropertyDescriptor descriptor = null;
        final PropertyDescriptor[] descriptors = getPropertyDescriptors(instance.getClass());
        for (final PropertyDescriptor desc : descriptors) {
            if (name.equals(desc.getName())) {
                descriptor = desc;
                break;
            }
        }
        if (Objects.isNull(descriptor)) {
            try {
                descriptor = new PropertyDescriptor(name, instance.getClass());
            } catch (IntrospectionException e) {
                throw new ToolException(
                        StringUtil.format("get instance {}'s fileld {}'s propertyDescriptor failure,the reason is: {}", ClassUtil.getClassName
                                (instance, false), name, e.getMessage()), e);
            }
        }
        return descriptor;
    }
}
