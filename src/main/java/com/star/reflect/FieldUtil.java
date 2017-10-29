package com.star.reflect;

import com.star.exception.ToolException;
import com.star.string.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 属性工具类
 *
 * @author starhq
 */
public final class FieldUtil {

    private FieldUtil() {
    }

    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     *
     * @param object    要读取的对象
     * @param fieldName 属性名
     * @return 属性值
     */
    public static Object getFieldValue(final Object object, final String fieldName) {
        final Field field = getDeclaredField(object, fieldName);

        makeAccessible(field);

        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new ToolException(
                    StringUtil.format("get filed {}'s value failure,the reason is: {}", fieldName, e.getMessage()), e);
        }
    }

    /**
     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     *
     * @param object    要设置的对象
     * @param fieldName 属性名
     * @param value     属性值
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        final Field field = getDeclaredField(object, fieldName);

        makeAccessible(field);

        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new ToolException(
                    StringUtil.format("set filed {}'s value failure,the reason is: {}", fieldName, e.getMessage()), e);
        }
    }


    /**
     * 循环向上转型,获取对象的DeclaredField.
     *
     * @param object    要获取的属性的对象
     * @param fieldName 属性名
     * @return 属性
     */
    public static Field getDeclaredField(final Object object, final String fieldName) {
        return getDeclaredField(object.getClass(), fieldName);
    }

    /**
     * 循环向上转型,获取类的DeclaredField.
     *
     * @param clazz     要获取的属性的对象
     * @param fieldName 属性名
     * @return 属性
     */
    @SuppressWarnings({"rawtypes", "unused"})
    public static Field getDeclaredField(final Class<?> clazz, final String fieldName) {
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                throw new ToolException(
                        StringUtil.format("get declared filed fialure,the reason is: {}", e.getMessage()), e);
            }
        }
        return null;
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