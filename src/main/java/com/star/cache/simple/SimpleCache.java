package com.star.cache.simple;

import com.star.cache.Cache;
import com.star.exception.CacheException;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 缓存简单实现
 *
 * @param <K> 键
 * @param <V> 值
 * @author starhq
 */
public class SimpleCache<K, V> implements Cache<K, V> {

    /**
     * 池，存放缓存
     */
    private final Map<K, V> CACHE = new WeakHashMap<>();

    /**
     * 锁
     */
    private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock(false);
    /**
     * 写锁
     */
    private final ReentrantReadWriteLock.WriteLock writeLock = cacheLock.writeLock();
    /**
     * 读锁
     */
    private final ReentrantReadWriteLock.ReadLock readLock = cacheLock.readLock();

    /**
     * 从缓存中获得值
     *
     * @param key 键
     * @return 值
     * @throws CacheException 缓存异常
     */
    @Override
    public V get(K key) throws CacheException {
        readLock.lock();
        try {
            return CACHE.get(key);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 数据存入缓存
     *
     * @param key   键
     * @param value 值
     * @return 值
     * @throws CacheException 缓存异常
     */
    @Override
    public V put(K key, V value) throws CacheException {
        writeLock.lock();
        try {
            CACHE.put(key, value);
        } finally {
            writeLock.unlock();
        }
        return value;
    }

    /**
     * 删除缓存
     *
     * @param key 键
     * @return 从缓存删除的值
     * @throws CacheException 缓存异常
     */
    @Override
    public V remove(K key) throws CacheException {
        writeLock.lock();
        try {
            return CACHE.remove(key);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 清空缓存
     *
     * @throws CacheException 缓存异常
     */
    @Override
    public void clear() throws CacheException {
        writeLock.lock();
        try {
            CACHE.clear();
        } finally {
            writeLock.unlock();
        }
    }
}
