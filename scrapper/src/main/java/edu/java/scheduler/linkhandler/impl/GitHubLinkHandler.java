package edu.java.scheduler.linkhandler.impl;

import edu.java.client.GitHubWebClient;
import edu.java.dto.github.GitHubCommit;
import edu.java.dto.github.GitHubResponse;
import edu.java.model.scheme.Link;
import edu.java.scheduler.linkhandler.HandlerResult;
import edu.java.scheduler.linkhandler.LinkHandler;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GitHubLinkHandler implements LinkHandler {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss");
    private final static String GITHUB_HOST = "github.com";
    private final static int OWNER = 3;
    private final static int REPO = 4;
    private final static int EXPECTED_LINK_LENGTH = 5;
    private final GitHubWebClient gitHubWebClient;

    @Override
    public HandlerResult updateLink(Link link) {
        String[] data = link.url().toString().split("/");

        if (data.length < EXPECTED_LINK_LENGTH) {
            return new HandlerResult(false, "The link does not match the format",
                null, 0, 0, 0
            );
        }

        String owner = data[OWNER];
        String repo = data[REPO];

        GitHubResponse gitHubResponse = gitHubWebClient.fetchRepository(owner, repo);
        List<GitHubCommit> gitHubCommit = gitHubWebClient.fetchCommits(owner, repo);

        StringBuilder description = new StringBuilder();
        Integer size = link.commitCount();

        OffsetDateTime time = link.lastUpdate();
        OffsetDateTime timeUpdate = gitHubResponse.update();

        if (!timeUpdate.equals(time)) {
            description.append("Updated at ").append(timeUpdate.format(DTF));
            time = timeUpdate;
        }

        if (gitHubCommit.size() != size) {
            description.append(", ").append("count new commits: ").append(gitHubCommit.size() - size);
            size = gitHubCommit.size();
        }

        if (description.isEmpty()) {
            return new HandlerResult(false, "Not updated", OffsetDateTime.now(), 0, 0, 0);
        }

        return new HandlerResult(true, description.toString(), time, size, 0, 0);
    }

    @Override
    public boolean supports(URI url) {
        return url.getHost().equals(GITHUB_HOST);
    }
}
