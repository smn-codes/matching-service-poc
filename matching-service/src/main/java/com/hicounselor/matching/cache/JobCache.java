package com.hicounselor.matching.cache;

public interface JobCache<K, V> {

    V get(K key);

    void put(K k, V v);

}
