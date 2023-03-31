package exceptions;

/**
 * Exception when working with the local currency
 */
public class LocalCurrencyException extends ApplicationException {
    public LocalCurrencyException(String message) {
        super(message);
    }
}
