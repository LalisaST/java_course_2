package edu.java.scrapper.scheduler.linkupdaters;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.model.scheme.Link;
import edu.java.scrapper.model.scheme.Type;
import edu.java.scrapper.scheduler.linkhandler.HandlerResult;
import edu.java.scrapper.scheduler.linkhandler.impl.GitHubLinkHandler;
import edu.java.scrapper.scheduler.service.DefaultLinkUpdater;
import edu.java.scrapper.scheduler.service.LinkUpdater;
import edu.java.scrapper.services.DefaultLinkService;
import edu.java.scrapper.services.DefaultTgChatService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import edu.java.scrapper.services.interfaces.NotificationService;
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
public class DefaultLinkUpdaterTest {
    @Mock
    private DefaultLinkService defaultLinkService;
    @Mock
    private DefaultTgChatService defaultTgChatService;
    @Mock
    private ApplicationConfig applicationConfig;
    @Mock
    private GitHubLinkHandler gitHubLinkHandler;
    @Mock
    private NotificationService service;

    @Test
    @DisplayName("Проверка функции update")
    void updateTest() {
        Duration time = Duration.ofSeconds(10L);
        when(applicationConfig.scheduler()).thenReturn(new ApplicationConfig.Scheduler(false, time, time));

        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        List<Link> links = List.of(
            new Link(
                1L,
                URI.create("https://github.com/LalisaST/java_course_2"),
                offsetDateTime,
                offsetDateTime,
                Type.GITHUB,
                0,
                0,
                0
            ));

        when(defaultLinkService.searchForUpdateLinks(
            applicationConfig.scheduler().forceCheckDelay().getSeconds())).thenReturn(links);
        when(gitHubLinkHandler.supports(URI.create("https://github.com/LalisaST/java_course_2"))).thenReturn(true);

        when(gitHubLinkHandler.updateLink(links.getFirst())).thenReturn(new HandlerResult(
            true,
            "updated",
            OffsetDateTime.now(), 0,0,0
        ));

        LinkUpdater linkUpdater = new DefaultLinkUpdater(
            applicationConfig,
            defaultLinkService,
            defaultTgChatService,
            List.of(gitHubLinkHandler),
            service
        );
        linkUpdater.update();

        verify(gitHubLinkHandler, times(1)).updateLink(any());
    }
}
