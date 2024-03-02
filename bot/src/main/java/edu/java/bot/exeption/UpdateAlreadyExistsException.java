package edu.java.bot.exeption;

public class UpdateAlreadyExistsException extends RuntimeException {
    public UpdateAlreadyExistsException(String message) {
        super(message);
    }
}
