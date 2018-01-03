package domain.dao.exception;

public class DataStoreException extends Exception {

    public DataStoreException(String message, Exception e) {
        super(message,e);
    }
}
