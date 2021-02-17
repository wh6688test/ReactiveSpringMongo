package org.tutorials.wproject1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="resource not found") 

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID=-9079454849611061074L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(final String message) {
        super(message);
    }

}
