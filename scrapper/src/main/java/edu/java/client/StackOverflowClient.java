package edu.java.client;

import edu.java.dto.stackoverflow.StackOverflowAnswer;
import edu.java.dto.stackoverflow.StackOverflowComment;
import edu.java.dto.stackoverflow.StackOverflowResponse;
import org.jetbrains.annotations.NotNull;

public interface StackOverflowClient {
    StackOverflowResponse fetchQuestion(@NotNull Long questionId);

    StackOverflowAnswer fetchAnswers(@NotNull Long questionId);

    StackOverflowComment fetchComments(@NotNull Long questionId);
}
