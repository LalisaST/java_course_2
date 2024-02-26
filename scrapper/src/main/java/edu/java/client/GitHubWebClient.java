package edu.java.client;

import edu.java.dto.GitHubResponse;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubWebClient implements GitHubClient {
    private static final String DEFAULT_BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    private GitHubWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public static GitHubWebClient create(String url) {
        WebClient webClient = WebClient
            .builder()
            .baseUrl(Objects.requireNonNullElse(url, DEFAULT_BASE_URL))
            .build();

        return new GitHubWebClient(webClient);
    }

    @Override
    public GitHubResponse fetchRepository(@NotNull String owner, @NotNull String repo) {
        return webClient
            .get()
            .uri("/repos/{owner}/{repo}", owner, repo)
            .retrieve()
            .bodyToMono(GitHubResponse.class)
            .block();
    }
}
