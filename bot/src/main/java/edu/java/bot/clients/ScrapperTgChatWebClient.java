package edu.java.bot.clients;

import java.util.Objects;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

public class ScrapperTgChatWebClient {
    private static final String DEFAULT_BASE_URL = "http://localhost:8080";
    private static final String TG_CHAT_PATH = "/tg-chat/{id}";
    private final WebClient webClient;

    private ScrapperTgChatWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public static ScrapperTgChatWebClient create(String url, ExchangeFilterFunction exchangeFilterFunction) {
        WebClient webClient = WebClient
            .builder()
            .baseUrl(Objects.requireNonNullElse(url, DEFAULT_BASE_URL))
            .filter(exchangeFilterFunction)
            .build();

        return new ScrapperTgChatWebClient(webClient);
    }

    public void registrationChat(Long id) {
        webClient
            .post()
            .uri(TG_CHAT_PATH, id)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public void deleteChat(Long id) {
        webClient
            .delete()
            .uri(TG_CHAT_PATH, id)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
