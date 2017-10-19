package com.star.collection.map;

import java.util.Map;

/**
 * Map创建类,build模式
 *
 * @param <K> 泛型 键
 * @param <V> 泛型 值
 * @author starhq
 */
public class MapBuilder<K, V> {

    /**
     * 需要操作的Map
     */
    private final Map<K, V> maps;

    /**
     * 构造函数
     *
     * @param maps 需要操作的Map
     */
    public MapBuilder(final Map<K, V> maps) {
        this.maps = maps;
    }

    /**
     * 链式Map创建
     *
     * @param key   键
     * @param value 值
     * @return 当前对象
     */
    public MapBuilder<K, V> put(final K key, final V value) {
        maps.put(key, value);
        return this;
    }

    /**
     * 链式Map创建
     *
     * @param map 合并map
     * @return 当前对象
     */
    public MapBuilder<K, V> putAll(final Map<K, V> map) {
        this.maps.putAll(map);
        return this;
    }

    /**
     * 创建后的map
     *
     * @return 创建后的map
     */
    public Map<K, V> map() {
        return maps;
    }
}
