package com.hra.hra.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoDataExist extends RuntimeException{
    private String message;

    NoDataExist(){}

    public NoDataExist(String message){
        super();
        this.message = message;
    }
}
