package com.verapdf.restapi.exception;

public class VeraPDFRestApiException extends RuntimeException{

    public VeraPDFRestApiException() {
        super();
    }

    public VeraPDFRestApiException(String message) {
        super(message);
    }

    public VeraPDFRestApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public VeraPDFRestApiException(Throwable cause) {
        super(cause);
    }
}
