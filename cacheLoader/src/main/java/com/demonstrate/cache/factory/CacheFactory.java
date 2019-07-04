package com.demonstrate.cache.factory;

import com.demonstrate.cache.loader.OptionalCacheLoader;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public final class CacheFactory {

    public static <K, V> Cache<K, Optional<V>> createCache(Function<K, V> loadMethod, @Nonnull final int maxSize, @Nonnull final long duration, @Nonnull final TimeUnit unit) {
        return CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(duration, unit)
                .build(new OptionalCacheLoader<>(loadMethod));
    }
}
