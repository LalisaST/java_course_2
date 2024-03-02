package edu.java.bot.clients;

import edu.java.bot.dto.scrapper.AddLinkRequest;
import edu.java.bot.dto.scrapper.LinkResponse;
import edu.java.bot.dto.scrapper.ListLinksResponse;
import edu.java.bot.dto.scrapper.RemoveLinkRequest;
import java.util.Objects;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class ScrapperLinkWebClient {
    private static final String DEFAULT_BASE_URL = "http://localhost:8080";
    private static final String LINKS_PATH = "/links";
    private static final String TG_CHAT_ID_PATH = "Tg-Chat-Id";
    private final WebClient webClient;

    private ScrapperLinkWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public static ScrapperLinkWebClient create(String url) {
        WebClient webClient = WebClient
            .builder()
            .baseUrl(Objects.requireNonNullElse(url, DEFAULT_BASE_URL))
            .build();

        return new ScrapperLinkWebClient(webClient);
    }

    public ListLinksResponse getAllLinks(Long id) {
        return webClient
            .get()
            .uri(LINKS_PATH)
            .header(TG_CHAT_ID_PATH, id.toString())
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(Long id, AddLinkRequest addLinkRequest) {
        return webClient
            .post()
            .uri(LINKS_PATH)
            .header(TG_CHAT_ID_PATH, id.toString())
            .body((BodyInserters.fromValue(addLinkRequest)))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse deleteLink(Long id, RemoveLinkRequest removeLinkRequest) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri(LINKS_PATH)
            .header(TG_CHAT_ID_PATH, id.toString())
            .body(BodyInserters.fromValue(removeLinkRequest))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }
}
