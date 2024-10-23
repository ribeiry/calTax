package com.capitalgains.exception;

public class NegativeValueException extends Throwable {
    public NegativeValueException(String errorMessage) {
        super(errorMessage);
    }
}
