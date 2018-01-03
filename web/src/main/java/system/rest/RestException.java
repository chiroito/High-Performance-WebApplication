package system.rest;

public class RestException extends RuntimeException {

    public RestException(String message, Throwable t) {
        super(message, t);
    }
}
