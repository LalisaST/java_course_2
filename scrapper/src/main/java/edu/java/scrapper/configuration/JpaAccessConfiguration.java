package edu.java.scrapper.configuration;

import edu.java.scrapper.client.BotWebClient;
import edu.java.scrapper.repositories.jpa.JpaChatRepository;
import edu.java.scrapper.repositories.jpa.JpaLinkRepository;
import edu.java.scrapper.scheduler.linkhandler.LinkHandler;
import edu.java.scrapper.scheduler.service.jpa.JpaLinkUpdater;
import edu.java.scrapper.services.jpa.JpaLinkService;
import edu.java.scrapper.services.jpa.JpaTgChatService;
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
