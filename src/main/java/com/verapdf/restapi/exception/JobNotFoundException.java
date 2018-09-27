package com.verapdf.restapi.exception;

public class JobNotFoundException extends VeraPDFRestApiException {

    public JobNotFoundException() {
        super();
    }

    public JobNotFoundException(String message) {
        super(message);
    }
}
