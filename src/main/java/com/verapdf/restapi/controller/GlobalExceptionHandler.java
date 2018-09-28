package com.verapdf.restapi.controller;

import com.verapdf.restapi.exception.ResourceNotFoundException;
import com.verapdf.restapi.exception.VeraPDFRestApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<String> handleVeraPDFRestApiException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

  //  @ExceptionHandler(Throwable.class)
 //   public ResponseEntity<String> handleException(Throwable e) {
  //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
 //   }

}
