package exception;

public class PaginationException extends Exception {
    public PaginationException(String message) {
        super("[PaginationException] " + message);
    }
}
