package exception;

public class PasswordUpdateException extends Exception {
    public PasswordUpdateException(String message) {
        super("[PasswordUpdateException] " + message);
    }
}
