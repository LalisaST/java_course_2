package edu.java.bot.service.kafka;

import edu.java.bot.dto.bot.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateListenerDeadLetterQueue {

    @KafkaListener(topics = "${app.kafka.topic-dlq.name}",
                   groupId = "${app.kafka.consumer.group-id}",
                   containerFactory = "kafkaListenerContainerFactory")
    public void listen(@Payload LinkUpdateRequest linkUpdateRequest, Acknowledgment acknowledgment) {
        log.info(linkUpdateRequest.toString());
        acknowledgment.acknowledge();
    }
}
