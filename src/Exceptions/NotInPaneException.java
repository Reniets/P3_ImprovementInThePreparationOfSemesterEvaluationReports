package Exceptions;

// Exception to be thrown when a check that a given Node has a Parent Pane fails
public class NotInPaneException extends Exception {
    public NotInPaneException(String message) {
        super(message);
    }
}
