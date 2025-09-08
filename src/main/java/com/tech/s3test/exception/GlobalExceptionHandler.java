package com.tech.s3test.exception;

import com.tech.s3test.dto.res.ExceptionResDto;
import com.tech.s3test.exception.custom.InternalServerErrorException;
import com.tech.s3test.exception.custom.MissingStatementException;
import com.tech.s3test.exception.custom.ResourceAlreadyExistsException;
import com.tech.s3test.exception.custom.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResDto handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ExceptionResDto(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                this.now()
        );
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResDto handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return new ExceptionResDto(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                this.now()
        );
    }

    @ExceptionHandler(MissingStatementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResDto handleMissingStatementException(MissingStatementException ex) {
        return new ExceptionResDto(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                this.now()
        );
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResDto handleInternalServerErrorException(InternalServerErrorException ex) {
        return new ExceptionResDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An internal server error occurred",
                this.now()
        );
    }

    private OffsetDateTime now() {
        return OffsetDateTime.now();
    }
}
