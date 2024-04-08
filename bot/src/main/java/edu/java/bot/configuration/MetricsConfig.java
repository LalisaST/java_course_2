package edu.java.bot.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {
    @Bean
    public Counter processedMessagesCounter(MeterRegistry registry, ApplicationConfig config) {
        return Counter.builder(config.metrics().messageCounter().name())
            .description(config.metrics().messageCounter().description())
            .register(registry);
    }
}
