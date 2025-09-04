package com.tech.s3test.exception.custom;

public class MissingStatementException extends RuntimeException {
    public MissingStatementException(String message) {
        super(message);
    }
}
