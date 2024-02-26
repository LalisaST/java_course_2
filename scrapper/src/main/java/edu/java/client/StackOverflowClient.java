package edu.java.client;

import edu.java.dto.StackOverflowResponse;
import org.jetbrains.annotations.NotNull;

public interface StackOverflowClient {
    StackOverflowResponse fetchQuestion(@NotNull Long questionId);
}
