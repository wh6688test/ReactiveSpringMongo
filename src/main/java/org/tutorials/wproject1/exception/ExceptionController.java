package org.tutorials.wproject1.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);
    
    @ExceptionHandler(value=InvalidInputException.class)
    public ResponseEntity<Object> exception(InvalidInputException ex) {
        logger.error("Business Exception: {} ",ex.getMessage());
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value=ResourceNotFoundException.class)
    public ResponseEntity<Object> exception(ResourceNotFoundException ex) {
        logger.error("Resource Not Found Exception: {} ",ex.getMessage());
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value=BusinessException.class)
    public ResponseEntity<Object> exception(BusinessException ex) {
        logger.error("Business Exception: {} ",ex.getMessage());
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(value=Exception.class)
    public ResponseEntity<Object> exception(Exception ex) {
        logger.error("Exception: {} ",ex.getMessage());
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
