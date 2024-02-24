package com.javamentor.qa.platform.exception;

public class UserAlreadyAddedToGroupchatException extends RuntimeException{
    public UserAlreadyAddedToGroupchatException(String message) {
        super(message);
    }
}
