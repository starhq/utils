package com.star.collection;

import com.star.lang.Filter;
import com.star.object.ObjectUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map工具类，初步意向用来初始化map
 *
 * @author starhq
 */
public final class MapUtil {

    /**
     * 默认初始大小
     */
    public static final int DEFAULT_CAPACITY = 16;
    /**
     * 默认增长因子
     */
    public static final float DEFAULT_FACTOR = 0.75f;


    private MapUtil() {
    }

    /**
     * 判断Map是否为空
     *
     * @param map 集合
     * @return 是否为空
     */
    public static boolean isEmpty(final Map<?, ?> map) {
        return Objects.isNull(map) || map.isEmpty();
    }

    /**
     * 创建新的HashMap
     *
     * @param <K> 泛型 键
     * @param <V> 泛型 值
     * @return HashMap
     */
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>();
    }

    /**
     * 创建新的HashMap
     *
     * @param size     HashMap大小
     * @param isSorted 是否排序，排序会返回LinkedHashMap
     * @param <K>      泛型 键
     * @param <V>      泛型 值
     * @return HashMap
     */
    public static <K, V> HashMap<K, V> newHashMap(final int size, final boolean isSorted) {
        final int capacity = (int) (size / DEFAULT_FACTOR);
        return isSorted ? new LinkedHashMap<>(capacity) : new HashMap<>(capacity);
    }

    /**
     * 创建新的HashMap
     *
     * @param size HashMap大小
     * @param <K>  泛型 键
     * @param <V>  泛型 值
     * @return HashMap
     */
    public static <K, V> HashMap<K, V> newHashMap(final int size) {
        return newHashMap(size, false);
    }

    /**
     * 创建新的HashMap
     *
     * @param isSorted 是否需要排序
     * @param <K>      泛型 键
     * @param <V>      泛型 值
     * @return HashMap
     */
    public static <K, V> HashMap<K, V> newHashMap(final boolean isSorted) {
        return newHashMap(DEFAULT_CAPACITY, isSorted);
    }

    /**
     * 初始化ConcurrentHashMap
     *
     * @param <K> 泛型 键
     * @param <V> 泛型 值
     * @return ConcurrentHashMap
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * 初始化ConcurrentHashMap
     *
     * @param size 初始化大小
     * @param <K>  泛型 键
     * @param <V>  泛型 值
     * @return ConcurrentHashMap
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(final int size) {
        return new ConcurrentHashMap<>(size);
    }

    /**
     * 初始化TreeMap
     *
     * @param <K> 泛型 键
     * @param <V> 泛型 值
     * @return TreeMap
     */
    public static <K, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap<>();
    }

    /**
     * 过滤ap
     *
     * @param map    需要过滤的map
     * @param filter 过滤器
     * @param <K>    泛型 键
     * @param <V>    泛型 值
     * @return 过滤后的Map
     */
    public static <K, V> Map<K, V> filter(final Map<K, V> map, final Filter<Map.Entry<K, V>> filter) {
        final Map<K, V> result = ObjectUtil.clone(map);
        if (!isEmpty(result)) {
            result.clear();
            for (final Map.Entry<K, V> entry : map.entrySet()) {
                if (filter.accept(entry)) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return result;
    }
}
