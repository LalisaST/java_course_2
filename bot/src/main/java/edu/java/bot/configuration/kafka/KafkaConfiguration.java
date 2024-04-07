package edu.java.bot.configuration.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.ApplicationConfig.Kafka.KafkaConsumerProperties;
import edu.java.bot.configuration.ApplicationConfig.Kafka.KafkaProducerProperties;
import edu.java.bot.dto.bot.LinkUpdateRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
@Slf4j
public class KafkaConfiguration {
    @Bean
    public ConsumerFactory<String, LinkUpdateRequest> consumerFactory(ApplicationConfig applicationConfig) {
        return new DefaultKafkaConsumerFactory<>(consumerProps(applicationConfig.kafka().consumer()));
    }

    private Map<String, Object> consumerProps(KafkaConsumerProperties kafkaConsumerProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerProperties.bootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProperties.groupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerProperties.autoOffsetReset());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaConsumerProperties.maxPollIntervalMs());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LinkUpdateRequest.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, LinkUpdateRequest> producerFactory(ApplicationConfig applicationConfig) {
        return new DefaultKafkaProducerFactory<>(senderProps(applicationConfig.kafka().producer()));
    }

    private Map<String, Object> senderProps(KafkaProducerProperties kafkaProducerProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperties.bootstrapServers());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerProperties.acksMode());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerProperties.lingerMs());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, LinkUpdateRequest>
    kafkaTemplate(ProducerFactory<String, LinkUpdateRequest> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaAdmin kafkaAdmin(ApplicationConfig applicationConfig) {
        var producerProps = applicationConfig.kafka().producer();
        var consumerProps = applicationConfig.kafka().consumer();
        Map<String, Object> configs = new HashMap<>();
        configs.put(
            AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
            producerProps.bootstrapServers() + "," + consumerProps.bootstrapServers()
        );
        return new KafkaAdmin(configs);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest>
    kafkaListenerContainerFactory(
        ConsumerFactory<String, LinkUpdateRequest> consumerFactory,
        ApplicationConfig applicationConfig,
        DefaultErrorHandler errorHandler
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(applicationConfig.kafka().consumer().concurrency());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        return new DefaultErrorHandler((consumerRecord, exception) -> log.error(
            "Couldn't process message: {}; {}",
            consumerRecord.value().toString(),
            exception.toString()
        ));
    }

    @Bean
    public NewTopic topic(ApplicationConfig applicationConfig) {
        var topic = applicationConfig.kafka().topic();
        return TopicBuilder.name(topic.name())
            .partitions(topic.partitions())
            .replicas(topic.replicas())
            .build();
    }

    @Bean
    public NewTopic topicDlq(ApplicationConfig applicationConfig) {
        var topic = applicationConfig.kafka().topicDlq();
        return TopicBuilder.name(topic.name())
            .partitions(topic.partitions())
            .replicas(topic.replicas())
            .build();
    }
}
