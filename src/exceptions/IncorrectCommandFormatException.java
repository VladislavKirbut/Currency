package exceptions;

/**
 * Invalid number of command arguments
 */
public class IncorrectCommandFormatException extends ApplicationException {
    public IncorrectCommandFormatException(String message) {
        super(message);
    }
}
