package com.base.baselibrary.model.entity;

import java.io.Serializable;

/**
 * Created by 15600 on 2017/7/4.
 */

public class KeyValuePair<K , V> implements
        Serializable{
    K key;
    V value;

    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }


    /**
     * @return the key
     */
    public K getKey() {
        return key;
    }

    /**
     * @return the value
     */
    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "[" + key + "," + value + "]";
    }
}
