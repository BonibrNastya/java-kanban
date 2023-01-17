package exception;

public class HandlerHttpException extends RuntimeException {

    public HandlerHttpException(String message, int statusCode) {
        super(message);

    }
}
