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
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

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
    public static <T> List<T> newArrayList(final T... values) {
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
    public static <T> List<T> newArrayList(final Collection<T> collection) {
        return CollectionUtil.isEmpty(collection) ? new ArrayList<>() : new ArrayList<>(collection);
    }

    /**
     * iterable包装成ArrayList
     *
     * @param iterable 迭代器
     * @param <T>      范型
     * @return ArrayList
     */
    public static <T> List<T> newArrayList(final Iterable<T> iterable) {
        return IterUtil.isEmpty(iterable) ? new ArrayList<>() : newArrayList(iterable.iterator());
    }

    /**
     * iterator包装成ArrayList
     *
     * @param iterator 迭代器
     * @param <T>      范型
     * @return ArrayList
     */
    public static <T> List<T> newArrayList(final Iterator<T> iterator) {
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
     * 创建定长队列
     *
     * @param capacity 队列大小
     * @param isLinked 是否有界
     * @param <T>      泛型
     * @return 队列
     */
    public static <T> BlockingQueue<T> newBlockingQueue(final int capacity, final boolean isLinked) {
        return isLinked ? new LinkedBlockingQueue<>(capacity) : new ArrayBlockingQueue<>(capacity);
    }

    /**
     * 集合去重
     *
     * @param collection 集合
     * @param <T>        泛型
     * @return AArrayList
     */
    public static <T> List<T> distinct(final Collection<T> collection) {
        return CollectionUtil.isEmpty(collection) ? new ArrayList<>() : collection instanceof Set ? new ArrayList<>
                (collection) : new ArrayList<>(new LinkedHashSet<>(collection));
    }

    /**
     * 创建LinkedList
     *
     * @param <T> 泛型
     * @return LinkedList
     */
    public static <T> List<T> newLinkedList() {
        return new LinkedList<>();
    }

    /**
     * 创建ArrayList
     *
     * @param capacity 初始化大小
     * @param <T>      泛型
     * @return ArrayList
     */
    public static <T> List<T> newArrayList(final int capacity) {
        return new ArrayList<>(capacity);
    }

    /**
     * 切割集合并进行处理
     *
     * @param collection 集合
     * @param size       按size大小切分
     * @param processors 处理器
     * @param <T>        输入泛型
     */
    @SuppressWarnings("unchecked")
    public static <T> void batchProcess(final Collection<T> collection, final int size, final ItemsProcessor processors) {
        List<T> subList = newArrayList(size);
        for (final T instance : collection) {
            if (subList.size() >= size) {
                processors.process(subList);
                subList = newArrayList(size);
            }
            subList.add(instance);
        }
        if (!CollectionUtil.isEmpty(subList)) {
            processors.process(subList);
        }
    }


}
