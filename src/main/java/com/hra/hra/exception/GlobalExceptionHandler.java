package com.hra.hra.exception;

import com.hra.hra.dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeAlreadyExist.class)
    public ResponseEntity<Response> handleEmployeeAlreadyExistException(EmployeeAlreadyExist ex, HttpServletRequest request) {
        log.info("Employee already exist exception occurred now in Global Exception Handler");
        Response response = Response.buildResponse(
                "FAILED",
                ex.getMessage(),
                null,
                HttpStatus.CONFLICT.value(),
                "Process executed success"
        );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(NoDataExist.class)
    public ResponseEntity<Response> handleNoDataExistException(NoDataExist ex, HttpServletRequest request) {
        log.info("No Data or searched value exist. Exception occurred now in Global Exception Handler");

        Response response = Response.buildResponse(
                "FAILED",
                ex.getMessage(),
                null,
                HttpStatus.NOT_FOUND.value(),
                "Process executed success"
        );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
    }

    // No role Exist for Role Entity
    @ExceptionHandler(NoRoleExist.class)
    public ResponseEntity<Response> handleNoRoleExistException(NoRoleExist ex, HttpServletRequest request) {
        log.info("No Role exist exception occurred now in Global Exception Handler");

        Response response = Response.buildResponse(
                "FAILED",
                ex.getMessage(),
                null,
                HttpStatus.NOT_FOUND.value(),
                "Process executed success"
        );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
    }

    // No Department Exist for Department Entity
    @ExceptionHandler(NoDepartmentExist.class)
    public ResponseEntity<Response> handleNoDepartmentExistException(NoDepartmentExist ex, HttpServletRequest request) {
        log.info("No Department exist exception occurred now in Global Exception Handler");

        Response response = Response.buildResponse(
                "FAILED",
                ex.getMessage(),
                null,
                HttpStatus.NOT_FOUND.value(),
                "Process executed success"
        );
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
    }

    // No Leave Exist for Leave Entity
    @ExceptionHandler(NoLeaveExist.class)
    public ResponseEntity<Response> handleNoLeaveExistException(NoLeaveExist ex, HttpServletRequest request) {
        log.info("No leave exist exception occurred now in Global Exception Handler");

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
