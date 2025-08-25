package com.hra.hra.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeAlreadyExist extends RuntimeException{
    private String message;

    EmployeeAlreadyExist(){}

    public EmployeeAlreadyExist(String message){
        super();
        this.message = message;
    }
}
