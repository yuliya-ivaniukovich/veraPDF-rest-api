package com.verapdf.restapi.controller;

import com.verapdf.restapi.exception.FileNotFoundException;
import com.verapdf.restapi.exception.JobNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({JobNotFoundException.class, FileNotFoundException.class})
    public ResponseEntity<String> handleJobNotFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleException(Throwable e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

}
