package com.demonstrate;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@Configuration
@ConditionalOnProperty(name = "exception.autoconfigure.enabled", matchIfMissing = true)
public class ExceptionConfiguration {
}
