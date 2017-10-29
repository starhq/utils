package com.star.collection.list;

import com.star.collection.CollectionUtil;
import com.star.collection.array.ArrayUtil;
import com.star.collection.iter.IterUtil;
import com.star.lang.ItemsProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 集合工具类
 *
 * @author starhq
 */
public final class ListUtil {

    private ListUtil() {
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
            Collections.addAll(arrayList, values);
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
        return CollectionUtil.isEmpty(collection) ? new ArrayList<>() : new ArrayList<>(collection);
    }

    /**
     * iterable包装成ArrayList
     *
     * @param iterable 迭代器
     * @param <T>      范型
     * @return ArrayList
     */
    public static <T> ArrayList<T> newArrayList(final Iterable<T> iterable) {
        return IterUtil.isEmpty(iterable) ? new ArrayList<>() : newArrayList(iterable.iterator());
    }

    /**
     * iterator包装成ArrayList
     *
     * @param iterator 迭代器
     * @param <T>      范型
     * @return ArrayList
     */
    public static <T> ArrayList<T> newArrayList(final Iterator<T> iterator) {
        final ArrayList<T> arrayList = new ArrayList<>();
        if (!IterUtil.isEmpty(iterator)) {
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
        return CollectionUtil.isEmpty(collection) ? new CopyOnWriteArrayList<>() : new CopyOnWriteArrayList<>
                (collection);
    }

    /**
     * 集合去重
     *
     * @param collection 集合
     * @param <T>        泛型
     * @return AArrayList
     */
    public static <T> ArrayList<T> distinct(final Collection<T> collection) {
        return CollectionUtil.isEmpty(collection) ? new ArrayList<>() : collection instanceof Set ? new ArrayList<>
                (collection) : new ArrayList<>(new LinkedHashSet<>(collection));
    }

    /**
     * 创建LinkedList
     *
     * @param <T> 泛型
     * @return LinkedList
     */
    public static <T> LinkedList<T> newLinkedList() {
        return new LinkedList<>();
    }

    /**
     * 创建ArrayList
     *
     * @param capacity 初始化大小
     * @param <T>      泛型
     * @return ArrayList
     */
    public static <T> ArrayList<T> newArrayList(final int capacity) {
        return new ArrayList<>(capacity);
    }

    /**
     * 切割集合并进行处理
     *
     * @param collection 集合
     * @param size       按size大小切分
     * @param processors 处理器
     * @param <T>        输入泛型
     * @param <R>        输出泛型
     * @return 按输出泛型定义返回
     */
    public static <T, R> R split(final Collection<T> collection, final int size, final ItemsProcessor<T, R>
            processors) {
        ArrayList<T> subList = newArrayList(size);
        for (final T instance : collection) {
            if (subList.size() > size) {
                processors.process(subList);
                subList = newArrayList(size);
            }
            subList.add(instance);
        }
        if (!CollectionUtil.isEmpty(subList)) {
            processors.process(subList);
        }
        return null;
    }


}
