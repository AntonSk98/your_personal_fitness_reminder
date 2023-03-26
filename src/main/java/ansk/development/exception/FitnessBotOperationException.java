package ansk.development.exception;

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
