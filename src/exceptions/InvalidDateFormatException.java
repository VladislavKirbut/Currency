package exceptions;

/**
 * Date parsing error
 */
public class InvalidDateFormatException extends ApplicationException {
    public InvalidDateFormatException(String message) {
        super(message);
    }
}
