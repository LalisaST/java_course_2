package edu.java.scrapper.client;

import edu.java.scrapper.dto.stackoverflow.StackOverflowAnswer;
import edu.java.scrapper.dto.stackoverflow.StackOverflowComment;
import edu.java.scrapper.dto.stackoverflow.StackOverflowResponse;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowWebClient implements StackOverflowClient {
    private static final String DEFAULT_BASE_URL = "https://api.stackexchange.com/2.3";
    private final WebClient webClient;

    private StackOverflowWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public static StackOverflowWebClient create(String url, ExchangeFilterFunction exchangeFilterFunction) {
        WebClient webClient = WebClient
            .builder()
            .baseUrl(Objects.requireNonNullElse(url, DEFAULT_BASE_URL))
            .filter(exchangeFilterFunction)
            .build();

        return new StackOverflowWebClient(webClient);
    }

    @Override
    public StackOverflowResponse fetchQuestion(@NotNull Long questionId) {
        return webClient
            .get()
            .uri("/questions/{questionId}?site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(StackOverflowResponse.class)
            .block();
    }

    @Override
    public StackOverflowAnswer fetchAnswers(@NotNull Long questionId) {
        return webClient
            .get()
            .uri("/questions/{questionId}/answers?site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(StackOverflowAnswer.class)
            .block();
    }

    @Override
    public StackOverflowComment fetchComments(@NotNull Long questionId) {
        return webClient
            .get()
            .uri("/questions/{questionId}/comments?site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(StackOverflowComment.class)
            .block();
    }
}
