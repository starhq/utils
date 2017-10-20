package com.star.collection.iter;

import com.star.collection.map.MapUtil;
import com.star.string.StringUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * 迭代器工具类
 *
 * @author starhq
 */
public final class IterUtil {

    private IterUtil() {
    }

    /**
     * 迭代器是否为空
     *
     * @param iterator 迭代器
     * @return 是否为空
     */
    public static boolean isEmpty(final Iterator<?> iterator) {
        return Objects.isNull(iterator) || iterator.hasNext();
    }

    /**
     * iterable是否为空
     *
     * @param iterable iterable对象
     * @return 是否为空
     */
    public static boolean isEmpty(final Iterable<?> iterable) {
        return Objects.isNull(iterable) || isEmpty(iterable.iterator());
    }

    /**
     * 获取iterable中元素出现的次数
     *
     * @param iterable iterable
     * @param <T>      泛型
     * @return 元素为key，次数为value的Map
     */
    public static <T> Map<T, Integer> countMap(final Iterable<T> iterable) {
        return Objects.isNull(iterable) ? Collections.emptyMap() : countMap(iterable.iterator());
    }

    /**
     * 获取迭代器中元素出现的次数
     *
     * @param iterator 迭代器
     * @param <T>      泛型
     * @return 元素为key，次数为value的Map
     */
    public static <T> Map<T, Integer> countMap(final Iterator<T> iterator) {
        final HashMap<T, Integer> countMap = MapUtil.newHashMap();
        if (!isEmpty(iterator)) {
            Integer count;
            T instance;
            while (iterator.hasNext()) {
                instance = iterator.next();
                count = countMap.get(instance);
                if (Objects.isNull(count)) {
                    countMap.put(instance, 1);
                } else {
                    countMap.put(instance, count + 1);
                }
            }
        }
        return countMap;
    }

    /**
     * 把迭代器转换成字符串
     *
     * @param iterator  迭代器
     * @param delimiter 分隔符
     * @param <T>       泛型
     * @return 封装好的字符串
     */
    public static <T> Optional<String> join(final Iterator<T> iterator, final String delimiter) {
        Optional<String> result = Optional.empty();
        if (!isEmpty(iterator)) {
            final StringJoiner joiner = StringUtil.joiner(StringUtil.EMPTY, StringUtil.EMPTY, delimiter);
            T instance;
            while (iterator.hasNext()) {
                instance = iterator.next();
                joiner.add(StringUtil.str(instance));
            }
            result = Optional.of(joiner.toString());
        }
        return result;
    }

    /**
     * 把iterable转换成字符串
     *
     * @param iterable  迭代器
     * @param delimiter 分隔符
     * @param <T>       泛型
     * @return 封装好的字符串
     */
    public static <T> Optional<String> join(final Iterable<T> iterable, final String delimiter) {
        return Objects.isNull(iterable) ? Optional.empty() : join(iterable.iterator(), delimiter);
    }

    /**
     * 获取迭代器中的元素类型
     *
     * @param iterator 迭代器
     * @return 元素类型
     */
    public static Optional<Class<?>> getElementType(final Iterator<?> iterator) {
        Optional<Class<?>> result = Optional.empty();
        if (!isEmpty(iterator)) {
            Object obj;
            while (iterator.hasNext()) {
                obj = iterator.next();
                if (!Objects.isNull(obj)) {
                    result = Optional.of(obj.getClass());
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 获取iterable中的元素类型
     *
     * @param iterable 迭代器
     * @return 元素类型
     */
    public static Optional<Class<?>> getElementType(final Iterable<?> iterable) {
        return Objects.isNull(iterable) ? Optional.empty() : getElementType(iterable.iterator());
    }
}
