package exception;

/**
 * Thrown when an expense or budget amount is invalid
 * (negative, zero where not allowed, or unparsable).
 */
public class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) {
        super(message);
    }
}
