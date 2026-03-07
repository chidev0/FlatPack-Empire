package exceptions;

public class CapacityExceededException extends RuntimeException {
    public CapacityExceededException(int Capacity) {
        super("[ ERROR ] - Array has reached Capacity. Max ArraySize - " + Capacity + ".Consider upgrading to a larger Array.");
    }
}
