package com.verapdf.restapi.exception;

public class FileAlreadyExistsException extends VeraPDFRestApiException {
    public FileAlreadyExistsException() {
        super();
    }

    public FileAlreadyExistsException(String message) {
        super(message);
    }

}
