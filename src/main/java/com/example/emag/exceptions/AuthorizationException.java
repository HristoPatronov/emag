package com.example.emag.exceptions;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String msg){
        super(msg);
    }

    public AuthorizationException(){
        super("You must log in to use this service!");
    }
}
