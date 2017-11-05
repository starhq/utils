package com.star.reflect;

import com.star.collection.array.ArrayUtil;
import com.star.exception.ToolException;
import com.star.string.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

/**
 * 属性工具类
 *
 * @author starhq
 */
public final class FieldUtil {

    /**
     * 缓存
     */
    public static final ClassValue<Field[]> CACHE = new ClassValue<Field[]>() {
        /**
         * 获取类的所有属性
         * @param clazz 类
         * @return 属性数组
         */
        @Override
        protected Field[] computeValue(final Class<?> clazz) {
            Field[] allFields = null;
            Class<?> searchType = clazz;
            Field[] declaredFields;
            while (searchType != null) {
                declaredFields = searchType.getDeclaredFields();
                if (null == allFields) {
                    allFields = declaredFields;
                } else {
                    allFields = ArrayUtil.append(allFields, declaredFields);
                }
                searchType = searchType.getSuperclass();
            }

            return allFields;
        }
    };

    private FieldUtil() {
    }

    /**
     * 获得类的属性列表
     *
     * @param beanClass 类
     * @return 属性列表
     */
    public static Field[] getFields(final Class<?> beanClass) {
        return CACHE.get(beanClass);
    }

    /**
     * 获取类的属性
     *
     * @param clazz     要获取的属性的类
     * @param fieldName 属性名
     * @return 属性
     */
    public static Optional<Field> getField(final Class<?> clazz, final String fieldName) {
        final Field[] fields = getFields(clazz);
        Optional<Field> result = Optional.empty();
        for (final Field field : fields) {
            if (fieldName.equals(field.getName())) {
                result = Optional.of(field);
                break;
            }
        }
        return result;
    }


    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     *
     * @param object 要读取的对象
     * @param field  属性
     * @return 属性值
     */
    public static Object getFieldValue(final Object object, final Field field) {
        makeAccessible(field);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new ToolException(
                    StringUtil.format("get filed {}'s value failure,the reason is: {}", field.getName(), e.getMessage()), e);
        }
    }

    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     *
     * @param object    要读取的对象
     * @param fieldName 属性名
     * @return 属性值
     */
    public static Object getFieldValue(final Object object, final String fieldName) {
        final Field field = getField(object.getClass(), fieldName).orElseThrow(ToolException::new);

        return getFieldValue(object, field);
    }

    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     *
     * @param object 要读取的对象
     * @param field  属性
     * @return 属性值
     */
    public static void setFieldValue(final Object object, final Field field, final Object value) {
        makeAccessible(field);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new ToolException(
                    StringUtil.format("set filed {}'s value {} failure,the reason is: {}", field.getName(), value, e.getMessage()), e);
        }
    }

    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     *
     * @param object    要读取的对象
     * @param fieldName 属性名
     * @return 属性值
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        final Field field = getField(object.getClass(), fieldName).orElseThrow(ToolException::new);

        setFieldValue(object, field, value);
    }

    /**
     * 强制转换fileld可访问.
     *
     * @param field 需要转换的属性
     */
    public static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }
}