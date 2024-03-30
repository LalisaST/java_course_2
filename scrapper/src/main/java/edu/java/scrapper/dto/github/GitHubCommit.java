package edu.java.scrapper.dto.github;

public record GitHubCommit(
    Commit commit
) {
    public record Commit(
    ) {
    }
}
