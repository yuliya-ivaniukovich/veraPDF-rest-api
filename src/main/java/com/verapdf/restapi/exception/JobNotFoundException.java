package com.verapdf.restapi.exception;

public class JobNotFoundException extends RuntimeException {

    public JobNotFoundException() {
        super();
    }

    public JobNotFoundException(String message) {
        super(message);
    }
}
