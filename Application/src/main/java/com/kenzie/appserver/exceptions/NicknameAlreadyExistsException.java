package com.kenzie.appserver.exceptions;

public class NicknameAlreadyExistsException extends RuntimeException{
    public NicknameAlreadyExistsException(String nickname) {
        super("User with Nickname: " + nickname + " already exists Please Choose another." );
    }
}


