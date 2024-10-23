package com.capitalgains.exception;

public class NegativeValueOrNullException extends Throwable {
    public NegativeValueOrNullException(String errorMessage) {
        super(errorMessage);
    }
}
