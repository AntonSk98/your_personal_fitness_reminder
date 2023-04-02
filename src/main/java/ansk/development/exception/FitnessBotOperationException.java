package ansk.development.exception;

/**
 * Exception that is related to processing one of the coming events.
 *
 * @author Anton Skripin
 */
public class FitnessBotOperationException extends Exception {
    public FitnessBotOperationException() {
        super();
    }

    public FitnessBotOperationException(String message) {
        super(message);
    }

    public FitnessBotOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FitnessBotOperationException(Throwable cause) {
        super(cause);
    }
}
