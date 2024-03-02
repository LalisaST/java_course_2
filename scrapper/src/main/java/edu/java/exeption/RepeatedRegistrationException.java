package edu.java.exeption;

public class RepeatedRegistrationException extends RuntimeException {
    public RepeatedRegistrationException(String message) {
        super(message);
    }
}
