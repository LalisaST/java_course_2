package edu.java.scrapper.scheduler.linkupdaters;

import edu.java.client.BotWebClient;
import edu.java.configuration.ApplicationConfig;
import edu.java.model.Link;
import edu.java.scheduler.service.LinkUpdater;
import edu.java.scheduler.linkhandler.impl.GitHubLinkHandler;
import edu.java.scheduler.linkhandler.HandlerResult;
import edu.java.scheduler.service.jdbc.JdbcLinkUpdater;
import edu.java.services.jdbc.JdbcLinkService;
import edu.java.services.jdbc.JdbcTgChatService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JdbcLinkUpdaterTest {
    @Mock
    private JdbcLinkService jdbcLinkService;
    @Mock
    private JdbcTgChatService jdbcTgChatService;
    @Mock
    private ApplicationConfig applicationConfig;
    @Mock
    private GitHubLinkHandler gitHubLinkHandler;
    @Mock
    private BotWebClient botWebClient;

    @Test
    @DisplayName("Проверка функции update")
    void updateTest() {
        Duration time = Duration.ofSeconds(10L);
        when(applicationConfig.scheduler()).thenReturn(new ApplicationConfig.Scheduler(false, time, time));

        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        List<Link> links = List.of(
            new Link(1L, URI.create("https://github.com/LalisaST/java_course_2"), offsetDateTime, offsetDateTime));

        when(jdbcLinkService.searchForUpdateLinks(
            applicationConfig.scheduler().forceCheckDelay().getSeconds())).thenReturn(links);
        when(gitHubLinkHandler.supports(URI.create("https://github.com/LalisaST/java_course_2"))).thenReturn(true);

        when(gitHubLinkHandler.updateLink(links.getFirst())).thenReturn(new HandlerResult(
            true,
            "updated",
            OffsetDateTime.now()
        ));

        LinkUpdater linkUpdater = new JdbcLinkUpdater(applicationConfig,
            jdbcLinkService,
            jdbcTgChatService,
            List.of(gitHubLinkHandler),
            botWebClient
        );
        linkUpdater.update();

        verify(gitHubLinkHandler, times(1)).updateLink(any());
    }
}
