package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dto.github.GitHubCommit;
import edu.java.scrapper.dto.github.GitHubResponse;
import edu.java.scrapper.retry.LinearRetryFilter;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GitHubWebClientTest {
    private static WireMockServer wireMockServer;
    private final ExchangeFilterFunction exchangeFilterFunction = new LinearRetryFilter(
        new ApplicationConfig.Client(
            "url",
            new ApplicationConfig.Client.Retry(
                ApplicationConfig.Client.BackoffPolicy.LINEAR,
                2,
                Duration.ofSeconds(2),
                Set.of(503)
            )
        ));

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
    @DisplayName("Проверка функции fetchRepository")
    public void testFetchRepository() {
        String owner = "LalisaSt";
        String repo = "java_course_2";
        String html = "aboba";
        OffsetDateTime offsetDateTime = OffsetDateTime.of(2024, 2, 21, 0, 0, 0, 0, ZoneOffset.UTC);
        GitHubResponse expectedResponse = new GitHubResponse(html, offsetDateTime);

        stubFor(get(urlEqualTo("/repos/" + owner + "/" + repo))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(
                    "{\"updated_at\": \"" + offsetDateTime + "\", \"html_url\": \"" + html +
                    "\"}")));

        GitHubWebClient gitHubWebClient =
            GitHubWebClient.create("http://localhost:" + wireMockServer.port(), exchangeFilterFunction);
        GitHubResponse actualResponse = gitHubWebClient.fetchRepository(owner, repo);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Проверка функции fetchCommits")
    public void testFetchCommits() {
        String owner = "LalisaSt";
        String repo = "java_course_2";
        GitHubCommit.Commit commit = new GitHubCommit.Commit();
        List<GitHubCommit> expectedResponse = List.of(new GitHubCommit(commit));

        stubFor(get(urlEqualTo("/repos/" + owner + "/" + repo + "/commits"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(
                    "[{\"commit\": {}}]")));

        GitHubWebClient gitHubWebClient =
            GitHubWebClient.create("http://localhost:" + wireMockServer.port(), exchangeFilterFunction);
        List<GitHubCommit> actualResponse = gitHubWebClient.fetchCommits(owner, repo);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Проверка неверного тела ответа")
    public void testWrongBody() {
        String owner = "LalisaSt";
        String repo = "java_course_2";

        stubFor(get(urlEqualTo("/repos/" + owner + "/" + repo))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("aboba")));

        GitHubWebClient gitHubWebClient =
            GitHubWebClient.create("http://localhost:" + wireMockServer.port(), exchangeFilterFunction);

        assertThatThrownBy(() -> gitHubWebClient.fetchRepository(owner, repo)).isInstanceOf(DecodingException.class);
    }

    @Test
    @DisplayName("Проверка retry")
    void clientRetry() {
        String owner = "LalisaSt";
        String repo = "java_course_2";

        stubFor(get(urlEqualTo("/repos/" + owner + "/" + repo))
            .willReturn(aResponse()
                .withStatus(503)));

        GitHubWebClient gitHubWebClient =
            GitHubWebClient.create("http://localhost:" + wireMockServer.port(), exchangeFilterFunction);

        assertThatThrownBy(() -> gitHubWebClient.fetchRepository(
            owner,
            repo
        )).isInstanceOf(WebClientResponseException.class);

        WireMock.verify(3, getRequestedFor(urlPathEqualTo("/repos/" + owner + "/" + repo)));
    }
}
