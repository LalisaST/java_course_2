package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.client.GitHubWebClient;
import edu.java.dto.GitHubResponse;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.MediaType;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GitHubWebClientTest {
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
    @DisplayName("Проверка функции fetchRepository")
    public void testFetchRepository() {
        String owner = "LalisaSt";
        String repo = "java_course_2";
        String name = "example";
        String html = "aboba";
        OffsetDateTime offsetDateTime = OffsetDateTime.of(2024, 2, 21, 0, 0, 0, 0, ZoneOffset.UTC);
        GitHubResponse expectedResponse = new GitHubResponse(name, offsetDateTime, html);

        stubFor(get(urlEqualTo("/repos/" + owner + "/" + repo))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(
                    "{\"name\": \"" + name + "\", \"updated_at\": \"" + offsetDateTime + "\", \"html_url\": \"" + html +
                        "\"}")));

        GitHubWebClient gitHubWebClient = GitHubWebClient.create("http://localhost:" + wireMockServer.port());
        GitHubResponse actualResponse = gitHubWebClient.fetchRepository(owner, repo);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Проверка неверного тела запроса")
    public void testWrongBody() {
        String owner = "LalisaSt";
        String repo = "java_course_2";

        stubFor(get(urlEqualTo("/repos/" + owner + "/" + repo))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("aboba")));

        GitHubWebClient gitHubWebClient = GitHubWebClient.create("http://localhost:" + wireMockServer.port());

        assertThatThrownBy(() -> gitHubWebClient.fetchRepository(owner, repo)).isInstanceOf(DecodingException.class);
    }
}
