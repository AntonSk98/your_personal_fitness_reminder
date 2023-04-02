package ansk.development.exception;

/**
 * Exception that should be thrown when an event of unknown type is received.
 *
 * @author Anton Skripin
 */
public class UnknownBotCommandException extends RuntimeException {
    public UnknownBotCommandException() {
        super();
    }

    public UnknownBotCommandException(String message) {
        super(message);
    }
}
