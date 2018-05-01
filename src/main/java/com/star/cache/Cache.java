package com.star.cache;

import com.star.exception.CacheException;

/**
 * 缓存接口库
 *
 * @param <K> 键
 * @param <V> 值
 * @author starhq
 */
public interface Cache<K, V> {

    /**
     * 从缓存中获得值
     *
     * @param key 键
     * @return 值
     * @throws CacheException 缓存异常
     */
    V get(K key) throws CacheException;

    /**
     * 数据存入缓存
     *
     * @param key   键
     * @param value 值
     * @return 值
     * @throws CacheException 缓存异常
     */
    V put(K key, V value) throws CacheException;

    /**
     * 删除缓存
     *
     * @param key 键
     * @return 从缓存删除的值
     * @throws CacheException 缓存异常
     */
    V remove(K key) throws CacheException;

    /**
     * 清空缓存
     *
     * @throws CacheException 缓存异常
     */
    void clear() throws CacheException;

}
