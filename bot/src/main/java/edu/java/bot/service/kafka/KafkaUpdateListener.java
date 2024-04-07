package edu.java.bot.service.kafka;

import edu.java.bot.dto.bot.LinkUpdateRequest;
import edu.java.bot.service.UpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaUpdateListener {
    private final UpdateService updateService;
    private final UpdateDeadLetterQueue updateDeadLetterQueue;

    @KafkaListener(topics = "${app.kafka.topic.name}",
                   groupId = "${app.kafka.consumer.group-id}",
                   containerFactory = "kafkaListenerContainerFactory")
    public void listen(@Payload LinkUpdateRequest linkUpdateRequest, Acknowledgment acknowledgment) {
        try {
            updateService.sendUpdate(linkUpdateRequest);
        } catch (Exception ignored) {
            updateDeadLetterQueue.send(linkUpdateRequest);
        }
        acknowledgment.acknowledge();
    }
}
