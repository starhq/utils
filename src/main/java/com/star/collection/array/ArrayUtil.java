package com.star.collection.array;

import com.star.clazz.ClassUtil;
import com.star.exception.ToolException;
import com.star.lang.Editor;
import com.star.lang.Filter;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 数组工具类
 * <p>
 * Created by starhq on 2017/5/13.
 */
public final class ArrayUtil {

    /**
     * 数组中元素未找到的下标，值为-1
     */
    private static final int INDEX_NOT_FOUND = -1;

    private ArrayUtil() {
    }

    /**
     * 数组是否为空
     *
     * @param array 需要判断的数组
     * @param <T>   泛型
     * @return 是否为数组
     */
    @SafeVarargs
    public static <T> boolean isEmpty(final T... array) {
        return Objects.isNull(array) || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param obj 需要判断的数组
     * @return 是否为数组
     */
    public static boolean isEmpty(final Object obj) {
        return Objects.isNull(obj) || isArray(obj) && 0 == getLength(obj);
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
        if (!isEmpty(buffer)) {
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
        return resize(buffer, newSize, ClassUtil.getComponentType(buffer));
    }

    /**
     * 数组中插入新元素
     *
     * @param buffer      原始数组
     * @param newElements 新元素
     * @param <T>         泛型
     * @return 插入新元素后的数组
     */
    @SafeVarargs
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
    @SafeVarargs
    public static <T> T[] merge(final T[]... arrays) {
        int len = 0;
        for (final T[] array : arrays) {
            if (isEmpty(array)) {
                continue;
            }
            len += array.length;
        }
        final T[] result = newArray(ClassUtil.getComponentType(arrays).getComponentType(), len);
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
        final List<T> list = new ArrayList<>(array.length);
        for (final T instance : array) {
            if (filter.accept(instance)) {
                list.add(instance);
            }
        }
        return list.toArray(Arrays.copyOf(array, list.size()));
    }

    /**
     * 过滤数组
     *
     * @param array  原始数组
     * @param editor 过滤器
     * @param <T>    泛型
     * @return 过滤后的数组
     */
    public static <T> T[] filter(final T[] array, final Editor<T> editor) {
        final List<T> list = new ArrayList<>(array.length);
        T modified;
        for (final T instance : array) {
            modified = editor.edit(instance);
            if (!Objects.isNull(modified)) {
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
        for (int i = len - 1; i >= 0; i--) {
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
        return indexOf(array, value) > INDEX_NOT_FOUND;
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
            try {
                str = Arrays.deepToString((Object[]) obj);
            } catch (ClassCastException e) {
                final String className = ClassUtil.getComponentType(obj).getName();
                switch (className) {
                    case "long":
                        str = Arrays.toString((long[]) obj);
                        break;
                    case "int":
                        str = Arrays.toString((int[]) obj);
                        break;
                    case "short":
                        str = Arrays.toString((short[]) obj);
                        break;
                    case "char":
                        str = Arrays.toString((char[]) obj);
                        break;
                    case "byte":
                        str = Arrays.toString((byte[]) obj);
                        break;
                    case "boolean":
                        str = Arrays.toString((boolean[]) obj);
                        break;
                    case "float":
                        str = Arrays.toString((float[]) obj);
                        break;
                    case "double":
                        str = Arrays.toString((double[]) obj);
                        break;
                    default:
                        throw new ToolException(e.getMessage(), e);
                }

            }
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
    public static int getLength(final Object obj) {
        return isArray(obj) ? Array.getLength(obj) : 0;
    }

    /**
     * bytebuffer转byte数组
     *
     * @param byteBuffer 要转换
     * @return 转换好的数组
     */
    public static byte[] toArray(final ByteBuffer byteBuffer) {
        byte[] result;
        if (byteBuffer.hasArray()) {
            result = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
        } else {
            final int oldPosition = byteBuffer.position();
            byteBuffer.position(0);
            final int size = byteBuffer.limit();
            final byte[] buffers = new byte[size];
            byteBuffer.get(buffers);
            byteBuffer.position(oldPosition);
            result = buffers;
        }
        return result;
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
        final int length = getLength(array);
        Object result;
        if (index < 0 || index >= length) {
            result = array;
        } else {
            result = newArray(ClassUtil.getComponentType(array), length - 1);
            System.arraycopy(array, 0, result, 0, index);
            if (index < length - 1) {
                System.arraycopy(array, index + 1, result, index, length - index - 1);
            }
        }
        return result;
    }

    /**
     * 克隆数组，如果非数组返回<code>null</code>
     *
     * @param <T> 数组元素类型
     * @param obj 数组对象
     * @return 克隆后的数组对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T clone(final T obj) {
        if (null == obj) {
            return null;
        }
        if (isArray(obj)) {
            final Object result;
            final Class<?> componentType = obj.getClass().getComponentType();
            if (componentType.isPrimitive()) {
                int length = Array.getLength(obj);
                result = Array.newInstance(componentType, length);
                while (length-- > 0) {
                    Array.set(result, length, Array.get(obj, length));
                }
            } else {
                result = ((Object[]) obj).clone();
            }
            return (T) result;
        }
        return null;
    }

    /**
     * 克隆数组
     *
     * @param <T>   数组元素类型
     * @param array 被克隆的数组
     * @return 新数组
     */
    public static <T> T[] clone(T[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }
}
