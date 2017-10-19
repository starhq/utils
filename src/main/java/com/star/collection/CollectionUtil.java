package com.star.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 集合工具类
 */
public final class CollectionUtil {

    /**
     * 默认增长因子，当Map的size达到 容量*增长因子时，开始扩充Map
     */
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private CollectionUtil() {
    }

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * map是否为空
     *
     * @param map map对象
     * @return 是否为空
     */
    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 迭代器是否为空
     *
     * @param iterator 迭代器
     * @return 是否为空
     */
    public static boolean isEmpty(final Iterator<?> iterator) {
        return iterator == null || iterator.hasNext();
    }

    /**
     * iterable是否为空
     *
     * @param iterable iterable对象
     * @return 是否为空
     */
    public static boolean isEmpty(final Iterable<?> iterable) {
        return iterable == null || isEmpty(iterable.iterator());
    }


    /**
     * 可变参数包装成HashSet
     *
     * @param instances 需要包装的参数
     * @param <T>       范型
     * @return HashSet
     */
    public static <T> HashSet<T> newHashSet(final T... instances) {
        return newHashSet(false, instances);
    }

    /**
     * 可变参数包装成HashSet
     *
     * @param isSorted  是否需要排序
     * @param instances 需要包装的参数
     * @param <T>       范型
     * @return HashSet
     */
    @SafeVarargs
    public static <T> HashSet<T> newHashSet(final boolean isSorted, final T... instances) {
        HashSet<T> result;
        if (ArrayUtil.isEmpty(instances)) {
            result = isSorted ? new LinkedHashSet<>() : new HashSet<>();
        } else {
            final int capacity = Math.max((int) (instances.length / DEFAULT_LOAD_FACTOR) + 1, 16);
            result = isSorted ? new LinkedHashSet<>(capacity) : new HashSet<>(capacity);
            for (final T instance : instances) {
                result.add(instance);
            }
        }
        return result;
    }

    /**
     * 集合包装成HashSet
     *
     * @param collection 需要包装的集合
     * @param <T>        范型
     * @return HashSet
     */
    public static <T> HashSet<T> newHashSet(final Collection<T> collection) {
        return newHashSet(false, collection);
    }

    /**
     * 集合包装成HashSet
     *
     * @param isSorted   是否需要排序
     * @param collection 需要包装的集合
     * @param <T>        范型
     * @return HashSet
     */
    public static <T> HashSet<T> newHashSet(final boolean isSorted, final Collection<T> collection) {
        return isSorted ? new LinkedHashSet<>(collection) : new HashSet<>(collection);
    }

    /**
     * 集合包装成HashSet
     *
     * @param isSorted 是否需要排序
     * @param iterator 迭代器
     * @param <T>      范型
     * @return HashSet
     */
    public static <T> HashSet<T> newHashSet(final boolean isSorted, final Iterator<T> iterator) {
        final HashSet<T> result = isSorted ? new LinkedHashSet<>() : new HashSet<>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }


    /**
     * 可变参数包装成ArrayList
     *
     * @param <T>    集合元素类型
     * @param values 数组
     * @return ArrayList
     */
    @SafeVarargs
    public static <T> ArrayList<T> newArrayList(final T... values) {
        ArrayList<T> arrayList;
        if (ArrayUtil.isEmpty(values)) {
            arrayList = new ArrayList<>();
        } else {
            arrayList = new ArrayList<>(values.length);
            for (final T value : values) {
                arrayList.add(value);
            }
        }
        return arrayList;
    }

    /**
     * 集合包装成ArrayList
     *
     * @param collection 集合
     * @param <T>        范型
     * @return ArrayList
     */
    public static <T> ArrayList<T> newArrayList(final Collection<T> collection) {
        return isEmpty(collection) ? new ArrayList<>() : new ArrayList<>(collection);
    }

    /**
     * iterable包装成ArrayList
     *
     * @param iterable 迭代器
     * @param <T>      范型
     * @return ArrayList
     */
    public static <T> ArrayList<T> newArrayList(final Iterable<T> iterable) {
        return isEmpty(iterable) ? new ArrayList<>() : newArrayList(iterable.iterator());
    }

    /**
     * iterator包装成ArrayList
     *
     * @param iterator 迭代器
     * @param <T>      范型
     * @return ArrayList
     */
    public static <T> ArrayList<T> newArrayList(final Iterator<T> iterator) {
        ArrayList<T> arrayList;
        if (isEmpty(iterator)) {
            arrayList = new ArrayList<>();
        } else {
            arrayList = new ArrayList<>();
            while (iterator.hasNext()) {
                arrayList.add(iterator.next());
            }
        }
        return arrayList;
    }

    /**
     * 将集合包装成CopyOnWriteArrayList
     *
     * @param collection 集合
     * @param <T>        泛型
     * @return CopyOnWriteArrayList
     */
    public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList(final Collection<T> collection) {
        return isEmpty(collection) ? new CopyOnWriteArrayList<>() : new CopyOnWriteArrayList<>(collection);
    }

    /**
     * 集合去重
     *
     * @param collection 集合
     * @param <T>        泛型
     * @return AArrayList
     */
    public static <T> ArrayList<T> distinct(final Collection<T> collection) {
        return isEmpty(collection) ? new ArrayList<>() : collection instanceof Set ? new ArrayList<>(collection) : new
                ArrayList<>(new LinkedHashSet<>(collection));
    }
}
