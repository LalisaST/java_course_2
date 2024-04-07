package edu.java.bot.service.kafka;

import edu.java.bot.TgBot;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dto.bot.LinkUpdateRequest;
import edu.java.bot.service.UpdateService;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1, ports = {9092}, kraft = false)
class KafkaUpdateListenerTest {
    @Autowired
    ApplicationConfig applicationConfig;
    @MockBean
    UpdateDeadLetterQueue updateDeadLetterQueue;
    @MockBean
    UpdateService updateService;
    @SpyBean
    @Autowired
    KafkaUpdateListener kafkaUpdateListener;
    @MockBean
    @Autowired
    TgBot tgBot;

    @Test
    @DisplayName("Проверка работы кафки")
    void checkingKafkaWork() {
        try (KafkaProducer<String, LinkUpdateRequest> kafkaProducer = new KafkaProducer<>(producerProps())) {
            LinkUpdateRequest request = new LinkUpdateRequest(1L, URI.create("url"), "description", List.of());
            kafkaProducer.send(new ProducerRecord<>(applicationConfig.kafka().topic().name(), request));
            kafkaProducer.send(new ProducerRecord<>(applicationConfig.kafka().topic().name(), request));
            kafkaProducer.send(new ProducerRecord<>(applicationConfig.kafka().topic().name(), request));

            verify(kafkaUpdateListener, after(1000).times(3)).listen(eq(request), any());
        }
    }

    @Test
    @DisplayName("Проверка работы DSQ")
    void checkingDsqWork() {
        doThrow(new RuntimeException()).when(updateService).sendUpdate(any());
        try (KafkaProducer<String, LinkUpdateRequest> kafkaProducer = new KafkaProducer<>(producerProps())) {
            LinkUpdateRequest request = new LinkUpdateRequest(1L, URI.create("url"), "description", List.of());
            kafkaProducer.send(new ProducerRecord<>(applicationConfig.kafka().topic().name(), request));
            kafkaProducer.send(new ProducerRecord<>(applicationConfig.kafka().topic().name(), request));
            kafkaProducer.send(new ProducerRecord<>(applicationConfig.kafka().topic().name(), request));

            verify(updateDeadLetterQueue, after(1000).times(3)).send(request);
        }
    }

    private Map<String, Object> producerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "0");
        props.put(ProducerConfig.LINGER_MS_CONFIG, 0);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @DynamicPropertySource
    static void kafkaConfig(DynamicPropertyRegistry registry) {
        registry.add("app.kafka.producer.bootstrap-servers", () -> "localhost:9092");
        registry.add("app.kafka.consumer.bootstrap-servers", () -> "localhost:9092");
        registry.add("app.kafka.consumer.group-id", () -> "group");
        registry.add("app.kafka.consumer.auto-offset-reset", () -> "earliest");
        registry.add("app.kafka.consumer.max-poll-interval-ms", () -> 1_000);
        registry.add("app.kafka.consumer.concurrency", () -> 1);
        registry.add("app.kafka.producer.acks-mode", () -> "0");
        registry.add("app.kafka.producer.linger-ms", () -> 0);
        registry.add("app.kafka.topic.name", () -> "updates");
        registry.add("app.kafka.topic.partitions", () -> 1);
        registry.add("app.kafka.topic.replicas", () -> 1);
    }
}
