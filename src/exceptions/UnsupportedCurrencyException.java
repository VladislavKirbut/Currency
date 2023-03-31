package exceptions;

/**
 * Error getting java.util.Currency
 */
public class UnsupportedCurrencyException extends ApplicationException {
    public UnsupportedCurrencyException(String message) {
        super(message);
    }
}
