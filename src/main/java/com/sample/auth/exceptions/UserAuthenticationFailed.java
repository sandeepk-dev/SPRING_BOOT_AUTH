package com.sample.auth.exceptions;

public class UserAuthenticationFailed extends Exception {

    public UserAuthenticationFailed() {
        super();
    }

    public UserAuthenticationFailed(String message) {
        super(message);
    }

    public UserAuthenticationFailed(String message, Throwable t) {
        super(message, t);
    }

}
