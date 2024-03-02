package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.client.BotWebClient;
import edu.java.dto.bot.LinkUpdateRequest;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClientException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class BotWebClientTest {
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
        BotWebClient botWebClient = BotWebClient.create("http://localhost:" + wireMockServer.port());

        assertDoesNotThrow(() -> botWebClient.update(linkUpdateRequest));
    }

    @Test
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
        BotWebClient botWebClient = BotWebClient.create("http://localhost:" + wireMockServer.port());

        assertThatThrownBy(() -> botWebClient.update(linkUpdateRequest)).isInstanceOf(WebClientException.class);
    }
}
