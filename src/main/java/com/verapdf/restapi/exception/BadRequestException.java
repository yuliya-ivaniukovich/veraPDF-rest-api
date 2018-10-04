package com.verapdf.restapi.exception;

public class BadRequestException extends VeraPDFRestApiException {
    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }
}
