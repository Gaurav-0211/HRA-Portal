package com.hra.hra.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class NoLeaveExist extends RuntimeException{
    private String message;

    NoLeaveExist() {}

    public NoLeaveExist(String message){
        super();
        this.message = message;
    }
}
