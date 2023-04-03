package exceptions;

/**
 * Number parsing error
 */
public class ParseNumberFormatException extends ApplicationException {
    public ParseNumberFormatException(String message) {
        super(message);
    }
}
