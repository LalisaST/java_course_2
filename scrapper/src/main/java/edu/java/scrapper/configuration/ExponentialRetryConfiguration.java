package edu.java.scrapper.configuration;

import edu.java.scrapper.retry.LinearRetryFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

public class ExponentialRetryConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "app", name = "bot-client.retry.backoff-policy", havingValue = "exponential")
    ExchangeFilterFunction botRetryFilter(ApplicationConfig applicationConfig) {
        return new LinearRetryFilter(applicationConfig.botClient());
    }

    @Bean
    @ConditionalOnProperty(prefix = "app",
                           name = "stackoverflow-client.retry.backoff-policy",
                           havingValue = "exponential")
    ExchangeFilterFunction stackOverflowRetryFilter(ApplicationConfig applicationConfig) {
        return new LinearRetryFilter(applicationConfig.stackoverflowClient());
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "github-client.retry.backoff-policy", havingValue = "exponential")
    ExchangeFilterFunction gitHubRetryFilter(ApplicationConfig applicationConfig) {
        return new LinearRetryFilter(applicationConfig.githubClient());
    }
}
