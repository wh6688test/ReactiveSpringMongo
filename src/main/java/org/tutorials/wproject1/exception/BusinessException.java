package org.tutorials.wproject1.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="General Server Error") 
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID=-9079454849611061073L;

    public BusinessException() {
        super();
    }

    public BusinessException(final String message) {
        super(message);
    }

}
