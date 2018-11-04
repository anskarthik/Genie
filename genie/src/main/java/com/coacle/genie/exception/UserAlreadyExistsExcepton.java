package com.coacle.genie.exception;

public class UserAlreadyExistsExcepton extends Exception {

    private String userId;

    public UserAlreadyExistsExcepton(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
