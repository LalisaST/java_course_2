package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    @NotNull
    Client scrapperClient,
    @NotNull
    Kafka kafka
) {
    public record Kafka(
        @NotNull KafkaProducerProperties producer,
        @NotNull KafkaConsumerProperties consumer,
        @NotNull Topic topic,
        @NotNull Topic topicDlq
    ) {
        public record KafkaConsumerProperties(
            @NotNull String bootstrapServers,
            @NotNull String groupId,
            @NotNull String autoOffsetReset,
            @NotNull Integer maxPollIntervalMs,
            @NotNull Integer concurrency
        ) {
        }

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

    public record Client(String url, @NotNull Retry retry) {
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

        public enum BackoffPolicy {
            FIXED, LINEAR, EXPONENTIAL
        }
    }
}
