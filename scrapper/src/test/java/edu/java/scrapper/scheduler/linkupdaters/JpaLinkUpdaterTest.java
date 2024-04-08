package edu.java.scrapper.scheduler.linkupdaters;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.model.entity.Link;
import edu.java.scrapper.model.scheme.Type;
import edu.java.scrapper.scheduler.linkhandler.HandlerResult;
import edu.java.scrapper.scheduler.linkhandler.impl.GitHubLinkHandler;
import edu.java.scrapper.scheduler.service.LinkUpdater;
import edu.java.scrapper.scheduler.service.jpa.JpaLinkUpdater;
import edu.java.scrapper.services.interfaces.NotificationService;
import edu.java.scrapper.services.jpa.JpaLinkService;
import edu.java.scrapper.services.jpa.JpaTgChatService;
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
public class JpaLinkUpdaterTest {
    @Mock
    private JpaLinkService jpaLinkService;
    @Mock
    private JpaTgChatService jpaTgChatService;
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
        var linkScheme = new edu.java.scrapper.model.scheme.Link(
            1L,
            URI.create("https://github.com/LalisaST/java_course_2"),
            offsetDateTime,
            offsetDateTime,
            Type.GITHUB,
            0,
            0,
            0
        );

        Link link = new Link();
        link.setId(1L);
        link.setLastUpdate(offsetDateTime);
        link.setLastCheck(offsetDateTime);
        link.setType(Type.GITHUB);
        link.setCommentCount(0);
        link.setAnswerCount(0);
        link.setCommitCount(0);
        link.setUrl(URI.create("https://github.com/LalisaST/java_course_2"));
        List<Link> links = List.of(link);

        when(jpaLinkService.searchForUpdateLinks(
            applicationConfig.scheduler().forceCheckDelay().getSeconds())).thenReturn(links);
        when(gitHubLinkHandler.supports(URI.create("https://github.com/LalisaST/java_course_2"))).thenReturn(true);

        when(gitHubLinkHandler.updateLink(linkScheme)).thenReturn(new HandlerResult(
            true,
            "updated",
            OffsetDateTime.now(), 0, 0, 0
        ));

        LinkUpdater linkUpdater = new JpaLinkUpdater(
            applicationConfig,
            jpaLinkService,
            jpaTgChatService,
            List.of(gitHubLinkHandler),
            service
        );
        linkUpdater.update();

        verify(gitHubLinkHandler, times(1)).updateLink(any());
    }
}
