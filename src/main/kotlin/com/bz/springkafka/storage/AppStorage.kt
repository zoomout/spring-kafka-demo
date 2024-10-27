package com.bz.springkafka.storage

import java.util.concurrent.ConcurrentHashMap

object AppStorage {
    private val STORAGE: MutableMap<String, String> = ConcurrentHashMap()

    fun add(k: String, v: String) {
        STORAGE[k] = v
    }

    fun get(k: String): String? {
        return STORAGE[k]
    }

}
