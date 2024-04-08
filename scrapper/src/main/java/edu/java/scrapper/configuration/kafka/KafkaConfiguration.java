package edu.java.scrapper.configuration.kafka;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.configuration.ApplicationConfig.Kafka.KafkaProducerProperties;
import edu.java.scrapper.dto.bot.LinkUpdateRequest;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@ConditionalOnProperty(name = "app.use-queue", havingValue = "true")
public class KafkaConfiguration {
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
    public KafkaAdmin kafkaAdmin(ApplicationConfig applicationConfig) {
        var producerProps = applicationConfig.kafka().producer();
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, producerProps.bootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public KafkaTemplate<String, LinkUpdateRequest>
    kafkaTemplate(ProducerFactory<String, LinkUpdateRequest> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic topic(ApplicationConfig applicationConfig) {
        var topic = applicationConfig.kafka().topic();
        return TopicBuilder.name(topic.name())
            .partitions(topic.partitions())
            .replicas(topic.replicas())
            .build();
    }
}
