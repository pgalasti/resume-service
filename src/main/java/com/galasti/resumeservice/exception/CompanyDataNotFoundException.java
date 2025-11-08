package com.galasti.resumeservice.exception;

public class CompanyDataNotFoundException extends RuntimeException {

    public CompanyDataNotFoundException(String message) {
        super(message);
    }

    public CompanyDataNotFoundException(String message, Exception e) {
        super(message, e);
    }
}
