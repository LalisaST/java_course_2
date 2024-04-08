package edu.java.scrapper.client;

import edu.java.scrapper.dto.bot.LinkUpdateRequest;
import java.util.Objects;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

public class BotWebClient {
    private static final String DEFAULT_BASE_URL = "http://localhost:8090";
    private final WebClient webClient;

    private BotWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public static BotWebClient create(String url, ExchangeFilterFunction exchangeFilterFunction) {
        WebClient webClient = WebClient
            .builder()
            .baseUrl(Objects.requireNonNullElse(url, DEFAULT_BASE_URL))
            .filter(exchangeFilterFunction)
            .build();

        return new BotWebClient(webClient);
    }

    public void update(LinkUpdateRequest linkUpdateRequest) {
        webClient
            .post()
            .uri("/updates")
            .body(BodyInserters.fromValue(linkUpdateRequest))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
