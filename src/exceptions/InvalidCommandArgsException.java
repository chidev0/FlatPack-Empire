package exceptions;

public class InvalidCommandArgsException extends RuntimeException {
    public InvalidCommandArgsException(String message) {
        super(message);
        System.out.println(message);
    }
}
