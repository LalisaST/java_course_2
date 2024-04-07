package edu.java.bot.service.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dto.bot.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateDeadLetterQueue {
    private final ApplicationConfig applicationConfig;
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    public void send(@Payload LinkUpdateRequest linkUpdateRequest) {
        kafkaTemplate.send(applicationConfig.kafka().topicDlq().name(), linkUpdateRequest);
    }
}
