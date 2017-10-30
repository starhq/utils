package com.star.cache;

import com.star.exception.CacheException;

public interface Cache<K, V> {

    <K, V> V get(K key) throws CacheException;

    <K, V> V put(K key, V value) throws CacheException;

    <K, V> V remove(K Key) throws CacheException;

    void clear() throws CacheException;

}
