package Exceptions;

// Exception to throw when login fails for any reason
public class LoginFailedException extends Exception {

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Exception innerException) {
        super(message, innerException);
    }
}
