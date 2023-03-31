package exceptions;

public class ApplicationException extends RuntimeException {

    /**
     * The variable contains the message about exception
     */
    private final String message;

    public ApplicationException(String message) {
        this.message = message;
    }

    /**
     * Method for getting message of exception
     */
    @Override
    public String getMessage() {
        return message;
    }
}
