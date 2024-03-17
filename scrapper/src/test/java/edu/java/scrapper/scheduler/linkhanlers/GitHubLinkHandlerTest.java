package edu.java.scrapper.scheduler.linkhanlers;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.client.GitHubWebClient;
import edu.java.model.Link;
import edu.java.scheduler.linkhandlers.GitHubLinkHandler;
import edu.java.scheduler.linkhandlers.HandlerResult;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GitHubLinkHandlerTest {
    private final GitHubWebClient gitHubWebClient =
        GitHubWebClient.create("http://localhost:" + wireMockServer.port());
    private final GitHubLinkHandler gitHubLinkHandler =
        new GitHubLinkHandler(gitHubWebClient);
    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor(wireMockServer.port());
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("Неверный формат ссылки")
    void invalidLinkFormat() {
        OffsetDateTime time = OffsetDateTime.now();
        Link link = new Link(1L, URI.create("url"), time, time);
        assertThat(gitHubLinkHandler.updateLink(link))
            .isEqualTo(new HandlerResult(false, "The link does not match the format", null));
    }

    @Test
    @DisplayName("Требуется обновление")
    void updateLink() {
        OffsetDateTime time = OffsetDateTime.now();
        Link link = new Link(1L,
            URI.create("https://github.com/LalisaST/java_course_2"),
            time, time
        );

        OffsetDateTime lastModified = OffsetDateTime.of(2024, 4, 21, 0, 0, 0, 0, ZoneOffset.UTC);
        stubFor(get(urlEqualTo("/repos/" + "LalisaST" + "/" + "java_course_2"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(
                    "{\"updated_at\": \"" + lastModified + "\", \"html_url\": \"" + link.url() +
                    "\"}")));

        assertThat(gitHubLinkHandler.updateLink(link))
            .isEqualTo(new HandlerResult(true, "Updated at 2024-04-21, 00:00:00", lastModified));
    }

    @Test
    @DisplayName("Не требуется обновление")
    void noUpdateLink() {
        OffsetDateTime lastModified = OffsetDateTime.of(2024, 4, 21, 0, 0, 0, 0, ZoneOffset.UTC);
        Link link = new Link(1L,
            URI.create("https://github.com/LalisaST/java_course_2"),
            lastModified, lastModified
        );

        stubFor(get(urlEqualTo("/repos/" + "LalisaST" + "/" + "java_course_2"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(
                    "{\"updated_at\": \"" + lastModified + "\", \"html_url\": \"" + link.url() +
                    "\"}")));

        assertThat(gitHubLinkHandler.updateLink(link).description().equals("Not updated")).isTrue();
    }

    @Test
    @DisplayName("Проверка хоста")
    void supportsTest() {
        assertThat(gitHubLinkHandler.supports(URI.create
            ("https://github.com/LalisaST/java_course_2")
        ))
            .isTrue();
    }
}
