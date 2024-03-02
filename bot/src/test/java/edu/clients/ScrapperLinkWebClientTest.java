package edu.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.bot.clients.ScrapperLinkWebClient;
import edu.java.bot.dto.scrapper.AddLinkRequest;
import edu.java.bot.dto.scrapper.RemoveLinkRequest;
import java.net.URI;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ScrapperLinkWebClientTest {
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
    @DisplayName("Проверка функции getAllLinks")
    public void checkingGetAllLinksFunction() {
        stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "links":[
                            {
                                "id": 0,
                                "url": "url"
                            }
                        ],
                        "size": 1
                    }
                    """)));

        ScrapperLinkWebClient scrapperLinkWebClient =
            ScrapperLinkWebClient.create("http://localhost:" + wireMockServer.port());

        assertDoesNotThrow(() -> scrapperLinkWebClient.getAllLinks(1L));
    }

    @Test
    @DisplayName("Проверка функции addLink")
    public void checkingAddLinkFunction() {
        stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .withRequestBody(equalToJson("""
                {
                    "url": "url"
                }
                """))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "id": 0,
                        "url": "url"
                    }
                    """)));

        ScrapperLinkWebClient scrapperLinkWebClient =
            ScrapperLinkWebClient.create("http://localhost:" + wireMockServer.port());
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create("url"));

        assertDoesNotThrow(() -> scrapperLinkWebClient.addLink(1L, addLinkRequest));
    }

    @Test
    @DisplayName("Проверка функции deleteLink")
    public void checkingDeleteLinkFunction() {
        stubFor(delete(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .withHeader("Content-Type", equalTo(MediaType.APPLICATION_JSON_VALUE))
            .withRequestBody(equalToJson("""
                {
                    "url": "url"
                }
                """))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "id": 0,
                        "url": "url"
                    }
                    """)));

        ScrapperLinkWebClient scrapperLinkWebClient =
            ScrapperLinkWebClient.create("http://localhost:" + wireMockServer.port());
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(URI.create("url"));

        assertDoesNotThrow(() -> scrapperLinkWebClient.deleteLink(1L, removeLinkRequest));
    }

    @Test
    @DisplayName("Проверка на ошибку")
    public void errorChecking() {
        stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(400)));

        ScrapperLinkWebClient scrapperLinkWebClient =
            ScrapperLinkWebClient.create("http://localhost:" + wireMockServer.port());

        assertThatThrownBy(() -> scrapperLinkWebClient.getAllLinks(1L));
    }
}
