package edu.java.configuration;

import edu.java.client.BotWebClient;
import edu.java.repositories.jpa.JpaChatRepository;
import edu.java.repositories.jpa.JpaLinkRepository;
import edu.java.scheduler.linkhandler.LinkHandler;
import edu.java.scheduler.service.jpa.JpaLinkUpdater;
import edu.java.services.jpa.JpaLinkService;
import edu.java.services.jpa.JpaTgChatService;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public JpaLinkService jpaLinkService(JpaChatRepository jpaChatRepository, JpaLinkRepository jpaLinkRepository) {
        return new JpaLinkService(jpaChatRepository, jpaLinkRepository);
    }

    @Bean
    public JpaTgChatService jpaTgChatService(JpaChatRepository jpaChatRepository, JpaLinkRepository jpaLinkRepository) {
        return new JpaTgChatService(jpaChatRepository, jpaLinkRepository);
    }

    @Bean
    public JpaLinkUpdater jpaLinkUpdater(
        ApplicationConfig applicationConfig,
        JpaLinkService jpaLinkService,
        JpaTgChatService jpaTgChatService,
        List<LinkHandler> linkHandlers,
        BotWebClient botWebClient
    ) {
        return new JpaLinkUpdater(applicationConfig, jpaLinkService, jpaTgChatService, linkHandlers, botWebClient);
    }
}
