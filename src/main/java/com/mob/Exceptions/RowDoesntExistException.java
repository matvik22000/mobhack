package com.mob.Exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "row with specified parameters doesn't exist")
public class RowDoesntExistException extends RuntimeException{
    private static final Log log = LogFactory.getLog(AuthorisationException.class);
    public RowDoesntExistException(String message) {
        super(message);
        log.debug("authorisation error: " + message);
    }
}
