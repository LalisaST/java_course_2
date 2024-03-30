package edu.java.scrapper.scheduler.service.jooq;

import edu.java.scrapper.client.BotWebClient;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.scheduler.linkhandler.LinkHandler;
import edu.java.scrapper.scheduler.service.DefaultLinkUpdater;
import edu.java.scrapper.services.jooq.JooqLinkService;
import edu.java.scrapper.services.jooq.JooqTgChatService;
import java.util.List;

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
