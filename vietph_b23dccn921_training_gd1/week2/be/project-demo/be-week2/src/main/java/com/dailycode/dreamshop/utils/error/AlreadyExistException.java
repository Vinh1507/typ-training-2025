package com.dailycode.dreamshop.utils.error;


public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String message){
        super(message);
    }
}
