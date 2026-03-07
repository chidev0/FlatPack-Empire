package exceptions;

public class EmptyStructureException extends RuntimeException {
    public EmptyStructureException() {
        super("[ ERROR ] - Array is empty.");
    }
}
