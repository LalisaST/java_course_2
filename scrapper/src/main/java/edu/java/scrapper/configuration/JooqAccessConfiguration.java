package edu.java.scrapper.configuration;

import edu.java.scrapper.repositories.jooq.JooqChatDao;
import edu.java.scrapper.repositories.jooq.JooqChatLinkDao;
import edu.java.scrapper.repositories.jooq.JooqLinkDao;
import edu.java.scrapper.scheduler.linkhandler.LinkHandler;
import edu.java.scrapper.scheduler.service.jooq.JooqLinkUpdater;
import edu.java.scrapper.services.interfaces.NotificationService;
import edu.java.scrapper.services.jooq.JooqLinkService;
import edu.java.scrapper.services.jooq.JooqTgChatService;
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
        NotificationService service
    ) {
        return new JooqLinkUpdater(applicationConfig, jooqLinkService, jooqTgChatService, linkHandlers, service);
    }
}
