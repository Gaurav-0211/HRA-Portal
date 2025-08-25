package com.hra.hra.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoRoleExist extends RuntimeException{
    String message;

    NoRoleExist(){}

    public NoRoleExist(String message){
        super();
        this.message = message;
    }
}
