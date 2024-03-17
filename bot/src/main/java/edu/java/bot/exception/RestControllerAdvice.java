package edu.java.bot.exception;

import edu.java.bot.dto.ApiErrorResponse;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@org.springframework.web.bind.annotation.RestControllerAdvice
@Slf4j
public class RestControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleBadRequest(MethodArgumentNotValidException e) {
        return createApiErrorResponse(
            e,
            "Invalid request parameters: " + e.getParameter().getParameterName()
        );
    }

    private ApiErrorResponse createApiErrorResponse(Exception e, String description) {
        return new ApiErrorResponse(
            description,
            HttpStatus.BAD_REQUEST.toString(),
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace())
                .map(Objects::toString)
                .toList()
        );
    }
}
