package com.coacle.genie.exception;

public class EmailAlreadyExistsException extends Exception {

    private String email;

    public EmailAlreadyExistsException(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
