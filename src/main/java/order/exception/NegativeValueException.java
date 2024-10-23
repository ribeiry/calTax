package order.exception;

public class NegativeValueException extends Throwable {
    public NegativeValueException(String errorMessage) {
        super(errorMessage);
    }
}
