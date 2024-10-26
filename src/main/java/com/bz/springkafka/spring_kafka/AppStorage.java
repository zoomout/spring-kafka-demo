package com.bz.springkafka.spring_kafka;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.NONE)
public class AppStorage {

    private static final Map<String, String> STORAGE = new ConcurrentHashMap<>();

    public static void add(String k, String v) {
        STORAGE.put(k, v);
    }

    public static String get(String k) {
        return STORAGE.get(k);
    }

}
