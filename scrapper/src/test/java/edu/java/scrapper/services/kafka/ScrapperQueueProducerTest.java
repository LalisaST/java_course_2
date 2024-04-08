package edu.java.scrapper.services.kafka;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dto.bot.LinkUpdateRequest;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ScrapperQueueProducerTest extends KafkaIntegrationTest {
    @Autowired
    ApplicationConfig applicationConfig;
    @Autowired
    ScrapperQueueProducer scrapperQueueProducer;

    @Test
    @DisplayName("Проверка работы ScrapperQueue")
    void checkingScrapperQueueWork () {
        try (KafkaConsumer<String, LinkUpdateRequest> consumer = new KafkaConsumer<>(consumerProps())) {
            consumer.subscribe(List.of(applicationConfig.kafka().topic().name()));
            scrapperQueueProducer.send(new LinkUpdateRequest(1L, URI.create("uri"), " ", List.of()));

            int count = consumer.poll(Duration.ofSeconds(1)).count();

            assertThat(count).isEqualTo(1);
        }
    }

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 1_000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LinkUpdateRequest.class);
        return props;
    }

    @DynamicPropertySource
    static void kafkaConfig(DynamicPropertyRegistry registry) {
        registry.add("app.kafka.producer.bootstrap-servers", () -> "localhost:9092");
        registry.add("app.kafka.producer.acks-mode", () -> "0");
        registry.add("app.kafka.producer.linger-ms", () -> 0);
        registry.add("app.kafka.topic.name", () -> "updates_queue");
        registry.add("app.kafka.topic.partitions", () -> 1);
        registry.add("app.kafka.topic.replicas", () -> 1);
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.liquibase.enabled", () -> false);
        registry.add("app.use-queue", () -> true);
    }
}
