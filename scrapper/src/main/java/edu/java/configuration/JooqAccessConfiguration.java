package edu.java.configuration;

import edu.java.client.BotWebClient;
import edu.java.repositories.jooq.JooqChatDao;
import edu.java.repositories.jooq.JooqChatLinkDao;
import edu.java.repositories.jooq.JooqLinkDao;
import edu.java.scheduler.linkhandler.LinkHandler;
import edu.java.scheduler.service.jooq.JooqLinkUpdater;
import edu.java.services.jooq.JooqLinkService;
import edu.java.services.jooq.JooqTgChatService;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    public JooqLinkService jooqLinkService(
        JooqLinkDao jooqLinkDao,
        JooqChatDao jooqChatDao,
        JooqChatLinkDao jooqChatLinkDao
    ) {
        return new JooqLinkService(jooqLinkDao, jooqChatDao, jooqChatLinkDao);
    }

    @Bean
    public JooqTgChatService jooqTgChatService(JooqChatDao jooqChatDao, JooqChatLinkDao jooqChatLinkDao) {
        return new JooqTgChatService(jooqChatDao, jooqChatLinkDao);
    }

    @Bean
    public JooqLinkUpdater jooqLinkUpdater(
        ApplicationConfig applicationConfig,
        JooqLinkService jooqLinkService,
        JooqTgChatService jooqTgChatService,
        List<LinkHandler> linkHandlers,
        BotWebClient botWebClient
    ) {
        return new JooqLinkUpdater(applicationConfig, jooqLinkService, jooqTgChatService, linkHandlers, botWebClient);
    }
}
