package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dto.bot.LinkUpdateRequest;
import edu.java.scrapper.retry.LinearRetryFilter;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClientException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BotWebClientTest {
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
    @Order(3)
    @DisplayName("Проверка функции update")
    public void checkingUpdateFunction() {
        stubFor(post(urlEqualTo("/updates"))
            .withHeader("Content-Type", equalTo(MediaType.APPLICATION_JSON_VALUE))
            .withRequestBody(equalToJson("""
                { "id": 1,
                  "url": "url",
                  "description": "description",
                  "tgChatIds": [1,2]
                }
                """))
            .willReturn(aResponse()
                .withStatus(200)));

        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            1L,
            URI.create("url"),
            "description",
            List.of(1L, 2L)
        );
        BotWebClient botWebClient =
            BotWebClient.create("http://localhost:" + wireMockServer.port(), exchangeFilterFunction);

        assertDoesNotThrow(() -> botWebClient.update(linkUpdateRequest));
    }

    @Test
    @Order(2)
    @DisplayName("Проверка на ошибку")
    public void errorChecking() {
        stubFor(post(urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withStatus(400)));

        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            1L,
            URI.create("url"),
            "description",
            List.of(1L, 2L)
        );
        BotWebClient botWebClient =
            BotWebClient.create("http://localhost:" + wireMockServer.port(), exchangeFilterFunction);

        assertThatThrownBy(() -> botWebClient.update(linkUpdateRequest)).isInstanceOf(WebClientException.class);
    }

    @Test
    @Order(1)
    @DisplayName("Проверка retry")
    void clientRetry() {
        stubFor(post(urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withStatus(503)));

        LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
            1L,
            URI.create("url"),
            "description",
            List.of(1L, 2L)
        );
        BotWebClient botWebClient =
            BotWebClient.create("http://localhost:" + wireMockServer.port(), exchangeFilterFunction);

        assertThatThrownBy(() -> botWebClient.update(linkUpdateRequest)).isInstanceOf(WebClientException.class);

        WireMock.verify(3, postRequestedFor(urlPathEqualTo("/updates")));
    }
}
