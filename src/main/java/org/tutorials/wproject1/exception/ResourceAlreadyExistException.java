package org.tutorials.wproject1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FOUND, reason="resource already exists") 

public class ResourceAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID=-9079454849611061074L;

    public ResourceAlreadyExistException() {
        super();
    }

    public ResourceAlreadyExistException(final String message) {
        super(message);
    }

}
