package com.vitor.authuser.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsernameAlreadyExistsException extends ResponseStatusException {
    public UsernameAlreadyExistsException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
