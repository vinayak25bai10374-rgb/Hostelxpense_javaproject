package exception;

/**
 * Thrown when an operation references a student ID
 * that does not exist in the system.
 */
public class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String message) {
        super(message);
    }
}
