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
import java.util.stream.Stream;

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
     * @param <T>      范型
     * @return 对应属性的值
     */
    public static <T> Object getSimpleProperty(final T instance, final String name) {
        final PropertyDescriptor descriptor = getDescriptor(instance.getClass(), name);
        final Method method = descriptor.getReadMethod();
        return MethodUtil.invoke(instance, method);
    }

    /**
     * 为对象设置简单属性
     *
     * @param instance 实例
     * @param name     属性名
     * @param value    值
     * @param <T>      范型
     */
    public static <T> void setSimpleProperty(final T instance, final String name, final Object value) {
        final PropertyDescriptor descriptor = getDescriptor(instance.getClass(), name);
        final Method method = descriptor.getWriteMethod();
        MethodUtil.invoke(instance, method, value);
    }

    /**
     * map转对象
     *
     * @param maps  键值对
     * @param clazz 要转换成的类
     * @param <T>   范型
     * @return 设置好参数的对象
     */
    public static <T> T mapToBean(final Map<String, Object> maps, final Class<T> clazz) {
        final T instance = ClassUtil.newInstance(clazz);
        PropertyDescriptor descriptor;
        Method method;
        for (final Map.Entry<String, Object> entry : maps.entrySet()) {
            descriptor = getDescriptor(clazz, entry.getKey());
            if (!Objects.isNull(descriptor)) {
                method = descriptor.getWriteMethod();
                MethodUtil.invoke(instance, method, entry.getValue());
            }
        }
        return instance;
    }

    /**
     * 对象转map
     *
     * @param instance 实例
     * @param <T>      范型
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
     * @param editable         target的父类，只复制target父类中的属性
     * @param ignoreProperties 忽略的属性值
     */
    public static void copyProperties(final Object source, final Object target, final Class<?> editable,
                                      final String... ignoreProperties) {
        final Class<?> actualEditable = !Objects.isNull(editable) && editable.isInstance(target) ? editable : target.getClass();

        final PropertyDescriptor[] sourceDescriptors = getPropertyDescriptors(source.getClass());
        for (final PropertyDescriptor sourceDescriptor : sourceDescriptors) {
            final String name = sourceDescriptor.getName();
            if (CLASS.equals(name) || ArrayUtil.contains(ignoreProperties, name)) {
                continue;
            }
            final PropertyDescriptor targetDescriptor = getDescriptor(actualEditable, name);
            if (!Objects.isNull(targetDescriptor)) {
                final Object value = getSimpleProperty(source, name);
                if (!Objects.isNull(value)) {
                    setSimpleProperty(target, name, value);
                }
            }
        }
    }


    /**
     * 获得实例中指定的propertydescriptor
     *
     * @param clazz 实例
     * @param name  属性名
     * @param <T>   泛型
     * @return 返回propertydescriptor
     */
    private static <T> PropertyDescriptor getDescriptor(final Class<T> clazz, final String name) {
        final PropertyDescriptor[] descriptors = getPropertyDescriptors(clazz);
        try {
            return Stream.of(descriptors).filter(desc -> name.equals(desc.getName())).findFirst().orElse(new
                    PropertyDescriptor(name, clazz));
        } catch (IntrospectionException e) {
            throw new ToolException(
                    StringUtil.format("get instance {}'s fileld {}'s propertyDescriptor failure,the reason is: {}", ClassUtil.getClassName
                            (clazz, false), name, e.getMessage()), e);
        }
    }
}
