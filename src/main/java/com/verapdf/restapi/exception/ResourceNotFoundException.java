package com.verapdf.restapi.exception;

public class ResourceNotFoundException extends VeraPDFRestApiException {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
