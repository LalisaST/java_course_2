package edu.java.bot.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.retry.LinearRetryFilter;
import java.time.Duration;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClientException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ScrapperTgChatWebClientTest {
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
    @Order(4)
    @DisplayName("Проверка функции registrationChat")
    public void checkingRegistrationChatFunction() {
        long id = 1L;
        stubFor(post(urlEqualTo("/tg-chat/" + id))
            .willReturn(aResponse()
                .withStatus(200)));

        ScrapperTgChatWebClient scrapperTgChatWebClient =
            ScrapperTgChatWebClient.create("http://localhost:" + wireMockServer.port(), exchangeFilterFunction);

        assertDoesNotThrow(() -> scrapperTgChatWebClient.registrationChat(id));
    }

    @Test
    @Order(3)
    @DisplayName("Проверка на ошибку")
    public void errorChecking() {
        long id = 1L;
        stubFor(post(urlEqualTo("/tg-chat/" + id))
            .willReturn(aResponse()
                .withStatus(400)));

        ScrapperTgChatWebClient scrapperTgChatWebClient =
            ScrapperTgChatWebClient.create("http://localhost:" + wireMockServer.port(), exchangeFilterFunction);

        assertThatThrownBy(() -> scrapperTgChatWebClient.registrationChat(id)).isInstanceOf(WebClientException.class);
    }

    @Test
    @Order(2)
    @DisplayName("Проверка функции deleteChat")
    public void checkingDeleteChatFunction() {
        long id = 1L;
        stubFor(delete(urlEqualTo("/tg-chat/" + id))
            .willReturn(aResponse()
                .withStatus(200)));

        ScrapperTgChatWebClient scrapperTgChatWebClient =
            ScrapperTgChatWebClient.create("http://localhost:" + wireMockServer.port(), exchangeFilterFunction);

        assertDoesNotThrow(() -> scrapperTgChatWebClient.deleteChat(id));
    }

    @Test
    @Order(1)
    @DisplayName("Проверка retry")
    void clientRetry() {
        long id = 1L;
        stubFor(delete(urlEqualTo("/tg-chat/" + id))
            .willReturn(aResponse()
                .withStatus(503)));

        ScrapperTgChatWebClient scrapperTgChatWebClient =
            ScrapperTgChatWebClient.create("http://localhost:" + wireMockServer.port(), exchangeFilterFunction);

        assertThatThrownBy(() -> scrapperTgChatWebClient.deleteChat(id));

        WireMock.verify(3, deleteRequestedFor(urlPathEqualTo("/tg-chat/" + id)));
    }
}
