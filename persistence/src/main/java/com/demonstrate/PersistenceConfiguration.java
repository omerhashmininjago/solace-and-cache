package com.demonstrate;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ConditionalOnProperty(name = "persistence.autoconfigure.enabled", matchIfMissing = true)
@Configuration
@EnableAutoConfiguration
@EntityScan
@EnableJpaRepositories
@ComponentScan
public class PersistenceConfiguration {
}
