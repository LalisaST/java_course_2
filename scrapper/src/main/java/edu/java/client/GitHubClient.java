package edu.java.client;

import edu.java.dto.GitHubResponse;
import org.jetbrains.annotations.NotNull;

public interface GitHubClient {
    GitHubResponse fetchRepository(@NotNull String owner, @NotNull String repo);
}
