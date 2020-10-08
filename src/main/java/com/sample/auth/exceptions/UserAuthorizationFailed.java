package com.sample.auth.exceptions;

public class UserAuthorizationFailed extends Exception {

    public UserAuthorizationFailed() {
        super();
    }

    public UserAuthorizationFailed(String message) {
        super(message);
    }

    public UserAuthorizationFailed(String message, Throwable t) {
        super(message, t);
    }

}
