package se.mulander.cosmos.common.model.exception;

import se.mulander.cosmos.common.model.ErrorMessage;

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

    public ErrorMessage toErrorMessage() {
        return new ErrorMessage(message, reason);
    }
}
