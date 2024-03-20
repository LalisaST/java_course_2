package edu.java.client;

import edu.java.dto.github.GitHubCommit;
import edu.java.dto.github.GitHubResponse;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface GitHubClient {
    GitHubResponse fetchRepository(@NotNull String owner, @NotNull String repo);

    List<GitHubCommit> fetchCommits(@NotNull String owner, @NotNull String repo);
}
