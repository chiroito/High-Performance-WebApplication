package domain.dao.exception;

public class DataStoreRuntimeException extends RuntimeException {

    public DataStoreRuntimeException(String message, Throwable t) {
        super(message, t);
    }
}
