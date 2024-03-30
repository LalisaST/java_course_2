package edu.java.scrapper.client;

import edu.java.scrapper.dto.github.GitHubCommit;
import edu.java.scrapper.dto.github.GitHubResponse;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface GitHubClient {
    GitHubResponse fetchRepository(@NotNull String owner, @NotNull String repo);

    List<GitHubCommit> fetchCommits(@NotNull String owner, @NotNull String repo);
}
