package edu.java.scrapper.configuration;

import edu.java.scrapper.configuration.utils.AccessType;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @Bean
    @NotNull
    Scheduler scheduler,

    @NotNull
    AccessType databaseAccessType,

    @NotNull
    Client githubClient,

    @NotNull
    Client stackoverflowClient,

    @NotNull
    Client botClient
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record Client(String url, @NotNull Retry retry) {
        public enum BackoffPolicy {
            FIXED, LINEAR, EXPONENTIAL
        }

        public record Retry(
            @NotNull
            BackoffPolicy backoffPolicy,
            @NotNull
            Integer maxAttempts,
            @NotNull
            Duration initDelay,
            Set<Integer> codes

        ) {
        }
    }
}
