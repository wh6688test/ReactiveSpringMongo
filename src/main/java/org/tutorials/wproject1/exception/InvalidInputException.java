package org.tutorials.wproject1.exception;

public class InvalidInputException extends Exception {

    private static final long serialVersionUID=-9079454849611061073L;

    public InvalidInputException() {
        super();
    }

    public InvalidInputException(final String message) {
        super(message);
    }

}
