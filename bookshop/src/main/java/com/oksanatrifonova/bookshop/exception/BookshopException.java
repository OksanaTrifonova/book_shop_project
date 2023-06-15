package com.oksanatrifonova.bookshop.exception;

public class BookshopException extends RuntimeException {

    public BookshopException(String message) {
        super(message);
    }

    public BookshopException(String message, Throwable cause) {
        super(message, cause);
    }
}

