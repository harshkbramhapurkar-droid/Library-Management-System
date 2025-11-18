package exception;

public class UserCreationException extends Exception {
    public UserCreationException(String message) {
        super("[UserCreationException] " + message);
    }
}
