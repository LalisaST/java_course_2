package edu.java.scrapper.configuration;

import edu.java.scrapper.client.BotWebClient;
import edu.java.scrapper.repositories.jdbc.JdbcChatDao;
import edu.java.scrapper.repositories.jdbc.JdbcChatLinkDao;
import edu.java.scrapper.repositories.jdbc.JdbcLinkDao;
import edu.java.scrapper.scheduler.linkhandler.LinkHandler;
import edu.java.scrapper.scheduler.service.jdbc.JdbcLinkUpdater;
import edu.java.scrapper.services.jdbc.JdbcLinkService;
import edu.java.scrapper.services.jdbc.JdbcTgChatService;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    @Bean
    public JdbcLinkService jdbcLinkService(
        JdbcLinkDao jdbcLinkDao,
        JdbcChatDao jdbcChatDao,
        JdbcChatLinkDao jdbcChatLinkDao
    ) {
        return new JdbcLinkService(jdbcLinkDao, jdbcChatDao, jdbcChatLinkDao);
    }

    @Bean
    public JdbcTgChatService jdbcTgChatService(JdbcChatDao jdbcChatDao, JdbcChatLinkDao jdbcChatLinkDao) {
        return new JdbcTgChatService(jdbcChatDao, jdbcChatLinkDao);
    }

    @Bean
    public JdbcLinkUpdater jdbcLinkUpdater(
        ApplicationConfig applicationConfig,
        JdbcLinkService jdbcLinkService,
        JdbcTgChatService jdbcTgChatService,
        List<LinkHandler> linkHandlers,
        BotWebClient botWebClient
    ) {
        return new JdbcLinkUpdater(applicationConfig, jdbcLinkService, jdbcTgChatService, linkHandlers, botWebClient);
    }

}
