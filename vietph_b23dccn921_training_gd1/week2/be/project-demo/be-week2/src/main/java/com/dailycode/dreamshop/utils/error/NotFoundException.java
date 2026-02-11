package com.dailycode.dreamshop.utils.error;

public class NotFoundException extends RuntimeException  {
    public NotFoundException(String message){
        super(message);
    }
}
