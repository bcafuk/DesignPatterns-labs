public class CircularReferenceException extends RuntimeException {
    public CircularReferenceException() {}

    public CircularReferenceException(String message) {
        super(message);
    }

    public CircularReferenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CircularReferenceException(Throwable cause) {
        super(cause);
    }

    protected CircularReferenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
