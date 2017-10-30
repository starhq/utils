package com.star.cache.simple;

import com.star.cache.Cache;
import com.star.exception.CacheException;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SimpleCache<K, V> implements Cache<K, V> {

    private final Map<K, V> cache = new WeakHashMap<K, V>();

    private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock(false);
    private final ReentrantReadWriteLock.ReadLock readLock = cacheLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = cacheLock.writeLock();


    @Override
    public <K, V> V get(K key) throws CacheException {
        return null;
    }

    @Override
    public <K, V> V put(K key, V value) throws CacheException {
        return null;
    }

    @Override
    public <K, V> V remove(K Key) throws CacheException {
        return null;
    }

    @Override
    public void clear() throws CacheException {
    }
}
