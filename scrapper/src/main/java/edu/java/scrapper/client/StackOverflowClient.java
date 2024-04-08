package edu.java.scrapper.client;

import edu.java.scrapper.dto.stackoverflow.StackOverflowAnswer;
import edu.java.scrapper.dto.stackoverflow.StackOverflowComment;
import edu.java.scrapper.dto.stackoverflow.StackOverflowResponse;
import org.jetbrains.annotations.NotNull;

public interface StackOverflowClient {
    StackOverflowResponse fetchQuestion(@NotNull Long questionId);

    StackOverflowAnswer fetchAnswers(@NotNull Long questionId);

    StackOverflowComment fetchComments(@NotNull Long questionId);
}
