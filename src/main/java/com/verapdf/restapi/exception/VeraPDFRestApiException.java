package com.verapdf.restapi.exception;

public class VeraPDFRestApiException extends RuntimeException{

    public VeraPDFRestApiException() {
        super();
    }

    public VeraPDFRestApiException(String message) {
        super(message);
    }
}
