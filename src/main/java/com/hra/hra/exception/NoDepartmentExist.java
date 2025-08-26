package com.hra.hra.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoDepartmentExist extends RuntimeException{
    private String message;

    NoDepartmentExist(){}

    public NoDepartmentExist(String message){
        super();
        this.message = message;
    }
}
