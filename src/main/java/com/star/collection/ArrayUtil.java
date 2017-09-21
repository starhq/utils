package com.star.collection;

import com.sun.xml.internal.ws.util.UtilException;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 数组工具类
 * <p>
 * Created by win7 on 2017/5/13.
 */
public class ArrayUtil {

    private ArrayUtil() {
    }

    /**
     * 新建一个空数组
     *
     * @param componentType 数组元素的类型
     * @param newSize       数组大小
     * @return 空数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(final Class<?> componentType, final int newSize) {
        return (T[]) Array.newInstance(componentType, newSize);
    }

    /**
     * 数组扩容
     *
     * @param buffer        原始数组
     * @param newSize       增加多少空间
     * @param componentType 数组元素类型
     * @return 扩容后的数组
     */
    public static <T> T[] resize(final T[] buffer, final int newSize, final Class<?> componentType) {
        final T[] newArray = newArray(componentType, newSize);
        System.arraycopy(buffer, 0, newArray, 0, buffer.length >= newSize ? newSize : buffer.length);
        return newArray;
    }

    /**
     * 往数组中添加一个元素
     */
    public static <T> T[] append(final T[] buffer, final T newElement) {
        final T[] array = resize(buffer, buffer.length + 1, newElement.getClass());
        array[buffer.length] = newElement;
        return array;
    }

    /**
     * 数组是否为空
     */
    @SafeVarargs
    public static <T> boolean isEmpty(final T... array) {
        return array == null || array.length <= 0;
    }

    /**
     * 对象是否为数组对象
     *
     * @param obj 对象
     * @return 是否为数组对象
     * @throws NullPointerException 提供被监测的对象为<code>null</code>
     */
    public static boolean isArray(Object obj) {
        if (null == obj) {
            throw new NullPointerException("Object check for isArray is null");
        }
        return obj.getClass().isArray();
    }

    /**
     * 数组或集合转String
     *
     * @param obj 集合或数组对象
     * @return 数组字符串，与集合转字符串格式相同
     */
    public static String toString(Object obj) {
        if (null == obj) {
            return null;
        }
        if (ArrayUtil.isArray(obj)) {
            try {
                return Arrays.deepToString((Object[]) obj);
            } catch (Exception e) {
                final String className = obj.getClass().getComponentType().getName();
                switch (className) {
                    case "long":
                        return Arrays.toString((long[]) obj);
                    case "int":
                        return Arrays.toString((int[]) obj);
                    case "short":
                        return Arrays.toString((short[]) obj);
                    case "char":
                        return Arrays.toString((char[]) obj);
                    case "byte":
                        return Arrays.toString((byte[]) obj);
                    case "boolean":
                        return Arrays.toString((boolean[]) obj);
                    case "float":
                        return Arrays.toString((float[]) obj);
                    case "double":
                        return Arrays.toString((double[]) obj);
                    default:
                        throw new UtilException(e);
                }
            }
        }
        return obj.toString();
    }
}
