package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import edu.java.client.StackOverflowWebClient;
import edu.java.dto.stackoverflow.StackOverflowAnswer;
import edu.java.dto.stackoverflow.StackOverflowComment;
import edu.java.dto.stackoverflow.StackOverflowResponse;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
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

public class StackOverflowWebClientTest {
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
    @DisplayName("Проверка функции fetchQuestion")
    public void testFetchQuestion() {
        Long questionId = 123L;
        String link = "LalisaSt";
        OffsetDateTime lastModified = OffsetDateTime.of(2024, 2, 21, 0, 0, 0, 0, ZoneOffset.UTC);
        List<StackOverflowResponse.Item> items = List.of(new StackOverflowResponse.Item(link, lastModified));
        StackOverflowResponse expectedResponse = new StackOverflowResponse(items);

        stubFor(get(urlEqualTo("/questions/" + questionId))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"items\": [{\"link\": \"" + link + "\", \"last_activity_date\": \"" + lastModified + "\"}]}")));

        StackOverflowWebClient stackOverflowWebClient =
            StackOverflowWebClient.create("http://localhost:" + wireMockServer.port());
        StackOverflowResponse actualResponse = stackOverflowWebClient.fetchQuestion(questionId);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Проверка неверного тела ответа")
    public void testWrongBody() {
        Long questionId = 123L;

        stubFor(get(urlEqualTo("/questions/" + questionId))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("aboba")));

        StackOverflowWebClient stackOverflowWebClient =
            StackOverflowWebClient.create("http://localhost:" + wireMockServer.port());

        assertThatThrownBy(() -> stackOverflowWebClient.fetchQuestion(questionId)).isInstanceOf(DecodingException.class);
    }

    @Test
    @DisplayName("Проверка функции fetchAnswers")
    public void testFetchAnswers() {
        Long questionId = 123L;
        List<StackOverflowAnswer.Item> items = List.of();
        StackOverflowAnswer expectedResponse = new StackOverflowAnswer(items);

        stubFor(get(urlEqualTo("/questions/" + questionId + "/answers"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"items\": []}")));

        StackOverflowWebClient stackOverflowWebClient =
            StackOverflowWebClient.create("http://localhost:" + wireMockServer.port());
        StackOverflowAnswer actualResponse = stackOverflowWebClient.fetchAnswers(questionId);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Проверка функции fetchComments")
    public void testFetchComments() {
        Long questionId = 123L;
        List<StackOverflowComment.Item> items = List.of();
        StackOverflowComment expectedResponse = new StackOverflowComment(items);

        stubFor(get(urlEqualTo("/questions/" + questionId + "/comments"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"items\": []}")));

        StackOverflowWebClient stackOverflowWebClient =
            StackOverflowWebClient.create("http://localhost:" + wireMockServer.port());
        StackOverflowComment actualResponse = stackOverflowWebClient.fetchComments(questionId);

        assertEquals(expectedResponse, actualResponse);
    }
}
