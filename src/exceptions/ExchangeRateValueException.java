package exceptions;

/**
 * The rate should be positive
 */
public class ExchangeRateValueException extends ApplicationException {
    public ExchangeRateValueException(String message) {
        super(message);
    }
}
