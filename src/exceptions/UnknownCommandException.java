package exceptions;

/**
 * Command doesn't exist in program
 */
public class UnknownCommandException extends ApplicationException {
    public UnknownCommandException(String message) {
        super(message);
    }
}
