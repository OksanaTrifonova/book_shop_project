package com.oksanatrifonova.bookshop.exception;

public class BookValidationException extends RuntimeException {

    public BookValidationException(String message) {
        super(message);
    }

    public BookValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

