package edu.java.scheduler.linkhandler.impl;

import edu.java.client.StackOverflowWebClient;
import edu.java.dto.StackOverflowResponse;
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
public class StackOverflowLinkHandler implements LinkHandler {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss");
    private final static String HOST_NAME = "stackoverflow.com";
    private final static int QUESTION_ID = 4;
    private final static int EXPECTED_LINK_LENGTH = 5;
    private final StackOverflowWebClient stackOverflowWebClient;

    @Override
    public HandlerResult updateLink(Link link) {
        String[] data = link.url().toString().split("/");

        if (data.length < EXPECTED_LINK_LENGTH) {
            return new HandlerResult(false, "The link does not match the format", null);
        }

        long questionID = Long.parseLong(data[QUESTION_ID]);

        StackOverflowResponse stackOverflowResponse =
            stackOverflowWebClient.fetchQuestion((questionID));

        OffsetDateTime timeUpdate = stackOverflowResponse.items().get(0).lastActivity();
        if (!timeUpdate.equals(link.lastUpdate())) {
            return new HandlerResult(true, "Updated at " + timeUpdate.format(DTF), timeUpdate);
        }

        return new HandlerResult(false, "Not updated", OffsetDateTime.now());
    }

    @Override
    public boolean supports(URI url) {
        return url.getHost().equals(HOST_NAME);
    }
}
