package edu.java.dto.github;

public record GitHubCommit(
    Commit commit
) {
    public record Commit(
    ) {
    }
}
