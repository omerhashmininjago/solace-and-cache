package com.demonstrate.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(name = "cache.autoconfigure.enabled", matchIfMissing = true)
public class CacheConfiguration {
}
