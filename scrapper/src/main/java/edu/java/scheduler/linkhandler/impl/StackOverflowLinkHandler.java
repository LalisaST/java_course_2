package edu.java.scheduler.linkhandler.impl;

import edu.java.client.StackOverflowWebClient;
import edu.java.dto.stackoverflow.StackOverflowAnswer;
import edu.java.dto.stackoverflow.StackOverflowComment;
import edu.java.dto.stackoverflow.StackOverflowResponse;
import edu.java.model.scheme.Link;
import edu.java.scheduler.linkhandler.HandlerResult;
import edu.java.scheduler.linkhandler.LinkHandler;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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
            return new HandlerResult(false, "The link does not match the format", null, 0, 0, 0);
        }

        long questionID = Long.parseLong(data[QUESTION_ID]);

        StackOverflowResponse stackOverflowResponse = stackOverflowWebClient.fetchQuestion((questionID));
        StackOverflowAnswer stackOverflowAnswer = stackOverflowWebClient.fetchAnswers(questionID);
        StackOverflowComment stackOverflowComment = stackOverflowWebClient.fetchComments(questionID);

        StringBuilder description = new StringBuilder();
        int answerCount = link.answerCount() == null ? 0 : link.answerCount();
        int commentCount = link.commentCount() == null ? 0 : link.commentCount();

        OffsetDateTime time = link.lastUpdate();
        OffsetDateTime timeUpdate = stackOverflowResponse.items().get(0).lastActivity();

        if (!timeUpdate.equals(time)) {
            description.append("Updated at ").append(timeUpdate.format(DTF));
            time = timeUpdate;
        }

        if (stackOverflowAnswer.items().size() != answerCount) {
            description.append(", ").append("count new answers: ")
                .append(stackOverflowAnswer.items().size() - answerCount);
            answerCount = stackOverflowAnswer.items().size();
        }

        if (stackOverflowComment.items().size() != commentCount) {
            description.append(", ").append("count new comments: ")
                .append(stackOverflowComment.items().size() - commentCount);
            commentCount = stackOverflowComment.items().size();
        }

        if (description.isEmpty()) {
            return new HandlerResult(false, "Not updated", OffsetDateTime.now(), 0, 0, 0);
        }

        return new HandlerResult(true, description.toString(), time, 0, answerCount, commentCount);
    }

    @Override
    public boolean supports(URI url) {
        return url.getHost().equals(HOST_NAME);
    }
}
