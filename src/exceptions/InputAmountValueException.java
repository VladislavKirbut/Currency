package exceptions;

/**
 * The initial amount must be non-negative
 */
public class InputAmountValueException extends ApplicationException {
    public InputAmountValueException(String message) {
        super(message);
    }
}
