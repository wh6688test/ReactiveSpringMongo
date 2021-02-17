package org.tutorials.wproject1.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Unexpected Error") 

public class UnexpectedException extends RuntimeException {

    private static final long serialVersionUID=-9079454849611061071L;

    public UnexpectedException() {
        super();
    }

    public UnexpectedException(final String message) {
        super(message);
    }

}
