package com.demonstrate.cache;

import com.demonstrate.cache.factory.CacheFactory;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class CacheUtil<K, V> {

    @Value("")
    private int maxSize;

    @Value("")
    private long duration;

    @Value("")
    private String timeUnit;

    @PostConstruct
    public Cache<K, V> initCache(Function<K, V> function) {

        return (Cache<K, V>) CacheFactory.createCache(function, maxSize, duration, TimeUnit.valueOf(timeUnit));
    }

}
