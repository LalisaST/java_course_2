package edu.java.scheduler.linkupdaters;

import edu.java.client.BotWebClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.scheduler.linkhandlers.LinkHandler;
import edu.java.services.jooq.JooqLinkService;
import edu.java.services.jooq.JooqTgChatService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JooqLinkUpdater extends DefaultLinkUpdater {
    public JooqLinkUpdater(
        ApplicationConfig applicationConfig,
        JooqLinkService jooqLinkService,
        JooqTgChatService jooqTgChatService,
        List<LinkHandler> linkHandlers,
        BotWebClient botWebClient
    ) {
        super(applicationConfig, jooqLinkService, jooqTgChatService, linkHandlers, botWebClient);
    }
}
