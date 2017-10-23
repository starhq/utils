package com.star.collection;

import com.star.collection.iter.IterUtil;
import com.star.collection.set.SetUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 集合工具类
 */
public final class CollectionUtil {


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
     * 两个集合去并集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param <T>         泛型
     * @return 并集
     */
    public static <T> Collection<T> union(final Collection<T> collection1, final Collection<T> collection2) {
        final ArrayList<T> list = new ArrayList<>();
        if (isEmpty(collection1)) {
            list.addAll(collection2);
        } else if (isEmpty(collection2)) {
            list.addAll(collection1);
        } else {
            final Map<T, Integer> map1 = IterUtil.countMap(collection1);
            final Map<T, Integer> map2 = IterUtil.countMap(collection2);
            final Set<T> sets = SetUtil.newHashSet(collection2);
            sets.addAll(collection1);
            int flag;
            for (final T instance : sets) {
                final int count1 = Objects.isNull(map1.get(instance)) ? 0 : map1.get(instance);
                final int count2 = Objects.isNull(map2.get(instance)) ? 0 : map2.get(instance);

                flag = Math.min(count1, count2);
                for (int i = 0; i < flag; i++) {
                    list.add(instance);
                }
            }
        }
        return list;
    }

    /**
     * 多个集合取并集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param collections 其他集合
     * @param <T>         泛型
     * @return 并集
     */
    public static <T> Collection<T> union(final Collection<T> collection1, final Collection<T> collection2, final
    Collection<T>... collections) {
        Collection<T> union = union(collection1, collection2);
        for (final Collection<T> collection : collections) {
            union = union(union, collection);
        }
        return union;
    }

    /**
     * 多个集合取交集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param <T>         泛型
     * @return 交集
     */
    public static <T> Collection<T> intersection(final Collection<T> collection1, final Collection<T> collection2) {
        final ArrayList<T> list = new ArrayList<>();
        if (isEmpty(collection1)) {
            list.addAll(collection2);
        } else if (isEmpty(collection2)) {
            list.addAll(collection1);
        } else {
            final Map<T, Integer> map1 = IterUtil.countMap(collection1);
            final Map<T, Integer> map2 = IterUtil.countMap(collection2);
            final Set<T> sets = SetUtil.newHashSet(collection2);
            int flag;
            for (final T instance : sets) {
                final int count1 = Objects.isNull(map1.get(instance)) ? 0 : map1.get(instance);
                final int count2 = Objects.isNull(map2.get(instance)) ? 0 : map2.get(instance);

                flag = Math.min(count1, count2);
                for (int i = 0; i < flag; i++) {
                    list.add(instance);
                }
            }
        }
        return list;
    }

    /**
     * 多个集合取交集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param collections 其他集合
     * @param <T>         泛型
     * @return 交集
     */
    public static <T> Collection<T> intersection(final Collection<T> collection1, final Collection<T> collection2, final
    Collection<T>... collections) {
        Collection<T> intersection = intersection(collection1, collection2);
        for (final Collection<T> collection : collections) {
            intersection = intersection(intersection, collection);
        }
        return intersection;
    }

    /**
     * 多个集合取差集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param <T>         泛型
     * @return 差集
     */
    public static <T> Collection<T> disjunction(final Collection<T> collection1, final Collection<T> collection2) {
        final ArrayList<T> list = new ArrayList<>();
        if (isEmpty(collection1)) {
            list.addAll(collection2);
        } else if (isEmpty(collection2)) {
            list.addAll(collection1);
        } else {
            final Map<T, Integer> map1 = IterUtil.countMap(collection1);
            final Map<T, Integer> map2 = IterUtil.countMap(collection2);
            final Set<T> sets = SetUtil.newHashSet(collection2);
            int flag;
            for (final T instance : sets) {
                final int count1 = Objects.isNull(map1.get(instance)) ? 0 : map1.get(instance);
                final int count2 = Objects.isNull(map2.get(instance)) ? 0 : map2.get(instance);

                flag = Math.abs(count1 - count2);
                for (int i = 0; i < flag; i++) {
                    list.add(instance);
                }
            }
        }
        return list;
    }

    /**
     * 多个集合取差集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param collections 其他集合
     * @param <T>         泛型
     * @return 差集
     */
    public static <T> Collection<T> disjunction(final Collection<T> collection1, final Collection<T> collection2, final
    Collection<T>... collections) {
        Collection<T> disjunction = disjunction(collection1, collection2);
        for (final Collection<T> collection : collections) {
            disjunction = union(disjunction, collection);
        }
        return disjunction;
    }
}
