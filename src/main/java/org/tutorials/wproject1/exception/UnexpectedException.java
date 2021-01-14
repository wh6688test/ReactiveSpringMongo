package org.tutorials.wproject1.exception;

public class UnexpectedException extends RuntimeException {

    private static final long serialVersionUID=-9079454849611061071L;

    public UnexpectedException() {
        super();
    }

    public UnexpectedException(final String message) {
        super(message);
    }

}
