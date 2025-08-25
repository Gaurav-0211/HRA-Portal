package com.hra.hra.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoEmployeeExist extends RuntimeException{
    private String message;

    NoEmployeeExist(){}

    public NoEmployeeExist(String message){
        super();
        this.message = message;
    }
}
