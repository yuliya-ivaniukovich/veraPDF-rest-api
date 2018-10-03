package com.verapdf.restapi.controller;

import com.verapdf.restapi.dto.ErrorDTO;
import com.verapdf.restapi.exception.ResourceNotFoundException;
import com.verapdf.restapi.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({BadRequestException.class})
        public ResponseEntity<ErrorDTO> handleBadRequestException(BadRequestException e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }


    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ErrorDTO> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorDTO> handleException(Throwable e) {
        //TODO: del e.getMessage()
        ErrorDTO errorDTO = new ErrorDTO(e.getMessage());
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }

}
