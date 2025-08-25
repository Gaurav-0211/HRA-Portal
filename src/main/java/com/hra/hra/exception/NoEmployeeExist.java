package com.hra.hra.exception;

public class NoEmployeeExist extends RuntimeException{
    private String message;

    NoEmployeeExist(){}

    public NoEmployeeExist(String message){
        super();
        this.message = message;
    }
}
