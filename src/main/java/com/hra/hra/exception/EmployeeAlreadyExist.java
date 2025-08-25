package com.hra.hra.exception;

public class EmployeeAlreadyExist extends RuntimeException{
    private String message;

    EmployeeAlreadyExist(){}

    public EmployeeAlreadyExist(String message){
        super();
        this.message = message;
    }
}
