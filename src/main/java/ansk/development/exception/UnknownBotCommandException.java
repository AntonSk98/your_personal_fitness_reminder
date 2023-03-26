package ansk.development.exception;

public class UnknownBotCommandException extends RuntimeException {
    public UnknownBotCommandException() {
        super();
    }

    public UnknownBotCommandException(String message) {
        super(message);
    }
}
