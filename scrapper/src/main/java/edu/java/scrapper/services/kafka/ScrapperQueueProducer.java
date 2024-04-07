package edu.java.scrapper.services.kafka;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dto.bot.LinkUpdateRequest;
import edu.java.scrapper.services.interfaces.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.use-queue", havingValue = "true")
public class ScrapperQueueProducer implements NotificationService {
    private final ApplicationConfig applicationConfig;
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    @Override
    public void send(LinkUpdateRequest update) {
        kafkaTemplate.send(applicationConfig.kafka().topic().name(), update);
    }
}
