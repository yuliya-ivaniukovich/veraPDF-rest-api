package com.verapdf.restapi.exception;

public class FileNotFoundException extends VeraPDFRestApiException{
    public FileNotFoundException() {
        super();
    }

    public FileNotFoundException(String message) {
        super(message);
    }
}
