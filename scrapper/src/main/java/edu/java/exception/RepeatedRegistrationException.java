package edu.java.exception;

public class RepeatedRegistrationException extends RuntimeException {
    public RepeatedRegistrationException(String message) {
        super(message);
    }
}
