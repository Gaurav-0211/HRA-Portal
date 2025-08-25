package com.hra.hra.exception;

public class NoRoleExist extends RuntimeException{
    String message;

    NoRoleExist(){}

    public NoRoleExist(String message){
        super();
        this.message = message;
    }
}
