package com.hra.hra.exception;

import com.hra.hra.dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeAlreadyExist.class)
    public ResponseEntity<Response> handleEmployeeAlreadyExistException(EmployeeAlreadyExist ex, HttpServletRequest request) {
        Response response = Response.buildResponse(
                "FAILED",
                ex.getMessage(),
                null,
                HttpStatus.CONFLICT.value(),
                "Process executed success"
        );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(NoEmployeeExist.class)
    public ResponseEntity<Response> handleNoEmployeeExistException(NoEmployeeExist ex, HttpServletRequest request) {
        Response response = Response.buildResponse(
                "FAILED",
                ex.getMessage(),
                null,
                HttpStatus.NOT_FOUND.value(),
                "Process executed success"
        );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
    }

}
