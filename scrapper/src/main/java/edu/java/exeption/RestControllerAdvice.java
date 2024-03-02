package edu.java.exeption;

import edu.java.dto.ApiErrorResponse;
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
    private static final String BAD_REQUEST_DESCRIPTION = "Invalid request parameters";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleBadRequest(MethodArgumentNotValidException e) {
        return createApiErrorResponse(
            e,
            HttpStatus.BAD_REQUEST,
            "Invalid request parameters: " + e.getParameter().getParameterName()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNotFound(NotFoundException e) {
        return createApiErrorResponse(e, HttpStatus.NOT_FOUND, "This resource was not found");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleRepeatedRegistration(RepeatedRegistrationException e) {
        return createApiErrorResponse(e, HttpStatus.BAD_REQUEST, BAD_REQUEST_DESCRIPTION);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleRepeatedLink(RepeatedLinkException e) {
        return createApiErrorResponse(e, HttpStatus.BAD_REQUEST, BAD_REQUEST_DESCRIPTION);
    }

    private ApiErrorResponse createApiErrorResponse(Exception e, HttpStatus httpStatus, String description) {
        return new ApiErrorResponse(
            description,
            httpStatus.toString(),
            e.getClass().getSimpleName(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace())
                .map(Objects::toString)
                .toList()
        );
    }
}
