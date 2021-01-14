package org.tutorials.wproject1.exception;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID=-9079454849611061073L;

    public BusinessException() {
        super();
    }

    public BusinessException(final String message) {
        super(message);
    }

}
