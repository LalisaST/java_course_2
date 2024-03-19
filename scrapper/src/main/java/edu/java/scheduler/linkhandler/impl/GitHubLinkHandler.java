package edu.java.scheduler.linkhandler.impl;

import edu.java.client.GitHubWebClient;
import edu.java.dto.GitHubResponse;
import edu.java.model.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import edu.java.scheduler.linkhandler.HandlerResult;
import edu.java.scheduler.linkhandler.LinkHandler;
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
            return new HandlerResult(false, "The link does not match the format", null);
        }

        String owner = data[OWNER];
        String repo = data[REPO];

        GitHubResponse gitHubResponse = gitHubWebClient.fetchRepository(owner, repo);

        OffsetDateTime timeUpdate = gitHubResponse.update();
        if (!timeUpdate.equals(link.lastUpdate())) {
            return new HandlerResult(true, "Updated at " + timeUpdate.format(DTF), timeUpdate);
        }

        return new HandlerResult(false, "Not updated", OffsetDateTime.now());
    }

    @Override
    public boolean supports(URI url) {
        return url.getHost().equals(GITHUB_HOST);
    }
}
