package com.mob.Exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProposalDoesntExistException extends RowDoesntExistException{
    private static final Log log = LogFactory.getLog(AuthorisationException.class);
    public ProposalDoesntExistException(String message) {
        super(message);
        log.debug("authorisation error: " + message);
    }

}
