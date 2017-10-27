package com.star.collection.set;

import com.star.collection.ArrayUtil;
import com.star.collection.CollectionUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.TreeSet;

/**
 * Set工具类
 *
 * @author starhq
 */
public final class SetUtil {

    /**
     * 默认增长因子
     */
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private SetUtil() {
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
     * 集合包装成TreeSet
     *
     * @param collection 集合
     * @param <T>        范型
     * @return TreeSet
     */
    public static <T> TreeSet<T> newTreeSet(final Collection<T> collection) {
        return CollectionUtil.isEmpty(collection) ? new TreeSet<>() : new TreeSet<>(collection);
    }

}