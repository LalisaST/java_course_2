package edu.java.scrapper.scheduler.service.jdbc;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.scheduler.linkhandler.LinkHandler;
import edu.java.scrapper.scheduler.service.DefaultLinkUpdater;
import edu.java.scrapper.services.interfaces.NotificationService;
import edu.java.scrapper.services.jdbc.JdbcLinkService;
import edu.java.scrapper.services.jdbc.JdbcTgChatService;
import java.util.List;

public class JdbcLinkUpdater extends DefaultLinkUpdater {
    public JdbcLinkUpdater(
        ApplicationConfig applicationConfig,
        JdbcLinkService jdbcLinkService,
        JdbcTgChatService jdbcTgChatService,
        List<LinkHandler> linkHandlers,
        NotificationService service
    ) {
        super(applicationConfig, jdbcLinkService, jdbcTgChatService, linkHandlers, service);
    }
}
