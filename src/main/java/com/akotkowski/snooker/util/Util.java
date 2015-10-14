package com.akotkowski.snooker.util;

import java.util.Map;

/**
 * Created by adam on 28/09/15.
 */
public class Util {

    public static Object getOrDefault(Map map, Object key, Object defaultVal) {
        Object ret = map.get(key);
        if (ret != null) return ret;
        return defaultVal;
    }

    public static Integer getOrDefault(Map<?,Integer> map, Object key, Integer defaultVal) {
        Integer ret = map.get(key);
        if (ret != null) return ret;
        return defaultVal;
    }
}
