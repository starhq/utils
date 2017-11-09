package com.star.beans;

import com.star.clazz.ClassUtil;
import com.star.collection.array.ArrayUtil;
import com.star.exception.ToolException;
import com.star.reflect.MethodUtil;
import com.star.string.StringUtil;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
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
     * 常量
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
     * 对象转map
     *
     * @param instance 实例
     * @return 实例的键值对
     */
    public static <T> Map<String, Object> beanToMap(final T instance) {
        final PropertyDescriptor[] descriptors = getPropertyDescriptors(instance.getClass());
        final Map<String, Object> maps = new HashMap<>(descriptors.length);
        for (final PropertyDescriptor descriptor : descriptors) {
            final String name = descriptor.getName();
            if (!CLASS.equals(name)) {
                try {
                    final Object value = getSimpleProperty(instance, name, descriptor);
                    if (!Objects.isNull(value)) {
                        maps.put(name, value);
                    }
                } catch (IllegalArgumentException e) {
                    throw new ToolException(
                            StringUtil.format("instance to map failure,the reasone is: {}", e.getMessage()), e);
                }

            }
        }
        return maps;
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
                setSimpleProperty(instance, name, maps.get(name), descriptor);
            }
        }
        return instance;
    }

    /**
     * 为对象设置简单属性
     *
     * @param instance 实例
     * @param name     属性名
     * @param value    值
     * @param desc     PropertyDescriptors实例
     */
    public static <T> void setSimpleProperty(final T instance, final String name, final Object value,
                                             final PropertyDescriptor desc) {
        PropertyDescriptor descriptor;
        if (Objects.isNull(desc)) {
            try {
                descriptor = new PropertyDescriptor(name, instance.getClass());
            } catch (IntrospectionException e) {
                throw new ToolException(StringUtil.format("filed {} set value {} failure,the reason is: {}", name,
                        value, e.getMessage()), e);
            }
        } else {
            descriptor = desc;
        }
        final Method method = descriptor.getWriteMethod();
        MethodUtil.invoke(instance, method, value);
    }

    /**
     * 获取对象的简单属性
     *
     * @param instance 实例
     * @param name     属性名
     * @param desc     PropertyDescriptors实例
     * @return 对应属性的值
     */
    public static <T> Object getSimpleProperty(final T instance, final String name, final PropertyDescriptor desc) {
        PropertyDescriptor descriptor;
        if (Objects.isNull(desc)) {
            try {
                descriptor = new PropertyDescriptor(name, instance.getClass());
            } catch (IntrospectionException e) {
                throw new ToolException(
                        StringUtil.format("get files {}'s value failure,the reason is: {}", name, e.getMessage()), e);
            }
        } else {
            descriptor = desc;
        }
        final Method method = descriptor.getReadMethod();
        return MethodUtil.invoke(instance, method);
    }

    /**
     * 复制Bean对象属性
     *
     * @param source           源
     * @param target           目标
     * @param ignoreProperties 忽略的属性值
     */
    public static void copyProperties(final Object source, final Object target, final String... ignoreProperties) {
        copyProperties(source, target, null, ignoreProperties);
    }

    /**
     * 复制Bean对象属性
     *
     * @param source           源
     * @param target           目标
     * @param editable         target的父类，只复制target父类中的属性
     * @param ignoreProperties 忽略的属性值
     */
    public static void copyProperties(final Object source, final Object target, final Class<?> editable,
                                      final String... ignoreProperties) {
        final Class<?> actualEditable = !Objects.isNull(editable) && editable.isInstance(target) ? editable
                : target.getClass();

        final PropertyDescriptor[] targetDescriptors = getPropertyDescriptors(actualEditable);
        final PropertyDescriptor[] sourceDescriptors = getPropertyDescriptors(source.getClass());
        for (final PropertyDescriptor targetDescriptor : targetDescriptors) {
            final String name = targetDescriptor.getName();
            if (CLASS.equals(name) || ArrayUtil.contains(ignoreProperties, name)) {
                continue;
            }

            for (final PropertyDescriptor sourceDescriptor : sourceDescriptors) {
                if (name.equals(sourceDescriptor.getName())) {
                    final Object value = getSimpleProperty(source, name, sourceDescriptor);
                    if (!Objects.isNull(value)) {
                        setSimpleProperty(target, name, value, targetDescriptor);
                        break;
                    }
                }
            }
        }
    }
}
