package com.isuper.eden.eve.boot.common.holder;

import java.util.concurrent.ConcurrentHashMap;


public class RequestHolder {

    private final static InheritableThreadLocal<ConcurrentHashMap<String, Object>> requestTheadHolder = new InheritableThreadLocal<>();


    public synchronized static void add(String key, Object vaule) {

        if (requestTheadHolder.get() == null) {
            requestTheadHolder.set(new ConcurrentHashMap<>(0));
        }
        requestTheadHolder.get().put(key, vaule);
    }

    public static Object get(String key) {
        return requestTheadHolder.get() == null ? null : requestTheadHolder.get().get(key);
    }

    public static Object get(String key, Object defaultValue) {
        return requestTheadHolder.get() == null ? defaultValue : requestTheadHolder.get().get(key);
    }

    public static void remove() {
        requestTheadHolder.remove();
    }

}
