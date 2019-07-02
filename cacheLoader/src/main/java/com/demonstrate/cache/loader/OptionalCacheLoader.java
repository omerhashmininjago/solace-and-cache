package com.demonstrate.cache.loader;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheLoader;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Function;

public final class OptionalCacheLoader<K, V> extends CacheLoader<K, Optional<V>> {

    private final Function<K, V> loadMethod;

    public OptionalCacheLoader(Function<K, V> loadMethod) {
        Preconditions.checkNotNull(loadMethod);
        this.loadMethod = loadMethod;
    }

    @Override
    public Optional<V> load(@Nonnull final K key) throws Exception {
        return Optional.ofNullable(loadMethod.apply(key));
    }
}
