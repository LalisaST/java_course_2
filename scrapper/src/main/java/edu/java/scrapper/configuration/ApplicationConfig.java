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
    Client botClient,
    Kafka kafka,
    @NotNull
    boolean useQueue
) {
    public record Kafka(
        @NotNull KafkaProducerProperties producer,
        @NotNull Topic topic
    ) {
        public record KafkaProducerProperties(
            @NotNull String bootstrapServers,
            @NotNull String acksMode,
            @NotNull Integer lingerMs
        ) {
        }

        public record Topic(
            @NotNull String name,
            @NotNull Integer partitions,
            @NotNull Integer replicas
        ) {
        }
    }

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
