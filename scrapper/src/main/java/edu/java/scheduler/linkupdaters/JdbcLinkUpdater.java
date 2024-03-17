package edu.java.scheduler.linkupdaters;

import edu.java.client.BotWebClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.scheduler.linkhandlers.LinkHandler;
import edu.java.services.jdbc.JdbcLinkService;
import edu.java.services.jdbc.JdbcTgChatService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkUpdater extends DefaultLinkUpdater {
    public JdbcLinkUpdater(
        ApplicationConfig applicationConfig,
        JdbcLinkService jdbcLinkService,
        JdbcTgChatService jdbcTgChatService,
        List<LinkHandler> linkHandlers,
        BotWebClient botWebClient
    ) {
        super(applicationConfig, jdbcLinkService, jdbcTgChatService, linkHandlers, botWebClient);
    }
}
