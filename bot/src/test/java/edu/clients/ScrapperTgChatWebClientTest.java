package edu.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.bot.clients.ScrapperTgChatWebClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ScrapperTgChatWebClientTest {
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
    @DisplayName("Проверка функции registrationChat")
    public void checkingRegistrationChatFunction() {
        long id = 1L;
        stubFor(post(urlEqualTo("/tg-chat/" + id))
            .willReturn(aResponse()
                .withStatus(200)));

        ScrapperTgChatWebClient scrapperTgChatWebClient =
            ScrapperTgChatWebClient.create("http://localhost:" + wireMockServer.port());

        assertDoesNotThrow(() -> scrapperTgChatWebClient.registrationChat(id));
    }

    @Test
    @DisplayName("Проверка на ошибку")
    public void errorChecking() {
        long id = 1L;
        stubFor(post(urlEqualTo("/tg-chat/" + id))
            .willReturn(aResponse()
                .withStatus(400)));

        ScrapperTgChatWebClient scrapperTgChatWebClient =
            ScrapperTgChatWebClient.create("http://localhost:" + wireMockServer.port());

        assertThatThrownBy(() -> scrapperTgChatWebClient.registrationChat(id)).isInstanceOf(WebClientException.class);
    }

    @Test
    @DisplayName("Проверка функции deleteChat")
    public void checkingDeleteChatFunction() {
        long id = 1L;
        stubFor(delete(urlEqualTo("/tg-chat/" + id))
            .willReturn(aResponse()
                .withStatus(200)));

        ScrapperTgChatWebClient scrapperTgChatWebClient =
            ScrapperTgChatWebClient.create("http://localhost:" + wireMockServer.port());

        assertDoesNotThrow(() -> scrapperTgChatWebClient.deleteChat(id));
    }
}
