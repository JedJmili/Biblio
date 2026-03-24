package com.bibliotech.exception;

public class MaxBorrowingsExceededException extends RuntimeException {
    public MaxBorrowingsExceededException(String message) {
        super(message);
    }
}
