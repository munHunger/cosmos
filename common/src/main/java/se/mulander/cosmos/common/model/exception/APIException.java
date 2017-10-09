package se.mulander.cosmos.common.model.exception;

/**
 * @author Marcus Münger
 */
public class APIException extends RuntimeException {
    public String message;
    public String reason;

    public APIException() {
    }

    public APIException(String message, String reason) {
        this.message = message;
        this.reason = reason;
    }
}
