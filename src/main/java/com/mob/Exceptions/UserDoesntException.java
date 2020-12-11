package com.mob.Exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserDoesntException extends RowDoesntExistException{
    private static final Log log = LogFactory.getLog(AuthorisationException.class);
    public UserDoesntException(String message) {
        super(message);
        log.debug("authorisation error: " + message);
    }
}
