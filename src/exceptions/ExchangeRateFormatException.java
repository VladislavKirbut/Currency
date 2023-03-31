package exceptions;

/**
 * Number parsing error
 */
public class ExchangeRateFormatException extends ApplicationException {
    public ExchangeRateFormatException(String message) {
        super(message);
    }
}
