package com.star.collection;

import com.star.lang.Filter;
import com.star.string.StringUtil;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 数组工具类
 * <p>
 * Created by win7 on 2017/5/13.
 */
public final class ArrayUtil {

    /**
     * 数组中元素未找到的下标，值为-1
     */
    public static final int INDEX_NOT_FOUND = -1;

    private ArrayUtil() {
    }

    /**
     * 数组是否为空
     *
     * @param array 需要判断的数组
     * @param <T>   泛型
     * @return 是否为数组
     */
    public static <T> boolean isEmpty(final T... array) {
        return array == null || array.length <= 0;
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
     * @param newSize       需要增大的空间
     * @param componentType 数组类型
     * @param <T>           泛型
     * @return 增大后的数组
     */
    public static <T> T[] resize(final T[] buffer, final int newSize, final Class<?> componentType) {
        final T[] newArray = newArray(componentType, newSize);
        if (isEmpty(buffer)) {
            System.arraycopy(buffer, 0, newArray, 0, Math.min(newSize, buffer.length));
        }
        return newArray;
    }

    /**
     * 数组扩容
     *
     * @param buffer  原始数组
     * @param newSize 需要增大的空间
     * @param <T>     泛型
     * @return 增大后的数组
     */
    public static <T> T[] resize(final T[] buffer, final int newSize) {
        return resize(buffer, newSize, getComponentType(buffer));
    }

    /**
     * 数组中插入新元素
     *
     * @param buffer      原始数组
     * @param newElements 新元素
     * @param <T>         泛型
     * @return 插入新元素后的数组
     */
    public static <T> T[] append(final T[] buffer, final T... newElements) {
        T[] arrays;
        if (isEmpty(newElements)) {
            arrays = buffer;
        } else {
            arrays = resize(buffer, buffer.length + newElements.length);
            System.arraycopy(newElements, 0, arrays, buffer.length, newElements.length);
        }
        return arrays;
    }

    /**
     * 组合数组
     *
     * @param arrays 一堆受阻
     * @param <T>    泛型
     * @return 组合后的数组
     */
    public static <T> T[] merge(final T[]... arrays) {
        int len = 0;
        for (final T[] array : arrays) {
            if (isEmpty(array)) {
                continue;
            }
            len += array.length;
        }
        final T[] result = newArray(getComponentType(arrays).getComponentType(), len);
        len = 0;
        for (final T[] array : arrays) {
            if (isEmpty(array)) {
                continue;
            }
            System.arraycopy(array, 0, result, len, array.length);
            len += array.length;
        }
        return result;
    }

    /**
     * 过滤数组
     *
     * @param array  原始数组
     * @param filter 过滤器
     * @param <T>    泛型
     * @return 过滤后的数组
     */
    public static <T> T[] filter(final T[] array, final Filter<T> filter) {
        final List<T> list = CollectionUtil.newArrayList(array);
        for (final T instance : array) {
            if (filter.accept(instance)) {
                list.add(instance);
            }
        }
        return list.toArray(Arrays.copyOf(array, list.size()));
    }

    /**
     * 获取元素在数组中首次出现的下标
     *
     * @param array 数组
     * @param value 需搜索的元素
     * @param <T>   泛型
     * @return 元素的位置
     */
    public static <T> int indexOf(final T[] array, final Object value) {
        final int len = array.length;
        int index = INDEX_NOT_FOUND;
        for (int i = 0; i < len; i++) {
            if (Objects.equals(value, array[i])) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 获取元素在数组中最后一次出现的下标
     *
     * @param array 数组
     * @param value 需搜索的元素
     * @param <T>   泛型
     * @return 元素的位置
     */
    public static <T> int lastIndexOf(final T[] array, final Object value) {
        final int len = array.length;
        int index = INDEX_NOT_FOUND;
        for (int i = len - 1; i >= 0; i++) {
            if (Objects.equals(value, array[i])) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 数组中是否包含指定元素
     *
     * @param array 数组
     * @param value 元素
     * @param <T>   泛型
     * @return 是否包含
     */
    public static <T> boolean contains(final T[] array, final Object value) {
        return indexOf(array, value) >= INDEX_NOT_FOUND;
    }

    /**
     * 判断对象是否为数组
     *
     * @param obj 需要判断的对象
     * @return 是否为数组
     */
    public static boolean isArray(final Object obj) {
        return !Objects.isNull(obj) && obj.getClass().isArray();
    }

    /**
     * 数组转字符串
     *
     * @param obj 数组对象
     * @return 字符串
     */
    public static String toString(final Object obj) {
        String str;
        if (isArray(obj)) {
            str = StringUtil.join(StringUtil.BRACE_START, StringUtil.BRACE_END, StringUtil.COMMA, (Object[]) obj);
        } else {
            str = obj.toString();
        }
        return str;
    }


    /**
     * 获取数组的长度
     *
     * @param obj 数组
     * @return 长度
     */
    public static int length(final Object obj) {
        return Objects.isNull(obj) ? 0 : Array.getLength(obj);
    }

    /**
     * bytebuffer转byte数组
     *
     * @param byteBuffer 要转换
     * @return 转换好的数组
     */
    public static byte[] toArray(final ByteBuffer byteBuffer) {
        if (byteBuffer.hasArray()) {
            return Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
        } else {
            final int oldPosition = byteBuffer.position();
            byteBuffer.position(0);
            final int size = byteBuffer.limit();
            final byte[] buffers = new byte[size];
            byteBuffer.get(buffers);
            byteBuffer.position(oldPosition);
            return buffers;
        }
    }

    /**
     * 集合转换成数组
     *
     * @param collection    集合
     * @param componentType 集合中的类型
     * @param <T>           泛型
     * @return 转换后的数组
     */
    public static <T> T[] toArray(final Collection<T> collection, final Class<T> componentType) {
        final T[] array = newArray(componentType, collection.size());
        return collection.toArray(array);
    }

    /**
     * 删除数组中指定位置的元素
     *
     * @param array 数组
     * @param index 指定位置
     * @return 删除后的数组
     */
    public static Object remove(final Object array, final int index) {
        if (null == array) {
            return array;
        }
        final int length = length(array);
        if (index < 0 || index >= length) {
            return array;
        }

        final Object result = Array.newInstance(getComponentType(array), length - 1);
        System.arraycopy(array, 0, result, 0, index);
        if (index < length - 1) {
            System.arraycopy(array, index + 1, result, index, length - index - 1);
        }
        return result;
    }

    private static Class<?> getComponentType(final Object obj) {
        final Class<?> clazz = obj.getClass();
        return clazz.getComponentType();
    }

}
