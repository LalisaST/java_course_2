package edu.java.scrapper.services.bot;

import edu.java.scrapper.client.BotWebClient;
import edu.java.scrapper.dto.bot.LinkUpdateRequest;
import edu.java.scrapper.services.interfaces.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.use-queue", havingValue = "false")
public class HttpNotificationService implements NotificationService {
    private final BotWebClient botWebClient;

    @Override
    public void send(LinkUpdateRequest update) {
        try {
            botWebClient.update(update);
        } catch (WebClientException e) {
            log.error(e.getMessage());
        }
    }
}
